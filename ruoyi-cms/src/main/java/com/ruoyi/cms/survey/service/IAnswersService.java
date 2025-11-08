package com.ruoyi.cms.survey.service;

import com.ruoyi.cms.survey.domain.Answers;
import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.domain.vo.AnswersVo;

import java.util.List;

/**
 * 问卷答案结果Service接口
 *
 * @author Shure
 * @date 2021-10-18
 */
public interface IAnswersService {
    /**
     * 查询问卷答案结果
     *
     * @param answerId 问卷答案结果主键
     * @return 问卷答案结果
     */
    public Answers selectAnswersById(Long answerId);

    /**
     * 查询问卷答案结果列表
     *
     * @param answers 问卷答案结果
     * @return 问卷答案结果集合
     */
    public List<Answers> selectAnswersList(Answers answers);


    public List<Answers> parseJsonResult(DocResults docResults);


    /**
     * 新增问卷答案结果
     *
     * @param answers 问卷答案结果
     * @return 结果
     */
    public int insertAnswers(Answers answers);

    /**
     * 批量插入问卷答案
     * @param answersVo 包含答案列表的值对象
     * @return 插入的记录数
     */
    public int batchInsertAnswer(AnswersVo answersVo);

    /**
     * 修改问卷答案结果
     *
     * @param answers 问卷答案结果
     * @return 结果
     */
    public int updateAnswers(Answers answers);

    /**
     * 批量删除问卷答案结果
     *
     * @param answerIds 需要删除的问卷答案结果主键集合
     * @return 结果
     */
    public int deleteAnswersByIds(Long[] answerIds);

    /**
     * 删除问卷答案结果信息
     *
     * @param answerId 问卷答案结果主键
     * @return 结果
     */
    public int deleteAnswersById(Long answerId);


    /**
     * 根据主键删除结果
     *
     * @param resultId
     * @return
     */
    public int deleteAnswersByResultId(Long resultId) ;

    /**
     * 根据主键删除结果
     *
     * @param resultIds
     * @return
     */
    public int deleteAnswersByResultIds(Long[] resultIds);


    /**
     * 根据问卷主键删除结果
     *
     * @param surveyId
     * @return
     */
    public int deleteAnswersBySurveyId(Long surveyId);

    /**
     * 根据问卷主键删除结果
     *
     * @param surveyIds
     * @return
     */
    public int deleteAnswersBySurveyIds(Long[] surveyIds);

}
