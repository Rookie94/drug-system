package com.ruoyi.cms.scale.service.impl;

import java.util.List;

import com.ruoyi.cms.scale.domain.LbsResults;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISerialNoService;
import com.ruoyi.system.service.impl.SerialNoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.scale.mapper.LbsResultsMapper;
import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.service.ILbsResultsService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 测评报告Service业务层处理
 * 
 * @author admin
 * @date 2025-05-16
 */
@Service
public class LbsResultsServiceImpl implements ILbsResultsService 
{
    @Autowired
    private LbsResultsMapper lbsResultsMapper;

    @Autowired
    private ISerialNoService serialNoService;

    /**
     * 查询测评报告
     * 
     * @param resultId 测评报告主键
     * @return 测评报告
     */
    @Override
    public LbsResultsVo selectLbsResultsByResultId(Long resultId)
    {
        return lbsResultsMapper.selectLbsResultsByResultId(resultId);
    }

    /**
     * 查询测评报告列表
     * 
     * @param lbsResults 测评报告
     * @return 测评报告
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<LbsResultsVo> selectLbsResultsList(LbsResultsVo lbsResults)
    {
        return lbsResultsMapper.selectLbsResultsList(lbsResults);
    }

    /**
     * 新增测评报告
     * 
     * @param lbsResults 测评报告
     * @return 结果
     */
    @Override
    public int insertLbsResults(LbsResults lbsResults)
    {
        lbsResults.setCommitTime(DateUtils.getNowDate());
        lbsResults.setUserId(getUserId());
        lbsResults.setDeptId(getDeptId());
        lbsResults.setCreateBy(getUsername());
        lbsResults.setCreateTime(DateUtils.getNowDate());
        String resultNo=serialNoService.getSerialNumber("ScaleReportNo");
        if(resultNo.equals("")){
            resultNo=serialNoService.getSerialNumber("ScaleReportNo");
        }
        lbsResults.setResultNo(resultNo);
        return lbsResultsMapper.insertLbsResults(lbsResults);
    }

    /**
     * 修改测评报告
     * 
     * @param lbsResults 测评报告
     * @return 结果
     */
    @Override
    public int updateLbsResults(LbsResults lbsResults)
    {
        lbsResults.setUpdateBy(getUsername());
        lbsResults.setUpdateTime(DateUtils.getNowDate());
        return lbsResultsMapper.updateLbsResults(lbsResults);
    }

    /**
     * 批量删除测评报告
     * 
     * @param resultIds 需要删除的测评报告主键
     * @return 结果
     */
    @Override
    public int deleteLbsResultsByResultIds(Long[] resultIds)
    {
        return lbsResultsMapper.deleteLbsResultsByResultIds(resultIds);
    }

    /**
     * 删除测评报告信息
     * 
     * @param resultId 测评报告主键
     * @return 结果
     */
    @Override
    public int deleteLbsResultsByResultId(Long resultId)
    {
        return lbsResultsMapper.deleteLbsResultsByResultId(resultId);
    }
}
