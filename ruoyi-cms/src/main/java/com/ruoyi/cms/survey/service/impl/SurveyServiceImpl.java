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
    private SurveyMapper surveyMapper;

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
        return surveyMapper.selectSurveyBySurveyId(surveyId);
    }

    /**
     * 查询问卷列表
     *
     * @param survey 问卷
     * @return 问卷
     */
    @Override
    public List<Survey> selectSurveyList(Survey survey) {
        return surveyMapper.selectSurveyList(survey);
    }

    @Override
    public SurveyVo selectFullSurveyById(Long surveyId) {
        SurveyVo survey = surveyMapper.selectFullSurveyById(surveyId);
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
        return surveyMapper.insertSurvey(survey);
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
        return surveyMapper.updateSurvey(survey);
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
        return surveyMapper.updateStatus(survey);
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
        return surveyMapper.deleteSurveyBySurveyIds(surveyIds);
    }

    /**
     * 永久删除问卷信息
     *
     * @param surveyId 问卷主键
     * @return 结果
     */
    @Override
    public int deleteSurveyBySurveyId(Long surveyId) {
        return surveyMapper.deleteSurveyBySurveyId(surveyId);
    }

    /**
     * 批量删除问卷
     *
     * @param surveyIds 需要删除的问卷主键
     * @return 结果
     */
    @Override
    public int removeSurveyBySurveyIds(Long[] surveyIds) {
        return surveyMapper.removeSurveyBySurveyIds(surveyIds);
    }

    /**
     * 删除问卷信息
     *
     * @param surveyId 问卷主键
     * @return 结果
     */
    @Override
    public int removeSurveyBySurveyId(Long surveyId) {
        return surveyMapper.removeSurveyBySurveyId(surveyId);
    }

    /**
     * 批量发布问卷
     *
     * @param surveyIds 需要发布的问卷主键集合
     * @return 结果
     */
    @Override
    public int publishSurveyBySurveyIds(Long[] surveyIds) {
        return surveyMapper.publishSurveyBySurveyIds(surveyIds);
    }

    /**
     * 批量还原问卷
     *
     * @param surveyIds 需要发布的问卷主键集合
     * @return 结果
     */
    @Override
    public int restoreSurveyBySurveyIds(Long[] surveyIds) {
        return surveyMapper.restoreSurveyBySurveyIds(surveyIds);
    }

    /**
     * 撤销发布问卷
     *
     * @param surveyId 需要发布的问卷主键
     * @return 结果
     */
    @Override
    public int revokeSurveyBySurveyId(Long surveyId) {
        return surveyMapper.revokeSurveyBySurveyId(surveyId);
    }
}
