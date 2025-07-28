package com.yearsalso.file.manage;

import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.dto.FmsSettingDto;
import com.yearsalso.data.entity.Setting;
import com.yearsalso.data.mapper.CmsSettingMapper;
import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Copy;
import com.qcloud.cos.transfer.TransferManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author
 */
@Slf4j
@Component
public class TencentFileManage implements FileManage{

    @Autowired
    private CmsSettingMapper settingService;


    @Override
    public FmsSettingDto getFmsSetting() {
        Setting setting = settingService.selectOneBySettingKey(StoreTypeConstant.TENCENT_OSS);
        if (setting == null || StrUtil.isBlank(setting.getSettingValue())) {
            throw new ApiException("您还未配置腾讯云COS存储");
        }
        return new Gson().fromJson(setting.getSettingValue(), FmsSettingDto.class);
    }

    @Override
    public String pathUpload(String filePath, String localPath) {

        FmsSettingDto os = getFmsSetting();

        COSCredentials cred = new BasicCOSCredentials(os.getAccessKey(), os.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(os.getBucketRegion()));
        COSClient cosClient = new COSClient(cred, clientConfig);

        PutObjectRequest putObjectRequest = new PutObjectRequest(os.getBucket(), localPath, new File(filePath));
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        return os.getHttp() + os.getEndpoint() + "/" + localPath;
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String localPath, MultipartFile file) {

        FmsSettingDto os = getFmsSetting();

        COSCredentials cred = new BasicCOSCredentials(os.getAccessKey(), os.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(os.getBucketRegion()));
        COSClient cosClient = new COSClient(cred, clientConfig);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(os.getBucket(), localPath, inputStream, objectMetadata);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        return os.getHttp() + os.getEndpoint() + "/" + localPath;
    }

    @Override
    public String renameFile(String fromKey, String toKey) {

        FmsSettingDto os = getFmsSetting();
        copyFile(fromKey, toKey);
        deleteFile(fromKey);
        return os.getHttp() + os.getEndpoint() + "/" + toKey;
    }

    @Override
    public String copyFile(String fromKey, String toKey) {

        FmsSettingDto os = getFmsSetting();

        COSCredentials cred = new BasicCOSCredentials(os.getAccessKey(), os.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(os.getBucketRegion()));
        COSClient cosClient = new COSClient(cred, clientConfig);

        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(os.getBucket(), fromKey, os.getBucket(), toKey);

        TransferManager transferManager = new TransferManager(cosClient);
        try {
            Copy copy = transferManager.copy(copyObjectRequest, cosClient, null);
            CopyResult copyResult = copy.waitForCopyResult();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("复制文件失败");
        }
        transferManager.shutdownNow();
        cosClient.shutdown();
        return os.getHttp() + os.getEndpoint() + "/" + toKey;
    }

    @Override
    public void deleteFile(String key) {

        FmsSettingDto os = getFmsSetting();

        COSCredentials cred = new BasicCOSCredentials(os.getAccessKey(), os.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(os.getBucketRegion()));
        COSClient cosClient = new COSClient(cred, clientConfig);

        cosClient.deleteObject(os.getBucket(), key);
        cosClient.shutdown();
    }

    @Override
    public void downloadFile(String path, HttpServletResponse response) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        FmsSettingDto os = getFmsSetting();

        COSCredentials cred = new BasicCOSCredentials(os.getAccessKey(), os.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(os.getBucketRegion()));
        COSClient cosClient = new COSClient(cred, clientConfig);

        try {
            inputStream = cosClient.getObject(os.getBucket(), path).getObjectContent();
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new ApiException("下载文件失败");
        } finally {
            cosClient.shutdown();
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
