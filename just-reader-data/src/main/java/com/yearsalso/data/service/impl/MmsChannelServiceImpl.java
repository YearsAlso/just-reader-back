package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.MessageChannel;
import com.yearsalso.data.mapper.MmsChannelMapper;
import com.yearsalso.data.service.IMmsChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息-通道 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("mmsChannelService")
public class MmsChannelServiceImpl extends ServiceImpl<MmsChannelMapper, MessageChannel> implements IMmsChannelService {

}
