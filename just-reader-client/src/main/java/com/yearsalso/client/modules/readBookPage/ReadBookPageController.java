package com.yearsalso.client.modules.readBookPage;

import com.yearsalso.client.modules.readBookPage.response.ReadBookGetSurveyResponse;
import com.yearsalso.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ReadBookPage")
@Tag(name = "书籍信息", description = "ReadBookPageController")
public class ReadBookPageController {
    @Resource(name = "readBookPageServiceImpl")
    private IReadBookPageService readBookPageService;

    // TODO: 概览生成，获取概览内容
    @RequestMapping("/getSurvey")
    @ResponseBody
    @Parameters({
            @Parameter(name = "bookId", description = "书籍ID", required = true),
            @Parameter(name = "chapterId", description = "章节ID", required = false)
    })
    @Operation(summary = "获取书籍概览")
    public CommonResult<ReadBookGetSurveyResponse> GetSurvey(@RequestParam(required = true) String bookId, @RequestParam(required = false) String chapterId) {
        ReadBookGetSurveyResponse survey = null;
        try {
            survey = readBookPageService.getSurvey(bookId, chapterId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CommonResult.success(survey);
    }

    // TODO: 提问，评估用户问题
    @ResponseBody
    @RequestMapping("/askQuestion")
    @Parameters({
            @Parameter(name = "bookId", description = "书籍ID", required = true),
            @Parameter(name = "chapterId", description = "章节ID", required = false),
            @Parameter(name = "question", description = "用户提问内容", required = true)
    })
    @Operation(summary = "用户提问")
    public CommonResult<ReadBookGetSurveyResponse> askQuestion(@RequestParam(required = true) String bookId,
                                                               @RequestParam(required = false) String chapterId,
                                                               @RequestParam(required = true) String question) {
        ReadBookGetSurveyResponse response = null;
        try {
            response = readBookPageService.askQuestion(bookId, chapterId, question);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CommonResult.success(response);
    }

    // TODO: 获取章节信息

    // TODO: 获取页中划线和段落信息

    // TODO: 提交复述和AI评估

    // TODO: 获取阅读进度

    // TODO: 获取AI的问题和答案

    // TODO: AI 评估复习状态
}
