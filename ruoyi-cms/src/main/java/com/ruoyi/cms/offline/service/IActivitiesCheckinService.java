package com.ruoyi.cms.offline.service;

import java.util.List;
import com.ruoyi.cms.offline.domain.ActivitiesCheckin;
import com.ruoyi.cms.offline.domain.vo.ActivitiesCheckinVo;

/**
 * 活动签到Service接口
 * 
 * @author admin
 * @date 2025-05-09
 */
public interface IActivitiesCheckinService 
{
    /**
     * 查询活动签到
     * 
     * @param checkinId 活动签到主键
     * @return 活动签到
     */
    public ActivitiesCheckinVo selectActivitiesCheckinByCheckinId(Long checkinId);

    /**
     * 查询活动签到列表
     * 
     * @param activitiesCheckin 活动签到
     * @return 活动签到集合
     */
    public List<ActivitiesCheckinVo> selectActivitiesCheckinList(ActivitiesCheckinVo activitiesCheckin);

    /**
     * 新增活动签到
     * 
     * @param activitiesCheckin 活动签到
     * @return 结果
     */
    public int insertActivitiesCheckin(ActivitiesCheckin activitiesCheckin);

    /**
     * 修改活动签到
     * 
     * @param activitiesCheckin 活动签到
     * @return 结果
     */
    public int updateActivitiesCheckin(ActivitiesCheckin activitiesCheckin);

    /**
     * 批量删除活动签到
     * 
     * @param checkinIds 需要删除的活动签到主键集合
     * @return 结果
     */
    public int deleteActivitiesCheckinByCheckinIds(Long[] checkinIds);

    /**
     * 删除活动签到信息
     * 
     * @param checkinId 活动签到主键
     * @return 结果
     */
    public int deleteActivitiesCheckinByCheckinId(Long checkinId);
}
