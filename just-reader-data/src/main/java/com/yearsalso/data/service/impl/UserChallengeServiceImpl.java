package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.UserChallenge;
import com.yearsalso.data.mapper.UserChallengeMapper;
import com.yearsalso.data.service.IUserChallengeService;
import org.springframework.stereotype.Service;

/**
 * 用户挑战服务实现类
 * <p>
 * 该类实现了对用户挑战信息的基本操作，包括创建、查询、更新和删除等操作
 * </p>
 *
 * @author yearsalso
 * @since 2025-08-10
 */
@Service
public class UserChallengeServiceImpl extends ServiceImpl<UserChallengeMapper, UserChallenge> implements IUserChallengeService {

}