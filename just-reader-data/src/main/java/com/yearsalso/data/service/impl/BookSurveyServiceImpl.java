package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.BookSurvey;
import com.yearsalso.data.mapper.BookSurveyMapper;
import com.yearsalso.data.service.IBookSurveyService;
import org.springframework.stereotype.Service;

/**
 * 图书审查服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IBookSurveyService接口，
 * 提供了对BookSurvey实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class BookSurveyServiceImpl extends ServiceImpl<BookSurveyMapper, BookSurvey> implements IBookSurveyService {

}