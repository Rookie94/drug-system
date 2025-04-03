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
 * @date 2025-04-03
 */
public class ResSkill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 技能ID */
    private Integer skillId;

    /** 技能标题 */
    @Excel(name = "技能标题")
    private String skillTitle;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String skillPic;

    /** 技能类型 */
    @Excel(name = "技能类型")
    private String skillType;

    /** 技能内容 */
    @Excel(name = "技能内容")
    private String skillContent;

    /** 技能状态（0正常 1关闭） */
    @Excel(name = "技能状态", readConverterExp = "0=正常,1=关闭")
    private String status;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String apporStatus;

    /** 审批者 */
    private String apporBy;

    /** 审批时间 */
    private Date apporTime;

    public void setSkillId(Integer skillId) 
    {
        this.skillId = skillId;
    }

    public Integer getSkillId() 
    {
        return skillId;
    }
    public void setSkillTitle(String skillTitle) 
    {
        this.skillTitle = skillTitle;
    }

    public String getSkillTitle() 
    {
        return skillTitle;
    }
    public void setSkillPic(String skillPic) 
    {
        this.skillPic = skillPic;
    }

    public String getSkillPic() 
    {
        return skillPic;
    }
    public void setSkillType(String skillType) 
    {
        this.skillType = skillType;
    }

    public String getSkillType() 
    {
        return skillType;
    }
    public void setSkillContent(String skillContent) 
    {
        this.skillContent = skillContent;
    }

    public String getSkillContent() 
    {
        return skillContent;
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
            .append("skillId", getSkillId())
            .append("skillTitle", getSkillTitle())
            .append("skillPic", getSkillPic())
            .append("skillType", getSkillType())
            .append("skillContent", getSkillContent())
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
