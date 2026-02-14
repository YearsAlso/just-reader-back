package com.yearsalso.document.service.impl;

import com.yearsalso.document.dto.UploadResponse;
import com.yearsalso.document.service.DocumentUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class DocumentUploadServiceImpl implements DocumentUploadService {
    
    @Override
    public UploadResponse uploadDocument(MultipartFile file, HttpServletRequest request) {
        return UploadResponse.builder()
                .documentId(System.currentTimeMillis())
                .fileKey(UUID.randomUUID().toString())
                .fileSize(file.getSize())
                .parseStatus("pending")
                .estimatedParseTime(10)
                .success(true)
                .build();
    }
}