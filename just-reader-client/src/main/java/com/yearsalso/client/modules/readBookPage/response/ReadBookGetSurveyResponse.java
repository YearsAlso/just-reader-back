package com.yearsalso.client.modules.readBookPage.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReadBookGetSurveyResponse {
    private String surveyId;

    private String surveyName;

    private String surveyDescription;
    
}
