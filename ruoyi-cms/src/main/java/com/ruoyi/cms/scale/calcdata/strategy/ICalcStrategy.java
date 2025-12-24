package com.ruoyi.cms.scale.calcdata.strategy;

import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.common.core.domain.ProcResult;

public interface ICalcStrategy {
    ProcResult calculate(ContextAnswerVo contextAnswerVo);
}
