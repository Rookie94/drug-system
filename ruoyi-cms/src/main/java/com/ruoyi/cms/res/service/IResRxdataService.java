package com.ruoyi.cms.res.service;

import java.util.List;
import com.ruoyi.cms.res.domain.ResRxdata;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 戒治处方Service接口
 * 
 * @author admin
 * @date 2025-05-08
 */
public interface IResRxdataService 
{
    /**
     * 查询戒治处方
     * 
     * @param rxId 戒治处方主键
     * @return 戒治处方
     */
    public ResRxdata selectResRxdataByRxId(Long rxId);

    /**
     * 查询戒治处方列表
     * 
     * @param resRxdata 戒治处方
     * @return 戒治处方集合
     */
    public List<ResRxdata> selectResRxdataList(ResRxdata resRxdata);

    /**
     * 新增戒治处方
     * 
     * @param resRxdata 戒治处方
     * @return 结果
     */
    public int insertResRxdata(ResRxdata resRxdata);

    /**
     * 修改戒治处方
     * 
     * @param resRxdata 戒治处方
     * @return 结果
     */
    public int updateResRxdata(ResRxdata resRxdata);

    /**
     * 批量删除戒治处方
     * 
     * @param rxIds 需要删除的戒治处方主键集合
     * @return 结果
     */
    public int deleteResRxdataByRxIds(Long[] rxIds);

    /**
     * 删除戒治处方信息
     * 
     * @param rxId 戒治处方主键
     * @return 结果
     */
    public int deleteResRxdataByRxId(Long rxId);

    /**
     * 修改状态
     *
     * @param resRxdata 处方
     * @return 结果
     */
    public int updateStatus(ResRxdata resRxdata);

    /**
     * 批量审批
     *
     * @param apporParams 批量审批参数
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);
    /**
     * 反审批
     *
     * @param Id 主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);

}
