package com.ruoyi.cms.offline.service;

import java.util.List;
import com.ruoyi.cms.offline.domain.Activities;
import com.ruoyi.cms.offline.domain.vo.ActivitiesStateVo;
import com.ruoyi.cms.res.domain.ResCase;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 活动发布Service接口
 *
 * @author admin
 * @date 2025-04-25
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
     * 查询带状态的活动发布列表
     *
     * @param activities 活动发布
     * @return 活动发布集合
     */
    public List<ActivitiesStateVo> selectActivitiesStateByStudent(ActivitiesStateVo activities);

    /**
     * 查询活动发布列表
     *
     * @param activities 活动发布
     * @return 活动发布集合
     */
    public List<Activities> selectActivitiesWithChild(Activities activities);

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


    /**
     * 修改戒治案例状态
     *
     * @param activities 戒治机构
     * @return 结果
     */
    public int updateStatus(Activities activities);

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
