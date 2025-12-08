package com.ruoyi.cms.offline.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class SubActivityVo {
    /** 子活动ID */
    private Integer subActivityId;

    /** 子活动代码 */
    private String subActivityCode;

    /** 子活动名称 */
    private String subActivityName;

    /** 子活动类型 */
    private String subActivityType;

    /** 子活动类型名称 */
    private String subActivityTypeName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 审批状态 */
    private String appored;

    /** 审批状态名称 */
    private String apporedName;

    /** 评价等级 */
    private String rating;

    /** 评价等级名称 */
    private String ratingName;

    /** 是否参与（数据库返回1/0，转换为boolean） */
    private Boolean participated;

    // getter和setter方法

    public Integer getSubActivityId() {
        return subActivityId;
    }

    public void setSubActivityId(Integer subActivityId) {
        this.subActivityId = subActivityId;
    }

    public String getSubActivityCode() {
        return subActivityCode;
    }

    public void setSubActivityCode(String subActivityCode) {
        this.subActivityCode = subActivityCode;
    }

    public String getSubActivityName() {
        return subActivityName;
    }

    public void setSubActivityName(String subActivityName) {
        this.subActivityName = subActivityName;
    }

    public String getSubActivityType() {
        return subActivityType;
    }

    public void setSubActivityType(String subActivityType) {
        this.subActivityType = subActivityType;
    }

    public String getSubActivityTypeName() {
        return subActivityTypeName;
    }

    public void setSubActivityTypeName(String subActivityTypeName) {
        this.subActivityTypeName = subActivityTypeName;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingName() {
        return ratingName;
    }

    public void setRatingName(String ratingName) {
        this.ratingName = ratingName;
    }

    public Boolean getParticipated() {
        return participated != null && participated;
    }

    public void setParticipated(Boolean participated) {
        this.participated = participated;
    }

    // 为了方便数据库映射，添加一个接受Integer的方法
    public void setParticipatedFromInt(Integer participated) {
        this.participated = participated != null && participated == 1;
    }
}