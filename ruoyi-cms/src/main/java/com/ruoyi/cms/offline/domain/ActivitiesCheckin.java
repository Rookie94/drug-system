package com.ruoyi.cms.offline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 活动签到对象 v_ofa_activities_checkin
 * 
 * @author admin
 * @date 2025-05-09
 */
public class ActivitiesCheckin extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 签到id */
    @Excel(name = "签到id")
    private Long checkinId;

    /** 活动id */
    @Excel(name = "活动id")
    private Long activityId;

    /** 签到时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签到时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkinTime;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 归属部门 */
    @Excel(name = "归属部门")
    private Long deptId;


    public void setCheckinId(Long checkinId) 
    {
        this.checkinId = checkinId;
    }

    public Long getCheckinId() 
    {
        return checkinId;
    }

    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }

    public Long getActivityId() 
    {
        return activityId;
    }

    public void setCheckinTime(Date checkinTime) 
    {
        this.checkinTime = checkinTime;
    }

    public Date getCheckinTime() 
    {
        return checkinTime;
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


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("checkinId", getCheckinId())
            .append("activityId", getActivityId())
            .append("checkinTime", getCheckinTime())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
