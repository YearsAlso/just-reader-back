package com.yearsalso.file.manage;

import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.dto.FmsSettingDto;
import com.yearsalso.data.entity.CmsSetting;
import com.yearsalso.data.mapper.CmsSettingMapper;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author
 */
@Component
public class AliFileManage implements FileManage {

    @Autowired
    private CmsSettingMapper cmsSettingMapper;

    @Override
    public FmsSettingDto getFmsSetting() {

        CmsSetting setting = cmsSettingMapper.selectOneBySettingKey(StoreTypeConstant.ALI_OSS);
        if (setting == null || StrUtil.isBlank(setting.getSettingValue())) {
            throw new ApiException("您还未配置阿里云OSS存储");
        }
        return new Gson().fromJson(setting.getSettingValue(), FmsSettingDto.class);
    }

    @Override
    public String pathUpload(String filePath, String localPath) {
        FmsSettingDto os = getFmsSetting();
        OSSClient ossClient = new OSSClient(os.getHttp() + os.getEndpoint(), new DefaultCredentialProvider(os.getAccessKey(), os.getSecretKey()), null);
        ossClient.putObject(os.getBucket(), localPath, new File(filePath));
        ossClient.shutdown();
        return os.getHttp() + os.getBucket() + "." + os.getEndpoint() + "/" + localPath;
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String localPath, MultipartFile file) {

        FmsSettingDto os = getFmsSetting();
        OSSClient ossClient = new OSSClient(os.getHttp() + os.getEndpoint(), new DefaultCredentialProvider(os.getAccessKey(), os.getSecretKey()), null);
        ossClient.putObject(os.getBucket(), localPath, inputStream);
        ossClient.shutdown();
        return os.getHttp() + os.getBucket() + "." + os.getEndpoint() + "/" + localPath;
    }

    @Override
    public String renameFile(String fromKey, String toKey) {

        FmsSettingDto os = getFmsSetting();
        copyFile(fromKey, toKey);
        deleteFile(fromKey);
        return os.getHttp() + os.getBucket() + "." + os.getEndpoint() + "/" + toKey;
    }

    @Override
    public String copyFile(String fromKey, String toKey) {

        FmsSettingDto os = getFmsSetting();
        OSSClient ossClient = new OSSClient(os.getHttp() + os.getEndpoint(), new DefaultCredentialProvider(os.getAccessKey(), os.getSecretKey()), null);
        ossClient.copyObject(os.getBucket(), fromKey, os.getBucket(), toKey);
        ossClient.shutdown();
        return os.getHttp() + os.getBucket() + "." + os.getEndpoint() + "/" + toKey;
    }

    @Override
    public void deleteFile(String key) {

        FmsSettingDto os = getFmsSetting();
        OSSClient ossClient = new OSSClient(os.getHttp() + os.getEndpoint(), new DefaultCredentialProvider(os.getAccessKey(), os.getSecretKey()), null);
        ossClient.deleteObject(os.getBucket(), key);
        ossClient.shutdown();
    }

    public boolean doesObjectExist(String key) {
        FmsSettingDto os = getFmsSetting();
        OSSClient ossClient = new OSSClient(os.getHttp() + os.getEndpoint(), new DefaultCredentialProvider(os.getAccessKey(), os.getSecretKey()), null);
        boolean b = ossClient.doesObjectExist(os.getBucket(), key);
        ossClient.shutdown();
        return b;
    }

    @Override
    public void downloadFile(String path, HttpServletResponse response) {
        FmsSettingDto os = getFmsSetting();
        OSSClient ossClient = new OSSClient(os.getHttp() + os.getEndpoint(), new DefaultCredentialProvider(os.getAccessKey(), os.getSecretKey()), null);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = ossClient.getObject(os.getBucket(), path).getObjectContent();
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new ApiException("下载文件失败");
        } finally {
            ossClient.shutdown();
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                throw new ApiException("下载文件失败");
            }
        }
    }

    @Override
    public Double getRemainingSpace() {
        return 0.0;
    }
}
