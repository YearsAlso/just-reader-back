-- 数据库迁移脚本
-- 文件名: 001_initial_schema.sql
-- 描述: 创建 JustReader 系统初始数据库结构
-- 创建时间: 2026-02-13

-- 启用必要的扩展
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. 创建用户相关表
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

CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_name VARCHAR(50) NOT NULL,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    action VARCHAR(100) NOT NULL,
    details JSONB,
    ip_address VARCHAR(45),
    user_agent TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. 创建书籍相关表
CREATE TABLE book_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT REFERENCES book_categories(id),
    description TEXT,
    sort_order INTEGER DEFAULT 0,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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

CREATE TABLE book_marks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    title VARCHAR(200),
    page_number VARCHAR(50),
    note TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE book_annotations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    page_number INTEGER,
    content TEXT NOT NULL,
    annotation_type VARCHAR(20),
    color VARCHAR(20),
    position JSONB,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. 创建 SQ3R 阅读流程表
CREATE TABLE reading_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    session_type VARCHAR(20) DEFAULT 'sq3r',
    current_stage VARCHAR(20) DEFAULT 'survey',
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    total_duration INTEGER,
    is_completed BOOLEAN DEFAULT false,
    notes TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reading_stages (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES reading_sessions(id) ON DELETE CASCADE,
    stage_type VARCHAR(20) NOT NULL,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP,
    duration INTEGER,
    content JSONB,
    notes TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reading_questions (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES reading_sessions(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    page_number INTEGER,
    question_type VARCHAR(20),
    answer_text TEXT,
    is_answered BOOLEAN DEFAULT false,
    difficulty_level INTEGER DEFAULT 1,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. 创建 AI 交互表
CREATE TABLE ai_questions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id BIGINT REFERENCES books(id) ON DELETE SET NULL,
    session_id BIGINT REFERENCES reading_sessions(id) ON DELETE SET NULL,
    question_text TEXT NOT NULL,
    context TEXT,
    ai_model VARCHAR(50),
    ai_response TEXT,
    response_time INTEGER,
    tokens_used INTEGER,
    confidence_score DECIMAL(3,2),
    feedback_rating INTEGER,
    feedback_comment TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ai_follow_up_questions (
    id BIGSERIAL PRIMARY KEY,
    parent_question_id BIGINT NOT NULL REFERENCES ai_questions(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    ai_response TEXT,
    conversation_context JSONB,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. 创建统计和分析表
CREATE TABLE read_counts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    pages_read INTEGER DEFAULT 0,
    reading_time INTEGER DEFAULT 0,
    questions_generated INTEGER DEFAULT 0,
    questions_answered INTEGER DEFAULT 0,
    annotations_count INTEGER DEFAULT 0,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, book_id, date)
);

CREATE TABLE user_question_interactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    question_id BIGINT NOT NULL REFERENCES reading_questions(id) ON DELETE CASCADE,
    interaction_type VARCHAR(20),
    time_spent INTEGER,
    is_correct BOOLEAN,
    confidence_level INTEGER,
    feedback TEXT,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. 创建系统管理表
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

CREATE TABLE user_plugins (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    plugin_id BIGINT NOT NULL REFERENCES plugins(id) ON DELETE CASCADE,
    config JSONB,
    is_active BOOLEAN DEFAULT true,
    last_used TIMESTAMP,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引以提高查询性能
-- 用户相关索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(enable_status);
CREATE INDEX idx_users_create_at ON users(create_at);

-- 书籍相关索引
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_category ON books(category_id);
CREATE INDEX idx_books_status ON books(status);
CREATE INDEX idx_books_create_at ON books(create_at);

-- 阅读相关索引
CREATE INDEX idx_reading_sessions_user ON reading_sessions(user_id);
CREATE INDEX idx_reading_sessions_book ON reading_sessions(book_id);
CREATE INDEX idx_reading_sessions_stage ON reading_sessions(current_stage);
CREATE INDEX idx_reading_sessions_completed ON reading_sessions(is_completed);

CREATE INDEX idx_reading_stages_session ON reading_stages(session_id);
CREATE INDEX idx_reading_stages_type ON reading_stages(stage_type);

CREATE INDEX idx_reading_questions_session ON reading_questions(session_id);
CREATE INDEX idx_reading_questions_user ON reading_questions(user_id);
CREATE INDEX idx_reading_questions_answered ON reading_questions(is_answered);

-- AI 相关索引
CREATE INDEX idx_ai_questions_user ON ai_questions(user_id);
CREATE INDEX idx_ai_questions_book ON ai_questions(book_id);
CREATE INDEX idx_ai_questions_create_at ON ai_questions(create_at);

CREATE INDEX idx_ai_follow_up_parent ON ai_follow_up_questions(parent_question_id);

-- 统计相关索引
CREATE INDEX idx_read_counts_user ON read_counts(user_id);
CREATE INDEX idx_read_counts_book ON read_counts(book_id);
CREATE INDEX idx_read_counts_date ON read_counts(date);

CREATE INDEX idx_user_interactions_user ON user_question_interactions(user_id);
CREATE INDEX idx_user_interactions_question ON user_question_interactions(question_id);

-- 插件相关索引
CREATE INDEX idx_plugins_enabled ON plugins(is_enabled);
CREATE INDEX idx_user_plugins_user ON user_plugins(user_id);
CREATE INDEX idx_user_plugins_active ON user_plugins(is_active);

-- 创建注释
COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.password IS '加密后的密码';
COMMENT ON COLUMN users.password_salt IS '密码盐值';

COMMENT ON TABLE books IS '书籍表';
COMMENT ON COLUMN books.status IS '书籍状态: uploaded, processing, ready, error';

COMMENT ON TABLE reading_sessions IS '阅读会话表';
COMMENT ON COLUMN reading_sessions.session_type IS '会话类型: sq3r, normal';
COMMENT ON COLUMN reading_sessions.current_stage IS '当前阶段: survey, question, read, recite, review';

COMMENT ON TABLE reading_stages IS '阅读阶段表';
COMMENT ON COLUMN reading_stages.stage_type IS '阶段类型: survey, question, read, recite, review';

COMMENT ON TABLE ai_questions IS 'AI问答表';
COMMENT ON COLUMN ai_questions.confidence_score IS 'AI回答置信度 0-1';

-- 创建初始数据
INSERT INTO users (username, email, password, real_name, role_name, enable_status) VALUES
('admin', 'admin@justreader.com', crypt('admin123', gen_salt('bf')), '系统管理员', 'admin', true),
('testuser', 'test@justreader.com', crypt('test123', gen_salt('bf')), '测试用户', 'user', true);

INSERT INTO book_categories (name, description, sort_order) VALUES
('计算机科学', '计算机相关书籍', 1),
('文学', '文学作品', 2),
('历史', '历史书籍', 3),
('科学', '科学类书籍', 4),
('教育', '教育类书籍', 5);

-- 创建数据库版本表
CREATE TABLE database_version (
    id SERIAL PRIMARY KEY,
    version VARCHAR(20) NOT NULL,
    description TEXT,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 记录当前版本
INSERT INTO database_version (version, description) VALUES ('1.0.0', '初始数据库结构');