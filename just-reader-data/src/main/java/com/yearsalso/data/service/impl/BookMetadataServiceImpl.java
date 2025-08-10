package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.BookMetadata;
import com.yearsalso.data.mapper.BookMetadataMapper;
import com.yearsalso.data.service.IBookMetadataService;
import org.springframework.stereotype.Service;

/**
 * 书籍元数据服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IBookMetadataService接口，
 * 提供了对BookMetadata实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class BookMetadataServiceImpl extends ServiceImpl<BookMetadataMapper, BookMetadata> implements IBookMetadataService {

}