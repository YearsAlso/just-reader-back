package com.yearsalso.data.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yearsalso.data.entity.CmsSetting;

/**
 * <p>
 * 内容-设置 服务类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
public interface ICmsSettingService extends IService<CmsSetting> {
    CmsSetting selectOneBySettingKey(String settingKey);

    void updateBySettingKey(String settingKey, String settingValue);

    void updateOneBySettingKey(String settingKey, CmsSetting setting);

    void deleteOneBySettingKey(String settingKey);
}
