package com.yearsalso.auth.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(title = "用户登录参数")
public class LoginRequestParam {

    @Schema(title = "用户名")
    public String username;

    @Schema(title = "密码")
    public String password;

    @Schema(title = "自动登录")
    public Boolean autoLogin = false;

    @Schema(title = "客户端类型")
    private String clientType;

    @Schema(title = "设备号")
    private String deviceCode;

    @Schema(title = "设备盐")
    private String deviceSalt;

    @Schema(title = "时间戳")
    private Long timestamp;
}
