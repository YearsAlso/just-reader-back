package com.yearsalso.client.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

public class MultipartFileUtil {

    public static String encodeToBase64(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();
        String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
        String header = "data:" + file.getContentType() + ";base64,";
        return header + base64Encoded;
    }
}