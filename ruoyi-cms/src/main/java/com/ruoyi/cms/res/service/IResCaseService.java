package com.ruoyi.cms.res.service;

import java.util.List;

import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.cms.res.domain.ResCase;

/**
 * 戒治案例Service接口
 * 
 * @author admin
 * @date 2025-04-10
 */
public interface IResCaseService 
{
    /**
     * 查询戒治案例
     * 
     * @param caseid 戒治案例主键
     * @return 戒治案例
     */
    public ResCase selectResCaseByCaseid(Long caseid);

    /**
     * 查询戒治案例列表
     * 
     * @param resCase 戒治案例
     * @return 戒治案例集合
     */
    public List<ResCase> selectResCaseList(ResCase resCase);

    /**
     * 新增戒治案例
     * 
     * @param resCase 戒治案例
     * @return 结果
     */
    public int insertResCase(ResCase resCase);

    /**
     * 修改戒治案例
     * 
     * @param resCase 戒治案例
     * @return 结果
     */
    public int updateResCase(ResCase resCase);

    /**
     * 批量删除戒治案例
     * 
     * @param caseids 需要删除的戒治案例主键集合
     * @return 结果
     */
    public int deleteResCaseByCaseids(Long[] caseids);

    /**
     * 删除戒治案例信息
     * 
     * @param caseid 戒治案例主键
     * @return 结果
     */
    public int deleteResCaseByCaseid(Long caseid);

    /**
     * 修改戒治案例状态
     *
     * @param resCase 戒治机构
     * @return 结果
     */
    public int updateStatus(ResCase resCase);

    /**
     * 批量审批
     *
     * @param apporParams 批量审批参数
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);

    /**
     * 批量反审批戒治案例
     *
     * @param ids 需要删除的戒治案例主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] ids);

    /**
     * 反审批戒治案例信息
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public int unApporById(Long ids);

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids);


}
