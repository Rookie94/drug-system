package com.ruoyi.cms.scale.domain.vo;

import java.util.List;

public class ContextAnswerVo {

    private String contextId;
    private List<AnswerVo> answers;


    // 外层类的Getters & Setters
    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public List<AnswerVo> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerVo> answers) {
        this.answers = answers;
    }
}
