package com.ruoyi.cms.offline.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * VIEW对象 t_ofa_activities_live
 * 
 * @author admin
 * @date 2025-05-09
 */
public class ActivitiesLiveVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 现场id */
    @Excel(name = "现场id")
    private Long liveId;

    /** 活动ID */
    @Excel(name = "活动ID")
    private Long activityId;

    /** 父活动ID */
    @Excel(name = "父活动ID")
    private Long parentActivityId;

    /** 活动主题 */
    @Excel(name = "活动主题")
    private String activityName;

    /** 活动主题 */
    @Excel(name = "活动主题")
    private String parentActivityName;

    /** 活动主题 */
    @Excel(name = "活动主题")
    private String subject;

    /** 现场照片 */
    @Excel(name = "现场照片")
    private String img;

    /** 现场详情 */
    @Excel(name = "现场详情")
    private String content;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 活动地点 */
    @Excel(name = "活动地点")
    private String address;

    /** 活动组织者 */
    @Excel(name = "活动组织者")
    private String orgName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 活动类型 */
    @Excel(name = "活动类型")
    private String activityType;

    /** 审批状态（0待审批 1已审批待发布,2已发布） */
    @Excel(name = "审批状态", readConverterExp = "0=待审批,1=已审批待发布,2已发布")
    private String appored;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户ID */
    private Long userId;

    /** 归属部门 */
    private Long deptId;

    /** 审核者 */
    @Excel(name = "审核者")
    private String apporBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date apporTime;

    public void setLiveId(Long liveId) 
    {
        this.liveId = liveId;
    }

    public Long getLiveId() 
    {
        return liveId;
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

    public void setParentActivityName(String parentActivityName) 
    {
        this.parentActivityName = parentActivityName;
    }

    public String getParentActivityName() 
    {
        return parentActivityName;
    }

    public void setSubject(String subject) 
    {
        this.subject = subject;
    }

    public String getSubject() 
    {
        return subject;
    }

    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setActivityType(String activityType) 
    {
        this.activityType = activityType;
    }

    public String getActivityType() 
    {
        return activityType;
    }

    public void setAppored(String appored) 
    {
        this.appored = appored;
    }

    public String getAppored() 
    {
        return appored;
    }

    public void setPublishTime(Date publishTime) 
    {
        this.publishTime = publishTime;
    }

    public Date getPublishTime() 
    {
        return publishTime;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("liveId", getLiveId())
            .append("activityId", getActivityId())
            .append("parentActivityId", getParentActivityId())
            .append("activityName", getActivityName())
            .append("parentActivityName", getParentActivityName())
            .append("subject", getSubject())
            .append("img", getImg())
            .append("content", getContent())
            .append("status", getStatus())
            .append("address", getAddress())
            .append("orgName", getOrgName())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("activityType", getActivityType())
            .append("appored", getAppored())
            .append("publishTime", getPublishTime())
            .append("delFlag", getDelFlag())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("apporBy", getApporBy())
            .append("apporTime", getApporTime())
            .append("remark", getRemark())
            .toString();
    }
}
