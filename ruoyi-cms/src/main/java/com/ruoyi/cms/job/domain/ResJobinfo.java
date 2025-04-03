package com.ruoyi.cms.job.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 招聘信息对象 res_jobinfo
 * 
 * @author admin
 * @date 2025-04-03
 */
public class ResJobinfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工作ID */
    private Integer jobId;

    /** 工作标题 */
    @Excel(name = "工作标题")
    private String jobTitle;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String jobPic;

    /** 工作内容 */
    @Excel(name = "工作内容")
    private String jobContent;

    /** 工作状态 */
    @Excel(name = "工作状态")
    private String status;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String apporStatus;

    /** 审批者 */
    private String apporBy;

    /** 审批时间 */
    private Date apporTime;

    public void setJobId(Integer jobId) 
    {
        this.jobId = jobId;
    }

    public Integer getJobId() 
    {
        return jobId;
    }
    public void setJobTitle(String jobTitle) 
    {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle() 
    {
        return jobTitle;
    }
    public void setJobPic(String jobPic) 
    {
        this.jobPic = jobPic;
    }

    public String getJobPic() 
    {
        return jobPic;
    }
    public void setJobContent(String jobContent) 
    {
        this.jobContent = jobContent;
    }

    public String getJobContent() 
    {
        return jobContent;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setApporStatus(String apporStatus) 
    {
        this.apporStatus = apporStatus;
    }

    public String getApporStatus() 
    {
        return apporStatus;
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
            .append("jobId", getJobId())
            .append("jobTitle", getJobTitle())
            .append("jobPic", getJobPic())
            .append("jobContent", getJobContent())
            .append("status", getStatus())
            .append("apporStatus", getApporStatus())
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
