package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.Role;
import com.yearsalso.data.mapper.RoleMapper;
import com.yearsalso.data.service.IUmsRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-角色 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("umsRoleService")
public class UmsRoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IUmsRoleService {

}
