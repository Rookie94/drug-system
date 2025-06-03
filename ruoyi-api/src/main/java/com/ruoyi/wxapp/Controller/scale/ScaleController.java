package com.ruoyi.wxapp.Controller.scale;

import com.alibaba.fastjson.JSONArray;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.LbsResults;
import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.report.ITemplateStrategy;
import com.ruoyi.cms.scale.report.TemplateStrategyFactory;
import com.ruoyi.cms.scale.service.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TemplateStrategyFactory reportFactory;

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

    @GetMapping("/getReport")
    public String renderTemplate(@RequestParam Long resultId,@RequestParam(defaultValue = "pc") String deviceType)
    {
        LbsResultsVo lbsResults = lbsResultsService.selectLbsResultsByResultId(resultId);
        Long contextId = lbsResults.getContextId();
        ITemplateStrategy strategy = reportFactory.getStrategy(contextId, deviceType);
        if (strategy == null) {
            if(deviceType.equals("pc")){
                return "templates/pc/default/" + "index.html";
            }
            else{
                return "templates/mobile/default/" + "index.html";
            }
        }
        return strategy.getTemplate(contextId,deviceType,lbsResults);
    }

}
