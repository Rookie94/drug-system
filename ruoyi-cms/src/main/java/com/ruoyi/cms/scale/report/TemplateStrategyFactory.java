package com.ruoyi.cms.scale.report;

import com.ruoyi.cms.scale.report.strategy.mobile.family.ASLECMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.mobile.family.CSQMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.mobile.family.ITSMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.mobile.family.LSIAMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.mobile.standard.HAMAMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.mobile.standard.SASMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.mobile.standard.SDSMobileStrategy;
import com.ruoyi.cms.scale.report.strategy.pc.family.ASLECPcStrategy;
import com.ruoyi.cms.scale.report.strategy.pc.family.CSQPcStrategy;
import com.ruoyi.cms.scale.report.strategy.pc.family.ITSPcStrategy;
import com.ruoyi.cms.scale.report.strategy.pc.family.LSIAPcStrategy;
import com.ruoyi.cms.scale.report.strategy.pc.standard.HAMAPcStrategy;
import com.ruoyi.cms.scale.report.strategy.pc.standard.SASPcStrategy;
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

        Long SDSContextId=7L;
        SDSStrategies.put("pc", new SDSPcStrategy());
        SDSStrategies.put("mobile", new SDSMobileStrategy());
        strategyMap.put(SDSContextId, SDSStrategies);

        Map<String, ITemplateStrategy> SASStrategies = new HashMap<>();
        Long SASContextId=8L;
        SASStrategies.put("pc", new SASPcStrategy());
        SASStrategies.put("mobile", new SASMobileStrategy());
        strategyMap.put(SASContextId, SASStrategies);

        Map<String, ITemplateStrategy> HAMAStrategies = new HashMap<>();
        Long HAMAContextId=37L;
        HAMAStrategies.put("pc", new HAMAPcStrategy());
        HAMAStrategies.put("mobile", new HAMAMobileStrategy());
        strategyMap.put(HAMAContextId, HAMAStrategies);

        Map<String, ITemplateStrategy> ITSStrategies = new HashMap<>();
        Long ITSContextId=40L;
        ITSStrategies.put("pc", new ITSPcStrategy());
        ITSStrategies.put("mobile", new ITSMobileStrategy());
        strategyMap.put(ITSContextId, ITSStrategies);

        Map<String, ITemplateStrategy> ASLECStrategies = new HashMap<>();
        Long ASLECContextId=41L;
        ASLECStrategies.put("pc", new ASLECPcStrategy());
        ASLECStrategies.put("mobile", new ASLECMobileStrategy());
        strategyMap.put(ASLECContextId, ASLECStrategies);

        Map<String, ITemplateStrategy> CSQStrategies = new HashMap<>();
        Long CSQContextId=42L;
        CSQStrategies.put("pc", new CSQPcStrategy());
        CSQStrategies.put("mobile", new CSQMobileStrategy());
        strategyMap.put(CSQContextId, CSQStrategies);

        Map<String, ITemplateStrategy> LSIAStrategies = new HashMap<>();
        Long LSIAContextId=43L;
        LSIAStrategies.put("pc", new LSIAPcStrategy());
        LSIAStrategies.put("mobile", new LSIAMobileStrategy());
        strategyMap.put(LSIAContextId, LSIAStrategies);

    }

}