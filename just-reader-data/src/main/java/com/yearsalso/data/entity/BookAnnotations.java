package com.yearsalso.data.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 书籍注释实体类
 * <p>
 * 该类用于表示用户在阅读书籍时所做的注释信息，包括高亮、划线、备注等不同类型的注释。
 * 继承自BaseEntity，具备基础的创建时间、更新时间、删除标志等通用字段。
 * </p>
 *
 * @author
 * @since
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "book_annotations")
@TableName("book_annotations")
@Schema(description = "书籍注释")
public class BookAnnotations extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 书籍ID - 关联的书籍唯一标识
     */
    @Schema(description = "书籍ID")
    private Long bookId;

    /**
     * 用户ID - 创建该注释的用户唯一标识
     */
    @Schema(description = "注释ID")
    private Long userId;

    /**
     * 注释内容 - 用户添加的具体注释文本
     */
    @Schema(description = "注释内容")
    private String noteContent;

    /**
     * 注释页码 - 注释所在的书籍页码
     */
    @Schema(description = "注释页码")
    private Integer pageNumber;

    /**
     * 注释类型 - 如：高亮、划线、备注等
     */
    @Schema(description = "注释类型，如：高亮、划线、备注等")
    private String type;

    /**
     * 开始位置 - 注释在文本中的起始位置
     */
    @Schema(description = "开始位置")
    private Integer startPosition;

    /**
     * 结束位置 - 注释在文本中的结束位置
     */
    @Schema(description = "结束位置")
    private Integer endPosition;

    /**
     * 高亮标记颜色 - 高亮或划线时使用的颜色标识
     */
    @Schema(description = "高亮标记颜色")
    private String color;

    /**
     * 划线类型 - 描述划线的具体样式或类型
     */
    @Schema(description = "划线类型")
    private String lineType;

    /**
     * 是否公开 - 标识该注释是否对其他用户可见
     */
    @Schema(description = "是否公开")
    private Boolean isPublic;
}
