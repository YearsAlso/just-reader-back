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
 * 用户-角色
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "ums_role")
@TableName("ums_role")
@Schema(description = "用户-角色")
public class UmsRole extends BaseEntity implements Serializable {

    /**
     * 数据类型
     */
    @Schema(description = "数据类型")
    private Integer dataType;

    /**
     * 是否为默认权限
     */
    @Schema(description = "是否为默认权限")
    private Boolean defaultRole;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 父级ID
     */
    @Schema(description = "父级ID")
    private Long parentId;
}
