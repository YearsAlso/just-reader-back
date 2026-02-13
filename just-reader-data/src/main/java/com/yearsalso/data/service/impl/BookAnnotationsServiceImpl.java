package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.BookAnnotations;
import com.yearsalso.data.mapper.BookAnnotationsMapper;
import com.yearsalso.data.service.IBookAnnotationsService;
import org.springframework.stereotype.Service;

/**
 * 书籍注释服务实现类
 *
 * @author 
 * @since 
 */
@Service
public class BookAnnotationsServiceImpl extends ServiceImpl<BookAnnotationsMapper, BookAnnotations> implements IBookAnnotationsService {
}