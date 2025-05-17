package com.ruoyi.cms.scale.domain.vo;

import java.util.List;

public class ContextAnswerVo {

    private Long contextId;
    private List<AnswerVo> answers;


    // 外层类的Getters & Setters
    public Long getContextId() {
        return contextId;
    }

    public void setContextId(Long contextId) {
        this.contextId = contextId;
    }

    public List<AnswerVo> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerVo> answers) {
        this.answers = answers;
    }
}
