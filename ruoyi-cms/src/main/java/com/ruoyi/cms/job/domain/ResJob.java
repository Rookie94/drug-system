package com.ruoyi.cms.job.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 招聘信息对象 res_job
 * 
 * @author admin
 * @date 2025-03-28
 */
public class ResJob extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工作ID */
    private Integer jobId;

    /** 职位名称 */
    @Excel(name = "职位名称")
    private String jobName;

    /** 公司名称 */
    @Excel(name = "公司名称")
    private String corpName;

    /** 工作地点 */
    @Excel(name = "工作地点")
    private String workAddr;

    /** 招聘人数 */
    @Excel(name = "招聘人数")
    private Integer number;

    /** 薪资待遇 */
    @Excel(name = "薪资待遇")
    private String xzLevel;

    /** 公司规模 */
    @Excel(name = "公司规模")
    private String corpLevel;

    /** 学历要求 */
    @Excel(name = "学历要求")
    private String eduLevel;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contact;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String tel;

    /** 邮箱地址 */
    @Excel(name = "邮箱地址")
    private String email;

    /** 截止日期 */
    @Excel(name = "截止日期")
    private String voidDate;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setJobId(Integer jobId) 
    {
        this.jobId = jobId;
    }

    public Integer getJobId() 
    {
        return jobId;
    }
    public void setJobName(String jobName) 
    {
        this.jobName = jobName;
    }

    public String getJobName() 
    {
        return jobName;
    }
    public void setCorpName(String corpName) 
    {
        this.corpName = corpName;
    }

    public String getCorpName() 
    {
        return corpName;
    }
    public void setWorkAddr(String workAddr) 
    {
        this.workAddr = workAddr;
    }

    public String getWorkAddr() 
    {
        return workAddr;
    }
    public void setNumber(Integer number) 
    {
        this.number = number;
    }

    public Integer getNumber() 
    {
        return number;
    }
    public void setXzLevel(String xzLevel) 
    {
        this.xzLevel = xzLevel;
    }

    public String getXzLevel() 
    {
        return xzLevel;
    }
    public void setCorpLevel(String corpLevel) 
    {
        this.corpLevel = corpLevel;
    }

    public String getCorpLevel() 
    {
        return corpLevel;
    }
    public void setEduLevel(String eduLevel) 
    {
        this.eduLevel = eduLevel;
    }

    public String getEduLevel() 
    {
        return eduLevel;
    }
    public void setContact(String contact) 
    {
        this.contact = contact;
    }

    public String getContact() 
    {
        return contact;
    }
    public void setTel(String tel) 
    {
        this.tel = tel;
    }

    public String getTel() 
    {
        return tel;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setVoidDate(String voidDate) 
    {
        this.voidDate = voidDate;
    }

    public String getVoidDate() 
    {
        return voidDate;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("corpName", getCorpName())
            .append("workAddr", getWorkAddr())
            .append("number", getNumber())
            .append("xzLevel", getXzLevel())
            .append("corpLevel", getCorpLevel())
            .append("eduLevel", getEduLevel())
            .append("contact", getContact())
            .append("tel", getTel())
            .append("email", getEmail())
            .append("voidDate", getVoidDate())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
