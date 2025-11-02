package com.ruoyi.cms.survey.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.cms.survey.domain.Answers;
import com.ruoyi.cms.survey.mapper.AnswersMapper;
import com.ruoyi.cms.survey.service.IAnswersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷答案结果Service业务层处理
 *
 * @author Shure
 * @date 2021-10-18
 */
@Service
public class AnswersServiceImpl implements IAnswersService {
    @Autowired
    private AnswersMapper answersMapper;

    /**
     * 查询问卷答案结果
     *
     * @param answerId 问卷答案结果主键
     * @return 问卷答案结果
     */
    @Override
    public Answers selectAnswersById(Long answerId) {
        return answersMapper.selectAnswersById(answerId);
    }

    /**
     * 查询问卷答案结果列表
     *
     * @param answers 问卷答案结果
     * @return 问卷答案结果
     */
    @Override
    public List<Answers> selectAnswersList(Answers answers) {
        return answersMapper.selectAnswersList(answers);
    }

    /**
     * 新增问卷答案结果
     *
     * @param answers 问卷答案结果
     * @return 结果
     */
    @Override
    public int insertAnswers(Answers answers) {
        answers.setCreateTime(DateUtils.getNowDate());
        return answersMapper.insertAnswers(answers);
    }

    /**
     * 修改问卷答案结果
     *
     * @param answers 问卷答案结果
     * @return 结果
     */
    @Override
    public int updateAnswers(Answers answers) {
        return answersMapper.updateAnswers(answers);
    }

    /**
     * 批量删除问卷答案结果
     *
     * @param answerIds 需要删除的问卷答案结果主键
     * @return 结果
     */
    @Override
    public int deleteAnswersByIds(Long[] answerIds) {
        return answersMapper.deleteAnswersByIds(answerIds);
    }

    /**
     * 删除问卷答案结果信息
     *
     * @param answerId 问卷答案结果主键
     * @return 结果
     */
    @Override
    public int deleteAnswersById(Long answerId) {
        return answersMapper.deleteAnswersById(answerId);
    }

    /**
     * 根据问卷主键删除结果
     *
     * @param surveyIds
     * @return
     */
    @Override
    public int deleteAnswersBySurveyIds(Long[] surveyIds) {
        return answersMapper.deleteAnswersBySurveyIds(surveyIds);
    }
}
