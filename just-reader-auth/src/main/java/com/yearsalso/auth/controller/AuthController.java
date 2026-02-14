package com.yearsalso.auth.controller;

import com.yearsalso.auth.domain.LoginRequestParam;
import com.yearsalso.auth.domain.UmsAdminLoginParam;
import com.yearsalso.auth.domain.UmsClientLoginParam;
import com.yearsalso.auth.service.UmsAdminService;
import com.yearsalso.auth.service.UmsClientService;
import com.yearsalso.common.api.CommonResult;
import com.yearsalso.common.constant.AuthConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 自定义Oauth2获取令牌接口
 * Created by macro on 2020/7/17.
 */
@RestController
@Tag(name = "AuthController", description = "统一认证授权接口")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private UmsClientService deviceService;

    @Operation(summary = "登录以后返回token")
    @PostMapping(value = "/login")
    @ResponseBody
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LoginRequestParam.class)
            )
    )
    public CommonResult login(@RequestBody LoginRequestParam loginParam) {
        String clientType = loginParam.getClientType();
        if (AuthConstant.ADMIN_CLIENT_APP.equals(clientType)) {
            UmsAdminLoginParam umsAdminLoginParam = UmsAdminLoginParam.builder()
                    .autoLogin(loginParam.getAutoLogin())
                    .password(loginParam.getPassword())
                    .username(loginParam.getUsername())
                    .build();

            return adminService.login(umsAdminLoginParam);
        } else if (AuthConstant.DEVICE_CLIENT_ID.equals(clientType)) {
            UmsClientLoginParam umsClientLoginParam = UmsClientLoginParam.builder()
                    .username(loginParam.getUsername())
                    .password(loginParam.getPassword())
                    .autoLogin(loginParam.getAutoLogin())
                    .clientType(clientType)
                    .build();
            return deviceService.login(umsClientLoginParam);
        } else {
            return CommonResult.failed("clientId不正确");
        }
    }
}
