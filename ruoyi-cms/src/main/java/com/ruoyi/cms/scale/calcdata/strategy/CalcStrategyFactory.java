package com.ruoyi.cms.scale.calcdata.strategy;

import com.ruoyi.cms.scale.calcdata.strategy.standard.SASStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.standard.SDSStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalcStrategyFactory {

    private final Map<Long, ICalcStrategy> strategyMap = new HashMap<>();

    public CalcStrategyFactory(){
        strategyMap.put(7l, new SDSStrategy());
        strategyMap.put(8L, new SASStrategy());
    }

    public ICalcStrategy getStrategy(Long contextId) {
        return strategyMap.get(contextId);
    }

}
