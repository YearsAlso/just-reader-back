package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.BookQuestion;
import com.yearsalso.data.mapper.BookQuestionMapper;
import com.yearsalso.data.service.IBookQuestionService;
import org.springframework.stereotype.Service;

/**
 * 图书问题索引服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IBookQuestionService接口，
 * 提供了对BookQuestion实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class BookQuestionServiceImpl extends ServiceImpl<BookQuestionMapper, BookQuestion> implements IBookQuestionService {

}