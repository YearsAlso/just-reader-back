package com.yearsalso.data.service.impl;

import com.yearsalso.data.entity.Setting;
import com.yearsalso.data.mapper.SettingMapper;
import com.yearsalso.data.service.ISettingService;
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
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements ISettingService {


    @Override
    @Cacheable(value = "cmsSettingService", key = "#settingKey")
    public Setting selectOneBySettingKey(String settingKey) {
        if (settingKey == null) {
            return null;
        }

        Setting cmsSetting = null;
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
    public void updateOneBySettingKey(String settingKey, Setting setting) {
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
