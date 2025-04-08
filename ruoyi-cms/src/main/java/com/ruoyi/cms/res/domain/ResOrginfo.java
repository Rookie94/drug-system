package com.ruoyi.cms.res.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 戒治机构对象 res_orginfo
 * 
 * @author ruoyi
 * @date 2025-04-08
 */
public class ResOrginfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 机构ID */
    private Long orgid;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 机构名称 */
    @Excel(name = "机构名称")
    private String orgName;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String pic;

    /** 机构简介 */
    @Excel(name = "机构简介")
    private String orgContent;

    /** 机构状态（0正常 1停用） */
    @Excel(name = "机构状态", readConverterExp = "0=正常,1=停用")
    private String status;

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
    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public String getOrgName() 
    {
        return orgName;
    }
    public void setPic(String pic) 
    {
        this.pic = pic;
    }

    public String getPic() 
    {
        return pic;
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

    //扩展内容

    /** 用户ID */
    private Long userId;

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}

    /** 部门ID */
    private Long deptId;
    public Long getDeptId() { return deptId;}
    public void setDeptId(Long deptId) {this.deptId = deptId;}

    @Excel(name = "审核状态", readConverterExp = "0=待审批,1=已发布")
    private String appored;

    public String getAppored() {return appored;}
    public void setAppored(String appored) {this.appored = appored;}

    /** 审批者 */
    private String apporBy;

    public String getApporBy() {return apporBy;}
    public void setApporBy(String apporBy) {this.apporBy = apporBy;}

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;
    public Date getApporTime() {return apporTime;}
    public void setApporTime(Date apporTime) {this.apporTime = apporTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orgid", getOrgid())
            .append("orderNum", getOrderNum())
            .append("orgName", getOrgName())
            .append("pic", getPic())
            .append("orgContent", getOrgContent())
            .append("status", getStatus())
            .append("appored", getAppored())
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
