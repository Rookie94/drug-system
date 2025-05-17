package com.ruoyi.wxapp.Controller.scale;

import com.alibaba.fastjson.JSONArray;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.LbsResults;
import com.ruoyi.cms.scale.domain.LbsTopics;
import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.domain.vo.LbsTopicsVo;
import com.ruoyi.cms.scale.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scale")
public class ScaleController extends BaseController {

    @Autowired
    private ILbsContextsService lbsContextsService;

    @Autowired
    private ILbsTopicsService lbsTopicsService;


    @Autowired
    private ILbsResultsService lbsResultsService;

    @Autowired
    private ILbsAnswerService lbsAnswerService;

    @Autowired
    private ILbsCalcService lbsCalcService;


    @GetMapping("/getContexts")
    public AjaxResult getContexts(LbsContexts lbsContexts)
    {
        lbsContexts.setStatus("0");
        List<LbsContexts> list = lbsContextsService.selectLbsContextsList(lbsContexts);
        return success(list);
    }

    @GetMapping(value = "/getContextInfo/{contextId}")
    public AjaxResult getContextInfo(@PathVariable("contextId") Long contextId)
    {
        return success(lbsContextsService.selectLbsContextsByContextId(contextId));
    }

    @GetMapping(value = "/getTopicsInfo/{contextId}")
    public AjaxResult getTopicsInfo(@PathVariable("contextId") Long contextId)
    {
        LbsContexts lbsContexts=lbsContextsService.selectLbsContextsByContextId(contextId);
        return success(lbsContexts.getJsonData());
    }

    @Log(title = "测评报告", businessType = BusinessType.INSERT)
    @PostMapping(value = "/commitResult")
    public AjaxResult commitResult(@RequestBody LbsResults lbsResults)
    {
        int result=lbsResultsService.insertLbsResults(lbsResults);
        try{
            Long resultId=lbsResults.getResultId();
            ContextAnswerVo contextAnswerVo=new ContextAnswerVo();
            contextAnswerVo.setContextId(lbsResults.getContextId());
            List<AnswerVo> list= JSONArray.parseArray(lbsResults.getJsonResult(),AnswerVo.class);
            contextAnswerVo.setAnswers(list);
            lbsAnswerService.deleteAnswerByResultId(resultId);
            lbsAnswerService.batchInsertAnswer(list);
            lbsCalcService.calcData(contextAnswerVo);
        }catch (Exception ex){
            //
        }
        return toAjax(result);
    }

    @Log(title = "测评报告", businessType = BusinessType.UPDATE)
    @GetMapping("/refreshResult/{resultIds}")
    public AjaxResult refreshResult(@PathVariable Long[] resultIds)
    {
        try{
            for (Long resultId : resultIds) {
                LbsResultsVo lbsResults=lbsResultsService.selectLbsResultsByResultId(resultId);
                ContextAnswerVo contextAnswerVo=new ContextAnswerVo();
                contextAnswerVo.setContextId(lbsResults.getContextId());
                List<AnswerVo> list= JSONArray.parseArray(lbsResults.getJsonResult(),AnswerVo.class);
                contextAnswerVo.setAnswers(list);
                lbsAnswerService.deleteAnswerByResultId(resultId);
                lbsAnswerService.batchInsertAnswer(list);
                lbsCalcService.calcData(contextAnswerVo);
            }
            return success("刷新成功");
        }catch (Exception ex){
            return error("刷新失败");
        }
    }
}
