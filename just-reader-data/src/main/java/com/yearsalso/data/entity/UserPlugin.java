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
@Table(name = "metadata")
@TableName("metadata")
@Schema(description = "元数据，指的是书籍检索、书籍内容等相关的元信息")
public class UserPlugin extends BaseEntity implements Serializable {
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "插件ID")
    private String pluginId;

    @Schema(description = "插件名称")
    private String pluginName;

    @Schema(description = "插件图标")
    private String pluginIcon;

    @Schema(description = "插件版本")
    private String pluginVersion;
}
