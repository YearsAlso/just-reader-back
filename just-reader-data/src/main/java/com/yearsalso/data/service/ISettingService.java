package com.yearsalso.data.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yearsalso.data.entity.Setting;

/**
 * <p>
 * 内容-设置 服务类
 * </p>
 */
public interface ISettingService extends IService<Setting> {
    Setting selectOneBySettingKey(String settingKey);

    void updateBySettingKey(String settingKey, String settingValue);

    void updateOneBySettingKey(String settingKey, Setting setting);

    void deleteOneBySettingKey(String settingKey);
}
