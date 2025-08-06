package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 图书问题索引表
 * <p>
 * 用于记录所有用户可以看到的图书问题，作为主要的索引表
 * </p>
 *
 * @author 
 * @since 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "book_question")
@TableName("book_question")
@Schema(description = "图书问题索引表")
public class BookQuestion extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的图书ID
     */
    @Schema(description = "图书ID")
    private Long bookId;

    /**
     * 图书名称
     */
    @Schema(description = "书名")
    private String bookName;

    /**
     * 图书章节ID
     */
    @Schema(description = "文章章节Id")
    private Long bookChapterId;

    /**
     * 章节名称
     */
    @Schema(description = "章节名称")
    private String chapterName;

    /**
     * 问题内容
     */
    @Schema(description = "问题内容")
    private String questionContent;

    /**
     * 问题标题
     */
    @Schema(description = "问题标题")
    private String questionTitle;

    /**
     * 问题答案
     */
    @Schema(description = "问题答案")
    private String questionAnswer;

    /**
     * 问题显示顺序
     */
    @Schema(description = "顺序")
    private Integer orderNum;

    /**
     * 问题类型
     */
    @Schema(description = "问题类型")
    private String questionType;

    /**
     * 是否为关键问题
     */
    @Schema(description = "是否为关键问题")
    private Boolean isCritical;
}