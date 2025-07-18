package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.CmsSetting;
import com.yearsalso.data.mapper.CmsSettingMapper;
import com.yearsalso.data.service.ICmsSettingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 内容-设置 服务实现类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Service("cmsSettingService")
@EnableCaching
public class CmsSettingServiceImpl extends ServiceImpl<CmsSettingMapper, CmsSetting> implements ICmsSettingService {


    @Override
    @Cacheable(value = "cmsSettingService", key = "#settingKey")
    public CmsSetting selectOneBySettingKey(String settingKey) {
        if (settingKey == null) {
            return null;
        }

        CmsSetting cmsSetting = null;
        try {
            cmsSetting = this.baseMapper.selectOneBySettingKey(settingKey);
        } catch (Exception e) {
            return null;
        }

        return cmsSetting;
    }

    @Override
    @CacheEvict(value = "cmsSettingService", key = "#settingKey")
    public void updateBySettingKey(String settingKey, String settingValue) {
        if (settingKey == null || settingValue == null) {
            return;
        }

        try {
            this.baseMapper.updateBySettingKey(settingKey, settingValue);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    @CacheEvict(value = "cmsSettingService", key = "#settingKey")
    public void updateOneBySettingKey(String settingKey, CmsSetting setting) {
        if (settingKey == null || setting == null) {
            return;
        }

        try {
            this.baseMapper.updateOneBySettingKey(settingKey, setting);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    @CacheEvict(value = "cmsSettingService", key = "#settingKey")
    public void deleteOneBySettingKey(String settingKey) {
        if (settingKey == null) {
            return;
        }

        try {
            this.baseMapper.deleteOneBySettingKey(settingKey);
        } catch (Exception e) {
            return;
        }
    }
}
