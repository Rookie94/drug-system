package com.ruoyi.cms.survey.domain;

// OptionStat.java
public class OptionStat {
    private String optionText;
    private Integer num;
    private String ratio;

    // 构造方法
    public OptionStat() {}

    public OptionStat(String optionText, Integer num, String ratio) {
        this.optionText = optionText;
        this.num = num;
        this.ratio = ratio;
    }

    // getter和setter
    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}