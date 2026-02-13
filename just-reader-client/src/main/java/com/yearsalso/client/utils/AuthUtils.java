package com.yearsalso.client.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthUtils {

    static SM4 sm4 = SmUtil.sm4();

    /**
     * 解密获得真实密码
     *
     * @param password 密码
     * @return 真实密码
     */
    public static String getRealPassword(String password) {
        String decodeStr = Base64.decodeStr(password);
        log.debug("decodeStr: {}", decodeStr); //+ IdUtil.fastUUID());
        return decodeStr;
    }

    /**
     * 加密获得加密密码
     *
     * @param password 密码
     * @return 加密密码
     */
    public static String getEncryptPassword(String password) {
        return Base64.encode(password);
    }
}
