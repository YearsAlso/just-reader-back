package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.UserLog;
import com.yearsalso.data.mapper.UmsLogMapper;
import com.yearsalso.data.service.IUmsLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户-日志 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("umsLogService")
public class UmsLogServiceImpl extends ServiceImpl<UmsLogMapper, UserLog> implements IUmsLogService {

}
