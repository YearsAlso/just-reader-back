package com.yearsalso.client.modules.myLibraryPage;

import com.yearsalso.client.vo.PageVo;
import com.yearsalso.client.vo.SearchVo;
import com.yearsalso.common.api.CommonResult;
import com.yearsalso.data.dto.CommonPage;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/myLibraryPage")
@Tag(name = "我的图书管页面", description = "MyLibraryPageController")
public class MyLibraryPageController {
    @Resource(name = "myLibraryPageServiceImpl")
    IMyLibraryPageService myLibraryPageService;

    @RequestMapping("/getMyLibraryPage")
    public CommonResult<MyLibraryPageResponse> getMyLibraryPage(@ModelAttribute SearchVo searchVo, @ModelAttribute PageVo pageVo, @ModelAttribute MyLibraryPageRequest myLibraryPageRequest) {
        log.info("获取我的图书馆页面");
        return myLibraryPageService.getMyLibraryPage();
    }
}
