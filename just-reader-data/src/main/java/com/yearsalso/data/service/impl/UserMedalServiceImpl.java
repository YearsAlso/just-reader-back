package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.UserMedal;
import com.yearsalso.data.mapper.UserMedalMapper;
import com.yearsalso.data.service.IUserMedalService;
import org.springframework.stereotype.Service;

/**
 * 用户勋章服务实现类
 * <p>
 * 该类实现了对用户勋章信息的基本操作，包括创建、查询、更新和删除等操作
 * </p>
 *
 * @author yearsalso
 * @since 2025-08-10
 */
@Service
public class UserMedalServiceImpl extends ServiceImpl<UserMedalMapper, UserMedal> implements IUserMedalService {

}