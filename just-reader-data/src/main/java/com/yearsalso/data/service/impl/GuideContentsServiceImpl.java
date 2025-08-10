package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.GuideContents;
import com.yearsalso.data.mapper.GuideContentsMapper;
import com.yearsalso.data.service.IGuideContentsService;
import org.springframework.stereotype.Service;

/**
 * 导读内容服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IGuideContentsService接口，
 * 提供了对GuideContents实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class GuideContentsServiceImpl extends ServiceImpl<GuideContentsMapper, GuideContents> implements IGuideContentsService {

}