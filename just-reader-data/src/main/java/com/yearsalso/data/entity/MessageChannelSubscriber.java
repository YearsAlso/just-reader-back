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
 * 消息-订阅者
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "channel_subscriber")
@TableName("channel_subscriber")
@Schema(description = "消息-订阅者")
public class MessageChannelSubscriber extends BaseEntity implements Serializable {

    /**
     * 频道ID
     */
    @Schema(description = "频道ID")
    private Long channelId;

    /**
     * 频道编号
     */
    @Schema(description = "频道编号")
    private String channelCode;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;
}
