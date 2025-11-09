package com.ruoyi.cms.survey.service.impl;

import com.ruoyi.cms.survey.domain.*;
import com.ruoyi.cms.survey.mapper.SurveyReportMapper;
import com.ruoyi.cms.survey.service.ISurveyReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SurveyReportServiceImpl implements ISurveyReportService {

    @Autowired
    private SurveyReportMapper surveyReportMapper;

    /**
     * 生成问卷调查统计报告
     */
    @Override
    public List<SurveyReport> generateSurveyReport(SurveyReportQuery query) {
        List<SurveyReport> reportData = new ArrayList<>();

        try {
            // 1. 获取符合条件的答卷ID
            List<Long> resultIds = surveyReportMapper.findResultIdsBySurveyAndTime(
                    query.getSurveyId(), query.getCommitTimeStart(), query.getCommitTimeEnd());

            if (resultIds.isEmpty()) {
                log.info("没有找到符合条件的答卷数据，surveyId: {}", query.getSurveyId());
                return reportData;
            }

            int totalResponses = resultIds.size();

            // 添加更详细的日志
            if (query.getCommitTimeStart() != null && query.getCommitTimeEnd() != null) {
                log.info("找到 {} 份符合条件的答卷，surveyId: {}, 时间范围: {} 至 {}",
                        totalResponses, query.getSurveyId(),
                        query.getCommitTimeStart(), query.getCommitTimeEnd());
            } else if (query.getCommitTimeStart() != null) {
                log.info("找到 {} 份符合条件的答卷，surveyId: {}, 开始时间: {}",
                        totalResponses, query.getSurveyId(), query.getCommitTimeStart());
            } else if (query.getCommitTimeEnd() != null) {
                log.info("找到 {} 份符合条件的答卷，surveyId: {}, 结束时间: {}",
                        totalResponses, query.getSurveyId(), query.getCommitTimeEnd());
            } else {
                log.info("找到 {} 份符合条件的答卷，surveyId: {} (未设置时间范围)",
                        totalResponses, query.getSurveyId());
            }

            // 2. 获取调查问卷的所有问题
            List<QuestionDTO> questions = surveyReportMapper.findQuestionsBySurveyId(query.getSurveyId());

            if (questions.isEmpty()) {
                log.info("调查问卷下没有找到问题，surveyId: {}", query.getSurveyId());
                return reportData;
            }

            // 3. 为每个问题生成统计报告
            for (QuestionDTO question : questions) {
                SurveyReport reportItem = generateQuestionReport(question, resultIds, totalResponses);
                if (reportItem != null) {
                    reportData.add(reportItem);
                }
            }

        } catch (Exception e) {
            log.error("生成调查问卷报告失败: surveyId={}", query.getSurveyId(), e);
            throw new RuntimeException("生成报告失败", e);
        }

        return reportData;
    }

    /**
     * 生成单个问题的统计报告
     */
    private SurveyReport generateQuestionReport(QuestionDTO question, List<Long> resultIds, int totalResponses) {
        SurveyReport report = new SurveyReport();
        report.setTitle(question.getQuestionName());
        report.setOptions(new ArrayList<>());

        // 根据题目类型处理
        switch (question.getQuestionType()) {
            case "radio":
                // 单选题 - 统计选项百分比
                processRadioQuestion(question, resultIds, totalResponses, report);
                break;
            case "select":
                // 下拉选择题 - 统计选项百分比
                processSelectQuestion(question, resultIds, totalResponses, report);
                break;
            case "checkbox":
                // 多选题处理
                processCheckboxQuestion(question, resultIds, totalResponses, report);
                break;
            case "imput":
            case "text":
                // 主观题处理
                processSubjectiveQuestion(question, resultIds, report);
                break;
            default:
                log.warn("未知的题目类型: {}，问题ID: {}", question.getQuestionType(), question.getQuestionId());
                return null;
        }

        return report;
    }

    /**
     * 处理单选题 - 修正：按选项ID匹配
     */
    private void processRadioQuestion(QuestionDTO question, List<Long> resultIds,
                                      int totalResponses, SurveyReport report) {
        // 获取问题的所有选项
        List<OptionDTO> options = surveyReportMapper.findOptionsByQuestionId(question.getQuestionId());
        if (options.isEmpty()) {
            log.warn("单选题没有找到选项，问题ID: {}", question.getQuestionId());
            return;
        }

        // 统计每个选项的选择次数（按选项ID）
        List<AnswerCount> answerCounts = surveyReportMapper.countRadioAnswers(resultIds, question.getQuestionId());
        Map<String, Integer> countMap = answerCounts.stream()
                .collect(Collectors.toMap(AnswerCount::getOptionValue, AnswerCount::getCount));

        // 构建选项统计 - 按选项ID匹配
        for (OptionDTO option : options) {
            // 将选项ID转换为字符串进行匹配
            String optionIdStr = String.valueOf(option.getOptionId());
            int count = countMap.getOrDefault(optionIdStr, 0);
            String ratio = calculateRatio(count, totalResponses);

            OptionStat optionStat = new OptionStat(option.getOptionText(), count, ratio);
            report.getOptions().add(optionStat);
        }
    }

    /**
     * 处理下拉选择题 - 修正：按选项ID匹配
     */
    private void processSelectQuestion(QuestionDTO question, List<Long> resultIds,
                                       int totalResponses, SurveyReport report) {
        // 获取问题的所有选项
        List<OptionDTO> options = surveyReportMapper.findOptionsByQuestionId(question.getQuestionId());
        if (options.isEmpty()) {
            log.warn("下拉选择题没有找到选项，问题ID: {}", question.getQuestionId());
            return;
        }

        // 统计每个选项的选择次数（按选项ID）
        List<AnswerTextCount> answerTextCounts = surveyReportMapper.countSelectAnswers(resultIds, question.getQuestionId());
        Map<String, Integer> textCountMap = answerTextCounts.stream()
                .collect(Collectors.toMap(AnswerTextCount::getAnswerText, AnswerTextCount::getCount));

        // 构建选项统计 - 按选项ID匹配
        for (OptionDTO option : options) {
            // 将选项ID转换为字符串进行匹配
            String optionIdStr = String.valueOf(option.getOptionId());
            int count = textCountMap.getOrDefault(optionIdStr, 0);
            String ratio = calculateRatio(count, totalResponses);

            OptionStat optionStat = new OptionStat(option.getOptionText(), count, ratio);
            report.getOptions().add(optionStat);
        }
    }

    /**
     * 处理多选题 - 修正：按选项ID匹配
     */
    private void processCheckboxQuestion(QuestionDTO question, List<Long> resultIds,
                                         int totalResponses, SurveyReport report) {
        // 获取问题的所有选项
        List<OptionDTO> options = surveyReportMapper.findOptionsByQuestionId(question.getQuestionId());
        if (options.isEmpty()) {
            log.warn("多选题没有找到选项，问题ID: {}", question.getQuestionId());
            return;
        }

        // 统计每个选项的选择次数（多选题）
        List<AnswerCount> answerCounts = surveyReportMapper.countCheckboxAnswers(resultIds, question.getQuestionId());
        Map<String, Integer> countMap = answerCounts.stream()
                .collect(Collectors.toMap(AnswerCount::getOptionValue, AnswerCount::getCount));

        // 构建选项统计 - 按选项ID匹配
        for (OptionDTO option : options) {
            // 将选项ID转换为字符串进行匹配
            String optionIdStr = String.valueOf(option.getOptionId());
            int count = countMap.getOrDefault(optionIdStr, 0);
            String ratio = calculateRatio(count, totalResponses);

            OptionStat optionStat = new OptionStat(option.getOptionText(), count, ratio);
            report.getOptions().add(optionStat);
        }
    }

    /**
     * 处理主观题 - 修正：显示实际答案
     */
    private void processSubjectiveQuestion(QuestionDTO question, List<Long> resultIds, SurveyReport report) {
        // 查询主观题的实际答案
        List<String> subjectiveAnswers = surveyReportMapper.findSubjectiveAnswers(resultIds, question.getQuestionId());

        if (subjectiveAnswers.isEmpty()) {
            // 如果没有答案，显示"无答案"
            OptionStat noAnswer = new OptionStat("无答案", 0, "不适用");
            report.getOptions().add(noAnswer);
        } else {
            // 显示实际的主观题答案（去重）
            Set<String> uniqueAnswers = new HashSet<>(subjectiveAnswers);
            for (String answer : uniqueAnswers) {
                if (answer != null && !answer.trim().isEmpty()) {
                    // 统计该答案出现的次数
                    long count = subjectiveAnswers.stream()
                            .filter(a -> a != null && a.equals(answer))
                            .count();

                    OptionStat optionStat = new OptionStat(answer, (int) count, "不适用");
                    report.getOptions().add(optionStat);
                }
            }

            // 如果没有有效答案，显示"无答案"
            if (report.getOptions().isEmpty()) {
                OptionStat noAnswer = new OptionStat("无答案", 0, "不适用");
                report.getOptions().add(noAnswer);
            }
        }
    }

    /**
     * 计算百分比
     */
    private String calculateRatio(int count, int total) {
        if (total == 0) return "0%";

        double ratio = (double) count / total * 100;
        return String.format("%.2f%%", ratio);
    }
}