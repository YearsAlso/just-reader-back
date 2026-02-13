package com.yearsalso.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yearsalso.data.entity.BookMetadata;
import com.yearsalso.data.entity.BookQuestion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookQuestionMapper extends BaseMapper<BookQuestion> {
}
