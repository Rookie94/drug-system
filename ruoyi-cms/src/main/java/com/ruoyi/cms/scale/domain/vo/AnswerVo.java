package com.ruoyi.cms.scale.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;

public class AnswerVo extends BaseEntity {
    private Long resultId;
    private Long topicId;
    private String optionIds;  // 改为String类型，存储逗号分隔的字符串
    private String answer;

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(String optionIds) {
        this.optionIds = optionIds;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}