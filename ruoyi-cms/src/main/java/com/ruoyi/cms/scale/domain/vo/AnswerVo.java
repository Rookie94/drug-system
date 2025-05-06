package com.ruoyi.cms.scale.domain.vo;

public class AnswerVo {
    private String topicId;
    private int[] optionIds;
    private String answer;

    // Getters & Setters
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int[] getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(int[] optionIds) {
        this.optionIds = optionIds;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
