package com.ruoyi.cms.survey.domain.vo;

import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import com.ruoyi.cms.survey.domain.Answers;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class AnswersVo extends BaseEntity {


    private Long resultId;

    private List<Answers> answersList;

    // 外层类的Getters & Setters
    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public List<Answers> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<Answers> answersList) {
        this.answersList = answersList;
    }

}
