package com.ruoyi.cms.survey.domain.vo;

import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import com.ruoyi.cms.survey.domain.Answers;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.List;

public class AnswersVo extends BaseEntity {


    private Long surveyId;

    private List<Answers> answersList;


    // 外层类的Getters & Setters
    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public List<Answers> getAnswersList() {
        return answersList;
    }

    public void setAnswersList(List<Answers> answersList) {
        this.answersList = answersList;
    }

}
