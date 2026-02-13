package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 图书复述表
 * <p>
 * 用于记录用户对整段内容的复述以及AI给出的评分和答案
 * </p>
 *
 * @author 
 * @since 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "book_recite")
@TableName("book_recite")
@Schema(description = "图书复述表")
public class BookRecite extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 图书ID
     */
    @Schema(description = "图书ID")
    private Long bookId;

    /**
     * 图书名称
     */
    @Schema(description = "图书名称")
    private String bookName;

    /**
     * 章节ID
     */
    @Schema(description = "章节ID")
    private Long chapterId;

    /**
     * 章节名称
     */
    @Schema(description = "章节名称")
    private String chapterName;

    /**
     * 原始内容
     */
    @Schema(description = "原始内容")
    private String originalContent;

    /**
     * 用户复述内容
     */
    @Schema(description = "用户复述内容")
    private String userRetelling;

    /**
     * AI评分
     */
    @Schema(description = "AI评分")
    private Integer aiScore;

    /**
     * AI评价
     */
    @Schema(description = "AI评价")
    private String aiComment;

    /**
     * AI标准答案
     */
    @Schema(description = "AI标准答案")
    private String aiStandardAnswer;

    /**
     * 复述类型
     */
    @Schema(description = "复述类型")
    private String retellingType;
}