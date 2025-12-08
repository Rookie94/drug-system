package com.ruoyi.cms.offline.mapper;

import com.ruoyi.cms.offline.domain.vo.ActivityOverviewVo;
import com.ruoyi.cms.offline.domain.vo.SubActivityVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityOverviewMapper {
    /**
     * 获取活动总览信息
     * @param activityId 活动ID
     * @return 活动总览信息
     */
    ActivityOverviewVo selectActivityOverview(@Param("activityId") Integer activityId);

    /**
     * 获取活动基本信息
     * @param activityId 活动ID
     * @return 活动基本信息
     */
    ActivityOverviewVo selectActivityBaseInfo(@Param("activityId") Integer activityId);

    /**
     * 获取子活动列表
     * @param parentActivityId 父活动ID
     * @return 子活动列表
     */
    List<SubActivityVo> selectSubActivities(@Param("parentActivityId") Integer parentActivityId);

    /**
     * 获取活动报名人数
     * @param activityId 活动ID
     * @return 报名人数
     */
    Integer selectSignupCount(@Param("activityId") Integer activityId);

    /**
     * 获取活动签到人数
     * @param activityId 活动ID
     * @return 签到人数
     */
    Integer selectCheckinCount(@Param("activityId") Integer activityId);

    /**
     * 获取活动资讯数量
     * @param activityId 活动ID
     * @return 资讯数量
     */
    Integer selectLiveCount(@Param("activityId") Integer activityId);

    /**
     * 获取活动评价统计
     * @param activityId 活动ID
     * @return 评价统计
     */
    ActivityOverviewVo selectReviewStats(@Param("activityId") Integer activityId);

    /**
     * 获取参与的子活动名称
     * @param activityId 活动ID
     * @return 子活动名称列表
     */
    List<String> selectParticipatedSubActivityNames(@Param("activityId") Integer activityId);

    /**
     * 获取参与的子活动数量
     * @param activityId 活动ID
     * @return 参与的子活动数量
     */
    Integer selectParticipatedSubActivityCount(@Param("activityId") Integer activityId);
}