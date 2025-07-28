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
@Table(name = "book_mark")
@TableName("book_mark")
@Schema(description = "书籍书签")
public class BookMark extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 书籍ID
     */
    @Schema(description = "书籍ID")
    private Long bookId;

    /**
     * 书签标题
     */
    @Schema(description = "书签标题")
    private String title;

    /**
     * 书签位置（如章节、页码等）
     */
    @Schema(description = "页码")
    private String pageNumber;

    /**
     * 书签备注
     */
    @Schema(description = "书签备注")
    private String note;
}
