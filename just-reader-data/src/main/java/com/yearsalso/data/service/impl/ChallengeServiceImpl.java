package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.Challenge;
import com.yearsalso.data.mapper.ChallengeMapper;
import com.yearsalso.data.service.IChallengeService;
import org.springframework.stereotype.Service;

/**
 * 挑战服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IChallengeService接口，
 * 提供了对Challenge实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class ChallengeServiceImpl extends ServiceImpl<ChallengeMapper, Challenge> implements IChallengeService {

}