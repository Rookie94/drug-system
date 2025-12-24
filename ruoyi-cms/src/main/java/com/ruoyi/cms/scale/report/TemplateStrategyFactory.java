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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TemplateStrategyFactory
{
    private final Map<Long, Map<String, ITemplateStrategy>> strategyMap = new ConcurrentHashMap<>();

    @Autowired
    public TemplateStrategyFactory(
            // 所有策略通过构造器注入
            SDSPcStrategy sdsPcStrategy,
            SDSMobileStrategy sdsMobileStrategy,
            SASPcStrategy sasPcStrategy,
            SASMobileStrategy sasMobileStrategy,
            HAMAPcStrategy hamaPcStrategy,
            HAMAMobileStrategy hamaMobileStrategy,
            ITSPcStrategy itsPcStrategy,
            ITSMobileStrategy itsMobileStrategy,
            ASLECPcStrategy aslecPcStrategy,
            ASLECMobileStrategy aslecMobileStrategy,
            CSQPcStrategy csqPcStrategy,
            CSQMobileStrategy csqMobileStrategy,
            LSIAPcStrategy lsiaPcStrategy,
            LSIAMobileStrategy lsiaMobileStrategy
    ) {
        registerStrategies(
                sdsPcStrategy, sdsMobileStrategy,
                sasPcStrategy, sasMobileStrategy,
                hamaPcStrategy, hamaMobileStrategy,
                itsPcStrategy, itsMobileStrategy,
                aslecPcStrategy, aslecMobileStrategy,
                csqPcStrategy, csqMobileStrategy,
                lsiaPcStrategy, lsiaMobileStrategy
        );
    }

    public ITemplateStrategy getStrategy(Long contextId, String deviceType) {
        Map<String, ITemplateStrategy> deviceStrategies = strategyMap.get(contextId);
        if (deviceStrategies == null) {
            return null; // 分类不存在
        }
        return deviceStrategies.get(deviceType); // 设备类型不存在时返回null
    }

    /*
     * 注册测评报告模板
     */
    private void registerStrategies(
            SDSPcStrategy sdsPcStrategy,
            SDSMobileStrategy sdsMobileStrategy,
            SASPcStrategy sasPcStrategy,
            SASMobileStrategy sasMobileStrategy,
            HAMAPcStrategy hamaPcStrategy,
            HAMAMobileStrategy hamaMobileStrategy,
            ITSPcStrategy itsPcStrategy,
            ITSMobileStrategy itsMobileStrategy,
            ASLECPcStrategy aslecPcStrategy,
            ASLECMobileStrategy aslecMobileStrategy,
            CSQPcStrategy csqPcStrategy,
            CSQMobileStrategy csqMobileStrategy,
            LSIAPcStrategy lsiaPcStrategy,
            LSIAMobileStrategy lsiaMobileStrategy
    ) {
        // SDS 策略
        Map<String, ITemplateStrategy> SDSStrategies = new HashMap<>();
        Long SDSContextId = 7L;
        SDSStrategies.put("pc", sdsPcStrategy);
        SDSStrategies.put("mobile", sdsMobileStrategy);
        strategyMap.put(SDSContextId, SDSStrategies);

        // SAS 策略
        Map<String, ITemplateStrategy> SASStrategies = new HashMap<>();
        Long SASContextId = 8L;
        SASStrategies.put("pc", sasPcStrategy);
        SASStrategies.put("mobile", sasMobileStrategy);
        strategyMap.put(SASContextId, SASStrategies);

        // HAMA 策略
        Map<String, ITemplateStrategy> HAMAStrategies = new HashMap<>();
        Long HAMAContextId = 37L;
        HAMAStrategies.put("pc", hamaPcStrategy);
        HAMAStrategies.put("mobile", hamaMobileStrategy);
        strategyMap.put(HAMAContextId, HAMAStrategies);

        // ITS 策略
        Map<String, ITemplateStrategy> ITSStrategies = new HashMap<>();
        Long ITSContextId = 40L;
        ITSStrategies.put("pc", itsPcStrategy);
        ITSStrategies.put("mobile", itsMobileStrategy);
        strategyMap.put(ITSContextId, ITSStrategies);

        // ASLEC 策略
        Map<String, ITemplateStrategy> ASLECStrategies = new HashMap<>();
        Long ASLECContextId = 41L;
        ASLECStrategies.put("pc", aslecPcStrategy);
        ASLECStrategies.put("mobile", aslecMobileStrategy);
        strategyMap.put(ASLECContextId, ASLECStrategies);

        // CSQ 策略
        Map<String, ITemplateStrategy> CSQStrategies = new HashMap<>();
        Long CSQContextId = 42L;
        CSQStrategies.put("pc", csqPcStrategy);
        CSQStrategies.put("mobile", csqMobileStrategy);
        strategyMap.put(CSQContextId, CSQStrategies);

        // LSIA 策略
        Map<String, ITemplateStrategy> LSIAStrategies = new HashMap<>();
        Long LSIAContextId = 43L;
        LSIAStrategies.put("pc", lsiaPcStrategy);
        LSIAStrategies.put("mobile", lsiaMobileStrategy);
        strategyMap.put(LSIAContextId, LSIAStrategies);
    }
}