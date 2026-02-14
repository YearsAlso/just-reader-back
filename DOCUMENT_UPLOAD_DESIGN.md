# JustReader 文档上传功能设计

## 🎯 功能概述

为JustReader系统实现完整的文档上传、解析和管理功能，支持PDF、EPUB、DOCX等格式的文档上传，并自动提取文档元数据和内容。

## 📋 功能需求

### 1. 核心功能
- ✅ 多格式文档上传（PDF、EPUB、DOCX、TXT）
- ✅ 文档元数据自动提取
- ✅ 文档内容解析和索引
- ✅ 文档版本管理
- ✅ 文档分类和标签

### 2. 高级功能
- ✅ PDF文本提取和OCR支持
- ✅ EPUB章节解析
- ✅ 文档预览生成
- ✅ 批量上传支持
- ✅ 上传进度显示

### 3. 管理功能
- ✅ 文档列表和搜索
- ✅ 文档状态管理
- ✅ 存储空间监控
- ✅ 上传日志记录
- ✅ 权限控制

## 🏗️ 系统架构

### 组件设计
```
前端上传界面 → API网关 → 上传服务 → MinIO存储
      ↓              ↓           ↓
  进度显示      文件验证    文档解析器
      ↓              ↓           ↓
  错误处理      权限检查    元数据提取
                      ↓           ↓
                  数据库记录   Elasticsearch索引
```

### 技术栈
- **存储**: MinIO (对象存储)
- **解析**: Apache PDFBox, EPublib, Apache POI
- **搜索**: Elasticsearch (文档内容搜索)
- **缓存**: Redis (上传状态缓存)
- **队列**: RabbitMQ (异步处理)

## 📁 目录结构

```
just-reader-document/
├── src/main/java/com/yearsalso/document/
│   ├── controller/          # 控制器
│   │   ├── DocumentUploadController.java
│   │   ├── DocumentManageController.java
│   │   └── DocumentPreviewController.java
│   ├── service/            # 服务层
│   │   ├── DocumentUploadService.java
│   │   ├── DocumentParseService.java
│   │   ├── DocumentIndexService.java
│   │   └── DocumentStorageService.java
│   ├── parser/            # 文档解析器
│   │   ├── PdfParser.java
│   │   ├── EpubParser.java
│   │   ├── DocxParser.java
│   │   └── TxtParser.java
│   ├── entity/            # 实体类
│   │   ├── Document.java
│   │   ├── DocumentVersion.java
│   │   └── DocumentMetadata.java
│   ├── dto/               # 数据传输对象
│   │   ├── UploadRequest.java
│   │   ├── UploadResponse.java
│   │   ├── DocumentInfo.java
│   │   └── ParseResult.java
│   ├── config/            # 配置类
│   │   ├── MinioConfig.java
│   │   ├── ParserConfig.java
│   │   └── StorageConfig.java
│   └── utils/             # 工具类
│       ├── FileUtils.java
│       ├── MimeTypeUtils.java
│       └── ChecksumUtils.java
├── src/main/resources/
│   ├── application-document.yml
│   └── templates/         # 预览模板
└── pom.xml
```

## 🗄️ 数据库设计

### 新增表结构

#### `documents` 文档表
```sql
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    title VARCHAR(500) NOT NULL,
    original_filename VARCHAR(500),
    file_key VARCHAR(255) UNIQUE NOT NULL,
    file_size BIGINT NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    mime_type VARCHAR(100),
    storage_path VARCHAR(1000),
    storage_type VARCHAR(50) DEFAULT 'minio',
    bucket_name VARCHAR(100),
    
    -- 文档元数据
    author VARCHAR(200),
    publisher VARCHAR(200),
    publish_year INTEGER,
    isbn VARCHAR(20),
    language VARCHAR(10) DEFAULT 'zh',
    page_count INTEGER,
    word_count BIGINT,
    
    -- 解析状态
    parse_status VARCHAR(20) DEFAULT 'pending', -- pending, parsing, success, failed
    parse_progress INTEGER DEFAULT 0,
    parse_error TEXT,
    
    -- 内容信息
    description TEXT,
    cover_image_url VARCHAR(500),
    preview_url VARCHAR(500),
    
    -- 分类和标签
    category_id BIGINT REFERENCES book_categories(id),
    tags JSONB,
    
    -- 权限控制
    visibility VARCHAR(20) DEFAULT 'private', -- private, shared, public
    shared_users JSONB, -- 共享用户ID列表
    
    -- 统计信息
    view_count INTEGER DEFAULT 0,
    download_count INTEGER DEFAULT 0,
    
    -- 时间戳
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    parsed_at TIMESTAMP,
    last_accessed_at TIMESTAMP,
    
    -- 基础字段
    create_by VARCHAR(50),
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_by VARCHAR(50),
    update_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    del_flag INTEGER DEFAULT 0
);
```

#### `document_versions` 文档版本表
```sql
CREATE TABLE document_versions (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES documents(id),
    version_number INTEGER NOT NULL,
    file_key VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    storage_path VARCHAR(1000),
    change_description TEXT,
    uploaded_by BIGINT REFERENCES users(id),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_document_version ON document_versions(document_id, version_number);
```

#### `document_pages` 文档页面表（用于搜索和预览）
```sql
CREATE TABLE document_pages (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES documents(id),
    page_number INTEGER NOT NULL,
    content TEXT,
    extracted_text TEXT,
    word_count INTEGER,
    image_url VARCHAR(500), -- 页面预览图
    metadata JSONB, -- 页面元数据（字体、布局等）
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_document_pages_doc ON document_pages(document_id, page_number);
```

#### `document_metadata` 文档扩展元数据
```sql
CREATE TABLE document_metadata (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES documents(id),
    metadata_type VARCHAR(50) NOT NULL, -- 'technical', 'bibliographic', 'structural'
    metadata_key VARCHAR(100) NOT NULL,
    metadata_value TEXT,
    metadata_json JSONB,
    create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_document_metadata ON document_metadata(document_id, metadata_type);
```

## 🔧 API设计

### 1. 文档上传API

#### POST `/api/documents/upload`
**请求**:
```json
{
  "file": "文件内容（multipart/form-data）",
  "title": "文档标题（可选）",
  "categoryId": 1,
  "tags": ["技术", "编程"],
  "visibility": "private",
  "sharedUsers": [2, 3]
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "documentId": 123,
    "fileKey": "doc_abc123.pdf",
    "uploadId": "upload_xyz789",
    "uploadUrl": "https://minio.example.com/bucket/doc_abc123.pdf",
    "fileSize": 1048576,
    "parseStatus": "pending",
    "estimatedParseTime": 30
  }
}
```

### 2. 分片上传API（大文件支持）

#### POST `/api/documents/upload/initiate`
**请求**:
```json
{
  "fileName": "large_document.pdf",
  "fileSize": 104857600,
  "fileType": "application/pdf",
  "chunkSize": 5242880
}
```

**响应**:
```json
{
  "uploadId": "upload_abc123",
  "chunkSize": 5242880,
  "totalChunks": 20,
  "uploadUrls": [
    "https://minio.example.com/upload/upload_abc123/1",
    "https://minio.example.com/upload/upload_abc123/2",
    "..."
  ]
}
```

#### POST `/api/documents/upload/complete`
**请求**:
```json
{
  "uploadId": "upload_abc123",
  "chunks": [1, 2, 3, ..., 20],
  "fileName": "large_document.pdf",
  "fileType": "application/pdf"
}
```

### 3. 文档管理API

#### GET `/api/documents`
**查询参数**:
- `page`: 页码
- `size`: 每页大小
- `categoryId`: 分类ID
- `tag`: 标签
- `status`: 解析状态
- `search`: 搜索关键词

#### GET `/api/documents/{id}`
获取文档详情

#### PUT `/api/documents/{id}`
更新文档信息

#### DELETE `/api/documents/{id}`
删除文档

#### POST `/api/documents/{id}/versions`
上传新版本

### 4. 文档解析状态API

#### GET `/api/documents/{id}/parse-status`
获取解析状态

#### POST `/api/documents/{id}/reparse`
重新解析文档

### 5. 文档预览API

#### GET `/api/documents/{id}/preview`
获取文档预览

#### GET `/api/documents/{id}/pages/{pageNumber}`
获取特定页面

#### GET `/api/documents/{id}/content`
获取文档内容（文本格式）

## ⚙️ 配置设计

### MinIO配置
```yaml
minio:
  endpoint: http://localhost:39000
  access-key: minioadmin
  secret-key: minioadmin
  bucket:
    documents: justreader-documents
    previews: justreader-previews
    temp: justreader-temp
  region: us-east-1
  secure: false
```

### 文档解析配置
```yaml
document:
  parser:
    pdf:
      enabled: true
      max-pages: 1000
      ocr:
        enabled: true
        language: chi_sim+eng
    epub:
      enabled: true
      extract-images: true
    docx:
      enabled: true
    txt:
      enabled: true
      encoding-detection: true
  
  storage:
    max-file-size: 1GB
    allowed-types:
      - application/pdf
      - application/epub+zip
      - application/vnd.openxmlformats-officedocument.wordprocessingml.document
      - text/plain
      - text/markdown
    
  preview:
    generate-preview: true
    preview-quality: 85
    thumbnail-size: 200x300
```

### 异步处理配置
```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: admin
    password: admin
    
  task:
    execution:
      pool:
        core-size: 5
        max-size: 10
        queue-capacity: 100
        
document:
  async:
    parse-queue: document.parse.queue
    preview-queue: document.preview.queue
    index-queue: document.index.queue
```

## 🚀 实现步骤

### 阶段1：基础上传功能（1-2天）
1. 创建文档实体和Repository
2. 实现MinIO文件上传
3. 创建上传API控制器
4. 实现文件验证和类型检查
5. 添加基础错误处理

### 阶段2：文档解析功能（2-3天）
1. 集成PDF解析库（Apache PDFBox）
2. 实现EPUB解析器
3. 添加DOCX和TXT解析
4. 实现元数据提取
5. 添加OCR支持（可选）

### 阶段3：高级功能（2-3天）
1. 实现分片上传（大文件支持）
2. 添加文档预览生成
3. 实现文档搜索索引
4. 添加版本管理
5. 实现权限控制

### 阶段4：优化和测试（1-2天）
1. 性能优化和缓存
2. 错误恢复机制
3. 单元测试和集成测试
4. 压力测试
5. 文档和部署脚本

## 📊 性能考虑

### 上传性能优化
1. **分片上传**: 支持大文件分片上传
2. **并行处理**: 多文件并行上传
3. **断点续传**: 上传中断后可以继续
4. **进度反馈**: 实时上传进度显示

### 解析性能优化
1. **异步处理**: 使用消息队列异步解析
2. **缓存结果**: 解析结果缓存到Redis
3. **增量解析**: 只解析变化部分
4. **资源限制**: 控制并发解析任务数

### 存储优化
1. **压缩存储**: 文本内容压缩存储
2. **分级存储**: 热数据SSD，冷数据HDD
3. **CDN加速**: 预览图片CDN加速
4. **清理策略**: 定期清理临时文件

## 🔒 安全考虑

### 文件安全
1. **病毒扫描**: 上传文件病毒扫描
2. **文件类型验证**: 严格的文件类型检查
3. **大小限制**: 防止超大文件攻击
4. **频率限制**: 防止暴力上传

### 数据安全
1. **加密存储**: 敏感文档加密存储
2. **访问控制**: 细粒度权限控制
3. **审计日志**: 所有操作记录日志
4. **数据备份**: 定期数据备份

### API安全
1. **身份验证**: JWT token验证
2. **速率限制**: API调用频率限制
3. **输入验证**: 所有输入参数验证
4. **SQL注入防护**: 使用参数化查询

## 🧪 测试策略

### 单元测试
- 文件上传功能测试
- 文档解析器测试
- 元数据提取测试
- 错误处理测试

### 集成测试
- 完整上传流程测试
- MinIO集成测试
- 数据库操作测试
- API端点测试

### 性能测试
- 并发上传测试
- 大文件上传测试
- 解析性能测试
- 内存使用测试

### 安全测试
- 文件类型绕过测试
- 权限绕过测试
- 注入攻击测试
- 暴力上传测试

## 📈 监控和日志

### 监控指标
1. **上传指标**: 上传成功率、平均上传时间
2. **解析指标**: 解析成功率、平均解析时间
3. **存储指标**: 存储使用量、文件数量
4. **性能指标**: API响应时间、错误率

### 日志记录
1. **操作日志**: 所有上传、删除、更新操作
2. **错误日志**: 详细的错误信息和堆栈跟踪
3. **性能日志**: 关键操作的性能数据
4. **安全日志**: 安全相关事件记录

## 🚨 错误处理

### 上传错误
- 文件大小超限
- 文件类型不支持
- 存储空间不足
- 网络连接中断

### 解析错误
- 文档格式损坏
- 编码识别失败
- OCR识别失败
- 内存不足错误

### 系统错误
- 数据库连接失败
- MinIO服务不可用
- 消息队列异常
- 缓存服务异常

## 📝 部署指南

### 开发环境
```bash
# 启动MinIO
docker-compose up -d just-reader-minio

# 启动RabbitMQ
docker-compose up -d just-reader-mq

# 启动应用
./mvnw spring-boot:run -pl just-reader-document
```

### 生产环境
```bash
# 1. 构建镜像
docker build -t just-reader-document:latest .

# 2. 部署服务
docker-compose -f docker-compose.prod.yml up -d

# 3. 初始化存储
./scripts/init-storage.sh

# 4. 监控部署
./scripts/deploy-monitoring.sh
```

## 🔮 未来扩展

### 短期扩展（1-3个月）
1. 支持更多文档格式（MOBI, AZW, RTF）
2. 文档对比功能
3. 批量导入导出
4. 文档协作编辑

### 中期扩展（3-6个月）
1. AI文档摘要
2. 智能标签推荐
3. 文档相似度分析
4. 阅读习惯分析

### 长期扩展（6-12个月）
1. 多语言文档支持
2. 手写文档识别
3. 音频文档处理
4. 视频文档处理

---

**设计完成时间**: 2026-02-13 23:50  
**下一步**: 开始实现文档上传功能的具体代码