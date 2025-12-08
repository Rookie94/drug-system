package com.ruoyi.cms.myboard.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ResStatisticsDataVO {
    private Integer totalAccess;
    private Integer uniqueVisitors;
    private Double avgVisitDuration;
    private Double avgVisitDepth;
    private String topModule;

    private List<ModuleAccessVO> userAccessData;
    private List<ModuleAccessVO> moduleAccessData;
    private DailyAccessDataVO dailyAccessData;
    private ModuleTrendDataVO moduleTrendData;
    private List<TopArticleVO> topArticles;
    private DailyLoginDataVO dailyLoginData;
    private TimeDistributionDataVO timeDistributionData;
}