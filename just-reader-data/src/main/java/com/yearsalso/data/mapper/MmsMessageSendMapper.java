package com.yearsalso.data.mapper;

import com.yearsalso.data.entity.MessageSend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 消息-消息发送 Mapper 接口
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Mapper
public interface MmsMessageSendMapper extends BaseMapper<MessageSend> {

}
