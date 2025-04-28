package com.ruoyi.cms.offline.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预约详情对象 v_signup
 * 
 * @author admin
 * @date 2025-04-28
 */
public class SignUp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预约id */
    private Long signId;

    /** 活动id */
    private Long activityId;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date signTime;

    /** 用户id */
    private Long userId;

    /** 部门id */
    private Long deptId;

     public void setSignId(Long signId)
    {
        this.signId = signId;
    }

    public Long getSignId() 
    {
        return signId;
    }
    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }
    public Long getActivityId() 
    {
        return activityId;
    }

    public void setSignTime(Date signTime) 
    {
        this.signTime = signTime;
    }

    public Date getSignTime() 
    {
        return signTime;
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
            .append("signId", getSignId())
            .append("activityId", getActivityId())
            .append("signTime", getSignTime())
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
