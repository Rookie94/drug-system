package com.ruoyi.cms.survey.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.domain.vo.AnswersVo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.cms.survey.domain.Answers;
import com.ruoyi.cms.survey.mapper.AnswersMapper;
import com.ruoyi.cms.survey.service.IAnswersService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 问卷答案结果Service业务层处理
 *
 * @author Shure
 * @date 2021-10-18
 */
@Service
@Slf4j
public class AnswersServiceImpl implements IAnswersService {
    @Autowired
    private AnswersMapper answersMapper;

    /**
     * 查询问卷答案结果
     *
     * @param answerId 问卷答案结果主键
     * @return 问卷答案结果
     */
    @Override
    public Answers selectAnswersById(Long answerId) {
        return answersMapper.selectAnswersById(answerId);
    }

    /**
     * 查询问卷答案结果列表
     *
     * @param answers 问卷答案结果
     * @return 问卷答案结果
     */
    @Override
    public List<Answers> selectAnswersList(Answers answers) {
        return answersMapper.selectAnswersList(answers);
    }

    /**
     * 查询问卷答案结果列表
     *
     * @param docResults 问卷答案结果
     * @return 问卷答案结果
     */
    @Override
    public List<Answers> parseJsonResult(DocResults docResults) {
        List<Answers> answerList = new ArrayList<>();
        if (StringUtils.isEmpty(docResults.getJsonResult())) {
            return answerList;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> root = mapper.readValue(docResults.getJsonResult(), Map.class);
            @SuppressWarnings("unchecked")
            Map<String, Object> answersMap = (Map<String, Object>) root.get("answers");

            if (answersMap == null) {
                return answerList;
            }

            Long resultId = docResults.getResultId();
            String createBy = SecurityUtils.getUsername();
            Date now = new Date();

            for (Map.Entry<String, Object> entry : answersMap.entrySet()) {
                Long questionId = Long.valueOf(entry.getKey());
                Object value = entry.getValue();

                // 单选题：数字类型
                if (value instanceof Number) {
                    Answers answer = new Answers();
                    answer.setResultId(resultId);
                    answer.setQuestionId(questionId);
                    answer.setAnswerValue(value.toString());
                    answer.setCreateBy(createBy);
                    answer.setCreateTime(now);
                    answerList.add(answer);
                }
                // 多选题：数组类型
                else if (value instanceof List) {
                    List<?> options = (List<?>) value;
                    for (Object opt : options) {
                        Answers answer = new Answers();
                        answer.setResultId(resultId);
                        answer.setQuestionId(questionId);
                        answer.setAnswerValue(opt.toString());
                        answer.setCreateBy(createBy);
                        answer.setCreateTime(now);
                        answerList.add(answer);
                    }
                }
                // 填空题/下拉题：字符串类型
                else if (value instanceof String) {
                    Answers answer = new Answers();
                    answer.setResultId(resultId);
                    answer.setQuestionId(questionId);
                    answer.setExtendValue((String) value);
                    answer.setCreateBy(createBy);
                    answer.setCreateTime(now);
                    answerList.add(answer);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}, 内容: {}", e.getMessage(), docResults.getJsonResult(), e);
            // 返回空列表或根据业务需求抛出运行时异常
            throw new RuntimeException("问卷答案格式错误", e);
        }
        return answerList;
    }

    /**
     * 新增问卷答案结果
     *
     * @param answers 问卷答案结果
     * @return 结果
     */
    @Override
    public int insertAnswers(Answers answers) {
        answers.setCreateBy(getUsername());
        answers.setCreateTime(DateUtils.getNowDate());
        return answersMapper.insertAnswers(answers);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertAnswer(AnswersVo answersVo) {
        if (answersVo == null || answersVo.getAnswersList() == null || answersVo.getAnswersList().isEmpty()) {
            log.warn("批量插入答案失败：答案列表为空");
            return 0;
        }
        answersMapper.deleteAnswersByResultId(answersVo.getResultId());
        int rows = answersMapper.batchInsertAnswer(answersVo);
        log.info("批量插入答案成功，surveyId: {}, 插入记录数: {}", answersVo.getResultId(), rows);
        return rows;
    }

    /**
     * 修改问卷答案结果
     *
     * @param answers 问卷答案结果
     * @return 结果
     */
    @Override
    public int updateAnswers(Answers answers) {
        answers.setUpdateBy(getUsername());
        answers.setUpdateTime(DateUtils.getNowDate());
        return answersMapper.updateAnswers(answers);
    }

    /**
     * 批量删除问卷答案结果
     *
     * @param answerIds 需要删除的问卷答案结果主键
     * @return 结果
     */
    @Override
    public int deleteAnswersByIds(Long[] answerIds) {
        return answersMapper.deleteAnswersByIds(answerIds);
    }

    /**
     * 删除问卷答案结果信息
     *
     * @param answerId 问卷答案结果主键
     * @return 结果
     */
    @Override
    public int deleteAnswersById(Long answerId) {
        return answersMapper.deleteAnswersById(answerId);
    }

    /**
     * 根据主键删除结果
     *
     * @param resultId
     * @return
     */
    @Override
    public int deleteAnswersByResultId(Long resultId) {
        return answersMapper.deleteAnswersByResultId(resultId);
    }

    /**
     * 根据主键删除结果
     *
     * @param resultIds
     * @return
     */
    @Override
    public int deleteAnswersByResultIds(Long[] resultIds) {
        return answersMapper.deleteAnswersByResultIds(resultIds);
    }

    /**
     * 根据问卷主键删除结果
     *
     * @param surveyId
     * @return
     */
    @Override
    public int deleteAnswersBySurveyId(Long surveyId) {
        return answersMapper.deleteAnswersBySurveyId(surveyId);
    }

    /**
     * 根据问卷主键删除结果
     *
     * @param surveyIds
     * @return
     */
    @Override
    public int deleteAnswersBySurveyIds(Long[] surveyIds) {
        return answersMapper.deleteAnswersBySurveyIds(surveyIds);
    }

}
