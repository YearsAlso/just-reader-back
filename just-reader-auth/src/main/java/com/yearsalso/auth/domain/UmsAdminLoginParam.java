package com.yearsalso.auth.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登录参数
 * Created by macro on 2018/4/26.
 */
@Data
@Builder
@Schema(title = "用户登录参数")
@EqualsAndHashCode(callSuper = false)
public class UmsAdminLoginParam {

    @Schema(title = "用户名", required = true)
    @NotEmpty
    public String username;

    @Schema(title = "密码", required = true)
    @NotEmpty
    public String password;

    @Schema(title = "自动登录", required = false)
    public Boolean autoLogin = false;

    @Schema(title = "客户端类型", required = true)
    private String clientType;
}
