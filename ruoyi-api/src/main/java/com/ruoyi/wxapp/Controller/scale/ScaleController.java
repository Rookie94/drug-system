package com.ruoyi.wxapp.Controller.scale;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.cms.scale.calcdata.strategy.CalcStrategyFactory;
import com.ruoyi.cms.scale.calcdata.strategy.ICalcStrategy;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.LbsResults;
import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.report.ITemplateStrategy;
import com.ruoyi.cms.scale.report.TemplateStrategyFactory;
import com.ruoyi.cms.scale.service.*;
import com.ruoyi.cms.survey.domain.vo.DocResultsVo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ProcResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    private CalcStrategyFactory calcFactory;

    @Autowired
    private TemplateStrategyFactory reportFactory;

    @GetMapping("/getCategories")
    public AjaxResult getCategories()
    {
        LbsContexts lbsContexts=new LbsContexts();
        lbsContexts.setParentContextId(1L);
        lbsContexts.setStatus("0");
        List<LbsContexts> list = lbsContextsService.selectLbsContextsList(lbsContexts);
        return success(list);
    }

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

    @Log(title = "测评量表", businessType = BusinessType.INSERT)
    @PostMapping(value = "/commitResult")
    public AjaxResult commitResult(@RequestBody JsonNode jsonNode)
    {
        //System.out.println(jsonNode);
        LbsResults lbsResults=new LbsResults();
        lbsResults.setContextId(jsonNode.get("scaleId").asLong());

        ObjectMapper mapper = new ObjectMapper();
        String jsonResult="";
        String thinJsonResult ="";
        try {
            jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            thinJsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode.get("answers"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("答案解析失败!", e);
        }
        lbsResults.setJsonResult(jsonResult);
        lbsResults.setThinJsonResult(thinJsonResult);

        lbsResults.setStatus("0");
        int result=lbsResultsService.insertLbsResults(lbsResults);
        try {
            Long resultId = lbsResults.getResultId();
            ContextAnswerVo contextAnswerVo = new ContextAnswerVo();
            contextAnswerVo.setContextId(lbsResults.getContextId());
            // 解析JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(lbsResults.getJsonResult());
            JsonNode answersArray = rootNode.get("answers");
            List<AnswerVo> list = new ArrayList<>();
            if (answersArray != null && answersArray.isArray()) {
                for (JsonNode answerNode : answersArray) {
                    AnswerVo answerVo = new AnswerVo();
                    // 设置resultId
                    answerVo.setResultId(resultId);
                    // 转换topicId为Long
                    if (answerNode.has("topicId")) {
                        String topicIdStr = answerNode.get("topicId").asText();
                        answerVo.setTopicId(Long.parseLong(topicIdStr));
                    }
                    // 转换optionId为字符串，直接存储
                    if (answerNode.has("optionId")) {
                        String optionIdStr = answerNode.get("optionId").asText();
                        answerVo.setOptionIds(optionIdStr);
                    }
                    // answer字段可能需要从其他地方获取或为空
                    // answerVo.setAnswer("");
                    list.add(answerVo);
                }
            }
            contextAnswerVo.setAnswers(list);
            lbsAnswerService.deleteAnswerByResultId(resultId);
            lbsAnswerService.batchInsertAnswer(list);

            ICalcStrategy strategy=calcFactory.getStrategy(contextAnswerVo.getContextId());
            ProcResult procResult= strategy.calculate(contextAnswerVo);
            if(procResult.getResult()){
                LbsResults lb=new LbsResults();
                lb.setResultId(resultId);
                lb.setJsonReport(procResult.getData().toString());
                lbsResultsService.updateLbsResults(lb);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return toAjax(result);
    }


	@GetMapping("/getReportList")
    public TableDataInfo getReportList()
    {
        startPage();
        LbsResultsVo lbsResults=new LbsResultsVo();
        lbsResults.setUseDataScope(false);
        lbsResults.setUserId(getLoginUser().getUserId());
        List<LbsResultsVo> list = lbsResultsService.selectLbsResultsList(lbsResults);
        return getDataTable(list);
    }

    @GetMapping("/getReport")
    public String renderTemplate(@RequestParam Long resultId, @RequestParam(defaultValue = "pc") String deviceType) {
        LbsResultsVo lbsResults = lbsResultsService.selectLbsResultsByResultId(resultId);
        Long contextId = lbsResults.getContextId();
        ITemplateStrategy strategy = reportFactory.getStrategy(contextId, deviceType);

        if (strategy == null) {
            // 读取默认模板文件内容并返回
            String templatePath;
            if (deviceType.equals("pc")) {
                templatePath = "templates/pc/default/index.html";
            } else {
                templatePath = "templates/mobile/default/index.html";
            }

            try {
                // 使用ClassPathResource读取模板文件内容
                ClassPathResource resource = new ClassPathResource(templatePath);
                if (resource.exists()) {
                    return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                } else {
                    // 如果默认模板文件不存在，返回一个简单的错误提示
                    return "<html><body><h1>报告模板不存在</h1><p>无法找到对应的报告模板文件</p></body></html>";
                }
            } catch (IOException e) {
                // 读取文件失败时返回错误信息
                return "<html><body><h1>报告生成失败</h1><p>读取模板文件时发生错误: " + e.getMessage() + "</p></body></html>";
            }
        }

        // 如果找到了策略，调用策略获取模板内容
        return strategy.getTemplate(contextId, deviceType, lbsResults);
    }

}
