package com.ruoyi.cms.res.service;

import java.util.List;
import com.ruoyi.cms.res.domain.ResOrginfo;

/**
 * 戒治机构Service接口
 * 
 * @author ruoyi
 * @date 2025-04-08
 */
public interface IResOrginfoService 
{
    /**
     * 查询戒治机构
     * 
     * @param orgid 戒治机构主键
     * @return 戒治机构
     */
    public ResOrginfo selectResOrginfoByOrgid(Long orgid);

    /**
     * 查询戒治机构列表
     * 
     * @param resOrginfo 戒治机构
     * @return 戒治机构集合
     */
    public List<ResOrginfo> selectResOrginfoList(ResOrginfo resOrginfo);

    /**
     * 新增戒治机构
     * 
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    public int insertResOrginfo(ResOrginfo resOrginfo);

    /**
     * 修改戒治机构
     * 
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    public int updateResOrginfo(ResOrginfo resOrginfo);

    /**
     * 修改戒治机构状态
     *
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    public int updateStatus(ResOrginfo resOrginfo);

    /**
     * 批量删除戒治机构
     * 
     * @param orgids 需要删除的戒治机构主键集合
     * @return 结果
     */
    public int deleteResOrginfoByOrgids(Long[] orgids);

    /**
     * 删除戒治机构信息
     * 
     * @param orgid 戒治机构主键
     * @return 结果
     */
    public int deleteResOrginfoByOrgid(Long orgid);

    /**
     * 批量审批戒治机构
     *
     * @param orgids 需要删除的戒治机构主键集合
     * @return 结果
     */
    public int apporResOrginfoByOrgids(Long[] orgids);

    /**
     * 审批戒治机构信息
     *
     * @param orgid 戒治机构主键
     * @return 结果
     */
    public int apporResOrginfoByOrgid(Long orgid);

    /**
     * 批量反审批戒治机构
     *
     * @param orgids 需要删除的戒治机构主键集合
     * @return 结果
     */
    public int unApporResOrginfoByOrgids(Long[] orgids);

    /**
     * 反审批戒治机构信息
     *
     * @param orgid 戒治机构主键
     * @return 结果
     */
    public int unApporResOrginfoByOrgid(Long orgid);

    /**
     * 查询已审核的单据清单
     *
     * @param orgids 戒治机构主键
     * @return 结果
     */
    public List<Integer> selectApporedOrgByOrgids(Long[] orgids);

}
