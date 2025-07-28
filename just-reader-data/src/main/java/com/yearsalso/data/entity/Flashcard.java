package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 记忆卡实体类
 * 用于存储用户的记忆卡片信息，包括学习内容、类型、复习计划等
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "flashcards")
@TableName("flashcards")
@Schema(description = "记忆卡")
public class Flashcard extends BaseEntity implements Serializable {
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
     * 卡片内容
     */
    @Schema(description = "卡片内容")
    private String content;

    /**
     * 卡片类型（如单词、短语等）
     */
    @Schema(description = "卡片类型")
    private String type;

    /**
     * 问题ID
     */
    @Schema(description = "问题ID")
    private Long questionId;

    /**
     * 问题总结
     */
    @Schema(description = "问题总结")
    private String answerSummary;

    /**
     * 下次复习时间
     */
    @Schema(description = "下次复习时间")
    private Date nextReview;

    /**
     * 复习次数
     */
    @Schema(description = "复习次数")
    private Long reviewCount;
}
