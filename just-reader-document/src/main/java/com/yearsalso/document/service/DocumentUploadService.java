package com.yearsalso.document.service;

import com.yearsalso.document.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档上传服务接口
 */
public interface DocumentUploadService {

    /**
     * 上传单个文档
     */
    UploadResponse uploadDocument(MultipartFile file, UploadRequest uploadRequest, HttpServletRequest request);

    /**
     * 批量上传文档
     */
    List<UploadResponse> uploadDocumentsBatch(MultipartFile[] files, Long categoryId, String visibility, HttpServletRequest request);

    /**
     * 初始化分片上传
     */
    InitiateChunkedUploadResponse initiateChunkedUpload(InitiateChunkedUploadRequest request);

    /**
     * 上传分片
     */
    void uploadChunk(String uploadId, Integer chunkNumber, MultipartFile chunk);

    /**
     * 完成分片上传
     */
    UploadResponse completeChunkedUpload(CompleteChunkedUploadRequest request);

    /**
     * 取消分片上传
     */
    void cancelChunkedUpload(String uploadId);

    /**
     * 获取上传进度
     */
    UploadProgress getUploadProgress(String uploadId);

    /**
     * 获取支持的文档类型
     */
    List<String> getSupportedDocumentTypes();

    /**
     * 获取上传限制
     */
    UploadLimits getUploadLimits();

    /**
     * 验证文件是否可以上传
     */
    ValidationResult validateFile(String fileName, Long fileSize, String fileType);
}