package com.yearsalso.client.feign;

import com.yearsalso.common.api.CommonResult;
import com.yearsalso.data.entity.File;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "just-reader-file")
public interface FeignFileService {
    @PostMapping(value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CommonResult<File> uploadFile(@RequestPart MultipartFile file,
                                  @RequestPart(required = false) String base64,
                                  @RequestPart(required = false) String filePath
    );

    @PostMapping(value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CommonResult<File> uploadFile(@RequestPart MultipartFile file,
                                     @RequestPart(required = false) String base64,
                                     @RequestPart(required = false) String filePath,
                                     @RequestPart(required = false) Boolean pathContainsFileName
    );

    @GetMapping(value = "/download/file", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> downloadFile(@RequestParam("id") String id);

    @GetMapping(value = "/download/fileByKey", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    ResponseEntity<byte[]> downloadFileByKey(@RequestParam("key") String key);

}
