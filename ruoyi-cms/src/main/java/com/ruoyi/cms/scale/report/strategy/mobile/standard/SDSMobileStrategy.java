package com.ruoyi.cms.scale.report.strategy.mobile.standard;

import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.report.ITemplateStrategy;

public class SDSMobileStrategy implements ITemplateStrategy {
    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults)
    {
        return "templates/cate1/mobile/" + contextId + ".html";
    }
}