package com.yearsalso.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户-用户
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ums_user")
@Schema(description = "用户-用户")
public class UmsUser extends BaseEntity implements Serializable {

    /**
     * 地址
     */
    @Schema(description = "地址")
    private String address;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 移动电话
     */
    @Schema(description = "移动电话")
    private String mobile;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 真实名称
     */
    @Schema(description = "真实名称")
    private String realName;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Boolean enableStatus;

    /**
     * 类型
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
}
