package com.ruoyi.cms.survey.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.mapper.DocResultsMapper;
import com.ruoyi.cms.survey.service.IDocResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 问卷答案结果jsonService业务层处理
 *
 * @author Shure
 * @date 2021-10-18
 */
@Service
public class DocResultsServiceImpl implements IDocResultsService {
    @Autowired
    private DocResultsMapper docResultsMapper;

    /**
     * 查询问卷答案结果json
     *
     * @param resultId 问卷答案结果json主键
     * @return 问卷答案结果json
     */
    @Override
    public DocResults selectDocResultsById(Long resultId) {
        return docResultsMapper.selectDocResultsById(resultId);
    }

    /**
     * 查询问卷答案结果json列表
     *
     * @param docResults 问卷答案结果json
     * @return 问卷答案结果json
     */
    @Override
    public List<DocResults> selectDocResultsList(DocResults docResults) {
        return docResultsMapper.selectDocResultsList(docResults);
    }

    /**
     * 查询问卷答案结果json列表
     *
     * @param surveyId 问卷id
     * @return 问卷答案结果json集合
     */
    @Override
    public List<DocResults> selectDocResultsBySurveyId(Long surveyId) {
        return docResultsMapper.selectDocResultsBySurveyId(surveyId);
    }

    /**
     * 新增问卷答案结果json
     *
     * @param docResults 问卷答案结果json
     * @return 结果
     */
    @Override
    public int insertDocResults(DocResults docResults) {
        docResults.setCreateTime(DateUtils.getNowDate());
        return docResultsMapper.insertDocResults(docResults);
    }

    /**
     * 修改问卷答案结果json
     *
     * @param docResults 问卷答案结果json
     * @return 结果
     */
    @Override
    public int updateDocResults(DocResults docResults) {
        return docResultsMapper.updateDocResults(docResults);
    }

    /**
     * 批量删除问卷答案结果json
     *
     * @param ResultsIds 需要删除的问卷答案结果json主键
     * @return 结果
     */
    @Override
    public int deleteDocResultsByIds(Long[] ResultsIds) {
        return docResultsMapper.deleteDocResultsByIds(ResultsIds);
    }

    /**
     * 删除问卷答案结果json信息
     *
     * @param resultId 问卷答案结果json主键
     * @return 结果
     */
    @Override
    public int deleteDocResultsById(Long resultId) {
        return docResultsMapper.deleteDocResultsById(resultId);
    }

    /**
     * 根据问卷主键删除答案结果
     *
     * @param surveyIds
     * @return
     */
    @Override
    public int deleteDocResultsBySurveyIds(Long[] surveyIds) {
        return docResultsMapper.deleteDocResultsBySurveyIds(surveyIds);
    }
}
