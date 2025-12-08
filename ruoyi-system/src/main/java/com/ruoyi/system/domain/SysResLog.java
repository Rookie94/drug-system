package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资源日志对象 sys_res_log
 * 
 * @author admin
 * @date 2025-11-09
 */
public class SysResLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志id */
    private Long logId;

    /** 模块名称 */
    @Excel(name = "模块名称")
    private String resName;

    /** 操作 */
    private String optName;

    /** 资源id */
    private Long resId;

    /** 资源标题 */
    @Excel(name = "资源标题")
    private String resTitle;


    /** userId */
    private Long userId;

    /** deptId */
    private Long deptId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 访问时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "访问时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date accessTime;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String operIp;

    /** IP归属地 */
    @Excel(name = "IP归属地")
    private String operLocation;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setResName(String resName) 
    {
        this.resName = resName;
    }

    public String getResName() 
    {
        return resName;
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

    public void setOptName(String optName) 
    {
        this.optName = optName;
    }

    public String getOptName() 
    {
        return optName;
    }

    public void setResId(Long resId) 
    {
        this.resId = resId;
    }

    public Long getResId() 
    {
        return resId;
    }

    public void setResTitle(String resTitle) 
    {
        this.resTitle = resTitle;
    }

    public String getResTitle() 
    {
        return resTitle;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }

    public void setAccessTime(Date accessTime) 
    {
        this.accessTime = accessTime;
    }

    public Date getAccessTime() 
    {
        return accessTime;
    }

    public void setOperIp(String operIp) 
    {
        this.operIp = operIp;
    }

    public String getOperIp() 
    {
        return operIp;
    }

    public void setOperLocation(String operLocation) 
    {
        this.operLocation = operLocation;
    }

    public String getOperLocation() 
    {
        return operLocation;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("resName", getResName())
            .append("optName", getOptName())
            .append("resId", getResId())
            .append("resTitle", getResTitle())
            .append("userName", getUserName())
            .append("deptName", getDeptName())
            .append("accessTime", getAccessTime())
            .append("operIp", getOperIp())
            .append("operLocation", getOperLocation())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
