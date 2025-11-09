package com.ruoyi.web.controller.survey;


import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.cms.survey.domain.SurveyReport;
import com.ruoyi.cms.survey.domain.SurveyReportQuery;
import com.ruoyi.cms.survey.service.ISurveyReportService;
import com.ruoyi.cms.survey.service.ISurveyService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问卷Controller
 *
 * @author Shure
 * @date 2021-10-18
 */
@RestController
@RequestMapping("/survey/report")
@Slf4j
public class SurveryReportController extends BaseController {

    @Autowired
    private ISurveyService surveyService;

    @Autowired
    private ISurveyReportService surveyReportService;

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

    /**
     * 生成问卷调查统计报告
     */
    @PostMapping("/generate")
    public AjaxResult generateReport(@RequestBody SurveyReportQuery query) {
        try {
            log.info("接收到生成报告请求: surveyId={}, startTime={}, endTime={}",
                    query.getSurveyId(), query.getCommitTimeStart(), query.getCommitTimeEnd());

            // 参数验证
            if (query.getSurveyId() == null) {
                return AjaxResult.error("调查问卷ID不能为空");
            }

            // 只有当开始时间和结束时间都不为空时，才进行时间范围验证
            if (query.getCommitTimeStart() != null && query.getCommitTimeEnd() != null) {
                if (query.getCommitTimeStart().after(query.getCommitTimeEnd())) {
                    return AjaxResult.error("开始时间不能晚于结束时间");
                }
            }

            List<SurveyReport> reportData = surveyReportService.generateSurveyReport(query);
            return AjaxResult.success(reportData);

        } catch (Exception e) {
            log.error("生成报告接口异常: ", e);
            return AjaxResult.error("生成报告失败: " + e.getMessage());
        }
    }

}
