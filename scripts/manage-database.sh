#!/bin/bash
# 数据库部署和管理脚本
# 文件名: manage-database.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置
DB_HOST="localhost"
DB_PORT="35432"
DB_NAME="els-just-reader-local"
DB_USER="root"
DB_PASSWORD="123456"
BACKUP_DIR="/Volumes/MxStore/Project/just-reader-back/database/backups"
MIGRATIONS_DIR="/Volumes/MxStore/Project/just-reader-back/database/migrations"

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查 PostgreSQL 客户端
check_postgres_client() {
    if ! command -v psql &> /dev/null; then
        log_error "PostgreSQL 客户端未安装"
        log_info "请安装 PostgreSQL 客户端: brew install postgresql"
        exit 1
    fi
    log_success "PostgreSQL 客户端已安装"
}

# 检查数据库连接
check_database_connection() {
    log_info "检查数据库连接..."
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "SELECT 1;" &> /dev/null; then
        log_success "数据库连接正常"
        return 0
    else
        log_error "无法连接到数据库"
        return 1
    fi
}

# 创建数据库
create_database() {
    log_info "创建数据库..."
    
    # 先连接到默认数据库创建目标数据库
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "CREATE DATABASE \"$DB_NAME\";" &> /dev/null; then
        log_success "数据库创建成功: $DB_NAME"
    else
        log_warning "数据库可能已存在，继续..."
    fi
}

# 运行迁移脚本
run_migrations() {
    log_info "运行数据库迁移..."
    
    if [ ! -d "$MIGRATIONS_DIR" ]; then
        log_error "迁移目录不存在: $MIGRATIONS_DIR"
        return 1
    fi
    
    # 按文件名排序运行迁移
    for migration_file in $(ls "$MIGRATIONS_DIR"/*.sql | sort); do
        log_info "运行迁移: $(basename "$migration_file")"
        
        if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$migration_file" &> /tmp/migration.log; then
            log_success "迁移成功: $(basename "$migration_file")"
        else
            log_error "迁移失败: $(basename "$migration_file")"
            cat /tmp/migration.log
            return 1
        fi
    done
    
    log_success "所有迁移完成"
}

# 备份数据库
backup_database() {
    log_info "备份数据库..."
    
    mkdir -p "$BACKUP_DIR"
    
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)
    BACKUP_FILE="$BACKUP_DIR/${DB_NAME}_backup_$TIMESTAMP.sql"
    
    if PGPASSWORD="$DB_PASSWORD" pg_dump -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -F p -f "$BACKUP_FILE"; then
        log_success "数据库备份成功: $BACKUP_FILE"
        
        # 压缩备份文件
        gzip "$BACKUP_FILE"
        log_success "备份文件已压缩: ${BACKUP_FILE}.gz"
        
        # 清理旧备份（保留最近7天）
        find "$BACKUP_DIR" -name "*.sql.gz" -mtime +7 -delete
        log_info "已清理7天前的旧备份"
    else
        log_error "数据库备份失败"
        return 1
    fi
}

# 恢复数据库
restore_database() {
    local backup_file="$1"
    
    if [ -z "$backup_file" ]; then
        log_error "请指定备份文件"
        echo "用法: $0 restore <备份文件>"
        return 1
    fi
    
    if [ ! -f "$backup_file" ]; then
        log_error "备份文件不存在: $backup_file"
        return 1
    fi
    
    log_info "恢复数据库..."
    
    # 如果是压缩文件，先解压
    local temp_file
    if [[ "$backup_file" == *.gz ]]; then
        temp_file="/tmp/restore_$(date +%s).sql"
        gunzip -c "$backup_file" > "$temp_file"
        backup_file="$temp_file"
    fi
    
    # 先删除现有数据库
    log_warning "正在删除现有数据库..."
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "DROP DATABASE IF EXISTS \"$DB_NAME\";"
    
    # 创建新数据库
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d postgres -c "CREATE DATABASE \"$DB_NAME\";"
    
    # 恢复数据
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$backup_file"; then
        log_success "数据库恢复成功"
    else
        log_error "数据库恢复失败"
        return 1
    fi
    
    # 清理临时文件
    if [ -n "$temp_file" ] && [ -f "$temp_file" ]; then
        rm "$temp_file"
    fi
}

# 查看数据库状态
show_database_status() {
    log_info "数据库状态:"
    
    echo ""
    echo "=== 数据库信息 ==="
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
        SELECT 
            current_database() as database,
            pg_size_pretty(pg_database_size(current_database())) as size,
            pg_postmaster_start_time() as start_time,
            version() as version;
    "
    
    echo ""
    echo "=== 表统计 ==="
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
        SELECT 
            schemaname,
            tablename,
            pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename)) as size,
            (SELECT count(*) FROM (schemaname || '.' || tablename)) as row_count
        FROM pg_tables 
        WHERE schemaname = 'public'
        ORDER BY pg_total_relation_size(schemaname || '.' || tablename) DESC;
    "
    
    echo ""
    echo "=== 连接统计 ==="
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
        SELECT 
            state,
            count(*) as connections,
            count(*) * 100.0 / (SELECT count(*) FROM pg_stat_activity) as percentage
        FROM pg_stat_activity 
        WHERE datname = '$DB_NAME'
        GROUP BY state
        ORDER BY connections DESC;
    "
}

# 创建测试数据
create_test_data() {
    log_info "创建测试数据..."
    
    local test_data_sql="$MIGRATIONS_DIR/../scripts/test_data.sql"
    
    if [ ! -f "$test_data_sql" ]; then
        log_warning "测试数据脚本不存在，创建默认测试数据..."
        
        cat > /tmp/test_data.sql << 'EOF'
-- 测试用户
INSERT INTO users (username, email, password, password_salt, real_name, role_name) VALUES
('test_user1', 'test1@justreader.com', crypt('test123', gen_salt('bf')), 'salt1', '测试用户1', 'user'),
('test_user2', 'test2@justreader.com', crypt('test123', gen_salt('bf')), 'salt2', '测试用户2', 'user'),
('test_user3', 'test3@justreader.com', crypt('test123', gen_salt('bf')), 'salt3', '测试用户3', 'user');

-- 测试书籍
INSERT INTO books (title, author, description, total_pages, status) VALUES
('深入理解计算机系统', 'Randal E. Bryant', '计算机系统经典教材', 500, 'ready'),
('代码大全', 'Steve McConnell', '软件开发经典著作', 800, 'ready'),
('设计模式', 'Erich Gamma', '面向对象设计经典', 400, 'ready'),
('算法导论', 'Thomas H. Cormen', '算法学习必备', 1200, 'ready'),
('Clean Code', 'Robert C. Martin', '代码整洁之道', 300, 'ready');

-- 测试阅读会话
INSERT INTO reading_sessions (user_id, book_id, session_type, current_stage) VALUES
(1, 1, 'sq3r', 'read'),
(2, 2, 'normal', 'read'),
(3, 3, 'sq3r', 'question');

-- 测试阅读问题
INSERT INTO reading_questions (session_id, user_id, book_id, question_text, question_type) VALUES
(1, 1, 1, '计算机系统的主要组成部分是什么？', 'pre_reading'),
(1, 1, 1, '虚拟内存的作用是什么？', 'during_reading'),
(3, 3, 3, '设计模式的分类有哪些？', 'pre_reading');

EOF
        
        test_data_sql="/tmp/test_data.sql"
    fi
    
    if PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -f "$test_data_sql"; then
        log_success "测试数据创建成功"
    else
        log_error "测试数据创建失败"
        return 1
    fi
}

# 清理数据库
clean_database() {
    log_warning "清理数据库..."
    
    read -p "确定要清理数据库吗？这将删除所有数据！(y/N): " confirm
    if [[ ! "$confirm" =~ ^[Yy]$ ]]; then
        log_info "取消清理操作"
        return 0
    fi
    
    log_info "开始清理数据库..."
    
    # 禁用外键约束
    PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME" -c "
        DO \$\$ 
        DECLARE
            r RECORD;
        BEGIN
            FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = 'public') LOOP
                EXECUTE 'TRUNCATE TABLE ' || quote_ident(r.tablename) || ' CASCADE';
            END LOOP;
        END \$\$;
    "
    
    log_success "数据库清理完成"
}

# 显示帮助
show_help() {
    echo "JustReader 数据库管理脚本"
    echo ""
    echo "用法: $0 [命令]"
    echo ""
    echo "命令:"
    echo "  init             初始化数据库（创建+迁移）"
    echo "  migrate          运行数据库迁移"
    echo "  backup           备份数据库"
    echo "  restore <文件>   从备份恢复数据库"
    echo "  status           查看数据库状态"
    echo "  test-data        创建测试数据"
    echo "  clean            清理数据库（删除所有数据）"
    echo "  check            检查数据库连接"
    echo "  help             显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 init          初始化数据库"
    echo "  $0 backup        备份数据库"
    echo "  $0 status        查看数据库状态"
}

# 主函数
main() {
    local command="${1:-help}"
    
    # 检查 PostgreSQL 客户端
    check_postgres_client
    
    case "$command" in
        init)
            create_database
            check_database_connection
            run_migrations
            ;;
        migrate)
            check_database_connection
            run_migrations
            ;;
        backup)
            check_database_connection
            backup_database
            ;;
        restore)
            check_database_connection
            restore_database "$2"
            ;;
        status)
            check_database_connection
            show_database_status
            ;;
        "test-data")
            check_database_connection
            create_test_data
            ;;
        clean)
            check_database_connection
            clean_database
            ;;
        check)
            check_database_connection
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "未知命令: $command"
            show_help
            exit 1
            ;;
    esac
}

# 运行主函数
main "$@"