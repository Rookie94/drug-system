package com.ruoyi.cms.online.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 群工作人员对象 v_chat_groupmbrs
 * 
 * @author admin
 * @date 2025-04-30
 */
public class ChatGroupMenbers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成员id */
    private Long mbrId;

    /** 群组id */
    private Long groupId;

    /** 用户id */
    private Long userId;

    /** 部门id */
    private Long deptId;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String appored;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 审批者 */
    @Excel(name = "审批者")
    private String apporBy;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    public void setMbrId(Long mbrId) 
    {
        this.mbrId = mbrId;
    }

    public Long getMbrId() 
    {
        return mbrId;
    }
    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
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
            .append("mbrId", getMbrId())
            .append("groupId", getGroupId())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("appored", getAppored())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
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
