package com.ruoyi.cms.survey.domain;

// Option.java
public class OptionDTO {
    private Long optionId;
    private String optionCode;
    private String optionText;
    private Integer optionNo;

    // getter和setter
    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
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
}