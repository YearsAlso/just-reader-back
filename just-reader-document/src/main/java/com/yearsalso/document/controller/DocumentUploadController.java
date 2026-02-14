package com.yearsalso.document.controller;

import com.yearsalso.common.api.CommonResult;
import com.yearsalso.document.dto.UploadResponse;
import com.yearsalso.document.service.DocumentUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@Tag(name = "文档上传管理", description = "文档上传相关接口")
public class DocumentUploadController {

    @Autowired
    private DocumentUploadService documentUploadService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "单文件上传", description = "上传单个文档文件")
    public CommonResult<UploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        
        try {
            UploadResponse response = documentUploadService.uploadDocument(file, request);
            return CommonResult.success(response);
        } catch (Exception e) {
            return CommonResult.failed("上传失败: " + e.getMessage());
        }
    }
}