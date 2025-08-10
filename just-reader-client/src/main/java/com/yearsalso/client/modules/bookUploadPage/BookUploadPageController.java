package com.yearsalso.client.modules.bookUploadPage;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/bookUploadPage")
@Tag(name = "书籍上传", description = "BookUploadPageController")
public class BookUploadPageController {
    @Resource(name = "bookUploadPageServiceImpl")
    private IBookUploadPageService bookUploadPageService;

}
