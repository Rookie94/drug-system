package com.ruoyi.cms.scale.report;

import com.ruoyi.cms.scale.report.strategy.mobile.standard.SDSMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.pc.standard.SDSPcStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemplateStrategyFactory
{
    private final Map<Long, Map<String, ITemplateStrategy>> strategyMap = new ConcurrentHashMap<>();

    public ITemplateStrategy getStrategy(Long contextId, String deviceType) {
        Map<String, ITemplateStrategy> deviceStrategies = strategyMap.get(contextId);
        if (deviceStrategies == null) {
            return null; // 分类不存在
        }
        return deviceStrategies.get(deviceType); // 设备类型不存在时返回null
    }

    /*
    *注册测评报告模板
    */
    public TemplateStrategyFactory() {
        Map<String, ITemplateStrategy> SDSStrategies = new HashMap<>();

        Long SDSContextId=1L;
        SDSStrategies.put("pc", new SDSPcStrategy());
        SDSStrategies.put("mobile", new SDSMobileStrategy());
        strategyMap.put(SDSContextId, SDSStrategies);
    }

}