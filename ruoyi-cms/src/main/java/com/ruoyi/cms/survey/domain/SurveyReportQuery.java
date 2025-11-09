package com.ruoyi.cms.survey.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

// SurveyReportQuery.java
public class SurveyReportQuery {
    private Long surveyId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitTimeStart;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitTimeEnd;

    // 构造方法
    public SurveyReportQuery() {}

    // getter和setter
    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Date getCommitTimeStart() {
        return commitTimeStart;
    }

    public void setCommitTimeStart(Date commitTimeStart) {
        this.commitTimeStart = commitTimeStart;
    }

    public Date getCommitTimeEnd() {
        return commitTimeEnd;
    }

    public void setCommitTimeEnd(Date commitTimeEnd) {
        this.commitTimeEnd = commitTimeEnd;
    }
}