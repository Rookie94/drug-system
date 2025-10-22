package com.ruoyi.cms.etl.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据总览对象 etl_report1
 * 
 * @author admin
 * @date 2025-10-22
 */
public class EtlReport1 extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 机构数量 */
    @Excel(name = "机构数量")
    private Long org;

    /** 资源数量 */
    @Excel(name = "资源数量")
    private Long resource;

    /** 专家数量 */
    @Excel(name = "专家数量")
    private Long expert;

    /** 戒治案例数 */
    @Excel(name = "戒治案例数")
    private Long cases;

    /** 量表数 */
    @Excel(name = "量表数")
    private Long scale;

    /** 问卷数 */
    @Excel(name = "问卷数")
    private Long survey;

    /** 处方数量 */
    @Excel(name = "处方数量")
    private Long rxdata;

    /** 活动数量 */
    @Excel(name = "活动数量")
    private Long activities;

    /** 留言数量 */
    @Excel(name = "留言数量")
    private Long chat;

    /** 招聘数量 */
    @Excel(name = "招聘数量")
    private Long jobinfo;

    /** 技能数量 */
    @Excel(name = "技能数量")
    private Long skill;

    /** 公告通知数 */
    @Excel(name = "公告通知数")
    private Long notice;

    /** 学员数量 */
    @Excel(name = "学员数量")
    private Long user;

    /** 警官数量 */
    @Excel(name = "警官数量")
    private Long police;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setOrg(Long org) 
    {
        this.org = org;
    }

    public Long getOrg() 
    {
        return org;
    }

    public void setResource(Long resource) 
    {
        this.resource = resource;
    }

    public Long getResource() 
    {
        return resource;
    }

    public void setExpert(Long expert) 
    {
        this.expert = expert;
    }

    public Long getExpert() 
    {
        return expert;
    }

    public void setCases(Long cases)
    {
        this.cases = cases;
    }

    public Long getCases()
    {
        return cases;
    }

    public void setScale(Long scale) 
    {
        this.scale = scale;
    }

    public Long getScale() 
    {
        return scale;
    }

    public void setSurvey(Long survey) 
    {
        this.survey = survey;
    }

    public Long getSurvey() 
    {
        return survey;
    }

    public void setRxdata(Long rxdata) 
    {
        this.rxdata = rxdata;
    }

    public Long getRxdata() 
    {
        return rxdata;
    }

    public void setActivities(Long activities) 
    {
        this.activities = activities;
    }

    public Long getActivities() 
    {
        return activities;
    }

    public void setChat(Long chat) 
    {
        this.chat = chat;
    }

    public Long getChat() 
    {
        return chat;
    }

    public void setJobinfo(Long jobinfo) 
    {
        this.jobinfo = jobinfo;
    }

    public Long getJobinfo() 
    {
        return jobinfo;
    }

    public void setSkill(Long skill) 
    {
        this.skill = skill;
    }

    public Long getSkill() 
    {
        return skill;
    }

    public void setNotice(Long notice) 
    {
        this.notice = notice;
    }

    public Long getNotice() 
    {
        return notice;
    }

    public void setUser(Long user) 
    {
        this.user = user;
    }

    public Long getUser() 
    {
        return user;
    }

    public void setPolice(Long police) 
    {
        this.police = police;
    }

    public Long getPolice() 
    {
        return police;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("org", getOrg())
            .append("resource", getResource())
            .append("expert", getExpert())
            .append("cases", getCases())
            .append("scale", getScale())
            .append("survey", getSurvey())
            .append("rxdata", getRxdata())
            .append("activities", getActivities())
            .append("chat", getChat())
            .append("jobinfo", getJobinfo())
            .append("skill", getSkill())
            .append("notice", getNotice())
            .append("user", getUser())
            .append("police", getPolice())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
