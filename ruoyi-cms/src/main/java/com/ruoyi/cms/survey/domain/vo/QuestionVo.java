package com.ruoyi.cms.survey.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionVo {

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(String questionNo) {
        this.questionNo = questionNo;
    }

    public Integer getQuestionSort() {
        return questionSort;
    }

    public void setQuestionSort(Integer questionSort) {
        this.questionSort = questionSort;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getValidateRule() {
        return validateRule;
    }

    public void setValidateRule(String validateRule) {
        this.validateRule = validateRule;
    }

    public Integer getShowOrHide() {
        return showOrHide;
    }

    public void setShowOrHide(Integer showOrHide) {
        this.showOrHide = showOrHide;
    }

    public String getQuestionAttr() {
        return questionAttr;
    }

    public void setQuestionAttr(String questionAttr) {
        this.questionAttr = questionAttr;
    }

    public String getRelationResult() {
        return relationResult;
    }

    public void setRelationResult(String relationResult) {
        this.relationResult = relationResult;
    }

    public Integer getNotEdit() {
        return notEdit;
    }

    public void setNotEdit(Integer notEdit) {
        this.notEdit = notEdit;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getOptionDisplay() {
        return optionDisplay;
    }

    public void setOptionDisplay(String optionDisplay) {
        this.optionDisplay = optionDisplay;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<OptionsVo> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsVo> options) {
        this.options = options;
    }

    private Long questionId;
    private Long surveyId;
    private String questionNo;
    private Integer questionSort;
    private String questionName;
    private String questionType;
    private String validateRule;
    private Integer showOrHide;
    private String questionAttr;
    private String relationResult;
    private Integer notEdit;
    private String defaultValue;
    private String formula;
    private String optionDisplay;
    private String bookCode;
    private Date createTime;

    // 选项列表
    private List<OptionsVo> options;

}