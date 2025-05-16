package com.ruoyi.cms.scale.calcdata.strategy;

import com.ruoyi.cms.scale.calcdata.strategy.standard.SASStrategy;
import com.ruoyi.cms.scale.calcdata.strategy.standard.SDSStrategy;

import java.util.HashMap;
import java.util.Map;

public  class CalcStrategyFactory {

    private static final Map<String, ICalcStrategy> STRATEGY_MAP = new HashMap<>();

    static {
        STRATEGY_MAP.put("1", new SDSStrategy());
        STRATEGY_MAP.put("2", new SASStrategy());
    }

    public static ICalcStrategy getStrategy(String contextId) {
        return STRATEGY_MAP.get(contextId);
    }
}
