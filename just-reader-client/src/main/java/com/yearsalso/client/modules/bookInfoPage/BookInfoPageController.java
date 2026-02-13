package com.yearsalso.client.modules.bookInfoPage;

import com.yearsalso.client.modules.bookInfoPage.response.BookBaseInfoResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookChapterResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookKeyTermResponse;
import com.yearsalso.client.modules.bookInfoPage.response.BookKnowledgeMapResponse;
import com.yearsalso.common.api.CommonResult;
import com.yearsalso.data.dto.CommonPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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

        BookBaseInfoResponse baseInfo = null;
        try {
            baseInfo = bookInfoPageService.getBaseInfo(bookId);
        } catch (Exception e) {
            return CommonResult.failed("获取书籍基础信息失败: " + e.getMessage());
        }

        return CommonResult.success(baseInfo);
    }

    // TODO：获取书籍的章节

    public CommonResult<CommonPage<BookChapterResponse>> GetBookChapters(@RequestParam(value = "bookId") String bookId,
                                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        CommonPage<BookChapterResponse> bookChapters = null;
        try {
            bookChapters = bookInfoPageService.getBookChapters(bookId, pageNum, pageSize);
        } catch (Exception e) {
            return CommonResult.failed("获取书籍章节失败: " + e.getMessage());
        }

        return CommonResult.success(bookChapters);
    }



    // TODO: 获取关键术语
    public CommonResult<CommonPage<BookKeyTermResponse>> GetKeyTerms(@RequestParam(value = "bookId") String bookId,
                                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        CommonPage<BookKeyTermResponse> keyTerms = null;
        try {
            keyTerms = bookInfoPageService.getKeyTerms(bookId, pageNum, pageSize);
        } catch (Exception e) {
            return CommonResult.failed("获取关键术语失败: " + e.getMessage());
        }

        return CommonResult.success(keyTerms);
    }

    // TODO: 获取知识脑图
    public CommonResult<BookKnowledgeMapResponse> GetKnowledgeMap(@RequestParam(value = "bookId") String bookId) {
        BookKnowledgeMapResponse knowledgeMap = null;
        try {
            knowledgeMap = bookInfoPageService.getKnowledgeMap(bookId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CommonResult.success(knowledgeMap);
    }
}
