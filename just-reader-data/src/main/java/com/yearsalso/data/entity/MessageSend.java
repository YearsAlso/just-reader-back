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
 * 消息-消息发送
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "message_send")
@TableName("message_send")
@Schema(description = "消息-消息发送")
public class MessageSend extends BaseEntity implements Serializable {

    /**
     * 消息ID
     */
    @Schema(description = "消息ID")
    private String messageId;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String targetUserId;

    /**
     * 发起用户Id
     */
    @Schema(description = "发起用户Id")
    private Long sourceUserId;

    /**
     * 通道编号
     */
    @Schema(description = "通道编号")
    private String channelCode;

    /**
     * 通道Id
     */
    @Schema(description = "通道Id")
    private Long channelId;
}
