package com.ruoyi.cms.offline.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesCheckinMapper;
import com.ruoyi.cms.offline.domain.ActivitiesCheckin;
import com.ruoyi.cms.offline.domain.vo.ActivitiesCheckinVo;
import com.ruoyi.cms.offline.service.IActivitiesCheckinService;

/**
 * 活动签到Service业务层处理
 * 
 * @author admin
 * @date 2025-05-09
 */
@Service
public class ActivitiesCheckinServiceImpl implements IActivitiesCheckinService 
{
    @Autowired
    private ActivitiesCheckinMapper activitiesCheckinMapper;

    /**
     * 查询活动签到
     * 
     * @param checkinId 活动签到主键
     * @return 活动签到
     */
    @Override
    public ActivitiesCheckinVo selectActivitiesCheckinByCheckinId(Long checkinId)
    {
        return activitiesCheckinMapper.selectActivitiesCheckinByCheckinId(checkinId);
    }

    /**
     * 查询活动签到列表
     * 
     * @param activitiesCheckin 活动签到
     * @return 活动签到
     */
    @Override
    public List<ActivitiesCheckinVo> selectActivitiesCheckinList(ActivitiesCheckinVo activitiesCheckin)
    {
        return activitiesCheckinMapper.selectActivitiesCheckinList(activitiesCheckin);
    }

    /**
     * 新增活动签到
     * 
     * @param activitiesCheckin 活动签到
     * @return 结果
     */
    @Override
    public int insertActivitiesCheckin(ActivitiesCheckin activitiesCheckin)
    {
        activitiesCheckin.setCreateTime(DateUtils.getNowDate());
        return activitiesCheckinMapper.insertActivitiesCheckin(activitiesCheckin);
    }

    /**
     * 修改活动签到
     * 
     * @param activitiesCheckin 活动签到
     * @return 结果
     */
    @Override
    public int updateActivitiesCheckin(ActivitiesCheckin activitiesCheckin)
    {
        activitiesCheckin.setUpdateTime(DateUtils.getNowDate());
        return activitiesCheckinMapper.updateActivitiesCheckin(activitiesCheckin);
    }

    /**
     * 批量删除活动签到
     * 
     * @param checkinIds 需要删除的活动签到主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesCheckinByCheckinIds(Long[] checkinIds)
    {
        return activitiesCheckinMapper.deleteActivitiesCheckinByCheckinIds(checkinIds);
    }

    /**
     * 删除活动签到信息
     * 
     * @param checkinId 活动签到主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesCheckinByCheckinId(Long checkinId)
    {
        return activitiesCheckinMapper.deleteActivitiesCheckinByCheckinId(checkinId);
    }
}
