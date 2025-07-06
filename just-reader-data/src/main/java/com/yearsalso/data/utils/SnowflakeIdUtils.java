package com.yearsalso.data.utils;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdUtils implements IdentifierGenerator {

    @Value("${snowflake.machine-id:1}")
    static long workerId = 1;

    @Value("${snowflake.data-center-id:1}")
    static long dataCenterId = 1;

    public static Long getSnowflakeId() {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }

    @Override
    public Number nextId(Object entity) {
        return IdUtil.getSnowflake(workerId, dataCenterId).nextId();
    }
}
