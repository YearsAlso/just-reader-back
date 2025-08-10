package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.UserPlugin;
import com.yearsalso.data.mapper.UserPluginMapper;
import com.yearsalso.data.service.IUserPluginService;
import org.springframework.stereotype.Service;

/**
 * 用户插件服务实现类
 * <p>
 * 该类实现了对用户插件信息的基本操作，包括创建、查询、更新和删除等操作
 * </p>
 *
 * @author yearsalso
 * @since 2025-08-10
 */
@Service
public class UserPluginServiceImpl extends ServiceImpl<UserPluginMapper, UserPlugin> implements IUserPluginService {

}