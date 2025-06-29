package com.yearsalso.data.utils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yearsalso.common.api.CommonSearch;

public class SearchUtils {

    public static <T> QueryWrapper<T> buildModelQueryWrapper(QueryWrapper<T> queryWrapper, T searchMap) {

        // TODO: 通过反射获取searchMap的属性，然后构建查询条件
        // 例如：searchMap中有一个属性为name，那么就构建一个queryWrapper.eq("name", searchMap.getName());

        Class<?> clazz = searchMap.getClass();

        return queryWrapper;
    }

    public static <T> QueryWrapper<T> buildSearchQueryWrapper(QueryWrapper<T> queryWrapper, CommonSearch search) {

        if (search == null) {
            return queryWrapper;
        }

        if (search.getContainsIds() != null && !search.getContainsIds().isEmpty()) {
            queryWrapper.in("id", search.getContainsIds());
        } else if (search.getExcludeIds() != null && !search.getExcludeIds().isEmpty()) {
            queryWrapper.notIn("id", search.getExcludeIds());
        }

        if (search.getStartDate() != null) {
            queryWrapper.ge("create_time", search.getStartDate());
        }

        if (search.getEndDate() != null) {
            queryWrapper.le("create_time", search.getEndDate());
        }

        return queryWrapper;
    }
}
