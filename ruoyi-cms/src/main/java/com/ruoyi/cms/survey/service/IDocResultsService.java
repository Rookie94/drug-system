package com.ruoyi.cms.survey.service;

import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.domain.vo.DocResultsVo;

import java.util.List;

/**
 * 问卷答案结果jsonService接口
 *
 * @author Shure
 * @date 2021-10-18
 */
public interface IDocResultsService {
    /**
     * 查询问卷答案结果json
     *
     * @param resultId 问卷答案结果json主键
     * @return 问卷答案结果json
     */
    DocResultsVo selectDocResultsById(Long resultId);

    /**
     * 查询问卷答案结果json列表
     *
     * @param docResultsVo 问卷答案结果json
     * @return 问卷答案结果json集合
     */
    List<DocResultsVo> selectDocResultsList(DocResultsVo docResultsVo);


    /**
     * 查询问卷答案结果json列表
     *
     * @param surveyId 问卷id
     * @return 问卷答案结果json集合
     */
    List<DocResultsVo> selectDocResultsBySurveyId(Long surveyId);


    /**
     * 新增问卷答案结果json
     *
     * @param docResults 问卷答案结果json
     * @return 结果
     */
    int insertDocResults(DocResults docResults);

    /**
     * 刷新问卷答案结果json
     *
     * @param resultIds 问卷答案结果json
     * @return 结果
     */
    int refreshDocResultsByIds(Long[] resultIds);


    /**
     * 修改问卷答案结果json
     *
     * @param docResults 问卷答案结果json
     * @return 结果
     */
    int updateDocResults(DocResults docResults);

    /**
     * 删除问卷答案结果json
     *
     * @param resultId 问卷答案结果json主键
     * @return 结果
     */
    int deleteDocResultsById(Long resultId);

    /**
     * 批量删除问卷答案结果json
     *
     * @param resultIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDocResultsByIds(Long[] resultIds);


    /**
     * 根据问卷主键删除答案结果
     *
     * @param surveyIds
     * @return
     */
    int deleteDocResultsBySurveyIds(Long[] surveyIds);
}
