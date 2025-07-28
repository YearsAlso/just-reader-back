package com.yearsalso.data.mapper;

import com.yearsalso.data.entity.UserPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * <p>
 * 用户-权限 Mapper 接口
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Mapper
public interface UmsPermissionMapper extends BaseMapper<UserPermission> {

    /**
     * 根据用户Id 获得权限列表
     *
     * @param userId
     * @return {@link List }<{@link UserPermission }>
     */
    @Cacheable(value = "userPermission", key = "#userId")
    List<UserPermission> selectListByUserId(String userId);

    // 更新权限
    @CachePut(value = "userPermission", key = "#permission.id")
    int updatePermission(UserPermission permission);
}
