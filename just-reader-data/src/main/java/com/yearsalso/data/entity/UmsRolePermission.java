package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户-角色权限
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_role_permission")
@Schema(description = "用户-角色权限")
public class UmsRolePermission extends BaseEntity implements Serializable  {

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private String permissionId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private String roleId;
}
