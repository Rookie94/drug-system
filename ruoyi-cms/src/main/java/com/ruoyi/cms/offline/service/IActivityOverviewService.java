package com.ruoyi.cms.offline.service;

import com.ruoyi.cms.offline.domain.vo.ActivityOverviewVo;

public interface IActivityOverviewService {
    /**
     * 获取活动总览信息
     * @param activityId 活动ID
     * @return 活动总览信息
     */
    ActivityOverviewVo getActivityOverview(Integer activityId);
}