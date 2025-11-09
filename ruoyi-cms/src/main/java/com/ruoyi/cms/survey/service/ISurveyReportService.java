package com.ruoyi.cms.survey.service;

import com.ruoyi.cms.survey.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

import com.ruoyi.cms.survey.domain.SurveyReport;
import com.ruoyi.cms.survey.domain.SurveyReportQuery;

import java.util.List;

public interface ISurveyReportService {
    /**
     * 生成问卷调查统计报告
     */
    List<SurveyReport> generateSurveyReport(SurveyReportQuery query);
}
