package com.ruoyi.cms.survey.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OptionsVo {
    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Integer getOptionNo() {
        return optionNo;
    }

    public void setOptionNo(Integer optionNo) {
        this.optionNo = optionNo;
    }

    public String getHideQuestion() {
        return hideQuestion;
    }

    public void setHideQuestion(String hideQuestion) {
        this.hideQuestion = hideQuestion;
    }

    public String getShowQuestion() {
        return showQuestion;
    }

    public void setShowQuestion(String showQuestion) {
        this.showQuestion = showQuestion;
    }

    public Integer getIsWtqd() {
        return isWtqd;
    }

    public void setIsWtqd(Integer isWtqd) {
        this.isWtqd = isWtqd;
    }

    public String getWtqdType() {
        return wtqdType;
    }

    public void setWtqdType(String wtqdType) {
        this.wtqdType = wtqdType;
    }

    public String getWtqdValue() {
        return wtqdValue;
    }

    public void setWtqdValue(String wtqdValue) {
        this.wtqdValue = wtqdValue;
    }

    public String getWtqdSymbol() {
        return wtqdSymbol;
    }

    public void setWtqdSymbol(String wtqdSymbol) {
        this.wtqdSymbol = wtqdSymbol;
    }

    public String getWtqdDesc() {
        return wtqdDesc;
    }

    public void setWtqdDesc(String wtqdDesc) {
        this.wtqdDesc = wtqdDesc;
    }

    public Integer getIsExtend() {
        return isExtend;
    }

    public void setIsExtend(Integer isExtend) {
        this.isExtend = isExtend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLogicSymbol() {
        return logicSymbol;
    }

    public void setLogicSymbol(String logicSymbol) {
        this.logicSymbol = logicSymbol;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    private Long optionId;
    private Long surveyId;
    private Long questionId;
    private String optionCode;
    private String optionText;
    private Integer optionNo;
    private String hideQuestion;
    private String showQuestion;
    private Integer isWtqd;
    private String wtqdType;
    private String wtqdValue;
    private String wtqdSymbol;
    private String wtqdDesc;
    private Integer isExtend;
    private Date createTime;
    private String logicSymbol;
    private String bookCode;
}
