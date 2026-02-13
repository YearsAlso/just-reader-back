package com.yearsalso.client.modules.bookUploadPage;

import com.yearsalso.client.feign.FeignFileService;
import com.yearsalso.common.api.CommonResult;
import com.yearsalso.data.entity.File;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/bookUploadPage")
@Tag(name = "书籍上传", description = "BookUploadPageController")
public class BookUploadPageController {
    @Resource(name = "bookUploadPageServiceImpl")
    private IBookUploadPageService bookUploadPageService;

    @Resource
    FeignFileService feignFileService;


    @Operation(summary = "单个文件上传接口")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Parameters({
            @Parameter(name = "file", description = "文件")
    })
    public CommonResult<File> uploadFile(@RequestPart MultipartFile file, @RequestPart String filePath) throws IOException {
        try {
            return feignFileService.uploadFile(file, null, "skeyepro-admin/" + filePath);
        } catch (Exception e) {
            log.error("uploadFile error:{}", e.getMessage());
            return CommonResult.failed("Upload failed");
        }
    }


    // TODO: 从其他链接导入
    @Operation(summary = "从其他链接导入")
    @PostMapping("/importFromOtherLink")
    @Parameters({
            @Parameter(name = "link", description = "其他链接"),
            @Parameter(name = "filePath", description = "文件路径")
    })
    @ResponseBody
    public CommonResult<File> importFromOtherLink(@RequestParam String link, @RequestParam String filePath) {
        try {
            return bookUploadPageService.importFromOtherLink(link, filePath);
        } catch (Exception e) {
            log.error("importFromOtherLink error:{}", e.getMessage());
            return CommonResult.failed("Import failed");
        }
    }

    // TODO: 从云存储导入
    @Operation(summary = "从云存储导入")
    @PostMapping("/importFromCloudStorage")
    @Parameters({
            @Parameter(name = "link", description = "云存储链接"),
            @Parameter(name = "filePath", description = "文件路径"),
            @Parameter(name = "cloudStorageType", description = "云存储类型")
    })
    @ResponseBody
    public CommonResult<File> importFromCloudStorage(@RequestParam String link, @RequestParam String filePath, @RequestParam String cloudStorageType) {
        try {
            return bookUploadPageService.importFromCloudStorage(link, filePath);
        } catch (Exception e) {
            log.error("importFromCloudStorage error:{}", e.getMessage());
            return CommonResult.failed("Import failed");
        }
    }


}
