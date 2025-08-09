package com.yearsalso.file.manage;

//import io.minio.MinioClient;

import com.yearsalso.common.constant.StoreTypeConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.dto.FmsSettingDto;
import com.yearsalso.data.entity.Setting;
import com.yearsalso.data.mapper.SettingMapper;
import com.yearsalso.file.dto.MinioBucketPolicyConfigDto;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.minio.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Minio文件管理
 *
 * @Author els
 * @Date 2023-08-22
 */
@Slf4j
@Component
public class MinioFileManage implements FileManage {

    @Autowired
    private SettingMapper settingDao;

    @Override
    public FmsSettingDto getFmsSetting() {
        Setting setting = settingDao.selectOneBySettingKey(StoreTypeConstant.MINIO_OSS);
        String settingValue = setting.getSettingValue();
        if (StrUtil.isBlankOrUndefined(settingValue) || !JSONUtil.isTypeJSON(settingValue)) {
            throw new ApiException("您还未配置MinIO存储");
        }

        FmsSettingDto result = JSONUtil.toBean(settingValue, FmsSettingDto.class);

        return result;
    }

    /**
     * 如果存储桶不存在 创建存储通
     *
     * @param os
     * @param minioClient
     * @throws Exception
     */
    public void checkBucket(FmsSettingDto os, MinioClient minioClient) throws Exception {
        String bucket = os.getBucket();
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        // 如果存储桶不存在 创建存储通
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            // 设置隐私权限 公开读
            String policy = "{\n" +
                    "    \"Statement\": [\n" +
                    "        {\n" +
                    "            \"Action\": [\n" +
                    "                \"s3:GetBucketLocation\",\n" +
                    "                \"s3:ListBucket\"\n" +
                    "            ],\n" +
                    "            \"Effect\": \"Allow\",\n" +
                    "            \"Principal\": \"*\",\n" +
                    "            \"Resource\": \"arn:aws:s3:::" + bucket + "\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"Action\": \"s3:GetObject\",\n" +
                    "            \"Effect\": \"Allow\",\n" +
                    "            \"Principal\": \"*\",\n" +
                    "            \"Resource\": \"arn:aws:s3:::" + bucket + "/*\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Version\": \"2012-10-17\"\n" +
                    "}\n";
            MinioBucketPolicyConfigDto.Statement statement = MinioBucketPolicyConfigDto.Statement.builder()
                    .Effect("Allow")
                    .Principal("*")
                    .Action("s3:GetObject")
                    .Resource("arn:aws:s3:::" + bucket + "/*.**").build();
            MinioBucketPolicyConfigDto bucketPolicyConfigDto = MinioBucketPolicyConfigDto.builder()
                    .Version("2012-10-17")
                    .Statement(CollUtil.toList(statement))
                    .build();

            SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                    .bucket(bucket)
                    .config(JSONUtil.toJsonStr(bucketPolicyConfigDto))
                    .build();
            minioClient.setBucketPolicy(setBucketPolicyArgs);
        }
    }

    @Override
    public String pathUpload(String filePath, String localPath) {
        FmsSettingDto fmsSetting = getFmsSetting();
        try {
            String endpoint = fmsSetting.getHttp() + fmsSetting.getEndpoint();
            log.info("endpoint: {}", endpoint);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(fmsSetting.getAccessKey(), fmsSetting.getSecretKey())
                    .build();
            checkBucket(fmsSetting, minioClient);
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(fmsSetting.getBucket())
                            .object(filePath + "/" + localPath)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new ApiException("上传出错，请检查MinIO配置");
        }
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + localPath;
    }

    @Override
    public String inputStreamUpload(
            InputStream inputStream,
            String localPath,
            MultipartFile file
    ) {
        FmsSettingDto fmsSetting = getFmsSetting();
        try {
            String endpoint = fmsSetting.getHttp() + fmsSetting.getEndpoint();
            log.info("endpoint: {}", endpoint);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(fmsSetting.getAccessKey(), fmsSetting.getSecretKey())
                    .build();

            checkBucket(fmsSetting, minioClient);
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(fmsSetting.getBucket())
                            .object(localPath)
                            .stream(inputStream, file.getSize(), -1)
                            .build()
            );
        } catch (Exception e) {
            throw new ApiException("上传出错，请检查MinIO配置:  " + e.getLocalizedMessage());
        }
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + localPath;
    }

    @Override
    public String renameFile(String fromKey, String toKey) {
        FmsSettingDto fmsSetting = getFmsSetting();
        copyFile(fromKey, toKey);
        deleteFile(fromKey);
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + toKey;
    }

    @Override
    public String copyFile(String fromKey, String toKey) {
        FmsSettingDto fmsSetting = getFmsSetting();
        try {
            MinioClient minioClient = MinioClient
                    .builder()
                    .endpoint(fmsSetting.getHttp() + fmsSetting.getEndpoint())
                    .credentials(fmsSetting.getAccessKey(), fmsSetting.getSecretKey())
                    .build();
            checkBucket(fmsSetting, minioClient);
            minioClient.copyObject(
                    CopyObjectArgs
                            .builder()
                            .bucket(fmsSetting.getBucket())
                            .object(toKey)
                            .source(
                                    CopySource
                                            .builder()
                                            .bucket(fmsSetting.getBucket())
                                            .object(fromKey)
                                            .build()
                            )
                            .build()
            );
        } catch (Exception e) {
            throw new ApiException("拷贝文件出错，请检查MinIO配置");
        }
        return fmsSetting.getHttp() + fmsSetting.getEndpoint() + "/" + fmsSetting.getBucket() + "/" + toKey;
    }

    @Override
    public void deleteFile(String key) {
        FmsSettingDto os = getFmsSetting();
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(os.getHttp() + os.getEndpoint())
                    .credentials(os.getAccessKey(), os.getSecretKey())
                    .build();
            checkBucket(os, minioClient);
            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(os.getBucket())
                            .object(key)
                            .build()
            );
        } catch (Exception e) {
            throw new ApiException("删除文件出错，请检查MinIO配置");
        }
    }

    MinioClient getMinioClient() {
        FmsSettingDto fmsSetting = getFmsSetting();
        return MinioClient.builder()
                .endpoint(fmsSetting.getHttp() + fmsSetting.getEndpoint())
                .credentials(fmsSetting.getAccessKey(), fmsSetting.getSecretKey())
                .build();
    }

    MinioClient getMinioClient(FmsSettingDto fmsSetting) {
        return MinioClient.builder()
                .endpoint(fmsSetting.getHttp() + fmsSetting.getEndpoint())
                .credentials(fmsSetting.getAccessKey(), fmsSetting.getSecretKey())
                .build();
    }

    /**
     * 下载文件
     *
     * @param path 文件路径
     */
    @Override
    public void downloadFile(
            String path,
            HttpServletResponse response
    ) {
        FmsSettingDto fmsSetting = getFmsSetting();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String endpoint = fmsSetting.getHttp() + fmsSetting.getEndpoint();
            log.info("endpoint: {}", endpoint);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(fmsSetting.getAccessKey(), fmsSetting.getSecretKey())
                    .build();

            inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(fmsSetting.getBucket())
                            .object(path)
                            .build()
            );

            String filename = path;
            if(filename.contains("/")){
                filename = filename.split("/")[filename.split("/").length - 1];
            }

            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            outputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public Double getRemainingSpace() {
        return 0.0;
    }
}
