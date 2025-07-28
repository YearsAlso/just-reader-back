package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.Message;
import com.yearsalso.data.mapper.MmsMessageMapper;
import com.yearsalso.data.service.IMmsMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息-消息 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("mmsMessageService")
public class MmsMessageServiceImpl extends ServiceImpl<MmsMessageMapper, Message> implements IMmsMessageService {

}
