package com.yearsalso.client.modules.readBookPage;

import com.yearsalso.client.modules.readBookPage.response.ReadBookGetSurveyResponse;

public interface IReadBookPageService {
    ReadBookGetSurveyResponse getSurvey(String bookId, String chapterId);

    ReadBookGetSurveyResponse askQuestion(String bookId, String chapterId, String question);
}
