package com.ruoyi.cms.offline.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 活动评价对象 v_ofa_activities_review
 * 
 * @author admin
 * @date 2025-05-09
 */
public class ActivitiesReviewVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价id */
    @Excel(name = "评价id")
    private Long reviewId;

    /** 活动id */
    @Excel(name = "活动id")
    private Long activityId;

    /** 活动代码 */
    @Excel(name = "活动代码")
    private String activityCode;

    /** 活动主题 */
    @Excel(name = "活动主题")
    private String activityName;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 评分：1-差评，2-中评，3-好评 */
    @Excel(name = "评分：1-差评，2-中评，3-好评")
    private String rating;

    /** 评价内容 */
    @Excel(name = "评价内容")
    private String content;

    /** 图片 */
    @Excel(name = "图片")
    private String pic;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户类型（00系统用户） */
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户")
    private String userType;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /** 归属部门 */
    @Excel(name = "归属部门")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    public void setReviewId(Long reviewId) 
    {
        this.reviewId = reviewId;
    }

    public Long getReviewId() 
    {
        return reviewId;
    }

    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }

    public Long getActivityId() 
    {
        return activityId;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public void setActivityName(String activityName) 
    {
        this.activityName = activityName;
    }

    public String getActivityName() 
    {
        return activityName;
    }

    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }

    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }

    public void setRating(String rating) 
    {
        this.rating = rating;
    }

    public String getRating() 
    {
        return rating;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setPic(String pic) 
    {
        this.pic = pic;
    }

    public String getPic() 
    {
        return pic;
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

    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }

    public void setPhoneNumber(String phoneNumber) 
    {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("reviewId", getReviewId())
            .append("activityId", getActivityId())
            .append("activityName", getActivityName())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("rating", getRating())
            .append("content", getContent())
            .append("pic", getPic())
            .append("delFlag", getDelFlag())
            .append("userId", getUserId())
            .append("userType", getUserType())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("phoneNumber", getPhoneNumber())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
