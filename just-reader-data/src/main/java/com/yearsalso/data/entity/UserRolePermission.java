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
 * 用户-角色权限
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "role_permission")
@TableName("role_permission")
@Schema(description = "用户-角色权限")
public class UserRolePermission extends BaseEntity implements Serializable {

    /**
     * 权限ID
     */
    @Schema(description = "权限ID")
    private Long permissionId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;
}
