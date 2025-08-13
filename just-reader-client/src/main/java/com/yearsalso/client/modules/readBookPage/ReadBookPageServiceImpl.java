package com.yearsalso.client.modules.readBookPage;

import com.yearsalso.client.modules.readBookPage.response.ReadBookGetSurveyResponse;
import org.springframework.stereotype.Service;

@Service("readBookPageServiceImpl")
public class ReadBookPageServiceImpl implements IReadBookPageService {
    @Override
    public ReadBookGetSurveyResponse getSurvey(String bookId, String chapterId) {
        // TODO: 获取缓存中的概览内容

        // TODO: 如果缓存中没有，则使用AI 生成，保存到缓存中，需要生成异步任务

        // TODO: 如果是AI 生成，则需要使用SSE 返回给前端

        // TODO: 内容主要分成3部分：1. 概览 2. 思维导引 3. 章节内容

        return null;
    }

    @Override
    public ReadBookGetSurveyResponse askQuestion(String bookId, String chapterId, String question) {
        // TODO: 生成问题 ID， 获取会话sessionID

        return null;
    }
}
