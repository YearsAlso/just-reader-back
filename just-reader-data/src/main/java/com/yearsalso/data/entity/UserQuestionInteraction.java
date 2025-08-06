package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户问题互动表
 * <p>
 * 用于记录用户追问以及AI回答的内容，跟踪用户与系统的问答交互过程
 * </p>
 *
 * @author 
 * @since 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "user_question_interaction")
@TableName("user_question_interaction")
@Schema(description = "用户问题互动表")
public class UserQuestionInteraction extends BaseEntity implements Serializable {

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
     * 用户提问内容
     */
    @Schema(description = "用户提问内容")
    private String userQuestion;

    /**
     * AI回答内容
     */
    @Schema(description = "AI回答内容")
    private String aiAnswer;

    /**
     * 互动类型（初始问题、追问等）
     */
    @Schema(description = "互动类型")
    private String interactionType;

    /**
     * 父级互动ID（用于构建对话树）
     */
    @Schema(description = "父级互动ID")
    private Long parentInteractionId;

    /**
     * 是否收藏
     */
    @Schema(description = "是否收藏")
    private Boolean isCollected;

    /**
     * 完成标记
     */
    @Schema(description = "完成标记")
    private Boolean isCompleted;
}