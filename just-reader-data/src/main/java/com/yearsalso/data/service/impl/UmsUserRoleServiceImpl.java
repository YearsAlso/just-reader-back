package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.UmsUserRole;
import com.yearsalso.data.mapper.UmsUserRoleMapper;
import com.yearsalso.data.service.IUmsUserRoleService;
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
public class UmsUserRoleServiceImpl extends ServiceImpl<UmsUserRoleMapper, UmsUserRole> implements IUmsUserRoleService {

}
