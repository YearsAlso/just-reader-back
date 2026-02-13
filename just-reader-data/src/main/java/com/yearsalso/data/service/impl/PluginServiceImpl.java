package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.Plugin;
import com.yearsalso.data.mapper.PluginMapper;
import com.yearsalso.data.service.IPluginService;
import org.springframework.stereotype.Service;

/**
 * 插件服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IPluginService接口，
 * 提供了对Plugin实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class PluginServiceImpl extends ServiceImpl<PluginMapper, Plugin> implements IPluginService {

}