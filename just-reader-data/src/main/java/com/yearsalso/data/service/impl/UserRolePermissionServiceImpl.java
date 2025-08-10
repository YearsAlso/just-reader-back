package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.UserRolePermission;
import com.yearsalso.data.mapper.UserRolePermissionMapper;
import com.yearsalso.data.service.IUserRolePermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色权限 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("umsRolePermissionService")
public class UserRolePermissionServiceImpl extends ServiceImpl<UserRolePermissionMapper, UserRolePermission> implements IUserRolePermissionService {

}
