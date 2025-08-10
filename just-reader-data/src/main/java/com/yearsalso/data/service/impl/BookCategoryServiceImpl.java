package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.BookCategory;
import com.yearsalso.data.mapper.BookCategoryMapper;
import com.yearsalso.data.service.IBookCategoryService;
import org.springframework.stereotype.Service;

/**
 * 书籍分类服务实现类
 *
 * @author 
 * @since 
 */
@Service
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory> implements IBookCategoryService {
}