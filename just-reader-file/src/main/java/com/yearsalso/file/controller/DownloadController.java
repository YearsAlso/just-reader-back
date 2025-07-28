package com.yearsalso.file.controller;


import com.yearsalso.common.constant.SettingsConstant;
import com.yearsalso.data.entity.File;
import com.yearsalso.data.entity.Setting;
import com.yearsalso.data.service.IFmsFileService;
import com.yearsalso.data.service.ICmsSettingService;
import com.yearsalso.file.FileManageFactory;
import cn.hutool.core.codec.Base64;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 文件下载接口
 */
@Slf4j
@Controller
@Tag(name = "文件下载接口", description = "DownloadController")
@RequestMapping("/download")
@Transactional
public class DownloadController {

    @Autowired
    private RestTemplate restTemplate;

    @Resource(name = "fmsFileService")
    private IFmsFileService fmsFileService;

    @Autowired
    private FileManageFactory fileManageFactory;

    @Autowired
    ICmsSettingService cmsSettingService;

    /**
     * 根据文件ID下载文件
     *
     * @param id
     * @param response
     * @return
     */
    @Operation(summary = "根据文件ID下载文件")
    @GetMapping("/file")
    @Parameters({
            @Parameter(name = "id", description = "文件ID", required = true),
    })
    public Object downloadFile(@RequestParam("id") String id, HttpServletResponse response) {
        try {
            // 从数据库中获取文件信息
            File file = fmsFileService.getById(id);
            if (file == null) {
                log.error("file is not exist");
                return null;
            }

            // 获取文件的 URL 地址
            String fileUrl = file.getLocationPath();
            fileManageFactory
                    .getFileManage(file.getStoreType())
                    .downloadFile(fileUrl, response);

        } catch (Exception e) {
            log.error("file download failed", e);
        }

        return null;
    }

    @Operation(summary = "根据文件Key下载文件")
    @GetMapping("/fileByKey")
    @Parameters({
            @Parameter(name = "key", description = "文件Key", required = true),
            @Parameter(name = "response", description = "HttpServletResponse", required = true)
    })
    public Object downloadFileByKey(@RequestParam("key") String key, HttpServletResponse response) {
        try {
            // 从数据库中获取文件信息
            File file = fmsFileService.getByKey(key);

            String fileUrl;

            String storeType;
            if (file == null) {
                log.warn("file is not exist");
                fileUrl = Base64.decodeStr(key);
                Setting setting = cmsSettingService.selectOneBySettingKey(SettingsConstant.OSS_USED);
                storeType = setting.getSettingValue();
            } else {
                // 获取文件的 URL 地址
                fileUrl = file.getLocationPath();
                storeType = file.getStoreType();
            }

            fileManageFactory
                    .getFileManage(storeType)
                    .downloadFile(fileUrl, response);

        } catch (Exception e) {
            log.error("file download failed", e);

        }

        return null;
    }
}