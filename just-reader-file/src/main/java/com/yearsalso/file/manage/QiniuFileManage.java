package com.yearsalso.file.manage;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.dto.FmsSettingDto;
import com.yearsalso.data.entity.Setting;
import com.yearsalso.data.mapper.SettingMapper;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author
 */
@Slf4j
@Component
public class QiniuFileManage implements FileManage {

    @Autowired
    private SettingMapper settingService;

    @Override
    public FmsSettingDto getFmsSetting() {
        Setting setting = settingService.selectOneBySettingKey(StoreTypeConstant.TENCENT_OSS);
        if (setting == null || StrUtil.isBlank(setting.getSettingValue())) {
            throw new ApiException("您还未配置腾讯云COS存储");
        }
        return new Gson().fromJson(setting.getSettingValue(), FmsSettingDto.class);
    }

    public Configuration getConfiguration(Integer zone) {

        Configuration cfg = null;
        if (zone.equals(StoreTypeConstant.ZONE_ZERO)) {
            cfg = new Configuration(Region.region0());
        } else if (zone.equals(StoreTypeConstant.ZONE_ONE)) {
            cfg = new Configuration(Region.region1());
        } else if (zone.equals(StoreTypeConstant.ZONE_TWO)) {
            cfg = new Configuration(Region.region2());
        } else if (zone.equals(StoreTypeConstant.ZONE_THREE)) {
            cfg = new Configuration(Region.regionNa0());
        } else if (zone.equals(StoreTypeConstant.ZONE_FOUR)) {
            cfg = new Configuration(Region.regionAs0());
        } else {
            cfg = new Configuration(Region.autoRegion());
        }
        return cfg;
    }

    public UploadManager getUploadManager(Configuration cfg) {

        UploadManager uploadManager = new UploadManager(cfg);
        return uploadManager;
    }

    @Override
    public String pathUpload(String filePath, String localPath) {

        FmsSettingDto os = getFmsSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        String upToken = auth.uploadToken(os.getBucket());
        try {
            Response response = getUploadManager(getConfiguration(os.getZone())).put(filePath, localPath, upToken);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return os.getHttp() + os.getEndpoint() + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            throw new ApiException("上传文件出错，请检查七牛云配置，" + r.toString());
        }
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String localPath, MultipartFile file) {

        FmsSettingDto os = getFmsSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        String upToken = auth.uploadToken(os.getBucket());
        try {
            Response response = getUploadManager(getConfiguration(os.getZone())).put(inputStream, localPath, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return os.getHttp() + os.getEndpoint() + "/" + putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            throw new ApiException("上传文件出错，请检查七牛云配置，" + r.toString());
        }
    }


    @Override
    public String renameFile(String fromKey, String toKey) {

        FmsSettingDto os = getFmsSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, getConfiguration(os.getZone()));
        try {
            bucketManager.move(os.getBucket(), fromKey, os.getBucket(), toKey);
            return os.getHttp() + os.getEndpoint() + "/" + toKey;
        } catch (QiniuException ex) {
            throw new ApiException("重命名文件失败，" + ex.response.toString());
        }
    }

    @Override
    public String copyFile(String fromKey, String toKey) {

        FmsSettingDto os = getFmsSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, getConfiguration(os.getZone()));
        try {
            bucketManager.copy(os.getBucket(), fromKey, os.getBucket(), toKey);
            return os.getHttp() + os.getEndpoint() + "/" + toKey;
        } catch (QiniuException ex) {
            throw new ApiException("复制文件失败，" + ex.response.toString());
        }
    }

    @Override
    public void deleteFile(String key) {

        FmsSettingDto os = getFmsSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, getConfiguration(os.getZone()));
        try {
            bucketManager.delete(os.getBucket(), key);
        } catch (QiniuException ex) {
            throw new ApiException("删除文件失败，" + ex.response.toString());
        }
    }

    @Override
    public void downloadFile(String path, HttpServletResponse response) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        FmsSettingDto os = getFmsSetting();
        Auth auth = Auth.create(os.getAccessKey(), os.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, getConfiguration(os.getZone()));

        try {
            inputStream = bucketManager.fetchResponse(path, os.getBucket(), path).bodyStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new ApiException("下载文件失败");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

    }

    @Override
    public Double getRemainingSpace() {
        return 0.0;
    }
}
