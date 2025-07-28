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
 * 内容-设置
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "setting")
@TableName("setting")
@Schema(description = "内容-设置")
public class Setting extends BaseEntity implements Serializable {

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 值
     */
    @Schema(description = "值")
    private String settingValue;

    /**
     * 键
     */
    @Schema(description = "键")
    private String settingKey;
}
