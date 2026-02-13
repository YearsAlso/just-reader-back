package com.yearsalso.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.Flashcard;
import com.yearsalso.data.mapper.FlashcardMapper;
import com.yearsalso.data.service.IFlashcardService;
import org.springframework.stereotype.Service;

/**
 * 记忆卡服务实现类
 * <p>
 * 该类继承了MyBatis-Plus的ServiceImpl类，实现了IFlashcardService接口，
 * 提供了对Flashcard实体的基本CRUD操作
 * </p>
 *
 * @author 
 * @since 
 */
@Service
public class FlashcardServiceImpl extends ServiceImpl<FlashcardMapper, Flashcard> implements IFlashcardService {

}