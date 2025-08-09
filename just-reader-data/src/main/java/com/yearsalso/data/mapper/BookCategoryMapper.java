package com.yearsalso.data.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yearsalso.data.entity.BookCategory;
import com.yearsalso.data.entity.BookMark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookCategoryMapper extends BaseMapper<BookCategory> {
}