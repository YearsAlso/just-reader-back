package com.yearsalso.data.mapper;

import com.yearsalso.data.entity.Setting;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 内容-设置 Mapper 接口
 * </p>
 *
 * @author els
 * @since 2024-08-12
 */
@Mapper
public interface CmsSettingMapper extends BaseMapper<Setting> {
    Setting selectOneBySettingKey(String settingKey);

    void updateBySettingKey(String settingKey, String settingValue);

    void updateOneBySettingKey(String settingKey, Setting setting);

    void deleteOneBySettingKey(String settingKey);
}
