package com.ruoyi.cms.offline.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesMapper;
import com.ruoyi.cms.offline.domain.Activities;
import com.ruoyi.cms.offline.service.IActivitiesService;

/**
 * 活动发布Service业务层处理
 * 
 * @author admin
 * @date 2025-03-30
 */
@Service
public class ActivitiesServiceImpl implements IActivitiesService 
{
    @Autowired
    private ActivitiesMapper activitiesMapper;

    /**
     * 查询活动发布
     * 
     * @param activityId 活动发布主键
     * @return 活动发布
     */
    @Override
    public Activities selectActivitiesByActivityId(Long activityId)
    {
        return activitiesMapper.selectActivitiesByActivityId(activityId);
    }

    /**
     * 查询活动发布列表
     * 
     * @param activities 活动发布
     * @return 活动发布
     */
    @Override
    public List<Activities> selectActivitiesList(Activities activities)
    {
        return activitiesMapper.selectActivitiesList(activities);
    }

    /**
     * 新增活动发布
     * 
     * @param activities 活动发布
     * @return 结果
     */
    @Override
    public int insertActivities(Activities activities)
    {
        activities.setCreateTime(DateUtils.getNowDate());
        return activitiesMapper.insertActivities(activities);
    }

    /**
     * 修改活动发布
     * 
     * @param activities 活动发布
     * @return 结果
     */
    @Override
    public int updateActivities(Activities activities)
    {
        activities.setUpdateTime(DateUtils.getNowDate());
        return activitiesMapper.updateActivities(activities);
    }

    /**
     * 批量删除活动发布
     * 
     * @param activityIds 需要删除的活动发布主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesByActivityIds(Long[] activityIds)
    {
        return activitiesMapper.deleteActivitiesByActivityIds(activityIds);
    }

    /**
     * 删除活动发布信息
     * 
     * @param activityId 活动发布主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesByActivityId(Long activityId)
    {
        return activitiesMapper.deleteActivitiesByActivityId(activityId);
    }
}
