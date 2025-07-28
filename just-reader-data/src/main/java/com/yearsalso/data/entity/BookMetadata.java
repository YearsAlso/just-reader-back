package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "book_metadata")
@TableName("book_metadata")
@Schema(description = "书籍元数据")
public class BookMetadata extends BaseEntity implements Serializable {
    /**
     * 书籍ID
     */
    @Schema(description = "书籍ID")
    private Long bookId;

    /**
     * 元数据来源ID
     */
    @Schema(description = "元数据ID")
    private Long metadataId;

    /**
     * 元数据类型
     */
    @Schema(description = "元数据类型")
    private String metadataType;

    /**
     * 元数据内容
     */
    @Schema(description = "元数据内容")
    private String metadataContent;
}
