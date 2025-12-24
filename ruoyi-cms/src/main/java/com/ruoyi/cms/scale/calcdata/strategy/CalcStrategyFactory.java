package com.ruoyi.cms.scale.calcdata.strategy;

import com.ruoyi.cms.scale.calcdata.strategy.family.ASLECCalcStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.family.CSQCalcStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.family.ITSCalcStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.family.LSIACalcStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.standard.HAMACalcStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.standard.SASCalcStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.standard.SDSCalcStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalcStrategyFactory {

    private final Map<Long, ICalcStrategy> strategyMap = new HashMap<>();

    @Autowired
    public CalcStrategyFactory(
            SDSCalcStrategy sdsCalcStrategy,
            SASCalcStrategy sasCalcStrategy,
            HAMACalcStrategy hamaCalcStrategy,
            ITSCalcStrategy itsCalcStrategy,
            ASLECCalcStrategy aslecCalcStrategy,
            CSQCalcStrategy csqCalcStrategy,
            LSIACalcStrategy lsiaCalcStrategy) {
        strategyMap.put(7L, sdsCalcStrategy);
        strategyMap.put(8L, sasCalcStrategy);
        strategyMap.put(37L, hamaCalcStrategy);
        strategyMap.put(40L, itsCalcStrategy);
        strategyMap.put(41L, aslecCalcStrategy);
        strategyMap.put(42L, csqCalcStrategy);
        strategyMap.put(43L, lsiaCalcStrategy);
    }

    public ICalcStrategy getStrategy(Long contextId) {
        return strategyMap.get(contextId);
    }
}