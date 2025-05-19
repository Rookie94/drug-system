package com.ruoyi.cms.scale.report;

import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;

public interface ITemplateStrategy
{
    String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults);
}