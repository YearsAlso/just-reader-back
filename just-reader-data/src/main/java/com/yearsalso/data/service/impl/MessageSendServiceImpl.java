package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.MessageSend;
import com.yearsalso.data.mapper.MessageSendMapper;
import com.yearsalso.data.service.IMessageSendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息-消息发送 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("mmsMessageSendService")
public class MessageSendServiceImpl extends ServiceImpl<MessageSendMapper, MessageSend> implements IMessageSendService {

}
