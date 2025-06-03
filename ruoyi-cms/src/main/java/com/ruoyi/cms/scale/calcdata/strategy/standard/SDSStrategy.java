package com.ruoyi.cms.scale.calcdata.strategy.standard;

import com.alibaba.fastjson.JSON;
import com.ruoyi.cms.scale.domain.LbsContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.cms.scale.calcdata.params.standard.SDSCalcParams;
import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.cms.scale.calcdata.strategy.ICalcStrategy;
import com.ruoyi.cms.scale.service.ILbsContextsService;

@Service
public class SDSStrategy implements ICalcStrategy {

    @Autowired
    private ILbsContextsService lbsContextsService;

    private SDSCalcParams sdsCalcParams;

    @Override
    public int calculate(ContextAnswerVo contextAnswerVo) {
        int result=0;
        try{
            Long contextId=contextAnswerVo.getContextId();
            LbsContexts lbsContexts=lbsContextsService.selectLbsContextsByContextId(contextId);
            String jsonStr = lbsContexts.getJsonParams();
            SDSCalcParams sdsCalcParams = JSON.parseObject(jsonStr, SDSCalcParams.class);
            result=1;
        }
        catch (Exception ex){
            //
        }
        return result;
    }




}