package com.yearsalso.file.manage;

import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.dto.FmsSettingDto;
import com.yearsalso.data.entity.CmsSetting;
import com.yearsalso.data.mapper.CmsSettingMapper;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.BlobProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Component
public class AzureBlobFileManage implements FileManage {
    @Autowired
    private CmsSettingMapper settingDao;

    private BlobServiceClient getBlobServiceClient(FmsSettingDto fmsSetting) {
        return new BlobServiceClientBuilder()
                .endpoint(fmsSetting.getHttp() + fmsSetting.getEndpoint())
                .sasToken(fmsSetting.getAccessKey())
                .buildClient();
    }

    @Override
    public FmsSettingDto getFmsSetting() {
        // Implement this method to return your FmsSettingDto
        CmsSetting setting = settingDao.selectOneBySettingKey(StoreTypeConstant.AZURE_BLOB_OSS);
        String settingValue = setting.getSettingValue();
        if (StrUtil.isBlankOrUndefined(settingValue) || !JSONUtil.isTypeJSON(settingValue)) {
            throw new ApiException("您还未配置MinIO存储");
        }

        FmsSettingDto result = JSONUtil.toBean(settingValue, FmsSettingDto.class);

        return result;
    }

    @Override
    public String pathUpload(String filePath, String localPath) {
        FmsSettingDto fmsSetting = getFmsSetting();
        BlobServiceClient blobServiceClient = getBlobServiceClient(fmsSetting);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(fmsSetting.getBucket());
        BlobClient blobClient = containerClient.getBlobClient(localPath);

        blobClient.uploadFromFile(filePath, true);
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + localPath;
    }

    @Override
    public String inputStreamUpload(InputStream inputStream, String localPath, MultipartFile file) {
        FmsSettingDto fmsSetting = getFmsSetting();
        BlobServiceClient blobServiceClient = getBlobServiceClient(fmsSetting);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(fmsSetting.getBucket());
        BlobClient blobClient = containerClient.getBlobClient(localPath);

        blobClient.upload(inputStream, file.getSize(), true);
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + localPath;
    }

    @Override
    public String renameFile(String fromKey, String toKey) {
        copyFile(fromKey, toKey);
        deleteFile(fromKey);
        FmsSettingDto fmsSetting = getFmsSetting();
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + toKey;
    }

    @Override
    public String copyFile(String fromKey, String toKey) {
        FmsSettingDto fmsSetting = getFmsSetting();
        BlobServiceClient blobServiceClient = getBlobServiceClient(fmsSetting);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(fmsSetting.getBucket());
        BlobClient sourceBlobClient = containerClient.getBlobClient(fromKey);
        BlobClient targetBlobClient = containerClient.getBlobClient(toKey);

        targetBlobClient.beginCopy(sourceBlobClient.getBlobUrl(), null);
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + toKey;
    }

    @Override
    public void deleteFile(String key) {
        FmsSettingDto fmsSetting = getFmsSetting();
        BlobServiceClient blobServiceClient = getBlobServiceClient(fmsSetting);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(fmsSetting.getBucket());
        BlobClient blobClient = containerClient.getBlobClient(key);

        blobClient.delete();
    }

    @Override
    public void downloadFile(String path, HttpServletResponse response) {
        FmsSettingDto fmsSetting = getFmsSetting();
        BlobServiceClient blobServiceClient = getBlobServiceClient(fmsSetting);
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(fmsSetting.getBucket());
        BlobClient blobClient = containerClient.getBlobClient(path);

        try (InputStream inputStream = blobClient.openInputStream();
             OutputStream outputStream = response.getOutputStream()) {

            response.setHeader("Content-Disposition", "attachment;filename=" + path);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from Azure Blob Storage", e);
        }
    }

    @Override
    public Double getRemainingSpace() {
        return 0.0;
    }
}