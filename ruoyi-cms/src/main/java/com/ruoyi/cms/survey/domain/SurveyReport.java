package com.ruoyi.cms.survey.domain;

import java.util.List;

// SurveyReport.java
public class SurveyReport {
    private String title;
    private List<OptionStat> options;

    // 构造方法
    public SurveyReport() {}

    public SurveyReport(String title, List<OptionStat> options) {
        this.title = title;
        this.options = options;
    }

    // getter和setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OptionStat> getOptions() {
        return options;
    }

    public void setOptions(List<OptionStat> options) {
        this.options = options;
    }
}



