package com.yearsalso.auth.service;


import com.yearsalso.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @auther mengx
 * @description 前台会员服务远程调用Service
 * @date 2024/1/30
 * @github
 **/
@FeignClient("just-reader-client")
public interface UmsClientService {
    @PostMapping("/dms/auth/login")
    CommonResult login(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       @RequestParam("deviceCode") String deviceCode,
                       @RequestParam("autoLogin") Boolean autoLogin
    );
}
