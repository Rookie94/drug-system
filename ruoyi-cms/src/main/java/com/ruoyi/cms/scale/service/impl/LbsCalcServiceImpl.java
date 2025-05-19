package com.ruoyi.cms.scale.service.impl;

import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.cms.scale.calcdata.strategy.ICalcStrategy;
import com.ruoyi.cms.scale.service.ILbsCalcService;
import com.ruoyi.cms.scale.calcdata.strategy.CalcStrategyFactory;
import org.springframework.stereotype.Service;

/**
 * 量表计算Service业务层处理
 *
 * @author admin
 * @date 2025-05-05
 */
@Service
public class LbsCalcServiceImpl implements ILbsCalcService {

    @Override
    public int calcData(ContextAnswerVo contextAnswerVo) {
        ICalcStrategy strategy = CalcStrategyFactory.getStrategy(
                contextAnswerVo.getContextId().toString());
        return strategy.calculate(contextAnswerVo);
    }

}
