package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI问题实体类
 * <p>
 * 用于表示AI问答系统中的问题及其相关信息，包括问题内容、答案、关联的书籍和章节等。
 * </p>
 *
 * @author 
 * @since 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "ai_question")
@TableName("ai_question")
@Schema(description = "ai问题")
public class AiQuestion extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 问题内容
     */
    @Schema(description = "问题内容")
    private String question;

    /**
     * 回答内容
     */
    @Schema(description = "回答内容")
    private String answer;

    /**
     * 关联的书籍ID
     */
    @Schema(description = "关联的书籍ID")
    private Long bookId;

    /**
     * 关联的章节ID
     */
    @Schema(description = "关联的章节ID")
    private Long chapterId;

    /**
     * 问题类型
     */
    @Schema(description = "问题类型")
    private String type;

    /**
     * 是否为关键问题
     */
    @Schema(description = "是否为关键问题")
    private Boolean isCritical;
}
