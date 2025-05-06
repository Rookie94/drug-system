package com.ruoyi.cms.scale.domain.vo;

import com.ruoyi.cms.scale.domain.LbsOptions;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 量表题目对象 lbs_topics
 * 
 * @author admin
 * @date 2025-05-05
 */
public class LbsTopicsVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 题目id */
    private Long topicId;

    /** 量表id */
    private Long contextId;

    /** 量表名称 */
    private String contextName;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 题目 */
    @Excel(name = "题目")
    private String title;

    /** 题目类型(1 单选,2多选,3填空) */
    @Excel(name = "题目类型(0 单选,1多选,2填空)")
    private String topicType;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 量选项信息 */
    private List<LbsOptions> lbsOptionsList;

    public void setTopicId(Long topicId) 
    {
        this.topicId = topicId;
    }

    public Long getTopicId() 
    {
        return topicId;
    }


    public Long getContextId() {return contextId;}

    public void setContextId(Long contextId) {this.contextId = contextId;}

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
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

    public void setTopicType(String topicType) 
    {
        this.topicType = topicType;
    }

    public String getTopicType() 
    {
        return topicType;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public List<LbsOptions> getLbsOptionsList()
    {
        return lbsOptionsList;
    }

    public void setLbsOptionsList(List<LbsOptions> lbsOptionsList)
    {
        this.lbsOptionsList = lbsOptionsList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("topicId", getTopicId())
            .append("contextId", getContextId())
            .append("contextName", getContextName())
            .append("orderNum", getOrderNum())
            .append("title", getTitle())
            .append("topicType", getTopicType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("lbsOptionsList", getLbsOptionsList())
            .toString();
    }
}
