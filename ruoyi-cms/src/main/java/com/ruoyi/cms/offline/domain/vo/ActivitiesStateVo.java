package com.ruoyi.cms.offline.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 活动发布对象 tb_activities
 *
 * @author admin
 * @date 2025-04-25
 */
public class ActivitiesStateVo extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private Long activityId;

    /** 子活动ID */
    @Excel(name = "子活动ID")
    private Long parentActivityId;

    /** 活动主题 */
    @Excel(name = "活动主题")
    private String activityName;

    /** 封面海报 */
    @Excel(name = "封面海报")
    private String img;

    /** 活动地点 */
    @Excel(name = "活动地点")
    private String address;

    /** 活动组织者 */
    @Excel(name = "活动组织者")
    private String orgName;

    /** 活动详情 */
    @Excel(name = "活动详情")
    private String content;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String tel;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 活动组织者 */
    @Excel(name = "活动分类")
    private String activityType;

    /** 活动状态 */
    @Excel(name = "活动状态")
    private String status;

    /** 审批状态（0待审批 1已发布） */
    @Excel(name = "审批状态", readConverterExp = "0=待审批,1=已发布")
    private String appored;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户ID */
    private Long userId;

    /** 归属部门 */
    private Long deptId;

    /** 更新者 */
    @Excel(name = "审核者")
    private String apporBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 活动状态描述 */
    @Excel(name = "活动状态描述")
    private String hdState;

    /** 是否报名 */
    @Excel(name = "是否报名")
    private String isSigned;

    /** 是否可以报名 */
    @Excel(name = "是否可以报名")
    private String canSign;

    /** 是否签到 */
    @Excel(name = "是否签到")
    private String isCheckIn;

    /** 是否可以签到 */
    @Excel(name = "是否可以签到")
    private String canCheckIn;

    /** 是否评价 */
    @Excel(name = "是否评价")
    private String isReview;

    /** 是否可以评价 */
    @Excel(name = "是否可以评价")
    private String canReview;

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public void setActivityId(Long activityId)
    {
        this.activityId = activityId;
    }

    public Long getActivityId()
    {
        return activityId;
    }
    public void setParentActivityId(Long parentActivityId)
    {
        this.parentActivityId = parentActivityId;
    }

    public Long getParentActivityId()
    {
        return parentActivityId;
    }
    public void setActivityName(String activityName)
    {
        this.activityName = activityName;
    }

    public String getActivityName()
    {
        return activityName;
    }
    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImg()
    {
        return img;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    public String getOrgName()
    {
        return orgName;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getTel()
    {
        return tel;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setAppored(String appored)
    {
        this.appored = appored;
    }

    public String getAppored()
    {
        return appored;
    }
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag()
    {
        return delFlag;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setApporBy(String apporBy)
    {
        this.apporBy = apporBy;
    }

    public String getApporBy()
    {
        return apporBy;
    }
    public void setApporTime(Date apporTime)
    {
        this.apporTime = apporTime;
    }

    public Date getApporTime()
    {
        return apporTime;
    }

    public String getHdState() {
        return hdState;
    }

    public void setHdState(String hdState) {
        this.hdState = hdState;
    }

    public String getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(String isSigned) {
        this.isSigned = isSigned;
    }

    public String getCanSign() {
        return canSign;
    }

    public void setCanSign(String canSign) {
        this.canSign = canSign;
    }

    public String getIsCheckIn() {
        return isCheckIn;
    }

    public void setIsCheckIn(String isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    public String getCanCheckIn() {
        return canCheckIn;
    }

    public void setCanCheckIn(String canCheckIn) {
        this.canCheckIn = canCheckIn;
    }

    public String getIsReview() {
        return isReview;
    }

    public void setIsReview(String isReview) {
        this.isReview = isReview;
    }

    public String getCanReview() {
        return canReview;
    }

    public void setCanReview(String canReview) {
        this.canReview = canReview;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("activityId", getActivityId())
                .append("parentActivityId", getParentActivityId())
                .append("orderNum", getOrderNum())
                .append("activityName", getActivityName())
                .append("img", getImg())
                .append("address", getAddress())
                .append("orgName", getOrgName())
                .append("content", getContent())
                .append("tel", getTel())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("status", getStatus())
                .append("appored", getAppored())
                .append("delFlag", getDelFlag())
                .append("userId", getUserId())
                .append("deptId", getDeptId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("apporBy", getApporBy())
                .append("apporTime", getApporTime())
                .append("hdState", getHdState())
                .append("isSigned", getIsSigned())
                .append("canSign", getCanSign())
                .append("isCheckIn", getIsCheckIn())
                .append("canCheckIn", getCanCheckIn())
                .append("isReview", getIsReview())
                .append("canReview", getCanReview())
                .toString();
    }
}
