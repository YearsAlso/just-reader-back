package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.Metadata;
import com.yearsalso.data.mapper.MetadataMapper;
import com.yearsalso.data.service.IMetadataService;
import org.springframework.stereotype.Service;

/**
 * 元数据服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IMetadataService接口，
 * 提供了对Metadata实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class MetadataServiceImpl extends ServiceImpl<MetadataMapper, Metadata> implements IMetadataService {

}