package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.UserPermission;
import com.yearsalso.data.mapper.UserPermissionMapper;
import com.yearsalso.data.service.IUmsPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-权限 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("umsPermissionService")
public class UmsPermissionServiceImpl extends ServiceImpl<UserPermissionMapper, UserPermission> implements IUmsPermissionService {

}
