package com.ruoyi.cms.scale.domain.vo;

public class AnswerVo {

    private Long resultId;
    private Long topicId;
    private int[] optionIds;
    private String answer;

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    // Getters & Setters
    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
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
