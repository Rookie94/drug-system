package com.ruoyi.cms.scale.domain;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

import com.ruoyi.common.annotation.Excel;

/**
 * 量表目录对象 lbs_contexts
 * 
 * @author admin
 * @date 2025-05-05
 */
public class LbsContexts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 目录id */
    private Long contextId;

    /** 祖级列表 */
    private String ancestors;

    /** 父目录id */
    @Excel(name = "父目录id")
    private Long parentContextId;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 目录编号 */
    @Excel(name = "目录编号")
    private String contextNo;

    /** 目录名称 */
    @Excel(name = "目录名称")
    private String contextName;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String pic;

    /** 简介 */
    @Excel(name = "简介")
    private String content;

    /** 自动生成的题目JSON */
    private String jsonMonitor;

    /** 题目JSON */
    private String jsonData;

    /** 量表计算参数 */
    private String jsonParams;

    /** 量表结果集JSON格式 */
    private String jsonResult;


    /** 目录状态 */
    @Excel(name = "目录状态")
    private String status;

    /** 审批状态 */
    @Excel(name = "审批状态")
    private String appored;

    /** 审批者 */
    @Excel(name = "审批者")
    private String apporBy;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    public void setContextId(Long contextId)
    {
        this.contextId = contextId;
    }

    public Long getContextId()
    {
        return contextId;
    }

    public void setParentContextId(Long parentContextId)
    {
        this.parentContextId = parentContextId;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public Long getParentContextId()
    {
        return parentContextId;
    }

    public void setContextName(String contextName) 
    {
        this.contextName = contextName;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public String getContextNo() {
        return contextNo;
    }

    public void setContextNo(String contextNo) {
        this.contextNo = contextNo;
    }

    public String getContextName() 
    {
        return contextName;
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


    public String getJsonMonitor() {
        return jsonMonitor;
    }

    public void setJsonMonitor(String jsonMonitor) {
        this.jsonMonitor = jsonMonitor;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(String jsonParams) {
        this.jsonParams = jsonParams;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
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

    /** 子 */
    private List<?> children = new ArrayList<>();

    public List<?> getChildren() {
        return children;
    }

    public void setChildren(List<?> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("contextId", getContextId())
            .append("parentContextId", getParentContextId())
            .append("orderNum", getOrderNum())
            .append("contextNo", getContextNo())
            .append("contextName", getContextName())
            .append("pic", getPic())
            .append("content", getContent())
            .append("jsonMonitor", getJsonMonitor())
            .append("jsonData", getJsonData())
            .append("jsonParams", getJsonParams())
            .append("jsonResult", getJsonResult())
            .append("status", getStatus())
            .append("appored", getAppored())
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
