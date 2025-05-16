package com.ruoyi.cms.scale.service;

import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;

public interface ILbsCalcService {

    /**
     * 计算量表结果
     *
     * @param contextAnswerVo 量表目录
     * @return 结果
     */
    public int calcData(ContextAnswerVo contextAnswerVo);


}
