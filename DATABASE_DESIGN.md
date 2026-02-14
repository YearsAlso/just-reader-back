# JustReader 后端数据库设计

## 📊 项目概述
JustReader 是一个 AI 辅助的 SQ3R 阅读系统，需要支持：
1. 用户管理和认证
2. 书籍和文档管理
3. SQ3R 阅读流程跟踪
4. AI 问答和交互
5. 阅读进度和统计

## 🗄️ 数据库选择
- **主数据库**: PostgreSQL 17.1
- **缓存**: Redis 6.0.9
- **消息队列**: RabbitMQ
- **对象存储**: MinIO

## 📋 核心表设计

### 1. 用户相关表

#### `users` 用户表
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    mobile VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    password_salt VARCHAR(50),
    real_name VARCHAR(100),
    avatar VARCHAR(255),
    address TEXT,
    role_name VARCHAR(50),
    enable_status BOOLEAN DEFAULT true,
    create_by VARCHAR(50),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50),
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_flag INTEGER DEFAULT 0
);
```

#### `user_roles` 用户角色表
```sql
CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    role_name VARCHAR(50) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `user_logs` 用户日志表
```sql
CREATE TABLE user_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    details JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 2. 书籍相关表

#### `books` 书籍表
```sql
CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    author VARCHAR(200),
    isbn VARCHAR(20),
    publisher VARCHAR(200),
    publish_year INTEGER,
    language VARCHAR(10) DEFAULT 'zh',
    cover_image VARCHAR(255),
    file_path VARCHAR(500),
    file_type VARCHAR(20),
    file_size BIGINT,
    total_pages INTEGER,
    description TEXT,
    category_id BIGINT REFERENCES book_categories(id),
    reading_progress INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'uploaded',
    create_by VARCHAR(50),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50),
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_flag INTEGER DEFAULT 0
);
```

#### `book_categories` 书籍分类表
```sql
CREATE TABLE book_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT REFERENCES book_categories(id),
    description TEXT,
    sort_order INTEGER DEFAULT 0,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `book_marks` 书签表
```sql
CREATE TABLE book_marks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    book_id BIGINT NOT NULL REFERENCES books(id),
    title VARCHAR(200),
    page_number VARCHAR(50),
    note TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `book_annotations` 书籍标注表
```sql
CREATE TABLE book_annotations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    book_id BIGINT NOT NULL REFERENCES books(id),
    page_number INTEGER,
    content TEXT NOT NULL,
    annotation_type VARCHAR(20), -- 'highlight', 'note', 'question'
    color VARCHAR(20),
    position JSONB, -- 页面上的位置信息
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. SQ3R 阅读流程表

#### `reading_sessions` 阅读会话表
```sql
CREATE TABLE reading_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    book_id BIGINT NOT NULL REFERENCES books(id),
    session_type VARCHAR(20) DEFAULT 'sq3r', -- 'sq3r', 'normal'
    current_stage VARCHAR(20) DEFAULT 'survey', -- survey, question, read, recite, review
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    total_duration INTEGER, -- 总时长（秒）
    is_completed BOOLEAN DEFAULT false,
    notes TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `reading_stages` 阅读阶段表
```sql
CREATE TABLE reading_stages (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES reading_sessions(id),
    stage_type VARCHAR(20) NOT NULL, -- survey, question, read, recite, review
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    duration INTEGER, -- 阶段时长（秒）
    content JSONB, -- 阶段内容
    notes TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `reading_questions` 阅读问题表
```sql
CREATE TABLE reading_questions (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES reading_sessions(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    book_id BIGINT NOT NULL REFERENCES books(id),
    question_text TEXT NOT NULL,
    page_number INTEGER,
    question_type VARCHAR(20), -- 'pre_reading', 'during_reading', 'post_reading'
    answer_text TEXT,
    is_answered BOOLEAN DEFAULT false,
    difficulty_level INTEGER DEFAULT 1, -- 1-5
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 4. AI 交互表

#### `ai_questions` AI 问题表
```sql
CREATE TABLE ai_questions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    book_id BIGINT REFERENCES books(id),
    session_id BIGINT REFERENCES reading_sessions(id),
    question_text TEXT NOT NULL,
    context TEXT, -- 问题上下文
    ai_model VARCHAR(50),
    ai_response TEXT,
    response_time INTEGER, -- 响应时间（毫秒）
    tokens_used INTEGER,
    confidence_score DECIMAL(3,2),
    feedback_rating INTEGER, -- 1-5
    feedback_comment TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `ai_follow_up_questions` AI 跟进问题表
```sql
CREATE TABLE ai_follow_up_questions (
    id BIGSERIAL PRIMARY KEY,
    parent_question_id BIGINT NOT NULL REFERENCES ai_questions(id),
    question_text TEXT NOT NULL,
    ai_response TEXT,
    conversation_context JSONB,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 5. 统计和分析表

#### `read_counts` 阅读统计表
```sql
CREATE TABLE read_counts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    book_id BIGINT NOT NULL REFERENCES books(id),
    date DATE NOT NULL,
    pages_read INTEGER DEFAULT 0,
    reading_time INTEGER DEFAULT 0, -- 阅读时长（秒）
    questions_generated INTEGER DEFAULT 0,
    questions_answered INTEGER DEFAULT 0,
    annotations_count INTEGER DEFAULT 0,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, book_id, date)
);
```

#### `user_question_interactions` 用户问题交互表
```sql
CREATE TABLE user_question_interactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    question_id BIGINT NOT NULL REFERENCES reading_questions(id),
    interaction_type VARCHAR(20), -- 'view', 'attempt', 'answer', 'skip'
    time_spent INTEGER, -- 花费时间（秒）
    is_correct BOOLEAN,
    confidence_level INTEGER, -- 1-5
    feedback TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 6. 系统管理表

#### `plugins` 插件表
```sql
CREATE TABLE plugins (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    version VARCHAR(20),
    description TEXT,
    author VARCHAR(100),
    plugin_type VARCHAR(50),
    config_schema JSONB,
    is_enabled BOOLEAN DEFAULT true,
    install_path VARCHAR(500),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### `user_plugins` 用户插件表
```sql
CREATE TABLE user_plugins (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    plugin_id BIGINT NOT NULL REFERENCES plugins(id),
    config JSONB,
    is_active BOOLEAN DEFAULT true,
    last_used TIMESTAMP,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🔗 表关系图

```
users
  ├── user_roles
  ├── user_logs
  ├── book_marks
  ├── book_annotations
  ├── reading_sessions
  ├── reading_questions
  ├── ai_questions
  ├── read_counts
  └── user_plugins

books
  ├── book_marks
  ├── book_annotations
  ├── reading_sessions
  ├── reading_questions
  ├── ai_questions
  └── read_counts

reading_sessions
  ├── reading_stages
  ├── reading_questions
  └── ai_questions

ai_questions
  └── ai_follow_up_questions

plugins
  └── user_plugins
```

## 🎯 索引设计

### 主键索引
所有表都有 `id` 作为主键索引。

### 外键索引
所有外键字段都创建索引以提高查询性能。

### 业务索引
```sql
-- 用户相关索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(enable_status);

-- 书籍相关索引
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_category ON books(category_id);
CREATE INDEX idx_books_status ON books(status);

-- 阅读相关索引
CREATE INDEX idx_reading_sessions_user ON reading_sessions(user_id);
CREATE INDEX idx_reading_sessions_book ON reading_sessions(book_id);
CREATE INDEX idx_reading_sessions_stage ON reading_sessions(current_stage);

-- 时间相关索引
CREATE INDEX idx_create_at ON users(create_at);
CREATE INDEX idx_reading_date ON read_counts(date);
```

## 📈 分区策略

对于大数据量表，考虑使用分区：

1. `read_counts` 表按 `date` 字段按月分区
2. `user_logs` 表按 `create_at` 字段按月分区
3. `ai_questions` 表按 `create_at` 字段按周分区

## 🔒 安全考虑

1. **密码安全**：
   - 使用 bcrypt 或 Argon2 加密
   - 添加密码盐值
   - 密码强度验证

2. **数据加密**：
   - 敏感字段加密存储
   - 使用 PostgreSQL 的 pgcrypto 扩展

3. **访问控制**：
   - 行级安全策略
   - 视图封装敏感数据
   - 最小权限原则

## 🚀 性能优化

1. **查询优化**：
   - 使用 EXPLAIN ANALYZE 分析查询
   - 避免 N+1 查询问题
   - 合理使用 JOIN

2. **缓存策略**：
   - Redis 缓存热点数据
   - 查询结果缓存
   - 会话状态缓存

3. **连接池**：
   - 配置合理的连接池大小
   - 连接超时设置
   - 连接复用

## 📝 迁移脚本

创建数据库迁移脚本目录结构：
```
database/
├── migrations/
│   ├── 001_initial_schema.sql
│   ├── 002_add_sq3r_tables.sql
│   └── 003_add_ai_tables.sql
├── seeds/
│   ├── 001_initial_data.sql
│   └── 002_test_data.sql
└── scripts/
    ├── backup.sh
    └── restore.sh
```

## 🧪 测试数据

为开发和测试环境准备测试数据：
- 10个测试用户
- 50本示例书籍
- 100个阅读会话
- 500个AI问答记录

## 🔄 备份策略

1. **每日全量备份**：凌晨2点执行
2. **每小时增量备份**：业务低峰期
3. **备份保留策略**：
   - 每日备份保留7天
   - 每周备份保留4周
   - 每月备份保留12个月

## 📊 监控指标

1. **数据库性能**：
   - 查询响应时间
   - 连接数使用率
   - 缓存命中率

2. **业务指标**：
   - 活跃用户数
   - 每日阅读时长
   - AI问答成功率

3. **系统健康**：
   - 磁盘使用率
   - 内存使用率
   - CPU使用率

---

**设计完成时间**: 2026-02-13 21:45  
**下一步**: 创建数据库迁移脚本和配置数据访问层