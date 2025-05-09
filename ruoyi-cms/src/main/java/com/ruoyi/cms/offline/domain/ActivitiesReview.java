package com.ruoyi.cms.offline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 活动评价对象 v_ofa_activities_review
 * 
 * @author admin
 * @date 2025-05-09
 */
public class ActivitiesReview extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评价id */
    @Excel(name = "评价id")
    private Long reviewId;

    /** 活动id */
    @Excel(name = "活动id")
    private Long activityId;

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

    /** 归属部门 */
    @Excel(name = "归属部门")
    private Long deptId;


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
            .append("reviewId", getReviewId())
            .append("activityId", getActivityId())
            .append("rating", getRating())
            .append("content", getContent())
            .append("pic", getPic())
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
