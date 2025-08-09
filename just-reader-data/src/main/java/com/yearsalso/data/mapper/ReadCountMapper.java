package com.yearsalso.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yearsalso.data.entity.ReadCount;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReadCountMapper extends BaseMapper<ReadCount> {
}