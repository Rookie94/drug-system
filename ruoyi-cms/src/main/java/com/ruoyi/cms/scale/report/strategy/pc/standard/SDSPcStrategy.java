package com.ruoyi.cms.scale.report.strategy.pc.standard;

import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.report.ITemplateStrategy;

public class SDSPcStrategy implements ITemplateStrategy {
    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults)
    {
        return "templates/pc/sds.html";
    }
}