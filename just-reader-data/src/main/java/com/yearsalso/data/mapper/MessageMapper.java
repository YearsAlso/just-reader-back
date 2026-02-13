package com.yearsalso.data.mapper;

import com.yearsalso.data.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 消息-消息 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}
