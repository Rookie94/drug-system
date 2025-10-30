package com.ruoyi.cms.res.mapper;

import java.util.List;

import com.ruoyi.cms.res.domain.ResRxdata;
import com.ruoyi.cms.res.domain.ResTech;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 戒治技术宣传Mapper接口
 * 
 * @author admin
 * @date 2025-10-29
 */
public interface ResTechMapper 
{
    /**
     * 查询戒治技术宣传
     * 
     * @param techId 戒治技术宣传主键
     * @return 戒治技术宣传
     */
    public ResTech selectResTechByTechId(Long techId);

    /**
     * 查询戒治技术宣传列表
     * 
     * @param resTech 戒治技术宣传
     * @return 戒治技术宣传集合
     */
    public List<ResTech> selectResTechList(ResTech resTech);

    /**
     * 新增戒治技术宣传
     * 
     * @param resTech 戒治技术宣传
     * @return 结果
     */
    public int insertResTech(ResTech resTech);

    /**
     * 修改戒治技术宣传
     * 
     * @param resTech 戒治技术宣传
     * @return 结果
     */
    public int updateResTech(ResTech resTech);

    /**
     * 删除戒治技术宣传
     * 
     * @param techId 戒治技术宣传主键
     * @return 结果
     */
    public int deleteResTechByTechId(Long techId);

    /**
     * 批量删除戒治技术宣传
     * 
     * @param techIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResTechByTechIds(Long[] techIds);

    /**
     * 修改状态
     *
     * @param resTech 处方
     * @return 结果
     */
    public int updateStatus(ResTech resTech);

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
