package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 书籍文件实体类
 * <p>
 * 该类继承自BaseEntity，用于表示书籍文件的相关信息，包括文件路径、大小、格式等。
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "book_file")
@TableName("book_file")
@Schema(description = "书籍文件")
public class BookFile extends BaseEntity implements Serializable {

    /**
     * 关联的书籍ID
     */
    @Schema(description = "关联的书籍ID")
    private Long bookId;

    /**
     * 文件存储路径
     */
    @Schema(description = "文件存储路径")
    private String filePath;

    /**
     * 文件大小（单位：字节）
     */
    @Schema(description = "文件大小（单位：字节）")
    private Long fileSize;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    private String fileName;

    /**
     * 文件格式（如：PDF, EPUB等）
     */
    @Schema(description = "文件格式（如：PDF, EPUB等）")
    private String fileFormat;

    /**
     * 文件状态（如：有效、无效等）
     */
    @Schema(description = "文件状态（如：有效、无效等）")
    private String fileStatus;

    /**
     * 文件唯一标识ID
     */
    @Schema(description = "文件唯一标识ID")
    private String fileId;

    /**
     * 文件访问密钥或标识符
     */
    @Schema(description = "文件访问密钥或标识符")
    private String fileKey;
}
