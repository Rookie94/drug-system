package com.ruoyi.cms.offline.domain.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 活动查询VO对象
 *
 * @author ruoyi
 * @date 2023-10-24
 */
public class ActivitiesQueryVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private Long activityId;

    /** 活动代码 */
    private String activityCode;

    /** 父活动ID */
    private Long parentActivityId;

    /** 显示顺序 */
    private Integer orderNum;

    /** 活动主题 */
    private String activityName;

    /** 封面海报 */
    private String img;

    /** 活动地点 */
    private String address;

    /** 组织者 */
    private String orgName;

    /** 活动详情 */
    private String content;

    /** 联系电话 */
    private String tel;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 报名截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signDeadline;

    /** 状态（0启用 1停用） */
    private String status;

    /** 活动类型 */
    private String activityType;

    /** 审批状态 */
    private String appored;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /** 用户ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 审核者 */
    private String apporBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    /** 活动ID数组（用于查询条件） */
    private List<Long> activityIds;

    /** 是否只显示主活动（用于选择框查询） */
    private Boolean onlyMain;

    /** 选择模式（single/multiple） */
    private String selectMode;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public Long getParentActivityId() {
        return parentActivityId;
    }

    public void setParentActivityId(Long parentActivityId) {
        this.parentActivityId = parentActivityId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public Date getSignDeadline() {
        return signDeadline;
    }

    public void setSignDeadline(Date signDeadline) {
        this.signDeadline = signDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getAppored() {
        return appored;
    }

    public void setAppored(String appored) {
        this.appored = appored;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getApporBy() {
        return apporBy;
    }

    public void setApporBy(String apporBy) {
        this.apporBy = apporBy;
    }

    public Date getApporTime() {
        return apporTime;
    }

    public void setApporTime(Date apporTime) {
        this.apporTime = apporTime;
    }

    public List<Long> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<Long> activityIds) {
        this.activityIds = activityIds;
    }

    public Boolean getOnlyMain() {
        return onlyMain;
    }

    public void setOnlyMain(Boolean onlyMain) {
        this.onlyMain = onlyMain;
    }

    public String getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(String selectMode) {
        this.selectMode = selectMode;
    }
}