package com.ruoyi.cms.survey.mapper;

import com.ruoyi.cms.survey.domain.Answers;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问卷答案结果Mapper接口
 *
 * @author Shure
 * @date 2021-10-18
 */
@Mapper
public interface AnswersMapper {
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
     * @param answer 问卷答案结果
     * @return 问卷答案结果集合
     */
    public List<Answers> selectAnswersList(Answers answer);

    /**
     * 新增问卷答案结果
     *
     * @param answer 问卷答案结果
     * @return 结果
     */
    public int insertAnswers(Answers answer);

    /**
     * 修改问卷答案结果
     *
     * @param answer 问卷答案结果
     * @return 结果
     */
    public int updateAnswers(Answers answer);

    /**
     * 删除问卷答案结果
     *
     * @param answerId 问卷答案结果主键
     * @return 结果
     */
    public int deleteAnswersById(Long answerId);

    /**
     * 批量删除问卷答案结果
     *
     * @param answerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAnswersByIds(Long[] answerIds);

    /**
     * 根据问卷主键删除结果
     *
     * @param surveyIds
     * @return
     */
    int deleteAnswersBySurveyIds(Long[] surveyIds);
}
