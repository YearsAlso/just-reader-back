package com.yearsalso.document.service;

import com.yearsalso.document.dto.UploadResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentUploadService {
    UploadResponse uploadDocument(MultipartFile file, HttpServletRequest request);
}