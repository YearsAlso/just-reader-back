package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.ReadCount;
import com.yearsalso.data.mapper.ReadCountMapper;
import com.yearsalso.data.service.IReadCountService;
import org.springframework.stereotype.Service;

/**
 * 阅读统计服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IReadCountService接口，
 * 提供了对ReadCount实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class ReadCountServiceImpl extends ServiceImpl<ReadCountMapper, ReadCount> implements IReadCountService {

}