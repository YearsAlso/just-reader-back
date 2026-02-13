package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.UserSurvey;
import com.yearsalso.data.mapper.UserSurveyMapper;
import com.yearsalso.data.service.IUserSurveyService;
import org.springframework.stereotype.Service;

/**
 * 用户审查服务实现类
 * <p>
 * 该类实现了对用户审查信息的基本操作，包括创建、查询、更新和删除等操作
 * </p>
 *
 * @author yearsalso
 * @since 2025-08-10
 */
@Service
public class UserSurveyServiceImpl extends ServiceImpl<UserSurveyMapper, UserSurvey> implements IUserSurveyService {

}