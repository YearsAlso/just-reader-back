package com.yearsalso.auth.service;

import com.yearsalso.auth.domain.UmsAdminLoginParam;
import com.yearsalso.common.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("just-reader-admin")
public interface UmsAdminService {

    @PostMapping("/ums/auth/login")
    CommonResult login(@RequestBody UmsAdminLoginParam umsAdminLoginParam);
}
