package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 文档-文档信息
 * </p>
 *
 * @author OpenClaw AI Assistant
 * @since 2026-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "documents")
@TableName("documents")
@Schema(description = "文档信息表")
public class Document extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 文档标题
     */
    @Schema(description = "文档标题")
    private String title;

    /**
     * 原始文件名
     */
    @Schema(description = "原始文件名")
    private String originalFilename;

    /**
     * 文件唯一标识
     */
    @Schema(description = "文件唯一标识")
    private String fileKey;

    /**
     * 文件大小（字节）
     */
    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String fileType;

    /**
     * MIME类型
     */
    @Schema(description = "MIME类型")
    private String mimeType;

    /**
     * 存储路径
     */
    @Schema(description = "存储路径")
    private String storagePath;

    /**
     * 存储类型
     */
    @Schema(description = "存储类型")
    private String storageType = "minio";

    /**
     * 存储桶名称
     */
    @Schema(description = "存储桶名称")
    private String bucketName;

    /**
     * 作者
     */
    @Schema(description = "作者")
    private String author;

    /**
     * 出版社
     */
    @Schema(description = "出版社")
    private String publisher;

    /**
     * 出版年份
     */
    @Schema(description = "出版年份")
    private Integer publishYear;

    /**
     * ISBN
     */
    @Schema(description = "ISBN")
    private String isbn;

    /**
     * 语言
     */
    @Schema(description = "语言")
    private String language = "zh";

    /**
     * 页数
     */
    @Schema(description = "页数")
    private Integer pageCount;

    /**
     * 字数
     */
    @Schema(description = "字数")
    private Long wordCount;

    /**
     * 解析状态
     */
    @Schema(description = "解析状态")
    private String parseStatus = "pending";

    /**
     * 解析进度
     */
    @Schema(description = "解析进度")
    private Integer parseProgress = 0;

    /**
     * 解析错误信息
     */
    @Schema(description = "解析错误信息")
    private String parseError;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 封面图片URL
     */
    @Schema(description = "封面图片URL")
    private String coverImageUrl;

    /**
     * 预览URL
     */
    @Schema(description = "预览URL")
    private String previewUrl;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    private Long categoryId;

    /**
     * 标签（JSON格式）
     */
    @Schema(description = "标签（JSON格式）")
    private String tags;

    /**
     * 可见性
     */
    @Schema(description = "可见性")
    private String visibility = "private";

    /**
     * 共享用户（JSON格式）
     */
    @Schema(description = "共享用户（JSON格式）")
    private String sharedUsers;

    /**
     * 查看次数
     */
    @Schema(description = "查看次数")
    private Integer viewCount = 0;

    /**
     * 下载次数
     */
    @Schema(description = "下载次数")
    private Integer downloadCount = 0;

    /**
     * 上传时间
     */
    @Schema(description = "上传时间")
    private java.util.Date uploadedAt;

    /**
     * 解析完成时间
     */
    @Schema(description = "解析完成时间")
    private java.util.Date parsedAt;

    /**
     * 最后访问时间
     */
    @Schema(description = "最后访问时间")
    private java.util.Date lastAccessedAt;
}