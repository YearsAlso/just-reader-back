package com.yearsalso.file.controller;


import com.yearsalso.common.api.CommonResult;
import com.yearsalso.common.api.ResultCode;
import com.yearsalso.common.constant.SettingsConstant;
import com.yearsalso.common.exception.ApiException;
import com.yearsalso.data.entity.Setting;
import com.yearsalso.data.entity.File;
import com.yearsalso.data.service.ICmsSettingService;
import com.yearsalso.data.service.IFmsFileService;
import com.yearsalso.file.FileManageFactory;
import com.yearsalso.file.utils.Base64DecodeMultipartFile;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

import static cn.hutool.core.date.DatePattern.PURE_DATE_PATTERN;

/**
 * 文件上传接口
 */
@Slf4j
@Controller
@Tag(name = "文件上传接口", description = "UploadController")
@RequestMapping("/upload")
@Transactional
public class UploadController {

    @Autowired
    private FileManageFactory fileManageFactory;

    @Autowired
    private ICmsSettingService cmsSettingService;

    @Autowired
    private IFmsFileService fmsFileService;

    /**
     * 获取存储类型
     *
     * @return
     */
    String getStoreType(Setting setting) {
        if (setting == null || StrUtil.isBlank(setting.getSettingValue())) {
            throw new ApiException("您还未配置OSS存储服务");
        }

        return setting.getSettingValue();
    }

    @Operation(summary = "文件上传")
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Parameters({
            @Parameter(name = "file", description = "文件"),
            @Parameter(name = "base64", description = "文件base64，为空不校验"),
            @Parameter(name = "filePath", description = "文件路径")
    })
    public CommonResult<File> upload(@RequestPart MultipartFile file,
                                     @RequestPart(required = false) String base64,
                                     @RequestPart(required = false) String filePath,
                                     @RequestParam(required = false, defaultValue = "true") Boolean isCover,
                                     @RequestParam(required = false, defaultValue = "false") Boolean pathContainsFileName
    ) {
        if (StrUtil.isNotBlank(base64)) {
            // base64上传
            file = Base64DecodeMultipartFile.base64Convert(base64);
        }

        Setting setting = cmsSettingService.selectOneBySettingKey(SettingsConstant.OSS_USED);
        if (setting == null) {
            return CommonResult.failed(ResultCode.FAILED, "Lost setting that your configured with OSS storage service");
        }

        String storeType = setting.getSettingValue();
        if (storeType.isEmpty()) {
            return CommonResult.failed(ResultCode.FAILED, "You are not configured with OSS storage service");
        }

        String result = "";
        String originalFilename = file.getOriginalFilename();
        String localPath;
        if (pathContainsFileName) {
            localPath = filePath;
        } else {
            if (StrUtil.isBlankOrUndefined(filePath)) {
                filePath = DateUtil.format(new Date(), PURE_DATE_PATTERN) + "/";
            } else {
                filePath = filePath.endsWith("/") ? filePath : filePath + "/";
            }

            localPath = filePath + originalFilename;
        }
        if (!isCover) {
            File fmsFile = fmsFileService.getByKey(Base64.encode(localPath));
            if (fmsFile != null) {
                return CommonResult.success(fmsFile);
            }
        }

        try {
            InputStream inputStream = file.getInputStream();

            // 上传至第三方云服务或服务器
            result = fileManageFactory
                    .getFileManage(storeType)
                    .inputStreamUpload(
                            inputStream,
                            localPath,
                            file
                    );

            File fmsFile = fmsFileService.getByKey(Base64.encode(localPath));

            if (fmsFile != null) {
                fmsFile.setUpdateAt(new Date());
                fmsFileService.updateById(fmsFile);
                return CommonResult.success(fmsFile);
            }

            fmsFile = new File()
                    // 指定ossParam或者为当前的第一个OSS配置
                    .setLocationPath(localPath)
                    // 保存数据信息至数据库
                    .setFileName(file.getOriginalFilename())
                    .setFileSize(file.getSize())
                    .setFileType(file.getContentType())
                    .setFileKey(Base64.encode(localPath))
                    .setUrlPath(result)
                    .setStoreType(storeType);
            fmsFileService.save(fmsFile);

            return CommonResult.success(fmsFile);
        } catch (Exception e) {
            log.error(e.toString());
            return CommonResult.failed(e.toString());
        }
    }

    /**
     * 获取文件key
     *
     * @param path
     * @return
     */
    String getFileKeyByPath(String path) {
        if (path.contains("\\")) {
            path = path.replace("\\", "/");
        }
        return path.substring(path.lastIndexOf("/") + 1);
    }

    @Operation(summary = "Folder upload")
    @PostMapping(value = "/folder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public CommonResult<Object> uploadFolder(@RequestPart MultipartFile[] files,
                                             @RequestPart(required = false) String base64,
                                             @RequestPart(required = false) String folderPath) {
        if (files == null || files.length == 0) {
            return CommonResult.failed("No files to upload");
        }

        if (StrUtil.isBlankOrUndefined(folderPath)) {
            folderPath = DateUtil.format(new Date(), PURE_DATE_PATTERN) + "/";
        } else {
            folderPath = folderPath.endsWith("/") ? folderPath : folderPath + "/";
        }

        Setting setting = cmsSettingService.selectOneBySettingKey(SettingsConstant.OSS_USED);
        if (setting == null) {
            return CommonResult.failed(ResultCode.FAILED, "Lost setting that your configured with OSS storage service");
        }

        String storeType = setting.getSettingValue();
        if (storeType.isEmpty()) {
            return CommonResult.failed(ResultCode.FAILED, "You are not configured with OSS storage service");
        }

        for (MultipartFile file : files) {
            String result = "";
            String originalFilename = file.getOriginalFilename();
            String localPath = folderPath + originalFilename;
            try {
                InputStream inputStream = file.getInputStream();

                // Upload to third-party cloud service or server
                result = fileManageFactory
                        .getFileManage(storeType)
                        .inputStreamUpload(
                                inputStream,
                                localPath,
                                file
                        );

                File newFile = new File()
                        .setLocationPath(localPath)
                        .setFileName(file.getOriginalFilename())
                        .setFileSize(file.getSize())
                        .setFileType(file.getContentType())
                        .setFileKey(Base64.encode(localPath))
                        .setUrlPath(result)
                        .setStoreType(storeType);
                fmsFileService.save(newFile);

            } catch (Exception e) {
                log.error(e.toString());
                return CommonResult.failed(e.toString());
            }
        }

        return CommonResult.success("Folder uploaded successfully");
    }
}
