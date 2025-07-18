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
 * 消息-通道
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "mms_channel")
@TableName("mms_channel")
@Schema(description = "消息-通道")
public class MmsChannel extends BaseEntity implements Serializable {

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String channelName;

    /**
     * 编号
     */
    @Schema(description = "编号")
    private String channelCode;
}
