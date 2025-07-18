package com.yearsalso.file;


import cn.hutool.core.util.StrUtil;
import com.yearsalso.common.constant.SettingsConstant;
import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.mapper.FmsFileMapper;
import com.yearsalso.data.mapper.CmsSettingMapper;
import com.yearsalso.data.entity.CmsSetting;
import com.yearsalso.file.manage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工厂模式
 *
 * @author
 */
@Component
public class FileManageFactory {

    @Autowired
    private FmsFileMapper fmsFileMapper;

    @Autowired
    private CmsSettingMapper cmsSettingMapper;

    @Autowired
    private QiniuFileManage qiniuFileManage;

    @Autowired
    private AliFileManage aliFileManage;

    @Autowired
    private TencentFileManage tencentFileManage;

    @Autowired
    private MinioFileManage minioFileManage;

    @Autowired
    private LocalFileManage localFileManage;

    /**
     * 使用配置的服务上传时location传入null 管理文件时需传入存储位置location
     *
     * @param type
     * @return
     */
    public FileManage getFileManage(String type) {
        return switch (type) {
            case StoreTypeConstant.QINIU_OSS -> qiniuFileManage; //qiniuFileManage;
            case StoreTypeConstant.ALI_OSS -> aliFileManage;
            case StoreTypeConstant.TENCENT_OSS -> tencentFileManage; //tencentFileManage;
            case StoreTypeConstant.MINIO_OSS -> minioFileManage; //minioFileManage;
            case StoreTypeConstant.LOCAL_OSS -> localFileManage;
            default -> throw new ApiException("暂不支持该存储配置，请检查配置");
        };
    }

    public FileManage getFileManage() {
        CmsSetting setting = cmsSettingMapper.selectOneBySettingKey(SettingsConstant.OSS_USED);
        if (setting == null || StrUtil.isBlank(setting.getSettingValue())) {
            setting = new CmsSetting();
            setting.setSettingKey(SettingsConstant.OSS_USED);
            setting.setSettingValue(StoreTypeConstant.LOCAL_OSS);
            cmsSettingMapper.insert(setting);
        }
        String type = setting.getSettingValue();
        return this.getFileManage(type);
    }
}
