package com.yearsalso.document.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 上传配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "document.upload")
public class UploadConfig {

    /**
     * 最大文件大小（字节）
     */
    private Long maxFileSize = 100 * 1024 * 1024L; // 100MB

    /**
     * 分片大小（字节）
     */
    private Long chunkSize = 5 * 1024 * 1024L; // 5MB

    /**
     * 支持的文件类型
     */
    private List<String> supportedTypes = List.of(
            "application/pdf",
            "application/epub+zip",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain",
            "application/msword"
    );

    /**
     * 支持的文件扩展名
     */
    private List<String> supportedExtensions = List.of(
            ".pdf", ".epub", ".docx", ".doc", ".txt"
    );

    /**
     * 临时文件目录
     */
    private String tempDir = "/tmp/just-reader/uploads";

    /**
     * 存储目录
     */
    private String storageDir = "/data/just-reader/documents";

    /**
     * 最大并发上传数
     */
    private Integer maxConcurrentUploads = 5;

    /**
     * 是否启用分片上传
     */
    private Boolean chunkedUploadEnabled = true;

    /**
     * 分片上传过期时间（分钟）
     */
    private Integer chunkedUploadExpiryMinutes = 60;

    /**
     * 是否启用文件验证
     */
    private Boolean validationEnabled = true;

    /**
     * 是否启用病毒扫描
     */
    private Boolean virusScanEnabled = false;

    /**
     * 是否启用自动解析
     */
    private Boolean autoParseEnabled = true;

    /**
     * 解析超时时间（秒）
     */
    private Integer parseTimeoutSeconds = 300;
}