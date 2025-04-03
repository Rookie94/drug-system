package com.ruoyi.cms.res.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 戒治机构对象 base_orginfo
 * 
 * @author admin
 * @date 2025-03-28
 */
public class BaseOrginfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 机构ID */
    private Long orgid;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 机构编码 */
    @Excel(name = "机构编码")
    private String orgCode;

    /** 机构名称 */
    @Excel(name = "机构名称")
    private String orgName;

    /** 机构简介 */
    private String orgContent;

    /** 机构状态（0正常 1停用） */
    @Excel(name = "机构状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setOrgid(Long orgid) 
    {
        this.orgid = orgid;
    }

    public Long getOrgid() 
    {
        return orgid;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setOrgCode(String orgCode) 
    {
        this.orgCode = orgCode;
    }

    public String getOrgCode() 
    {
        return orgCode;
    }
    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public String getOrgName() 
    {
        return orgName;
    }
    public void setOrgContent(String orgContent) 
    {
        this.orgContent = orgContent;
    }

    public String getOrgContent() 
    {
        return orgContent;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("orgid", getOrgid())
            .append("orderNum", getOrderNum())
            .append("orgCode", getOrgCode())
            .append("orgName", getOrgName())
            .append("orgContent", getOrgContent())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
