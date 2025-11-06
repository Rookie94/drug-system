package com.ruoyi.cms.survey.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SurveyVo extends BaseEntity {

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

    // 添加题目列表
    private List<QuestionVo> questions;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyDesc() {
        return surveyDesc;
    }

    public void setSurveyDesc(String surveyDesc) {
        this.surveyDesc = surveyDesc;
    }

    public String getSurveyType() {
        return surveyType;
    }

    public void setSurveyType(String surveyType) {
        this.surveyType = surveyType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public List<QuestionVo> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionVo> questions) {
        this.questions = questions;
    }


}
