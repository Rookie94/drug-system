package com.ruoyi.web.controller.survey;


import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.cms.survey.service.ISurveyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 问卷Controller
 *
 * @author Shure
 * @date 2021-10-18
 */
@RestController
@RequestMapping("/survey/report")
public class SurveryReportController extends BaseController {

    @Autowired
    private ISurveyService surveyService;

    /**
     * 查询问卷列表
     */
    @PreAuthorize("@ss.hasPermi('survey:report:list')")
    @GetMapping("/listSurvey")
    public TableDataInfo listSurvey(Survey survey) {
        startPage();
        List<Survey> list = surveyService.selectSurveyList(survey);
        return getDataTable(list);
    }

}
