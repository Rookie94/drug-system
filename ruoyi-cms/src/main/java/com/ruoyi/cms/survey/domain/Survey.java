package com.ruoyi.cms.survey.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 问卷对象 tb_survey
 *
 * @author Shure
 * @date 2021-10-18
 */
public class Survey extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 问卷主键
     */
    private Long surveyId;

    /**
     * 问卷名称
     */
    @Excel(name = "问卷名称")
    private String surveyName;

    /**
     * 问卷说明描述
     */
    @Excel(name = "问卷说明描述")
    private String surveyDesc;

    /**
     * 问卷类型
     */
    @Excel(name = "问卷类型")
    private String surveyType;


    /** 截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "截止时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 问卷状态（0：未发布，1：收集中，2：已结束）
     */
    @Excel(name = "问卷状态", readConverterExp = "0=：未发布，1：收集中，2：已结束")
    private String surveyStatus;

    /**
     * 数据状态（1：有效，0：无效）
     */
    @Excel(name = "数据状态", readConverterExp = "1=：有效，0：无效")
    private String status;

    /**
     * 问卷删除状态（"0=：正常，1：已删除"）
     */
    @Excel(name = "删除状态", readConverterExp = "0=：正常，1：已删除")
    private String delFlag;


    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyDesc(String surveyDesc) {
        this.surveyDesc = surveyDesc;
    }

    public String getSurveyDesc() {
        return surveyDesc;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public String getSurveyStatus() {
        return surveyStatus;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("surveyId", getSurveyId())
                .append("surveyName", getSurveyName())
                .append("surveyDesc", getSurveyDesc())
                .append("surveyType", getSurveyType())
                .append("endTime", getEndTime())
                .append("surveyStatus", getSurveyStatus())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
