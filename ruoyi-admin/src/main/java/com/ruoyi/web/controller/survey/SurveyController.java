package com.ruoyi.web.controller.survey;

import com.ruoyi.cms.survey.domain.vo.DocResultsVo;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.cms.survey.service.IDocResultsService;
import com.ruoyi.cms.survey.service.IAnswersService;
import com.ruoyi.cms.survey.service.IQuestionService;
import com.ruoyi.cms.survey.service.ISurveyService;

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
@RequestMapping("/survey/vote")
public class SurveyController extends BaseController {

    @Autowired
    private ISurveyService surveyService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IDocResultsService docResultsService;
    @Autowired
    private IAnswersService answerService;


    /**
     * 查询问卷列表
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:list')")
    @GetMapping("/list")
    public TableDataInfo list(Survey survey) {
        startPage();
        List<Survey> list = surveyService.selectSurveyList(survey);
        return getDataTable(list);
    }

    /**
     * 导出问卷列表
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:export')")
    @Log(title = "问卷导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult export(Survey survey) {
        List<Survey> list = surveyService.selectSurveyList(survey);
        ExcelUtil<Survey> util = new ExcelUtil<Survey>(Survey.class);
        return util.exportExcel(list, "问卷数据");
    }

    /**
     * 获取问卷详细信息
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:query')")
    @GetMapping(value = "/{surveyId}")
    public AjaxResult getInfo(@PathVariable("surveyId") Long surveyId) {
        return AjaxResult.success(surveyService.selectSurveyBySurveyId(surveyId));
    }

    /**
     * 新增问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:add')")
    @Log(title = "问卷新增", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Survey survey) {
        return toAjax(surveyService.insertSurvey(survey));
    }

    /**
     * 修改问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:edit')")
    @Log(title = "问卷修改", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Survey survey) {
        return toAjax(surveyService.updateSurvey(survey));
    }

    /**
     * 删除问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:remove')")
    @Log(title = "问卷删除", businessType = BusinessType.UPDATE)
    @PutMapping("/remove/{surveyIds}")
    public AjaxResult remove(@PathVariable Long[] surveyIds) {
        return toAjax(surveyService.removeSurveyBySurveyIds(surveyIds));
    }

    /**
     * 还原问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:recycle:restore')")
    @Log(title = "问卷还原", businessType = BusinessType.DELETE)
    @PutMapping("/restore/{surveyIds}")
    public AjaxResult restore(@PathVariable Long[] surveyIds) {
        return toAjax(surveyService.restoreSurveyBySurveyIds(surveyIds));
    }

    /**
     * 永久删除问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:delete')")
    @Log(title = "问卷删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{surveyIds}")
    public AjaxResult delete(@PathVariable Long[] surveyIds) {
        questionService.deleteQuestionBySurveyIds(surveyIds);
        docResultsService.deleteDocResultsBySurveyIds(surveyIds);
        answerService.deleteAnswersBySurveyIds(surveyIds);
        return toAjax(surveyService.deleteSurveyBySurveyIds(surveyIds));
    }

    /**
     * 发布问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:publish')")
    @Log(title = "问卷发布", businessType = BusinessType.DELETE)
    @PutMapping("/publish/{surveyIds}")
    public AjaxResult publish(@PathVariable Long[] surveyIds) {
        return toAjax(surveyService.publishSurveyBySurveyIds(surveyIds));
    }

    /**
     * 撤销发布问卷
     */
    @PreAuthorize("@ss.hasPermi('survey:vote:revoke')")
    @Log(title = "问卷撤销发布", businessType = BusinessType.DELETE)
    @PutMapping("/revoke/{surveyId}")
    public AjaxResult revoke(@PathVariable Long surveyId) {
        List<DocResultsVo> answerJsons = docResultsService.selectDocResultsBySurveyId(surveyId);
        if (!answerJsons.isEmpty()) {
            return AjaxResult.error("数据已采集，不能撤销");
        }
        return toAjax(surveyService.revokeSurveyBySurveyId(surveyId));
    }


}
