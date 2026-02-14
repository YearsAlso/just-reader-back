package com.yearsalso.document.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 文档上传请求DTO
 */
@Data
@Schema(description = "文档上传请求")
public class UploadRequest {
    
    @Schema(description = "文档标题（可选，不提供则使用文件名）")
    private String title;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "标签列表")
    private List<String> tags;
    
    @Schema(description = "可见性：private, shared, public", defaultValue = "private")
    private String visibility = "private";
    
    @Schema(description = "共享用户ID列表（当visibility为shared时有效）")
    private List<Long> sharedUsers;
    
    @Schema(description = "文档描述")
    private String description;
}

/**
 * 文档上传响应DTO
 */
@Data
@Schema(description = "文档上传响应")
public class UploadResponse {
    
    @Schema(description = "文档ID")
    private Long documentId;
    
    @Schema(description = "文件唯一标识")
    private String fileKey;
    
    @Schema(description = "上传ID（用于分片上传）")
    private String uploadId;
    
    @Schema(description = "文件访问URL")
    private String fileUrl;
    
    @Schema(description = "文件大小（字节）")
    private Long fileSize;
    
    @Schema(description = "解析状态：pending, parsing, success, failed")
    private String parseStatus;
    
    @Schema(description = "预计解析时间（秒）")
    private Integer estimatedParseTime;
    
    @Schema(description = "上传是否成功")
    private Boolean success;
    
    @Schema(description = "错误信息")
    private String error;
}

/**
 * 分片上传初始化请求DTO
 */
@Data
@Schema(description = "分片上传初始化请求")
public class InitiateChunkedUploadRequest {
    
    @Schema(description = "文件名", required = true)
    private String fileName;
    
    @Schema(description = "文件大小（字节）", required = true)
    private Long fileSize;
    
    @Schema(description = "文件MIME类型", required = true)
    private String fileType;
    
    @Schema(description = "分片大小（字节）", defaultValue = "5242880")
    private Long chunkSize = 5242880L; // 5MB
    
    @Schema(description = "文档标题（可选）")
    private String title;
    
    @Schema(description = "分类ID")
    private Long categoryId;
}

/**
 * 分片上传初始化响应DTO
 */
@Data
@Schema(description = "分片上传初始化响应")
public class InitiateChunkedUploadResponse {
    
    @Schema(description = "上传ID", required = true)
    private String uploadId;
    
    @Schema(description = "分片大小（字节）")
    private Long chunkSize;
    
    @Schema(description = "总分片数")
    private Integer totalChunks;
    
    @Schema(description = "分片上传URL列表")
    private List<String> uploadUrls;
    
    @Schema(description = "是否支持并行上传")
    private Boolean parallelUpload = true;
}

/**
 * 分片上传完成请求DTO
 */
@Data
@Schema(description = "分片上传完成请求")
public class CompleteChunkedUploadRequest {
    
    @Schema(description = "上传ID", required = true)
    private String uploadId;
    
    @Schema(description = "已上传的分片列表", required = true)
    private List<Integer> chunks;
    
    @Schema(description = "文件名", required = true)
    private String fileName;
    
    @Schema(description = "文件MIME类型", required = true)
    private String fileType;
    
    @Schema(description = "文档标题（可选）")
    private String title;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "标签列表")
    private List<String> tags;
}

/**
 * 文档信息DTO
 */
@Data
@Schema(description = "文档信息")
public class DocumentInfo {
    
    @Schema(description = "文档ID")
    private Long id;
    
    @Schema(description = "文档标题")
    private String title;
    
    @Schema(description = "原始文件名")
    private String originalFilename;
    
    @Schema(description = "文件大小（格式化）")
    private String fileSizeFormatted;
    
    @Schema(description = "文件类型")
    private String fileType;
    
    @Schema(description = "作者")
    private String author;
    
    @Schema(description = "页数")
    private Integer pageCount;
    
    @Schema(description = "解析状态")
    private String parseStatus;
    
    @Schema(description = "解析进度")
    private Integer parseProgress;
    
    @Schema(description = "上传时间")
    private String uploadedAt;
    
    @Schema(description = "最后访问时间")
    private String lastAccessedAt;
    
    @Schema(description = "查看次数")
    private Integer viewCount;
    
    @Schema(description = "下载次数")
    private Integer downloadCount;
    
    @Schema(description = "封面图片URL")
    private String coverImageUrl;
    
    @Schema(description = "预览URL")
    private String previewUrl;
    
    @Schema(description = "文件访问URL")
    private String fileUrl;
}

/**
 * 文档解析状态DTO
 */
@Data
@Schema(description = "文档解析状态")
public class ParseStatus {
    
    @Schema(description = "文档ID")
    private Long documentId;
    
    @Schema(description = "解析状态：pending, parsing, success, failed")
    private String status;
    
    @Schema(description = "解析进度（0-100）")
    private Integer progress;
    
    @Schema(description = "错误信息")
    private String error;
    
    @Schema(description = "预计剩余时间（秒）")
    private Integer estimatedRemainingTime;
    
    @Schema(description = "解析开始时间")
    private String startedAt;
    
    @Schema(description = "解析完成时间")
    private String completedAt;
}

/**
 * 文档搜索请求DTO
 */
@Data
@Schema(description = "文档搜索请求")
public class DocumentSearchRequest {
    
    @Schema(description = "搜索关键词")
    private String keyword;
    
    @Schema(description = "分类ID")
    private Long categoryId;
    
    @Schema(description = "标签")
    private String tag;
    
    @Schema(description = "解析状态")
    private String parseStatus;
    
    @Schema(description = "文件类型")
    private String fileType;
    
    @Schema(description = "作者")
    private String author;
    
    @Schema(description = "开始时间")
    private String startDate;
    
    @Schema(description = "结束时间")
    private String endDate;
    
    @Schema(description = "排序字段")
    private String sortBy = "uploadedAt";
    
    @Schema(description = "排序方向：asc, desc")
    private String sortDirection = "desc";
    
    @Schema(description = "页码", defaultValue = "1")
    private Integer page = 1;
    
    @Schema(description = "每页大小", defaultValue = "20")
    private Integer size = 20;
}

/**
 * 文档搜索响应DTO
 */
@Data
@Schema(description = "文档搜索响应")
public class DocumentSearchResponse {
    
    @Schema(description = "文档列表")
    private List<DocumentInfo> documents;
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "总页数")
    private Integer totalPages;
    
    @Schema(description = "当前页码")
    private Integer currentPage;
    
    @Schema(description = "每页大小")
    private Integer pageSize;
}