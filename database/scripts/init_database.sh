#!/bin/bash
# database/scripts/init_database.sh
# JustReader 数据库初始化脚本

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 JustReader 数据库初始化脚本${NC}"
echo "========================================"

# 配置参数
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-35432}
DB_NAME=${DB_NAME:-els-just-reader-local}
DB_USER=${DB_USER:-root}
DB_PASSWORD=${DB_PASSWORD:-123456}
MIGRATIONS_DIR="$(dirname "$0")/../migrations"
SEEDS_DIR="$(dirname "$0")/../seeds"

# 显示配置
echo -e "${YELLOW}数据库配置:${NC}"
echo "主机: $DB_HOST:$DB_PORT"
echo "数据库: $DB_NAME"
echo "用户: $DB_USER"
echo "迁移目录: $MIGRATIONS_DIR"
echo "种子目录: $SEEDS_DIR"
echo ""

# 检查 PostgreSQL 客户端
check_psql() {
    if ! command -v psql &> /dev/null; then
        echo -e "${RED}错误: psql 命令未找到${NC}"
        echo "请安装 PostgreSQL 客户端:"
        echo "  macOS: brew install postgresql"
        echo "  Ubuntu: apt-get install postgresql-client"
        exit 1
    fi
}

# 测试数据库连接
test_connection() {
    echo -e "${YELLOW}测试数据库连接...${NC}"
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1;" &> /dev/null; then
        echo -e "${GREEN}✓ 数据库连接成功${NC}"
        return 0
    else
        echo -e "${RED}✗ 数据库连接失败${NC}"
        return 1
    fi
}

# 创建数据库（如果不存在）
create_database() {
    echo -e "${YELLOW}检查数据库是否存在...${NC}"
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -lqt | cut -d \| -f 1 | grep -qw "$DB_NAME"; then
        echo -e "${GREEN}✓ 数据库已存在: $DB_NAME${NC}"
    else
        echo -e "${YELLOW}创建数据库: $DB_NAME${NC}"
        PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "CREATE DATABASE \"$DB_NAME\";"
        echo -e "${GREEN}✓ 数据库创建成功${NC}"
    fi
}

# 执行迁移文件
run_migrations() {
    echo -e "${YELLOW}执行数据库迁移...${NC}"
    
    local migration_files=("$MIGRATIONS_DIR"/*.sql)
    if [ ${#migration_files[@]} -eq 0 ]; then
        echo -e "${YELLOW}⚠️  没有找到迁移文件${NC}"
        return
    fi
    
    # 按文件名排序
    IFS=$'\n' sorted_files=($(sort <<<"${migration_files[*]}"))
    unset IFS
    
    for file in "${sorted_files[@]}"; do
        if [ -f "$file" ]; then
            echo -e "执行: $(basename "$file")"
            if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$file" &> /tmp/migration.log; then
                echo -e "${GREEN}  ✓ 成功${NC}"
            else
                echo -e "${RED}  ✗ 失败${NC}"
                echo "错误日志:"
                cat /tmp/migration.log
                exit 1
            fi
        fi
    done
}

# 执行种子数据
run_seeds() {
    echo -e "${YELLOW}插入种子数据...${NC}"
    
    local seed_files=("$SEEDS_DIR"/*.sql)
    if [ ${#seed_files[@]} -eq 0 ]; then
        echo -e "${YELLOW}⚠️  没有找到种子文件${NC}"
        return
    fi
    
    # 按文件名排序
    IFS=$'\n' sorted_files=($(sort <<<"${seed_files[*]}"))
    unset IFS
    
    for file in "${sorted_files[@]}"; do
        if [ -f "$file" ]; then
            echo -e "执行: $(basename "$file")"
            if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$file" &> /tmp/seed.log; then
                echo -e "${GREEN}  ✓ 成功${NC}"
            else
                echo -e "${RED}  ✗ 失败${NC}"
                echo "错误日志:"
                cat /tmp/seed.log
                exit 1
            fi
        fi
    done
}

# 验证数据库结构
validate_database() {
    echo -e "${YELLOW}验证数据库结构...${NC}"
    
    local tables_query="SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name;"
    local tables=$(PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -t -c "$tables_query")
    
    echo -e "找到的表:"
    echo "$tables" | while read -r table; do
        if [ -n "$table" ]; then
            echo -e "  • $table"
        fi
    done
    
    local table_count=$(echo "$tables" | wc -l | tr -d ' ')
    echo -e "${GREEN}✓ 数据库包含 $table_count 个表${NC}"
}

# 显示数据库状态
show_status() {
    echo -e "${YELLOW}数据库状态:${NC}"
    
    local status_query="
    SELECT 
        '用户数' as metric, COUNT(*) as value FROM users
    UNION ALL
    SELECT '书籍数', COUNT(*) FROM books
    UNION ALL
    SELECT '阅读会话数', COUNT(*) FROM reading_sessions
    UNION ALL
    SELECT 'AI问答数', COUNT(*) FROM ai_questions
    UNION ALL
    SELECT '插件数', COUNT(*) FROM plugins;
    "
    
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "$status_query"
}

# 备份数据库
backup_database() {
    local backup_file="justreader_backup_$(date +%Y%m%d_%H%M%S).sql"
    echo -e "${YELLOW}备份数据库到: $backup_file${NC}"
    
    if PGPASSWORD="$DB_PASSWORD" pg_dump -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -F c -f "$backup_file"; then
        echo -e "${GREEN}✓ 备份成功${NC}"
        echo "备份文件: $(pwd)/$backup_file"
        echo "大小: $(du -h "$backup_file" | cut -f1)"
    else
        echo -e "${RED}✗ 备份失败${NC}"
    fi
}

# 主函数
main() {
    local command=${1:-init}
    
    case $command in
        init)
            check_psql
            test_connection || exit 1
            create_database
            run_migrations
            run_seeds
            validate_database
            show_status
            ;;
        migrate)
            check_psql
            test_connection || exit 1
            run_migrations
            validate_database
            ;;
        seed)
            check_psql
            test_connection || exit 1
            run_seeds
            show_status
            ;;
        status)
            check_psql
            test_connection || exit 1
            show_status
            ;;
        backup)
            check_psql
            test_connection || exit 1
            backup_database
            ;;
        validate)
            check_psql
            test_connection || exit 1
            validate_database
            ;;
        test)
            check_psql
            test_connection && echo -e "${GREEN}连接测试通过${NC}" || echo -e "${RED}连接测试失败${NC}"
            ;;
        help|--help|-h)
            echo "使用方法: $0 [命令]"
            echo ""
            echo "命令:"
            echo "  init     初始化数据库（默认）"
            echo "  migrate  只执行迁移"
            echo "  seed     只插入种子数据"
            echo "  status   显示数据库状态"
            echo "  backup   备份数据库"
            echo "  validate 验证数据库结构"
            echo "  test     测试数据库连接"
            echo "  help     显示帮助信息"
            echo ""
            echo "环境变量:"
            echo "  DB_HOST     数据库主机（默认: localhost）"
            echo "  DB_PORT     数据库端口（默认: 35432）"
            echo "  DB_NAME     数据库名称（默认: els-just-reader-local）"
            echo "  DB_USER     数据库用户（默认: root）"
            echo "  DB_PASSWORD 数据库密码（默认: 123456）"
            ;;
        *)
            echo -e "${RED}未知命令: $command${NC}"
            echo "使用: $0 help 查看可用命令"
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"