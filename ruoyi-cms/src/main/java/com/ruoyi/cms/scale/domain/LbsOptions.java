package com.ruoyi.cms.scale.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 量选项对象 lbs_options
 * 
 * @author admin
 * @date 2025-05-05
 */
public class LbsOptions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 选项id */
    private Long optionId;

    /** 题目id */
    private Long topicId;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 选项名称 */
    @Excel(name = "选项名称")
    private String title;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setOptionId(Long optionId) 
    {
        this.optionId = optionId;
    }

    public Long getOptionId() 
    {
        return optionId;
    }
    public void setTopicId(Long topicId) 
    {
        this.topicId = topicId;
    }

    public Long getTopicId() 
    {
        return topicId;
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
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("optionId", getOptionId())
            .append("topicId", getTopicId())
            .append("orderNum", getOrderNum())
            .append("title", getTitle())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
