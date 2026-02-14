package com.yearsalso.document.controller;

import com.yearsalso.common.api.CommonResult;
import com.yearsalso.document.dto.*;
import com.yearsalso.document.service.DocumentUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/documents")
@Tag(name = "文档上传管理", description = "文档上传相关接口")
public class DocumentUploadController {

    @Autowired
    private DocumentUploadService documentUploadService;

    /**
     * 单文件上传
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "单文件上传", description = "上传单个文档文件")
    public CommonResult<UploadResponse> uploadDocument(
            @Parameter(description = "文件内容", required = true)
            @RequestParam("file") MultipartFile file,
            
            @Parameter(description = "文档标题（可选）")
            @RequestParam(value = "title", required = false) String title,
            
            @Parameter(description = "分类ID")
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            
            @Parameter(description = "标签（逗号分隔）")
            @RequestParam(value = "tags", required = false) String tags,
            
            @Parameter(description = "可见性：private, shared, public")
            @RequestParam(value = "visibility", defaultValue = "private") String visibility,
            
            @Parameter(description = "共享用户ID（逗号分隔）")
            @RequestParam(value = "sharedUsers", required = false) String sharedUsers,
            
            @Parameter(description = "文档描述")
            @RequestParam(value = "description", required = false) String description,
            
            HttpServletRequest request) {
        
        try {
            UploadRequest uploadRequest = new UploadRequest();
            uploadRequest.setTitle(title);
            uploadRequest.setCategoryId(categoryId);
            uploadRequest.setVisibility(visibility);
            uploadRequest.setDescription(description);
            
            // 解析标签
            if (tags != null && !tags.isEmpty()) {
                uploadRequest.setTags(List.of(tags.split(",")));
            }
            
            // 解析共享用户
            if (sharedUsers != null && !sharedUsers.isEmpty()) {
                uploadRequest.setSharedUsers(
                    List.of(sharedUsers.split(",")).stream()
                        .map(Long::parseLong)
                        .toList()
                );
            }
            
            UploadResponse response = documentUploadService.uploadDocument(file, uploadRequest, request);
            return CommonResult.success(response);
            
        } catch (Exception e) {
            log.error("文档上传失败", e);
            UploadResponse errorResponse = new UploadResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError(e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 批量文件上传
     */
    @PostMapping(value = "/upload/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "批量文件上传", description = "批量上传多个文档文件")
    public CommonResult<List<UploadResponse>> uploadDocumentsBatch(
            @Parameter(description = "文件列表", required = true)
            @RequestParam("files") MultipartFile[] files,
            
            @Parameter(description = "分类ID")
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            
            @Parameter(description = "可见性：private, shared, public")
            @RequestParam(value = "visibility", defaultValue = "private") String visibility,
            
            HttpServletRequest request) {
        
        try {
            List<UploadResponse> responses = documentUploadService.uploadDocumentsBatch(files, categoryId, visibility, request);
            return CommonResult.success(responses);
            
        } catch (Exception e) {
            log.error("批量文档上传失败", e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 初始化分片上传
     */
    @PostMapping("/upload/initiate")
    @Operation(summary = "初始化分片上传", description = "初始化大文件的分片上传")
    public CommonResult<InitiateChunkedUploadResponse> initiateChunkedUpload(
            @RequestBody InitiateChunkedUploadRequest request) {
        
        try {
            InitiateChunkedUploadResponse response = documentUploadService.initiateChunkedUpload(request);
            return CommonResult.success(response);
            
        } catch (Exception e) {
            log.error("初始化分片上传失败", e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 上传分片
     */
    @PostMapping(value = "/upload/chunk/{uploadId}/{chunkNumber}", 
                 consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传分片", description = "上传文件的一个分片")
    public CommonResult<String> uploadChunk(
            @Parameter(description = "上传ID", required = true)
            @PathVariable String uploadId,
            
            @Parameter(description = "分片编号", required = true)
            @PathVariable Integer chunkNumber,
            
            @Parameter(description = "分片内容", required = true)
            @RequestParam("chunk") MultipartFile chunk) {
        
        try {
            documentUploadService.uploadChunk(uploadId, chunkNumber, chunk);
            return CommonResult.success("分片上传成功");
            
        } catch (Exception e) {
            log.error("分片上传失败: uploadId={}, chunkNumber={}", uploadId, chunkNumber, e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 完成分片上传
     */
    @PostMapping("/upload/complete")
    @Operation(summary = "完成分片上传", description = "完成所有分片上传并合并文件")
    public CommonResult<UploadResponse> completeChunkedUpload(
            @RequestBody CompleteChunkedUploadRequest request) {
        
        try {
            UploadResponse response = documentUploadService.completeChunkedUpload(request);
            return CommonResult.success(response);
            
        } catch (Exception e) {
            log.error("完成分片上传失败: uploadId={}", request.getUploadId(), e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 取消分片上传
     */
    @DeleteMapping("/upload/cancel/{uploadId}")
    @Operation(summary = "取消分片上传", description = "取消正在进行的分片上传")
    public CommonResult<String> cancelChunkedUpload(
            @Parameter(description = "上传ID", required = true)
            @PathVariable String uploadId) {
        
        try {
            documentUploadService.cancelChunkedUpload(uploadId);
            return CommonResult.success("分片上传已取消");
            
        } catch (Exception e) {
            log.error("取消分片上传失败: uploadId={}", uploadId, e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 获取上传进度
     */
    @GetMapping("/upload/progress/{uploadId}")
    @Operation(summary = "获取上传进度", description = "获取分片上传的进度信息")
    public CommonResult<UploadProgress> getUploadProgress(
            @Parameter(description = "上传ID", required = true)
            @PathVariable String uploadId) {
        
        try {
            UploadProgress progress = documentUploadService.getUploadProgress(uploadId);
            return CommonResult.success(progress);
            
        } catch (Exception e) {
            log.error("获取上传进度失败: uploadId={}", uploadId, e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 获取支持的文档类型
     */
    @GetMapping("/supported-types")
    @Operation(summary = "获取支持的文档类型", description = "获取系统支持的文档类型列表")
    public CommonResult<List<String>> getSupportedDocumentTypes() {
        
        try {
            List<String> supportedTypes = documentUploadService.getSupportedDocumentTypes();
            return CommonResult.success(supportedTypes);
            
        } catch (Exception e) {
            log.error("获取支持的文档类型失败", e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 获取上传限制
     */
    @GetMapping("/upload/limits")
    @Operation(summary = "获取上传限制", description = "获取文件上传的大小和类型限制")
    public CommonResult<UploadLimits> getUploadLimits() {
        
        try {
            UploadLimits limits = documentUploadService.getUploadLimits();
            return CommonResult.success(limits);
            
        } catch (Exception e) {
            log.error("获取上传限制失败", e);
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 验证文件是否可以上传
     */
    @PostMapping("/upload/validate")
    @Operation(summary = "验证文件", description = "验证文件是否符合上传要求")
    public CommonResult<ValidationResult> validateFile(
            @Parameter(description = "文件名", required = true)
            @RequestParam String fileName,
            
            @Parameter(description = "文件大小（字节）", required = true)
            @RequestParam Long fileSize,
            
            @Parameter(description = "文件类型")
            @RequestParam(required = false) String fileType) {
        
        try {
            ValidationResult result = documentUploadService.validateFile(fileName, fileSize, fileType);
            return CommonResult.success(result);
            
        } catch (Exception e) {
            log.error("文件验证失败", e);
            return CommonResult.failed(e.getMessage());
        }
    }
}

/**
 * 上传进度DTO
 */
@Data
class UploadProgress {
    
    @Parameter(description = "上传ID")
    private String uploadId;
    
    @Parameter(description = "已上传分片数")
    private Integer uploadedChunks;
    
    @Parameter(description = "总分片数")
    private Integer totalChunks;
    
    @Parameter(description = "上传进度（0-100）")
    private Integer progress;
    
    @Parameter(description = "上传状态：uploading, merging, completed, failed")
    private String status;
    
    @Parameter(description = "错误信息")
    private String error;
}

/**
 * 上传限制DTO
 */
@Data
class UploadLimits {
    
    @Parameter(description = "最大文件大小（字节）")
    private Long maxFileSize;
    
    @Parameter(description = "最大文件大小（格式化）")
    private String maxFileSizeFormatted;
    
    @Parameter(description = "支持的MIME类型列表")
    private List<String> supportedMimeTypes;
    
    @Parameter(description = "支持的文件扩展名列表")
    private List<String> supportedExtensions;
    
    @Parameter(description = "是否支持分片上传")
    private Boolean chunkedUploadSupported;
    
    @Parameter(description = "分片大小（字节）")
    private Long chunkSize;
    
    @Parameter(description = "最大并发上传数")
    private Integer maxConcurrentUploads;
}

/**
 * 验证结果DTO
 */
@Data
class ValidationResult {
    
    @Parameter(description = "是否有效")
    private Boolean valid;
    
    @Parameter(description = "验证通过的消息")
    private String message;
    
    @Parameter(description = "验证失败的原因")
    private String error;
    
    @Parameter(description = "建议的操作")
    private String suggestion;
    
    @Parameter(description = "文件类型是否支持")
    private Boolean typeSupported;
    
    @Parameter(description = "文件大小是否在限制内")
    private Boolean sizeWithinLimit;
    
    @Parameter(description = "推荐的上传方式：direct, chunked")
    private String recommendedUploadMethod;
}