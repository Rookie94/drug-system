package com.ruoyi.cms.offline.service.impl;

import com.ruoyi.cms.offline.domain.vo.ActivityOverviewVo;
import com.ruoyi.cms.offline.domain.vo.SubActivityVo;
import com.ruoyi.cms.offline.mapper.ActivityOverviewMapper;
import com.ruoyi.cms.offline.service.IActivityOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActivityOverviewServiceImpl implements IActivityOverviewService {

    @Autowired
    private ActivityOverviewMapper activityOverviewMapper;

    @Override
    public ActivityOverviewVo getActivityOverview(Integer activityId) {
        if (activityId == null) {
            return null;
        }

        // 获取基本信息
        ActivityOverviewVo overview = activityOverviewMapper.selectActivityBaseInfo(activityId);
        if (overview == null) {
            return null;
        }

        // 获取子活动列表
        List<SubActivityVo> subActivities = activityOverviewMapper.selectSubActivities(activityId);
        if (subActivities != null) {
            for (SubActivityVo subActivity : subActivities) {
                // 设置评价名称
                if ("1".equals(subActivity.getRating())) {
                    subActivity.setRatingName("好评");
                } else if ("2".equals(subActivity.getRating())) {
                    subActivity.setRatingName("中评");
                } else if ("3".equals(subActivity.getRating())) {
                    subActivity.setRatingName("差评");
                } else {
                    subActivity.setRatingName("未评价");
                }

                // 设置参与状态（将Integer转换为Boolean）
                if (subActivity.getParticipated() == null) {
                    subActivity.setParticipated(false);
                }
            }
        }
        overview.setSubActivities(subActivities);

        // 获取统计信息
        Integer signupCount = activityOverviewMapper.selectSignupCount(activityId);
        overview.setSignupCount(signupCount != null ? signupCount : 0);

        Integer checkinCount = activityOverviewMapper.selectCheckinCount(activityId);
        overview.setCheckinCount(checkinCount != null ? checkinCount : 0);

        Integer liveCount = activityOverviewMapper.selectLiveCount(activityId);
        overview.setLiveCount(liveCount != null ? liveCount : 0);

        // 获取参与的子活动数量
        Integer participatedCount = activityOverviewMapper.selectParticipatedSubActivityCount(activityId);
        overview.setParticipatedSubActivityCount(participatedCount != null ? participatedCount : 0);

        // 获取参与的子活动名称
        List<String> participatedNames = activityOverviewMapper.selectParticipatedSubActivityNames(activityId);
        overview.setParticipatedSubActivityNames(participatedNames);

        // 获取评价统计
        ActivityOverviewVo reviewStats = activityOverviewMapper.selectReviewStats(activityId);
        if (reviewStats != null) {
            overview.setGoodReviewCount(reviewStats.getGoodReviewCount() != null ? reviewStats.getGoodReviewCount() : 0);
            overview.setMediumReviewCount(reviewStats.getMediumReviewCount() != null ? reviewStats.getMediumReviewCount() : 0);
            overview.setBadReviewCount(reviewStats.getBadReviewCount() != null ? reviewStats.getBadReviewCount() : 0);
            overview.setGoodRate(reviewStats.getGoodRate() != null ? reviewStats.getGoodRate() : 0.0);
        } else {
            overview.setGoodReviewCount(0);
            overview.setMediumReviewCount(0);
            overview.setBadReviewCount(0);
            overview.setGoodRate(0.0);
        }

        // 设置活动类型和审批状态名称
        if ("0".equals(overview.getActivityType())) {
            overview.setActivityTypeName("经颅磁技术");
        } else if ("1".equals(overview.getActivityType())) {
            overview.setActivityTypeName("VR技术");
        } else if ("2".equals(overview.getActivityType())) {
            overview.setActivityTypeName("生物反馈技术");
        } else if ("3".equals(overview.getActivityType())) {
            overview.setActivityTypeName("内观技术");
        } else if ("9".equals(overview.getActivityType())) {
            overview.setActivityTypeName("其它");
        } else {
            overview.setActivityTypeName("未知");
        }

        if ("0".equals(overview.getAppored())) {
            overview.setApporedName("待审批");
        } else if ("1".equals(overview.getAppored())) {
            overview.setApporedName("发布中");
        } else if ("2".equals(overview.getAppored())) {
            overview.setApporedName("已发布报名中");
        } else if ("3".equals(overview.getAppored())) {
            overview.setApporedName("活动中");
        } else if ("4".equals(overview.getAppored())) {
            overview.setApporedName("已结束");
        } else if ("5".equals(overview.getAppored())) {
            overview.setApporedName("已归档");
        } else {
            overview.setApporedName("未知");
        }

        return overview;
    }
}