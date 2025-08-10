package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.Medal;
import com.yearsalso.data.mapper.MedalMapper;
import com.yearsalso.data.service.IMedalService;
import org.springframework.stereotype.Service;

/**
 * 勋章服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IMedalService接口，
 * 提供了对Medal实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class MedalServiceImpl extends ServiceImpl<MedalMapper, Medal> implements IMedalService {

}