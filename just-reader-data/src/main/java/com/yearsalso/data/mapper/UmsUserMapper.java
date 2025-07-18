package com.yearsalso.data.mapper;

import com.yearsalso.data.entity.UmsUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.annotation.Nullable;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户-用户 Mapper 接口
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Mapper
public interface UmsUserMapper extends BaseMapper<UmsUser> {

    /**
     * 根据用户名称获得用户
     *
     * @param username
     * @return
     */
    UmsUser selectOneByUsername(String username);

    /**
     * 根据用户名查询用户
     *
     * @param input 输入内容
     * @return 用户
     */
    UmsUser selectOneByUsernameOrEmailOrMobile(String input);

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @param email
     * @param mobile
     * @return
     */
    int countByUsernameOrEmailOrMobile(String username, @Nullable String email, @Nullable String mobile);
}
