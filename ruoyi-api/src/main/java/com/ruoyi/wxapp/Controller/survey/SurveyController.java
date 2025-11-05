package com.ruoyi.wxapp.Controller.survey;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.cms.survey.domain.vo.DocResultsVo;
import com.ruoyi.cms.survey.service.IDocResultsService;
import com.ruoyi.cms.survey.service.ISurveyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/survey")
public class SurveyController extends BaseController {

    @Autowired
    private ISurveyService surveyService;

    @Autowired
    private IDocResultsService docResultsService;

    @GetMapping(value = "/getSurvey")
    public TableDataInfo getSurvey(Survey survey)
    {
        startPage();
        survey.setUseDataScope(false);
        survey.setStatus("0");
        survey.setSurveyStatus("1");
        List<Survey> list = surveyService.selectSurveyList(survey);
        return getDataTable(list);
    }

    @GetMapping(value = "/getSurveyInfo/{surveyId}")
    public AjaxResult getSurveyInfo(@PathVariable("surveyId") Long surveyId)
    {
        return success(surveyService.selectFullSurveyById(surveyId));
    }

    @PostMapping(value = "/commitResult")
    public AjaxResult commitResult(@RequestBody JsonNode jsonNode)
    {
        //System.out.println(jsonNode);
        DocResults docResults=new DocResults();
        docResults.setSurveyId(jsonNode.get("surveyId").asLong());

        ObjectMapper mapper = new ObjectMapper();
        String jsonResult="";
        String thinJsonResult ="";
        try {
            jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            thinJsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode.get("answers"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("答案解析失败!", e);
        }

        docResults.setJsonResult(jsonResult);
        docResults.setThinJsonResult(thinJsonResult);

        docResults.setStatus("0");

        int result=docResultsService.insertDocResults(docResults);
        try{
            Long resultId=docResults.getResultId();
            //AnswersVo answersVo=new AnswersVo();
            //answersVo.setSurveyId(docResults.getSurveyId());
            //List<Answers> list= JSONArray.parseArray(docResults.getJsonResult(),Answers.class);
            //answersVo.setAnswersList(list);
            //docResultsService.deleteDocResultsById(resultId);
            //docResultsService.batchInsertAnswer(list);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return toAjax(result);
    }

    @GetMapping("/getRecord")
    public TableDataInfo getRecord(DocResultsVo docResultsVo)
    {
        startPage();
        docResultsVo.setUseDataScope(false);
        docResultsVo.setUserId(getLoginUser().getUserId());
        List<DocResultsVo> list = docResultsService.selectDocResultsList(docResultsVo);
        return getDataTable(list);
    }

}
