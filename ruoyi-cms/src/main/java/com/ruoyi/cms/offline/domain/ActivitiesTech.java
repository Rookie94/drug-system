package com.ruoyi.cms.offline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 活动戒治技术资料对象 ofa_activities_tech
 * 
 * @author admin
 * @date 2025-05-10
 */
public class ActivitiesTech extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 技术id */
    @Excel(name = "技术id")
    private Long techId;

    /** 活动id */
    @Excel(name = "活动id")
    private Long activityId;

    /** 技术类型 */
    @Excel(name = "技术类型")
    private String techType;

    /** 照片记录 */
    @Excel(name = "照片记录")
    private String pic;

    /** 康复建议 */
    @Excel(name = "康复建议")
    private String content;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 年龄 */
    @Excel(name = "年龄")
    private String age;

    /** 治疗医师 */
    @Excel(name = "治疗医师")
    private String doctor;

    /** 评分：1-差评，2-中评，3-好评 */
    @Excel(name = "评分：1-好评,2-中评,3-差评")
    private String rating;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String comment;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 归属部门 */
    @Excel(name = "归属部门")
    private Long deptId;


    public void setTechId(Long techId) 
    {
        this.techId = techId;
    }

    public Long getTechId() 
    {
        return techId;
    }

    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }

    public Long getActivityId() 
    {
        return activityId;
    }

    public void setTechType(String techType) 
    {
        this.techType = techType;
    }

    public String getTechType() 
    {
        return techType;
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

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setAge(String age) 
    {
        this.age = age;
    }

    public String getAge() 
    {
        return age;
    }

    public void setDoctor(String doctor) 
    {
        this.doctor = doctor;
    }

    public String getDoctor() 
    {
        return doctor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("techId", getTechId())
            .append("activityId", getActivityId())
            .append("techType", getTechType())
            .append("pic", getPic())
            .append("content", getContent())
            .append("name", getName())
            .append("sex", getSex())
            .append("age", getAge())
            .append("doctor", getDoctor())
            .append("rating", getRating())
            .append("comment", getComment())
             .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
