package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "plugins")
@TableName("plugins")
@Schema(description = "插件")
public class Plugin extends BaseEntity implements Serializable {

    @Schema(description = "插件名称")
    private String name;

    @Schema(description = "插件描述")
    private String description;

    @Schema(description = "插件图标")
    private String icon;

    @Schema(description = "插件状态")
    private Integer status;

    @Schema(description = "插件配置")
    private String config;

    @Schema(description = "插件最新版本")
    private String latestVersion;
}
