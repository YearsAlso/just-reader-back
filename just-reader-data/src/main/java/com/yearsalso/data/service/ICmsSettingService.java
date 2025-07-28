package com.yearsalso.data.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yearsalso.data.entity.Setting;

/**
 * <p>
 * 内容-设置 服务类
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
public interface ICmsSettingService extends IService<Setting> {
    Setting selectOneBySettingKey(String settingKey);

    void updateBySettingKey(String settingKey, String settingValue);

    void updateOneBySettingKey(String settingKey, Setting setting);

    void deleteOneBySettingKey(String settingKey);
}
