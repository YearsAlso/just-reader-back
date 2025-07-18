package com.yearsalso.data.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.mapper.UmsPermissionMapper;
import com.yearsalso.data.mapper.UmsUserMapper;
import com.yearsalso.data.entity.UmsUser;
import com.yearsalso.data.service.IUmsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户-用户 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Slf4j
@Service("umsUserService")
public class UmsUserServiceImpl extends ServiceImpl<UmsUserMapper, UmsUser> implements IUmsUserService {

    @Autowired
    private UmsPermissionMapper umsPermissionMapper;

    @Autowired
    private UmsUserMapper umsUserMapper;

}
