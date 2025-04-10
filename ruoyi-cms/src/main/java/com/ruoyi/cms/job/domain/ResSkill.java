package com.ruoyi.cms.job.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 技能信息对象 res_skill
 *
 * @author admin
 * @date 2025-04-10
 */
public class ResSkill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 技能ID */
    private Long skillid;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 技能标题 */
    @Excel(name = "技能标题")
    private String title;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String pic;

    /** 技能类型 */
    @Excel(name = "技能类型")
    private String skillType;

    /** 技能内容 */
    @Excel(name = "技能内容")
    private String content;

    /** 技能状态（0正常 1关闭） */
    @Excel(name = "技能状态", readConverterExp = "0=正常,1=关闭")
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

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    public void setSkillid(Long skillid)
    {
        this.skillid = skillid;
    }

    public Long getSkillid()
    {
        return skillid;
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
    public void setSkillType(String skillType)
    {
        this.skillType = skillType;
    }

    public String getSkillType()
    {
        return skillType;
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
                .append("skillid", getSkillid())
                .append("orderNum", getOrderNum())
                .append("title", getTitle())
                .append("pic", getPic())
                .append("skillType", getSkillType())
                .append("content", getContent())
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
