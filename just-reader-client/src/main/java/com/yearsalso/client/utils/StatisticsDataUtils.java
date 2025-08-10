package com.yearsalso.client.utils;

import com.yearsalso.common.constant.StatisticsConstant;
import cn.hutool.core.date.DateUtil;
import org.jetbrains.annotations.NotNull;

public class StatisticsDataUtils {

    @NotNull
    public static String getActiveUserKey() {
        return getDateActiveUserKey();
    }

    @NotNull
    private static String getDateActiveUserKey() {
        return StatisticsConstant.ACTIVE_USER_PREFIX + ":" + DateUtil.today();
    }
}
