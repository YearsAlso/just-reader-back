package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.FmsFileTag;
import com.yearsalso.data.mapper.FmsFileTagMapper;
import com.yearsalso.data.service.IFmsFileTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件-文件标签 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("fmsFileTagService")
public class FmsFileTagServiceImpl extends ServiceImpl<FmsFileTagMapper, FmsFileTag> implements IFmsFileTagService {

}
