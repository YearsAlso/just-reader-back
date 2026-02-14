# JustReader 数据库配置和使用指南

## 📋 目录
1. [数据库架构](#数据库架构)
2. [环境配置](#环境配置)
3. [部署步骤](#部署步骤)
4. [管理命令](#管理命令)
5. [监控和维护](#监控和维护)
6. [故障排除](#故障排除)

## 🗄️ 数据库架构

### 核心表关系
```
用户系统
├── users (用户表)
├── user_roles (用户角色)
└── user_logs (用户日志)

书籍系统
├── books (书籍表)
├── book_categories (书籍分类)
├── book_marks (书签)
└── book_annotations (标注)

SQ3R阅读系统
├── reading_sessions (阅读会话)
├── reading_stages (阅读阶段)
└── reading_questions (阅读问题)

AI交互系统
├── ai_questions (AI问答)
└── ai_follow_up_questions (跟进问题)

统计系统
├── read_counts (阅读统计)
└── user_question_interactions (问题交互)

插件系统
├── plugins (插件)
└── user_plugins (用户插件)
```

### 技术栈
- **数据库**: PostgreSQL 17.1
- **缓存**: Redis 6.0.9
- **ORM**: MyBatis Plus + Spring Data JPA
- **迁移工具**: Flyway (可选) + 自定义脚本

## ⚙️ 环境配置

### 1. 开发环境

#### 使用 Docker Compose
```bash
# 启动数据库服务
cd /Volumes/MxStore/Project/just-reader-back
docker-compose -f config/docker/docker-compose.yml up -d just-reader-db just-reader-redis
```

#### 手动安装 PostgreSQL
```bash
# macOS
brew install postgresql@17
brew services start postgresql@17

# 创建数据库
createdb -U postgres els-just-reader-local
```

### 2. 应用配置

#### application-dev.yml
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:35432/els-just-reader-local
    username: root
    password: 123456
    hikari:
      maximum-pool-size: 10
      
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
    
  data:
    redis:
      host: localhost
      port: 36379
```

## 🚀 部署步骤

### 1. 初始部署

```bash
# 1. 克隆项目
git clone <repository>
cd just-reader-back

# 2. 启动数据库
docker-compose -f config/docker/docker-compose.yml up -d just-reader-db

# 3. 初始化数据库
./scripts/manage-database.sh init

# 4. 加载种子数据
./scripts/manage-database.sh test-data

# 5. 启动应用
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### 2. 生产部署

```bash
# 1. 备份现有数据库
./scripts/manage-database.sh backup

# 2. 运行数据库迁移
./scripts/manage-database.sh migrate

# 3. 构建应用
./mvnw clean package -DskipTests

# 4. 部署应用
java -jar target/just-reader-*.jar --spring.profiles.active=prod
```

## 🛠️ 管理命令

### 数据库管理脚本

```bash
# 查看所有命令
./scripts/manage-database.sh help

# 初始化数据库（创建+迁移）
./scripts/manage-database.sh init

# 运行迁移
./scripts/manage-database.sh migrate

# 备份数据库
./scripts/manage-database.sh backup

# 恢复数据库
./scripts/manage-database.sh restore backups/els-just-reader-local_backup_20240213_220000.sql.gz

# 查看状态
./scripts/manage-database.sh status

# 创建测试数据
./scripts/manage-database.sh test-data

# 清理数据库（谨慎使用）
./scripts/manage-database.sh clean
```

### 直接数据库操作

```bash
# 连接到数据库
psql -h localhost -p 35432 -U root -d els-just-reader-local

# 常用查询
-- 查看用户
SELECT id, username, email, role_name FROM users WHERE del_flag = 0;

-- 查看书籍统计
SELECT b.title, COUNT(DISTINCT rs.id) as session_count, 
       SUM(rc.pages_read) as total_pages_read
FROM books b
LEFT JOIN reading_sessions rs ON b.id = rs.book_id
LEFT JOIN read_counts rc ON b.id = rc.book_id
GROUP BY b.id, b.title
ORDER BY total_pages_read DESC;

-- 查看阅读进度
SELECT u.username, b.title, rs.current_stage, 
       rs.start_time, rs.is_completed
FROM reading_sessions rs
JOIN users u ON rs.user_id = u.id
JOIN books b ON rs.book_id = b.id
WHERE rs.is_completed = false
ORDER BY rs.start_time DESC;
```

## 📊 监控和维护

### 1. 性能监控

#### 查看数据库性能
```sql
-- 查看慢查询
SELECT query, calls, total_time, mean_time
FROM pg_stat_statements
ORDER BY mean_time DESC
LIMIT 10;

-- 查看索引使用情况
SELECT schemaname, tablename, indexname, idx_scan
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC;

-- 查看表大小和行数
SELECT schemaname, tablename, 
       pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename)) as size,
       (SELECT count(*) FROM (schemaname || '.' || tablename)) as row_count
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname || '.' || tablename) DESC;
```

#### 应用监控端点
```
健康检查: http://localhost:8401/actuator/health
性能指标: http://localhost:8401/actuator/metrics
数据库连接: http://localhost:8401/actuator/health/db
缓存状态: http://localhost:8401/actuator/health/redis
```

### 2. 定期维护任务

#### 每日任务
```bash
# 备份数据库
0 2 * * * /Volumes/MxStore/Project/just-reader-back/scripts/manage-database.sh backup

# 清理旧日志
0 3 * * * psql -h localhost -p 35432 -U root -d els-just-reader-local -c "DELETE FROM user_logs WHERE create_at < NOW() - INTERVAL '30 days';"
```

#### 每周任务
```bash
# 重新统计索引
0 4 * * 0 psql -h localhost -p 35432 -U root -d els-just-reader-local -c "ANALYZE;"

# 生成周报
0 5 * * 0 /Volumes/MxStore/Project/just-reader-back/scripts/generate-weekly-report.sh
```

### 3. 备份和恢复

#### 完整备份策略
```bash
#!/bin/bash
# 完整备份脚本: /Volumes/MxStore/Project/just-reader-back/scripts/full-backup.sh

# 1. 停止应用
systemctl stop just-reader

# 2. 备份数据库
./scripts/manage-database.sh backup

# 3. 备份配置文件
tar -czf /backup/config_$(date +%Y%m%d).tar.gz config/

# 4. 备份上传文件
tar -czf /backup/uploads_$(date +%Y%m%d).tar.gz /path/to/uploads

# 5. 启动应用
systemctl start just-reader
```

#### 恢复流程
```bash
# 1. 停止应用
systemctl stop just-reader

# 2. 恢复数据库
./scripts/manage-database.sh restore /backup/els-just-reader-local_backup_20240213_220000.sql.gz

# 3. 恢复配置文件
tar -xzf /backup/config_20240213.tar.gz -C /

# 4. 恢复上传文件
tar -xzf /backup/uploads_20240213.tar.gz -C /

# 5. 启动应用
systemctl start just-reader
```

## 🔧 故障排除

### 常见问题

#### 1. 数据库连接失败
```bash
# 检查服务状态
docker ps | grep postgres
systemctl status postgresql

# 检查端口
netstat -an | grep 35432

# 检查连接数
psql -h localhost -p 35432 -U root -d els-just-reader-local -c "SELECT count(*) FROM pg_stat_activity;"
```

#### 2. 性能问题
```sql
-- 检查锁
SELECT relation::regclass, mode, granted, query
FROM pg_locks
JOIN pg_stat_activity ON pg_locks.pid = pg_stat_activity.pid
WHERE granted = false;

-- 检查长事务
SELECT pid, now() - xact_start AS duration, query
FROM pg_stat_activity
WHERE state != 'idle'
ORDER BY duration DESC;
```

#### 3. 数据不一致
```bash
# 检查外键约束
./scripts/manage-database.sh status | grep -A5 "外键检查"

# 修复数据
psql -h localhost -p 35432 -U root -d els-just-reader-local -f scripts/fix-data-consistency.sql
```

### 监控告警

配置 Prometheus 告警规则：
```yaml
# prometheus-alerts.yml
groups:
  - name: database_alerts
    rules:
      - alert: HighDatabaseConnections
        expr: pg_stat_database_numbackends{datname="els-just-reader-local"} > 50
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "数据库连接数过高"
          description: "数据库 {{ $labels.datname }} 连接数超过50"
      
      - alert: SlowQueries
        expr: rate(pg_stat_statements_mean_time_seconds[5m]) > 1
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "慢查询检测"
          description: "平均查询时间超过1秒"
```

## 📈 性能优化建议

### 1. 索引优化
```sql
-- 添加缺失索引
CREATE INDEX CONCURRENTLY idx_reading_sessions_user_book 
ON reading_sessions(user_id, book_id);

CREATE INDEX CONCURRENTLY idx_ai_questions_user_time 
ON ai_questions(user_id, create_at DESC);

-- 删除无用索引
DROP INDEX CONCURRENTLY IF EXISTS idx_unused_index;
```

### 2. 查询优化
```sql
-- 使用覆盖索引
EXPLAIN ANALYZE SELECT id, username FROM users WHERE email = 'test@example.com';

-- 避免全表扫描
SET enable_seqscan = off;

-- 使用分区表
CREATE TABLE read_counts_partitioned PARTITION BY RANGE (date);
```

### 3. 配置优化
```yaml
# application-prod.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 50
      minimum-idle: 10
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      
  jpa:
    properties:
      hibernate:
        jdbc.batch_size: 50
        order_inserts: true
        order_updates: true
        batch_versioned_data: true
```

## 📚 相关文档

- [数据库设计文档](./DATABASE_DESIGN.md)
- [数据访问层配置](./DATA_ACCESS_CONFIG.md)
- [迁移脚本](./database/migrations/)
- [API文档](http://localhost:8401/swagger-ui.html)

## 🆘 紧急情况

### 紧急联系人
- **数据库管理员**: DBA Team (dba@justreader.com)
- **开发团队**: Dev Team (dev@justreader.com)
- **运维团队**: Ops Team (ops@justreader.com)

### 紧急恢复流程
1. 立即停止应用
2. 从最新备份恢复数据库
3. 检查数据完整性
4. 逐步恢复服务
5. 记录事故报告

---

**文档版本**: v1.0  
**最后更新**: 2026-02-13  
**维护者**: OpenClaw AI Assistant