package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 书籍分类实体类
 * <p>
 * 该类表示书籍与分类之间的关联关系，继承自基础实体类BaseEntity，
 * 包含书籍ID、分类ID和分类标题等属性。
 * </p>
 *
 * @author 
 * @since 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "book_categories")
@TableName("book_categories")
@Schema(description = "书籍书签")
public class BookCategory extends BaseEntity implements Serializable {

    /**
     * 书籍ID
     */
    @Schema(description = "书籍ID")
    private Long bookId;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    private Long categoryId;

    /**
     * 分类标题
     */
    @Schema(description = "分类标题")
    private String categoryTitle;
}
