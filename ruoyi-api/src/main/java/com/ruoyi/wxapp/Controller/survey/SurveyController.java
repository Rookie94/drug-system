package com.ruoyi.wxapp.Controller.survey;

import com.ruoyi.cms.survey.service.ISurveyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/survey")
public class SurveyController extends BaseController {

    @Autowired
    private ISurveyService surveyService;

    @GetMapping(value = "/getSurveyInfo/{surveyId}")
    public AjaxResult getSurveyInfo(@PathVariable("surveyId") Long surveyId)
    {
        return success(surveyService.selectFullSurveyById(surveyId));
    }


}
