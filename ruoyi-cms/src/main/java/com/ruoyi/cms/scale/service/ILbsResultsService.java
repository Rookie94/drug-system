package com.ruoyi.cms.scale.service;

import java.util.List;

import com.ruoyi.cms.scale.domain.LbsResults;
import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;

/**
 * 测评报告Service接口
 * 
 * @author admin
 * @date 2025-05-16
 */
public interface ILbsResultsService 
{
    /**
     * 查询测评报告
     * 
     * @param resultId 测评报告主键
     * @return 测评报告
     */
    public LbsResultsVo selectLbsResultsByResultId(Long resultId);

    /**
     * 查询测评报告列表
     * 
     * @param lbsResults 测评报告
     * @return 测评报告集合
     */
    public List<LbsResultsVo> selectLbsResultsList(LbsResultsVo lbsResults);

    /**
     * 新增测评报告
     * 
     * @param lbsResults 测评报告
     * @return 结果
     */
    public int insertLbsResults(LbsResults lbsResults);

    /**
     * 修改测评报告
     * 
     * @param lbsResults 测评报告
     * @return 结果
     */
    public int updateLbsResults(LbsResults lbsResults);

    /**
     * 批量删除测评报告
     * 
     * @param resultIds 需要删除的测评报告主键集合
     * @return 结果
     */
    public int deleteLbsResultsByResultIds(Long[] resultIds);

    /**
     * 删除测评报告信息
     * 
     * @param resultId 测评报告主键
     * @return 结果
     */
    public int deleteLbsResultsByResultId(Long resultId);
}
