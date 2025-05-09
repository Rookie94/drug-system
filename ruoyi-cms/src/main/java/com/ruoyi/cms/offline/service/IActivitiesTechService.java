package com.ruoyi.cms.offline.service;

import java.util.List;

import com.ruoyi.cms.offline.domain.ActivitiesTech;
import com.ruoyi.cms.offline.domain.vo.ActivitiesTechVo;

/**
 * 活动戒治技术资料Service接口
 * 
 * @author admin
 * @date 2025-05-10
 */
public interface IActivitiesTechService 
{
    /**
     * 查询活动戒治技术资料
     * 
     * @param techId 活动戒治技术资料主键
     * @return 活动戒治技术资料
     */
    public ActivitiesTechVo selectActivitiesTechByTechId(Long techId);

    /**
     * 查询活动戒治技术资料列表
     * 
     * @param activitiesTech 活动戒治技术资料
     * @return 活动戒治技术资料集合
     */
    public List<ActivitiesTechVo> selectActivitiesTechList(ActivitiesTechVo activitiesTech);

    /**
     * 新增活动戒治技术资料
     * 
     * @param activitiesTech 活动戒治技术资料
     * @return 结果
     */
    public int insertActivitiesTech(ActivitiesTech activitiesTech);

    /**
     * 修改活动戒治技术资料
     * 
     * @param activitiesTech 活动戒治技术资料
     * @return 结果
     */
    public int updateActivitiesTech(ActivitiesTech activitiesTech);

    /**
     * 批量删除活动戒治技术资料
     * 
     * @param techIds 需要删除的活动戒治技术资料主键集合
     * @return 结果
     */
    public int deleteActivitiesTechByTechIds(Long[] techIds);

    /**
     * 删除活动戒治技术资料信息
     * 
     * @param techId 活动戒治技术资料主键
     * @return 结果
     */
    public int deleteActivitiesTechByTechId(Long techId);
}
