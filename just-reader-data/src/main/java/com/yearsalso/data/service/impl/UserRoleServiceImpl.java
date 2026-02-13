package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.UserRole;
import com.yearsalso.data.mapper.UserRoleMapper;
import com.yearsalso.data.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-用户角色 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("umsUserRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
