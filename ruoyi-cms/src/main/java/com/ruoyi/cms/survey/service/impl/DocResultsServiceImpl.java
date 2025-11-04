package com.ruoyi.cms.survey.service.impl;

import com.ruoyi.cms.survey.domain.vo.DocResultsVo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.mapper.DocResultsMapper;
import com.ruoyi.cms.survey.service.IDocResultsService;
import com.ruoyi.system.service.ISerialNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.*;

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

    @Autowired
    private ISerialNoService serialNoService;

    /**
     * 查询问卷答案结果json
     *
     * @param resultId 问卷答案结果json主键
     * @return 问卷答案结果json
     */
    @Override
    public DocResultsVo selectDocResultsById(Long resultId) {
        return docResultsMapper.selectDocResultsById(resultId);
    }

    /**
     * 查询问卷答案结果json列表
     *
     * @param docResultsVo 问卷答案结果json
     * @return 问卷答案结果json
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<DocResultsVo> selectDocResultsList(DocResultsVo docResultsVo) {
        LoginUser user=getLoginUser();
        if(!user.getUser().getUserType().trim().equals("00")){
            docResultsVo.setUserId(user.getUserId());
        }
        else{
            docResultsVo.setUserId(null);
        }
        return docResultsMapper.selectDocResultsList(docResultsVo);
    }

    /**
     * 查询问卷答案结果json列表
     *
     * @param surveyId 问卷id
     * @return 问卷答案结果json集合
     */
    @Override
    public List<DocResultsVo> selectDocResultsBySurveyId(Long surveyId) {
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
        docResults.setCommitTime(DateUtils.getNowDate());
        docResults.setUserId(getUserId());
        docResults.setDeptId(getDeptId());
        docResults.setCreateBy(getUsername());
        docResults.setCreateTime(DateUtils.getNowDate());
        String resultNo=serialNoService.getSerialNumber("SurveyRecordNo");
        if(resultNo.equals("")){
            resultNo=serialNoService.getSerialNumber("SurveyRecordNo");
        }
        docResults.setResultNo(resultNo);
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
        docResults.setUpdateBy(getUsername());
        docResults.setUpdateTime(DateUtils.getNowDate());
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
