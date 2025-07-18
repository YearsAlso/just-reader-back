package com.yearsalso.data.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yearsalso.data.entity.CmsDict;
import com.yearsalso.data.mapper.CmsDictMapper;
import com.yearsalso.data.service.ICmsDictService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内容-数据字典 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("cmsDictService")
public class CmsDictServiceImpl extends ServiceImpl<CmsDictMapper, CmsDict> implements ICmsDictService {

}
