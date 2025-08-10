package com.yearsalso.client.modules.myLibraryPage;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/myLibraryPage")
@Tag(name = "我的图书管页面", description = "MyLibraryPageController")
public class MyLibraryPageController {
    @Resource(name = "myLibraryPageServiceImpl")
    IMyLibraryPageService myLibraryPageService;
}
