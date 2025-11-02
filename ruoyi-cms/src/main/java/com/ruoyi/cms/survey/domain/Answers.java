package com.ruoyi.cms.survey.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 问卷答案结果对象 tb_answer
 *
 * @author Shure
 * @date 2021-10-18
 */
public class Answers extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 答案主键
     */
    private Long answerId;

    /**
     * 问卷主键
     */
    @Excel(name = "问卷主键")
    private Long surveyId;

    /**
     * 问题主键
     */
    @Excel(name = "问题主键")
    private Long questionId;

    /**
     * 选项编码
     */
    @Excel(name = "选项Id")
    private String optionId;

    /**
     * 答案结果
     */
    @Excel(name = "答案结果")
    private String answerValue;

    /**
     * 扩展填空值
     */
    @Excel(name = "扩展填空值")
    private String extendValue;


    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setOptionCode(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionCode() {
        return optionId;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setExtendValue(String extendValue) {
        this.extendValue = extendValue;
    }

    public String getExtendValue() {
        return extendValue;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("answerId", getAnswerId())
                .append("surveyId", getSurveyId())
                .append("questionId", getQuestionId())
                .append("optionCode", getOptionCode())
                .append("answerValue", getAnswerValue())
                .append("extendValue", getExtendValue())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
