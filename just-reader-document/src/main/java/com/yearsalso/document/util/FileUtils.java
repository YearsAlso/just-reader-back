package com.yearsalso.document.util;

import com.yearsalso.document.exception.UploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件工具类
 */
@Slf4j
public class FileUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 生成文件存储路径
     */
    public static String generateStoragePath(String originalFilename, String baseDir) {
        String datePath = LocalDateTime.now().format(DATE_FORMATTER);
        String filename = UUID.randomUUID().toString() + getFileExtension(originalFilename);
        return Paths.get(baseDir, datePath, filename).toString();
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    /**
     * 获取文件MIME类型
     */
    public static String getMimeType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();
        return switch (extension) {
            case ".pdf" -> "application/pdf";
            case ".epub" -> "application/epub+zip";
            case ".docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case ".doc" -> "application/msword";
            case ".txt" -> "text/plain";
            default -> "application/octet-stream";
        };
    }

    /**
     * 计算文件MD5
     */
    public static String calculateMD5(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            byte[] digest = md.digest(fileBytes);
            
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("计算文件MD5失败", e);
            return null;
        }
    }

    /**
     * 计算文件MD5（MultipartFile版本）
     */
    public static String calculateMD5(MultipartFile file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(file.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
            
        } catch (NoSuchAlgorithmException | IOException e) {
            log.error("计算文件MD5失败", e);
            return null;
        }
    }

    /**
     * 创建目录（如果不存在）
     */
    public static void createDirectoryIfNotExists(String dirPath) {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                log.info("创建目录: {}", dirPath);
            } catch (IOException e) {
                log.error("创建目录失败: {}", dirPath, e);
                throw new UploadException(UploadException.STORAGE_ERROR, "创建目录失败: " + dirPath, e);
            }
        }
    }

    /**
     * 安全删除文件
     */
    public static void safeDelete(File file) {
        if (file != null && file.exists()) {
            try {
                Files.delete(file.toPath());
                log.debug("删除文件: {}", file.getAbsolutePath());
            } catch (IOException e) {
                log.warn("删除文件失败: {}", file.getAbsolutePath(), e);
            }
        }
    }

    /**
     * 安全删除文件（路径版本）
     */
    public static void safeDelete(String filePath) {
        if (filePath != null) {
            safeDelete(new File(filePath));
        }
    }

    /**
     * 格式化文件大小
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 检查文件是否安全（基础检查）
     */
    public static boolean isFileSafe(String filename) {
        // 检查文件名是否包含危险字符
        if (filename.contains("..") || filename.contains("/") || filename.contains("\\")) {
            return false;
        }
        
        // 检查文件扩展名
        String extension = getFileExtension(filename).toLowerCase();
        return !extension.isEmpty();
    }

    /**
     * 生成唯一文件名
     */
    public static String generateUniqueFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(originalFilename);
        return uuid + extension;
    }
}