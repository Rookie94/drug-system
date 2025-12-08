package com.ruoyi.cms.offline.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;

public class ActivityOverviewVo {

    /** 活动ID */
    private Integer activityId;

    /** 活动代码 */
    private String activityCode;

    /** 活动名称 */
    private String activityName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 活动类型 */
    private String activityType;

    /** 活动类型名称 */
    private String activityTypeName;

    /** 状态 */
    private String appored;

    /** 状态名称 */
    private String apporedName;

    /** 报名人数 */
    private Integer signupCount;

    /** 签到人数 */
    private Integer checkinCount;

    /** 子活动列表 */
    private List<SubActivityVo> subActivities;

    /** 资讯数量 */
    private Integer liveCount;

    /** 好评数量 */
    private Integer goodReviewCount;

    /** 差评数量 */
    private Integer badReviewCount;

    /** 中评数量 */
    private Integer mediumReviewCount;

    /** 好评率 */
    private Double goodRate;

    /** 参与的子活动数量 */
    private Integer participatedSubActivityCount;

    /** 参与的子活动名称列表 */
    private List<String> participatedSubActivityNames;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityTypeName() {
        return activityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        this.activityTypeName = activityTypeName;
    }

    public String getAppored() {
        return appored;
    }

    public void setAppored(String appored) {
        this.appored = appored;
    }

    public String getApporedName() {
        return apporedName;
    }

    public void setApporedName(String apporedName) {
        this.apporedName = apporedName;
    }

    public Integer getSignupCount() {
        return signupCount;
    }

    public void setSignupCount(Integer signupCount) {
        this.signupCount = signupCount;
    }

    public Integer getCheckinCount() {
        return checkinCount;
    }

    public void setCheckinCount(Integer checkinCount) {
        this.checkinCount = checkinCount;
    }

    public List<SubActivityVo> getSubActivities() {
        return subActivities;
    }

    public void setSubActivities(List<SubActivityVo> subActivities) {
        this.subActivities = subActivities;
    }

    public Integer getLiveCount() {
        return liveCount;
    }

    public void setLiveCount(Integer liveCount) {
        this.liveCount = liveCount;
    }

    public Integer getGoodReviewCount() {
        return goodReviewCount;
    }

    public void setGoodReviewCount(Integer goodReviewCount) {
        this.goodReviewCount = goodReviewCount;
    }

    public Integer getBadReviewCount() {
        return badReviewCount;
    }

    public void setBadReviewCount(Integer badReviewCount) {
        this.badReviewCount = badReviewCount;
    }

    public Integer getMediumReviewCount() {
        return mediumReviewCount;
    }

    public void setMediumReviewCount(Integer mediumReviewCount) {
        this.mediumReviewCount = mediumReviewCount;
    }

    public Double getGoodRate() {
        return goodRate;
    }

    public void setGoodRate(Double goodRate) {
        this.goodRate = goodRate;
    }

    public Integer getParticipatedSubActivityCount() {
        return participatedSubActivityCount;
    }

    public void setParticipatedSubActivityCount(Integer participatedSubActivityCount) {
        this.participatedSubActivityCount = participatedSubActivityCount;
    }

    public List<String> getParticipatedSubActivityNames() {
        return participatedSubActivityNames;
    }

    public void setParticipatedSubActivityNames(List<String> participatedSubActivityNames) {
        this.participatedSubActivityNames = participatedSubActivityNames;
    }
}