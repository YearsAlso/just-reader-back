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
 * 文件-文件
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "fms_file")
@TableName("fms_file")
@Schema(description = "文件-文件")
public class FmsFile extends BaseEntity implements Serializable {

    /**
     * 文件主键
     */
    @Schema(description = "文件主键")
    private String fileKey;

    /**
     * 本地地址
     */
    @Schema(description = "本地地址")
    private String locationPath;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    private String fileName;

    /**
     * 大小
     */
    @Schema(description = "大小")
    private Long fileSize;

    /**
     * 类型;图片,json,yaml,txt,xml
     */
    @Schema(description = "类型;图片,json,yaml,txt,xml")
    private String fileType;

    /**
     * 文件后缀
     */
    @Schema(description = "文件后缀")
    private String fileSuffix;

    /**
     * 连接地址
     */
    @Schema(description = "连接地址")
    private String urlPath;

    /**
     * 存储方式,本地存储,阿里云,腾讯云,七牛云,华为云
     */
    @Schema(description = "存储方式")
    private String storeType;
}
