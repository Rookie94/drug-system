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
 * @date 2025-04-10
 */
public class ResJobinfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工作ID */
    private Long jobid;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 工作标题 */
    @Excel(name = "工作标题")
    private String title;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String pic;

    /** 工作内容 */
    @Excel(name = "工作内容")
    private String content;

    /** 工作状态 */
    @Excel(name = "工作状态")
    private String status;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String appored;

    /** 用户ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 审批者 */
    @Excel(name = "审批者")
    private String apporBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public void setJobid(Long jobid)
    {
        this.jobid = jobid;
    }

    public Long getJobid()
    {
        return jobid;
    }
    public void setOrderNum(Long orderNum)
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum()
    {
        return orderNum;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getPic()
    {
        return pic;
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
                .append("jobid", getJobid())
                .append("orderNum", getOrderNum())
                .append("title", getTitle())
                .append("pic", getPic())
                .append("content", getContent())
                .append("status", getStatus())
                .append("appored", getAppored())
                .append("publishTime", getPublishTime())
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
