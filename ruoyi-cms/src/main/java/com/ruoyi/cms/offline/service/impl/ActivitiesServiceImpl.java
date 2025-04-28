package com.ruoyi.cms.offline.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.cms.res.domain.ResCase;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ResApporParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesMapper;
import com.ruoyi.cms.offline.domain.Activities;
import com.ruoyi.cms.offline.service.IActivitiesService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 活动发布Service业务层处理
 *
 * @author admin
 * @date 2025-04-25
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
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<Activities> selectActivitiesList(Activities activities)
    {
        return activitiesMapper.selectActivitiesList(activities);
    }

    /**
     * 查询活动发布列表
     *
     * @param activities 活动发布
     * @return 活动发布
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<Activities> selectActivitiesWithChild(Activities activities)
    {
        return activitiesMapper.selectActivitiesWithChild(activities);
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
        activities.setUserId(getUserId());
        activities.setDeptId(getDeptId());
        activities.setCreateBy(getUsername());
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
        activities.setUpdateBy(getUsername());
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

    /**
     * 修改戒治案例状态
     *
     * @param activities 戒治机构
     * @return 结果
     */
    public int updateStatus(Activities activities)
    {
        if(activities.getStatus()=="0"){
            activities.setStatus("1");
        }
        else{
            activities.setStatus("0");
        }
        activities.setUpdateBy(getUsername());
        activities.setUpdateTime(DateUtils.getNowDate());
        return activitiesMapper.updateStatus(activities);
    }

    /**
     * 批量审批
     *
     * @param apporParams 审批参数
     * @return 结果
     */
    @Override
    public int apporByIds(ResApporParam apporParams)
    {
        apporParams.setApporBy(getUsername());
        apporParams.setApporTime(DateUtils.getNowDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(apporParams.flag==2){
            apporParams.setPublishTime(dateFormat.format(DateUtils.getNowDate()));
        }
        return activitiesMapper.apporByIds(apporParams);
    }

    /**
     * 反审批戒治案例
     *
     * @param id 戒治案例主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return activitiesMapper.unApporById(id);
    }

    /**
     * 批量反审批戒治案例
     *
     * @param ids 需要删除的戒治案例主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return activitiesMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return activitiesMapper.selectApporedByIds(ids);
    }

}
