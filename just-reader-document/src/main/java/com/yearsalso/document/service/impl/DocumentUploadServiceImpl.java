package com.yearsalso.document.service.impl;

import com.yearsalso.document.config.UploadConfig;
import com.yearsalso.document.dto.*;
import com.yearsalso.document.exception.UploadException;
import com.yearsalso.document.service.DocumentUploadService;
import com.yearsalso.document.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文档上传服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentUploadServiceImpl implements DocumentUploadService {

    private final UploadConfig uploadConfig;
    
    // 存储上传状态（生产环境应该使用Redis）
    private final Map<String, UploadProgress> uploadProgressMap = new ConcurrentHashMap<>();
    private final Map<String, List<File>> chunkFilesMap = new ConcurrentHashMap<>();

    @Override
    public UploadResponse uploadDocument(MultipartFile file, UploadRequest uploadRequest, HttpServletRequest request) {
        log.info("开始上传文档: {}", file.getOriginalFilename());
        
        try {
            // 1. 验证文件
            ValidationResult validationResult = validateFile(
                file.getOriginalFilename(), 
                file.getSize(), 
                file.getContentType()
            );
            
            if (!validationResult.getValid()) {
                throw new UploadException(
                    UploadException.VALIDATION_FAILED, 
                    validationResult.getError()
                );
            }
            
            // 2. 生成存储路径
            String storagePath = FileUtils.generateStoragePath(
                file.getOriginalFilename(),
                uploadConfig.getStorageDir()
            );
            
            // 3. 创建目录
            FileUtils.createDirectoryIfNotExists(Paths.get(storagePath).getParent().toString());
            
            // 4. 保存文件
            Path targetPath = Paths.get(storagePath);
            file.transferTo(targetPath);
            log.info("文件保存成功: {}", storagePath);
            
            // 5. 生成响应
            UploadResponse response = new UploadResponse();
            response.setDocumentId(generateDocumentId());
            response.setFileKey(FileUtils.calculateMD5(file));
            response.setFileSize(file.getSize());
            response.setParseStatus("pending");
            response.setEstimatedParseTime(estimateParseTime(file.getSize(), file.getContentType()));
            response.setSuccess(true);
            
            // 6. 异步解析文档（如果启用）
            if (uploadConfig.getAutoParseEnabled()) {
                scheduleDocumentParse(response.getDocumentId(), storagePath, file.getContentType());
            }
            
            return response;
            
        } catch (IOException e) {
            log.error("文件上传失败: {}", file.getOriginalFilename(), e);
            throw new UploadException(UploadException.UPLOAD_FAILED, "文件上传失败", e);
        }
    }

    @Override
    public List<UploadResponse> uploadDocumentsBatch(MultipartFile[] files, Long categoryId, String visibility, HttpServletRequest request) {
        log.info("开始批量上传文档，文件数量: {}", files.length);
        
        List<UploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                UploadRequest uploadRequest = new UploadRequest();
                uploadRequest.setCategoryId(categoryId);
                uploadRequest.setVisibility(visibility);
                
                UploadResponse response = uploadDocument(file, uploadRequest, request);
                responses.add(response);
                
            } catch (Exception e) {
                log.error("批量上传中单个文件失败: {}", file.getOriginalFilename(), e);
                
                UploadResponse errorResponse = new UploadResponse();
                errorResponse.setSuccess(false);
                errorResponse.setError(e.getMessage());
                responses.add(errorResponse);
            }
        }
        
        return responses;
    }

    @Override
    public InitiateChunkedUploadResponse initiateChunkedUpload(InitiateChunkedUploadRequest request) {
        log.info("初始化分片上传: {}", request.getFileName());
        
        // 验证文件
        ValidationResult validationResult = validateFile(
            request.getFileName(), 
            request.getFileSize(), 
            request.getFileType()
        );
        
        if (!validationResult.getValid()) {
            throw new UploadException(
                UploadException.VALIDATION_FAILED, 
                validationResult.getError()
            );
        }
        
        // 生成上传ID
        String uploadId = UUID.randomUUID().toString();
        
        // 计算总分片数
        int totalChunks = (int) Math.ceil((double) request.getFileSize() / request.getChunkSize());
        
        // 初始化上传进度
        UploadProgress progress = new UploadProgress();
        progress.setUploadId(uploadId);
        progress.setTotalChunks(totalChunks);
        progress.setUploadedChunks(0);
        progress.setProgress(0);
        progress.setStatus("uploading");
        
        uploadProgressMap.put(uploadId, progress);
        chunkFilesMap.put(uploadId, new ArrayList<>());
        
        // 生成响应
        InitiateChunkedUploadResponse response = new InitiateChunkedUploadResponse();
        response.setUploadId(uploadId);
        response.setChunkSize(request.getChunkSize());
        response.setTotalChunks(totalChunks);
        response.setParallelUpload(true);
        
        return response;
    }

    @Override
    public void uploadChunk(String uploadId, Integer chunkNumber, MultipartFile chunk) {
        log.debug("上传分片: uploadId={}, chunkNumber={}", uploadId, chunkNumber);
        
        // 检查上传是否存在
        UploadProgress progress = uploadProgressMap.get(uploadId);
        if (progress == null) {
            throw new UploadException(UploadException.UPLOAD_NOT_FOUND, "上传不存在或已过期");
        }
        
        if (!"uploading".equals(progress.getStatus())) {
            throw new UploadException(UploadException.UPLOAD_FAILED, "上传状态异常: " + progress.getStatus());
        }
        
        try {
            // 保存分片文件
            String chunkFilename = String.format("%s_%04d.part", uploadId, chunkNumber);
            Path chunkPath = Paths.get(uploadConfig.getTempDir(), "chunks", chunkFilename);
            
            FileUtils.createDirectoryIfNotExists(chunkPath.getParent().toString());
            chunk.transferTo(chunkPath.toFile());
            
            // 更新进度
            List<File> chunkFiles = chunkFilesMap.get(uploadId);
            chunkFiles.add(chunkPath.toFile());
            
            progress.setUploadedChunks(progress.getUploadedChunks() + 1);
            progress.setProgress((int) ((double) progress.getUploadedChunks() / progress.getTotalChunks() * 100));
            
            uploadProgressMap.put(uploadId, progress);
            
        } catch (IOException e) {
            log.error("分片上传失败: uploadId={}, chunkNumber={}", uploadId, chunkNumber, e);
            throw new UploadException(UploadException.CHUNK_UPLOAD_FAILED, "分片上传失败", e);
        }
    }

    @Override
    public UploadResponse completeChunkedUpload(CompleteChunkedUploadRequest request) {
        log.info("完成分片上传: uploadId={}", request.getUploadId());
        
        UploadProgress progress = uploadProgressMap.get(request.getUploadId());
        if (progress == null) {
            throw new UploadException(UploadException.UPLOAD_NOT_FOUND, "上传不存在或已过期");
        }
        
        try {
            // 1. 合并分片文件
            progress.setStatus("merging");
            uploadProgressMap.put(request.getUploadId(), progress);
            
            List<File> chunkFiles = chunkFilesMap.get(request.getUploadId());
            if (chunkFiles == null || chunkFiles.size() != progress.getTotalChunks()) {
                throw new UploadException(UploadException.UPLOAD_FAILED, "分片数量不完整");
            }
            
            // 2. 生成最终文件路径
            String storagePath = FileUtils.generateStoragePath(
                request.getFileName(),
                uploadConfig.getStorageDir()
            );
            
            FileUtils.createDirectoryIfNotExists(Paths.get(storagePath).getParent().toString());
            
            // 3. 合并文件
            Path finalPath = Paths.get(storagePath);
            try (var outputStream = Files.newOutputStream(finalPath)) {
                for (File chunkFile : chunkFiles) {
                    Files.copy(chunkFile.toPath(), outputStream);
                }
            }
            
            // 4. 清理分片文件
            for (File chunkFile : chunkFiles) {
                FileUtils.safeDelete(chunkFile);
            }
            
            // 5. 更新进度
            progress.setStatus("completed");
            progress.setProgress(100);
            uploadProgressMap.put(request.getUploadId(), progress);
            
            // 6. 生成响应
            UploadResponse response = new UploadResponse();
            response.setDocumentId(generateDocumentId());
            response.setFileKey(FileUtils.calculateMD5(finalPath.toFile()));
            response.setUploadId(request.getUploadId());
            response.setFileSize(Files.size(finalPath));
            response.setParseStatus("pending");
            response.setEstimatedParseTime(estimateParseTime(Files.size(finalPath), request.getFileType()));
            response.setSuccess(true);
            
            // 7. 异步解析文档
            if (uploadConfig.getAutoParseEnabled()) {
                scheduleDocumentParse(response.getDocumentId(), storagePath, request.getFileType());
            }
            
            return response;
            
        } catch (IOException e) {
            log.error("合并分片文件失败: uploadId={}", request.getUploadId(), e);
            
            progress.setStatus("failed");
            progress.setError(e.getMessage());
            uploadProgressMap.put(request.getUploadId(), progress);
            
            throw new UploadException(UploadException.UPLOAD_FAILED, "合并分片文件失败", e);
        }
    }

    @Override
    public void cancelChunkedUpload(String uploadId) {
        log.info("取消分片上传: uploadId={}", uploadId);
        
        // 清理分片文件
        List<File> chunkFiles = chunkFilesMap.remove(uploadId);
        if (chunkFiles != null) {
            for (File chunkFile : chunkFiles) {
                FileUtils.safeDelete(chunkFile);
            }
        }
        
        // 移除进度记录
        uploadProgressMap.remove(uploadId);
    }

    @Override
    public UploadProgress getUploadProgress(String uploadId) {
        UploadProgress progress = uploadProgressMap.get(uploadId);
        if (progress == null) {
            throw new UploadException(UploadException.UPLOAD_NOT_FOUND, "上传不存在或已过期");
        }
        return progress;
    }

    @Override
    public List<String> getSupportedDocumentTypes() {
        return uploadConfig.getSupportedTypes();
    }

    @Override
    public UploadLimits getUploadLimits() {
        UploadLimits limits = new UploadLimits();
        limits.setMaxFileSize(uploadConfig.getMaxFileSize());
        limits.setMaxFileSizeFormatted(FileUtils.formatFileSize(uploadConfig.getMaxFileSize()));
        limits.setSupportedMimeTypes(uploadConfig.getSupportedTypes());
        limits.setSupportedExtensions(uploadConfig.getSupportedExtensions());
        limits.setChunkedUploadSupported(uploadConfig.getChunkedUploadEnabled());
        limits.setChunkSize(uploadConfig.getChunkSize());
        limits.setMaxConcurrentUploads(uploadConfig.getMaxConcurrentUploads());
        return limits;
    }

    @Override
    public ValidationResult validateFile(String fileName, Long fileSize, String fileType) {
        ValidationResult result = new ValidationResult();
        
        // 检查文件大小
        boolean sizeValid = fileSize <= uploadConfig.getMaxFileSize();
        result.setSizeWithinLimit(sizeValid);
        
        if (!sizeValid) {
            result.setValid(false);
            result.setError("文件大小超过限制");
            result.setSuggestion("请压缩文件或使用分片上传");
            return result;
        }
        
        // 检查文件类型
        String mimeType = fileType != null ? fileType : FileUtils.getMimeType(fileName);
        boolean typeValid = uploadConfig.getSupportedTypes().contains(mimeType) ||
                           uploadConfig.getSupportedExtensions().stream()
                               .anyMatch(ext -> fileName.toLowerCase().endsWith(ext));
        
        result.setTypeSupported(typeValid);
        
        if (!typeValid) {
            result.setValid(false);
            result.setError("不支持的文件类型");
            result.setSuggestion("请上传PDF、EPUB、DOCX、DOC或TXT格式的文件");
            return result;
        }
        
        // 检查文件名安全性
        boolean nameValid = FileUtils.isFileSafe(fileName);
        if (!nameValid) {
            result.setValid(false);
            result.setError("文件名不安全");
            result.setSuggestion("请使用安全的文件名");
            return result;
        }
        
        // 推荐上传方式
        if (fileSize > uploadConfig.getChunkSize() && uploadConfig.getChunkedUploadEnabled()) {
            result.setRecommendedUploadMethod("chunked");
        } else {
            result.setRecommendedUploadMethod("direct");
        }
        
        result.setValid(true);
        result.setMessage("文件验证通过");
        return result;
    }

    // ========== 私有方法 ==========
    
    private Long generateDocumentId() {
        return System.currentTimeMillis() + new Random().nextInt(1000);
    }
    
    private Integer estimateParseTime(Long fileSize, String fileType) {
        // 简单的估算逻辑
        if (fileType == null) {
            return 30;
        }
        
        if (fileType.contains("pdf")) {
            return (int) Math.min(300, fileSize / (1024 * 1024) * 2); // 每MB 2秒，最多5分钟
        } else if (fileType.contains("epub")) {
            return (int) Math.min(180, fileSize / (1024 * 1024)); // 每MB 1秒，最多3分钟
        } else if (fileType.contains("word") || fileType.contains("document")) {
            return (int) Math.min(120, fileSize / (1024 * 1024) * 0.5); // 每MB 0.5秒，最多2分钟
        } else {
            return 10; // 文本文件很快
        }
    }
    
    private void scheduleDocumentParse(Long documentId, String filePath, String fileType) {
        // 这里应该使用消息队列或线程池异步处理
        // 暂时只记录日志
        log.info("计划解析文档: documentId={}, filePath={}, fileType={}", 
                documentId, filePath, fileType);
        
        // 实际实现应该：
        // 1. 将解析任务加入队列
        // 2. 异步处理文档解析
        // 3. 更新解析状态到数据库
        // 4. 提取文档元数据和内容
    }
}