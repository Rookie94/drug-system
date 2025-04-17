package com.ruoyi.cms.job.service;

import java.util.List;
import com.ruoyi.cms.job.domain.ResJobinfo;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 招聘信息Service接口
 *
 * @author admin
 * @date 2025-04-10
 */
public interface IResJobinfoService
{
    /**
     * 查询招聘信息
     *
     * @param jobid 招聘信息主键
     * @return 招聘信息
     */
    public ResJobinfo selectResJobinfoByJobid(Long jobid);

    /**
     * 查询招聘信息列表
     *
     * @param resJobinfo 招聘信息
     * @return 招聘信息集合
     */
    public List<ResJobinfo> selectResJobinfoList(ResJobinfo resJobinfo);

    /**
     * 新增招聘信息
     *
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    public int insertResJobinfo(ResJobinfo resJobinfo);

    /**
     * 修改招聘信息
     *
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    public int updateResJobinfo(ResJobinfo resJobinfo);

    /**
     * 批量删除招聘信息
     *
     * @param jobids 需要删除的招聘信息主键集合
     * @return 结果
     */
    public int deleteResJobinfoByJobids(Long[] jobids);

    /**
     * 删除招聘信息信息
     *
     * @param jobid 招聘信息主键
     * @return 结果
     */
    public int deleteResJobinfoByJobid(Long jobid);

    /**
     * 修改工作状态
     *
     * @param resJobInfo 工作
     * @return 结果
     */
    public int updateStatus(ResJobinfo resJobInfo);

    /**
     * 批量审批
     *
     * @param apporParams 主键集合
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);
    /**
     * 批量反审批工作
     *
     * @param ids 需要删除的工作主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] ids);

    /**
     * 反审批工作信息
     *
     * @param ids 工作主键
     * @return 结果
     */
    public int unApporById(Long ids);

    /**
     * 查询已审核的单据清单
     *
     * @param ids 工作主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids);


}
