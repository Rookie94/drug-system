package com.ruoyi.cms.survey.mapper;

import com.ruoyi.cms.survey.domain.AnswerCount;
import com.ruoyi.cms.survey.domain.AnswerTextCount;
import com.ruoyi.cms.survey.domain.OptionDTO;
import com.ruoyi.cms.survey.domain.QuestionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

// SurveyReportMapper.java
@Mapper
public interface SurveyReportMapper {

    /**
     * 查询符合条件的答卷ID列表
     */
    List<Long> findResultIdsBySurveyAndTime(@Param("surveyId") Long surveyId,
                                            @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime);

    /**
     * 查询调查问卷下的所有问题
     */
    List<QuestionDTO> findQuestionsBySurveyId(@Param("surveyId") Long surveyId);

    /**
     * 查询问题的所有选项
     */
    List<OptionDTO> findOptionsByQuestionId(@Param("questionId") Long questionId);

    /**
     * 统计单选题的答案数量
     */
    List<AnswerCount> countRadioAnswers(@Param("resultIds") List<Long> resultIds,
                                        @Param("questionId") Long questionId);

    /**
     * 统计多选题的答案数量
     */
    List<AnswerCount> countCheckboxAnswers(@Param("resultIds") List<Long> resultIds,
                                           @Param("questionId") Long questionId);

    /**
     * 统计下拉选择题的答案数量
     */
    List<AnswerTextCount> countSelectAnswers(@Param("resultIds") List<Long> resultIds,
                                             @Param("questionId") Long questionId);

    /**
     * 查询主观题答案
     */
    List<String> findSubjectiveAnswers(@Param("resultIds") List<Long> resultIds,
                                       @Param("questionId") Long questionId);
}