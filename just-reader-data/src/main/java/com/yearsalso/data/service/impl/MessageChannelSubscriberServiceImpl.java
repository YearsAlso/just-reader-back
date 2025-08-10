package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.MessageChannelSubscriber;
import com.yearsalso.data.mapper.MessageChannelSubscriberMapper;
import com.yearsalso.data.service.IMessageChannelSubscriberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息-订阅者 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("mmsChannelSubscriberService")
public class MessageChannelSubscriberServiceImpl extends ServiceImpl<MessageChannelSubscriberMapper, MessageChannelSubscriber> implements IMessageChannelSubscriberService {

}
