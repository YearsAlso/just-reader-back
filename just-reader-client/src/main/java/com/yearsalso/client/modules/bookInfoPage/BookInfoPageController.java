package com.yearsalso.client.modules.bookInfoPage;

import com.yearsalso.common.api.CommonResult;
import com.yearsalso.data.dto.CommonPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/bookInfoPage")
@Tag(name = "书籍信息", description = "BookInfoPageController")
public class BookInfoPageController {
    @Resource(name = "bookInfoPageServiceImpl")
    IBookInfoPageService bookInfoPageService;

    // TODO：获取书籍基础信息
    @Operation(summary = "获取书籍基础信息")
    @GetMapping(value = "/getBaseInfo")
    @ResponseBody
    @Parameters({
            @Parameter(name = "bookId", description = "图书Id")
    })
    public CommonResult<BookBaseInfoResponse> GetBaseInfo(@RequestParam(value = "bookId") String bookId) {



        return CommonResult.failed();
    }

    // TODO：获取书籍的章节



    // TODO: 获取关键术语

    // TODO: 获取知识脑图
}
