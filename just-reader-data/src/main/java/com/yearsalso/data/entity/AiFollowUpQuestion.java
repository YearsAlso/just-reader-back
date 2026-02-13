package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * AI反问表
 * <p>
 * 用于记录AI反问的内容，跟踪系统主动提出的问题
 * </p>
 *
 * @author 
 * @since 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "ai_follow_up_question")
@TableName("ai_follow_up_question")
@Schema(description = "AI反问表")
public class AiFollowUpQuestion extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 图书问题ID（关联BookQuestion表）
     */
    @Schema(description = "图书问题ID")
    private Long bookQuestionId;

    /**
     * 用户问题互动ID（关联UserQuestionInteraction表）
     */
    @Schema(description = "用户问题互动ID")
    private Long userInteractionId;

    /**
     * AI反问内容
     */
    @Schema(description = "AI反问内容")
    private String followUpQuestion;

    /**
     * 反问类型
     */
    @Schema(description = "反问类型")
    private String followUpType;

    /**
     * 是否已回答
     */
    @Schema(description = "是否已回答")
    private Boolean isAnswered;

    /**
     * 用户回答内容
     */
    @Schema(description = "用户回答内容")
    private String userAnswer;
}