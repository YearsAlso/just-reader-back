-- 种子数据脚本
-- 文件名: 001_initial_data.sql
-- 描述: JustReader 系统初始数据
-- 创建时间: 2026-02-13

-- 1. 创建管理员用户
INSERT INTO users (username, email, password, password_salt, real_name, role_name, enable_status) VALUES
('admin', 'admin@justreader.com', crypt('Admin@2024', gen_salt('bf')), 'admin_salt_001', '系统管理员', 'admin', true),
('demo_user', 'demo@justreader.com', crypt('Demo@2024', gen_salt('bf')), 'demo_salt_001', '演示用户', 'user', true),
('test_reader', 'test@justreader.com', crypt('Test@2024', gen_salt('bf')), 'test_salt_001', '测试读者', 'user', true);

-- 2. 创建书籍分类
INSERT INTO book_categories (name, description, sort_order, parent_id) VALUES
('计算机科学', '计算机相关书籍和资料', 1, NULL),
('编程语言', '各种编程语言学习', 2, 1),
('算法与数据结构', '算法和数据结构相关', 3, 1),
('软件开发', '软件开发实践', 4, 1),

('文学', '文学作品和小说', 5, NULL),
('小说', '各类小说作品', 6, 5),
('散文', '散文作品', 7, 5),
('诗歌', '诗歌作品', 8, 5),

('科学', '科学类书籍', 9, NULL),
('物理', '物理学相关', 10, 9),
('化学', '化学相关', 11, 9),
('生物', '生物学相关', 12, 9),

('教育', '教育类书籍', 13, NULL),
('学习方法', '学习方法和技巧', 14, 13),
('教育理论', '教育理论和实践', 15, 13);

-- 3. 创建示例书籍
INSERT INTO books (title, author, isbn, publisher, publish_year, language, description, category_id, total_pages, status) VALUES
('深入理解计算机系统', 'Randal E. Bryant, David R. O''Hallaron', '9787111321330', '机械工业出版社', 2010, 'zh', '计算机系统经典教材，被誉为"计算机科学圣经"', 1, 702, 'ready'),
('代码大全（第2版）', 'Steve McConnell', '9787121022982', '电子工业出版社', 2006, 'zh', '软件开发领域的经典著作，涵盖软件构建的各个方面', 4, 944, 'ready'),
('设计模式：可复用面向对象软件的基础', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '9787111075752', '机械工业出版社', 2000, 'zh', '设计模式领域的经典著作，GOF 23种设计模式', 4, 254, 'ready'),
('算法导论（原书第3版）', 'Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein', '9787111407010', '机械工业出版社', 2013, 'zh', '算法领域的权威教材，涵盖广泛的算法主题', 3, 780, 'ready'),
('Clean Code：代码整洁之道', 'Robert C. Martin', '9787115216878', '人民邮电出版社', 2010, 'zh', '编写高质量代码的实践指南', 4, 388, 'ready'),
('人月神话', 'Frederick P. Brooks Jr.', '9787111129692', '清华大学出版社', 2002, 'zh', '软件工程领域的经典著作', 4, 369, 'ready'),
('重构：改善既有代码的设计', 'Martin Fowler', '9787111643667', '人民邮电出版社', 2019, 'zh', '重构技术的权威指南', 4, 424, 'ready'),
('计算机程序的构造和解释', 'Harold Abelson, Gerald Jay Sussman, Julie Sussman', '9787111135104', '机械工业出版社', 2004, 'zh', 'MIT经典教材，计算机科学入门', 1, 473, 'ready'),
('编程珠玑', 'Jon Bentley', '9787115357618', '人民邮电出版社', 2014, 'zh', '编程技巧和算法思维的经典', 3, 228, 'ready'),
('黑客与画家', 'Paul Graham', '9787121185354', '人民邮电出版社', 2011, 'zh', '硅谷创业教父的文集', 4, 264, 'ready');

-- 4. 创建用户角色
INSERT INTO user_roles (user_id, role_name) VALUES
(1, 'admin'),
(1, 'user'),
(2, 'user'),
(3, 'user');

-- 5. 创建示例书签
INSERT INTO book_marks (user_id, book_id, title, page_number, note) VALUES
(2, 1, '虚拟内存章节', '156', '虚拟内存的概念很重要，需要重点理解'),
(2, 1, '缓存机制', '89', '缓存层次结构对性能影响很大'),
(3, 4, '动态规划算法', '324', '动态规划的状态转移方程需要多练习'),
(3, 5, '函数命名规范', '45', '函数命名要清晰表达意图');

-- 6. 创建示例阅读会话
INSERT INTO reading_sessions (user_id, book_id, session_type, current_stage, start_time, is_completed, notes) VALUES
(2, 1, 'sq3r', 'read', '2024-01-15 10:00:00', false, '学习计算机系统基础'),
(2, 2, 'normal', 'read', '2024-01-16 14:30:00', true, '代码规范学习'),
(3, 4, 'sq3r', 'question', '2024-01-17 09:15:00', false, '算法学习'),
(3, 5, 'sq3r', 'review', '2024-01-18 16:45:00', true, '代码整洁度提升');

-- 7. 创建阅读阶段记录
INSERT INTO reading_stages (session_id, stage_type, start_time, end_time, duration, notes) VALUES
(1, 'survey', '2024-01-15 10:00:00', '2024-01-15 10:15:00', 900, '浏览全书目录和前言'),
(1, 'question', '2024-01-15 10:15:00', '2024-01-15 10:30:00', 900, '提出问题：计算机系统如何工作？'),
(1, 'read', '2024-01-15 10:30:00', '2024-01-15 11:30:00', 3600, '阅读第一章：计算机系统漫游'),
(2, 'read', '2024-01-16 14:30:00', '2024-01-16 16:00:00', 5400, '阅读代码规范章节'),
(3, 'survey', '2024-01-17 09:15:00', '2024-01-17 09:30:00', 900, '浏览算法导论目录'),
(3, 'question', '2024-01-17 09:30:00', '2024-01-17 10:00:00', 1800, '提出算法相关问题');

-- 8. 创建阅读问题
INSERT INTO reading_questions (session_id, user_id, book_id, question_text, page_number, question_type, answer_text, is_answered, difficulty_level) VALUES
(1, 2, 1, '计算机系统的主要组成部分有哪些？', 5, 'pre_reading', '硬件、操作系统、应用程序、用户', true, 2),
(1, 2, 1, '什么是虚拟内存？它的作用是什么？', 156, 'during_reading', '虚拟内存是一种内存管理技术，它使得应用程序认为它拥有连续可用的内存空间，而实际上它通常被分隔成多个物理内存碎片，还有部分暂时存储在外部磁盘存储器上，在需要时进行数据交换。', true, 3),
(1, 2, 1, '缓存对计算机性能的影响有多大？', 89, 'during_reading', NULL, false, 4),
(3, 3, 4, '动态规划的核心思想是什么？', 324, 'pre_reading', '将原问题分解为相对简单的子问题，通过解决子问题来解决原问题，并存储子问题的解以避免重复计算。', true, 3),
(3, 3, 4, '分治算法和动态规划有什么区别？', 340, 'during_reading', NULL, false, 4);

-- 9. 创建AI问答记录
INSERT INTO ai_questions (user_id, book_id, session_id, question_text, context, ai_model, ai_response, response_time, tokens_used, confidence_score) VALUES
(2, 1, 1, '请解释计算机系统中的缓存一致性', '正在学习计算机系统缓存章节', 'gpt-4', '缓存一致性是指多个处理器或核心共享内存时，确保各个缓存中的数据副本保持一致性的机制。主要解决写传播和事务串行化问题。常用协议有MESI、MOESI等。', 1250, 156, 0.92),
(3, 4, 3, '动态规划的时间复杂度如何分析？', '学习算法时间复杂度分析', 'gpt-4', '动态规划的时间复杂度通常由状态数量和状态转移复杂度决定。时间复杂度 = 状态数 × 每个状态的转移复杂度。空间复杂度可以通过状态压缩优化。', 980, 132, 0.88);

-- 10. 创建阅读统计
INSERT INTO read_counts (user_id, book_id, date, pages_read, reading_time, questions_generated, questions_answered, annotations_count) VALUES
(2, 1, '2024-01-15', 45, 5400, 3, 2, 5),
(2, 2, '2024-01-16', 32, 5400, 0, 0, 3),
(3, 4, '2024-01-17', 28, 2700, 2, 1, 4),
(3, 5, '2024-01-18', 15, 3600, 0, 0, 2);

-- 11. 创建用户交互记录
INSERT INTO user_question_interactions (user_id, question_id, interaction_type, time_spent, is_correct, confidence_level, feedback) VALUES
(2, 1, 'answer', 120, true, 4, '问题理解清晰'),
(2, 2, 'answer', 180, true, 5, '回答准确完整'),
(3, 4, 'answer', 150, true, 4, '掌握了核心概念');

-- 12. 创建插件数据
INSERT INTO plugins (name, version, description, author, plugin_type, config_schema, is_enabled) VALUES
('PDF解析器', '1.0.0', '解析PDF文档，提取文本和元数据', 'JustReader Team', 'parser', '{"maxFileSize": 104857600, "supportedFormats": ["pdf"]}', true),
('AI问答助手', '1.2.0', '基于AI的阅读问答助手', 'JustReader Team', 'ai', '{"model": "gpt-4", "temperature": 0.7, "maxTokens": 1000}', true),
('阅读统计', '1.0.0', '阅读进度和统计功能', 'JustReader Team', 'analytics', '{"trackReadingTime": true, "generateReports": true}', true),
('导出工具', '1.0.0', '导出阅读笔记和标注', 'JustReader Team', 'export', '{"formats": ["pdf", "markdown", "html"]}', true);

INSERT INTO user_plugins (user_id, plugin_id, config, is_active) VALUES
(2, 1, '{"autoParse": true}', true),
(2, 2, '{"autoSuggestQuestions": true}', true),
(2, 3, '{"weeklyReport": true}', true),
(3, 1, '{"autoParse": true}', true),
(3, 2, '{"autoSuggestQuestions": false}', true);

-- 13. 创建用户日志
INSERT INTO user_logs (user_id, action, details, ip_address, user_agent) VALUES
(2, 'login', '{"method": "password"}', '192.168.1.100', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)'),
(2, 'book_view', '{"book_id": 1, "book_title": "深入理解计算机系统"}', '192.168.1.100', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)'),
(2, 'reading_start', '{"session_id": 1, "book_id": 1}', '192.168.1.100', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7)'),
(3, 'login', '{"method": "password"}', '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
(3, 'question_submit', '{"question_id": 4, "session_id": 3}', '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)');

-- 记录种子数据版本
INSERT INTO database_version (version, description) VALUES ('1.0.1', '初始种子数据');