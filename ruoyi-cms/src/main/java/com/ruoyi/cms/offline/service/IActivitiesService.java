package com.ruoyi.cms.offline.service;

import java.util.List;
import com.ruoyi.cms.offline.domain.Activities;

/**
 * 活动发布Service接口
 * 
 * @author admin
 * @date 2025-03-30
 */
public interface IActivitiesService 
{
    /**
     * 查询活动发布
     * 
     * @param activityId 活动发布主键
     * @return 活动发布
     */
    public Activities selectActivitiesByActivityId(Long activityId);

    /**
     * 查询活动发布列表
     * 
     * @param activities 活动发布
     * @return 活动发布集合
     */
    public List<Activities> selectActivitiesList(Activities activities);

    /**
     * 新增活动发布
     * 
     * @param activities 活动发布
     * @return 结果
     */
    public int insertActivities(Activities activities);

    /**
     * 修改活动发布
     * 
     * @param activities 活动发布
     * @return 结果
     */
    public int updateActivities(Activities activities);

    /**
     * 批量删除活动发布
     * 
     * @param activityIds 需要删除的活动发布主键集合
     * @return 结果
     */
    public int deleteActivitiesByActivityIds(Long[] activityIds);

    /**
     * 删除活动发布信息
     * 
     * @param activityId 活动发布主键
     * @return 结果
     */
    public int deleteActivitiesByActivityId(Long activityId);
}
