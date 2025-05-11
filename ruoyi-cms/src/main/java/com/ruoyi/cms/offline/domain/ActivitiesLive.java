package com.ruoyi.cms.offline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 现场资讯对象 t_ofa_activities_live
 * 
 * @author admin
 * @date 2025-05-09
 */
public class ActivitiesLive extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 现场id */
    @Excel(name = "现场id")
    private Long liveId;

    /** 活动ID */
    @Excel(name = "活动ID")
    private Long activityId;

    /** 活动主题 */
    @Excel(name = "现场主题")
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

    /** 审批状态（0待审批 1已审批待发布,2已发布） */
    @Excel(name = "审批状态", readConverterExp = "0=待审批,1=已审批待发布,2已发布")
    private String appored;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
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
            .append("subject", getSubject())
            .append("img", getImg())
            .append("content", getContent())
            .append("status", getStatus())
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
