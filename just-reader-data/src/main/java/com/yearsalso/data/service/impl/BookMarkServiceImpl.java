package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.BookMark;
import com.yearsalso.data.mapper.BookMarkMapper;
import com.yearsalso.data.service.IBookMarkService;
import org.springframework.stereotype.Service;

/**
 * 书籍书签服务实现类
 *
 * @author 
 * @since 
 */
@Service
public class BookMarkServiceImpl extends ServiceImpl<BookMarkMapper, BookMark> implements IBookMarkService {
}