package com.ruoyi.cms.survey.service.impl;

import com.ruoyi.cms.survey.domain.vo.SurveyVo;
import com.ruoyi.cms.survey.service.*;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.cms.survey.mapper.SurveyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 问卷Service业务层处理
 *
 * @author Shure
 * @date 2021-10-18
 */
@Service
public class SurveyServiceImpl implements ISurveyService {
    @Autowired
    private ISurveyService surveyService;

    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IOptionsService optionsService;
    @Autowired
    private IAnswersService answerService;
    @Autowired
    private IDocResultsService docResultsService;

    /**
     * 查询问卷
     *
     * @param surveyId 问卷主键
     * @return 问卷
     */
    @Override
    public Survey selectSurveyBySurveyId(Long surveyId) {
        return surveyService.selectSurveyBySurveyId(surveyId);
    }

    /**
     * 查询问卷列表
     *
     * @param survey 问卷
     * @return 问卷
     */
    @Override
    public List<Survey> selectSurveyList(Survey survey) {
        return surveyService.selectSurveyList(survey);
    }

    @Override
    public SurveyVo selectFullSurveyById(Long surveyId) {
        SurveyVo survey = surveyService.selectFullSurveyById(surveyId);
        return survey;
    }

    /**
     * 新增问卷
     *
     * @param survey 问卷
     * @return 结果
     */
    @Override
    public int insertSurvey(Survey survey) {
        survey.setCreateBy(getUsername());
        survey.setCreateTime(DateUtils.getNowDate());
        return surveyService.insertSurvey(survey);
    }

    /**
     * 修改问卷
     *
     * @param survey 问卷
     * @return 结果
     */
    @Override
    public int updateSurvey(Survey survey) {
        survey.setUpdateBy(getUsername());
        survey.setUpdateTime(DateUtils.getNowDate());
        return surveyService.updateSurvey(survey);
    }

    /**
     * 修改问卷状态
     *
     * @param survey 问卷
     * @return 结果
     */
    public int updateStatus(Survey survey)
    {
        survey.setUpdateBy(getUsername());
        survey.setUpdateTime(DateUtils.getNowDate());
        return surveyService.updateStatus(survey);
    }

    /**
     * 批量永久删除问卷
     *
     * @param surveyIds 需要永久删除的问卷主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSurveyBySurveyIds(Long[] surveyIds) {
        answerService.deleteAnswersBySurveyIds(surveyIds);
        docResultsService.deleteDocResultsBySurveyIds(surveyIds);
        optionsService.deleteOptionsBySurveyIds(surveyIds);
        questionService.deleteQuestionBySurveyIds(surveyIds);
        return surveyService.deleteSurveyBySurveyIds(surveyIds);
    }

    /**
     * 永久删除问卷信息
     *
     * @param surveyId 问卷主键
     * @return 结果
     */
    @Override
    public int deleteSurveyBySurveyId(Long surveyId) {
        return surveyService.deleteSurveyBySurveyId(surveyId);
    }

    /**
     * 批量删除问卷
     *
     * @param surveyIds 需要删除的问卷主键
     * @return 结果
     */
    @Override
    public int removeSurveyBySurveyIds(Long[] surveyIds) {
        return surveyService.removeSurveyBySurveyIds(surveyIds);
    }

    /**
     * 删除问卷信息
     *
     * @param surveyId 问卷主键
     * @return 结果
     */
    @Override
    public int removeSurveyBySurveyId(Long surveyId) {
        return surveyService.removeSurveyBySurveyId(surveyId);
    }

    /**
     * 批量发布问卷
     *
     * @param surveyIds 需要发布的问卷主键集合
     * @return 结果
     */
    @Override
    public int publishSurveyBySurveyIds(Long[] surveyIds) {
        return surveyService.publishSurveyBySurveyIds(surveyIds);
    }

    /**
     * 批量还原问卷
     *
     * @param surveyIds 需要发布的问卷主键集合
     * @return 结果
     */
    @Override
    public int restoreSurveyBySurveyIds(Long[] surveyIds) {
        return surveyService.restoreSurveyBySurveyIds(surveyIds);
    }

    /**
     * 撤销发布问卷
     *
     * @param surveyId 需要发布的问卷主键
     * @return 结果
     */
    @Override
    public int revokeSurveyBySurveyId(Long surveyId) {
        return surveyService.revokeSurveyBySurveyId(surveyId);
    }
}
