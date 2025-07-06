package com.yearsalso.auth.service;


import com.yearsalso.auth.domain.UmsClientLoginParam;
import com.yearsalso.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @auther mengx
 * @description 前台会员服务远程调用Service
 * @date 2024/1/30
 * @github
 **/
@FeignClient("just-reader-client")
public interface UmsClientService {
    @PostMapping("/ums/auth/login")
    CommonResult login(@RequestBody UmsClientLoginParam umsClientLoginParam);
}
