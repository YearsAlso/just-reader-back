package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 消息-消息
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "mms_message")
@TableName("mms_message")
@Schema(description = "消息-消息")
public class MmsMessage extends BaseEntity implements Serializable {

    /**
     * 是否撤销
     */
    @Schema(description = "是否撤销")
    private Integer isUndo;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String type;
}
