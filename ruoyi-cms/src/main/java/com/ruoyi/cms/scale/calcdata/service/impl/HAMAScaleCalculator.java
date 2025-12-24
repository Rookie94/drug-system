package com.ruoyi.cms.scale.calcdata.service.impl;

import com.ruoyi.cms.scale.calcdata.params.standard.HAMACalcParams;
import com.ruoyi.cms.scale.domain.dto.HAMAResultDTO;
import com.ruoyi.cms.scale.domain.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class HAMAScaleCalculator {

    private static final Logger logger = LoggerFactory.getLogger(HAMAScaleCalculator.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 计算HAMA测评结果并返回DTO
     */
    public HAMAResultDTO calculateResult(ContextAnswerVo contextAnswer, HAMACalcParams calcParams,
                                         ContextVo contextVo, LbsResultsVo lbsResults) {
        if (contextAnswer == null) {
            logger.error("contextAnswer参数为空");
            throw new IllegalArgumentException("测评数据不能为空");
        }

        if (calcParams == null) {
            logger.error("calcParams参数为空");
            throw new IllegalArgumentException("计算参数不能为空");
        }

        if (contextVo == null) {
            logger.error("contextVo参数为空");
            throw new IllegalArgumentException("量表题目配置不能为空");
        }

        try {
            // 1. 构建选项映射
            Map<Long, Integer> optionScoreMap = buildOptionScoreMap(contextVo);
            Set<Long> reverseTopics = getReverseTopics(contextVo, calcParams);

            logger.debug("HAMA选项映射构建完成，选项数量: {}", optionScoreMap.size());

            // 2. 计算原始得分
            ScoreResult scoreResult = calculateRawScores(contextAnswer, calcParams, optionScoreMap, reverseTopics);

            // 3. 创建结果DTO
            HAMAResultDTO resultDTO = new HAMAResultDTO();

            // 4. 设置基本得分信息
            resultDTO.setRawTotalScore(scoreResult.rawTotalScore);
            resultDTO.setStandardScore(scoreResult.rawTotalScore); // HAMA标准分等于原始分
            resultDTO.setAnxietyLevel(scoreResult.anxietyLevel);
            resultDTO.setLevelDescription(getLevelDescription(scoreResult.anxietyLevel));
            resultDTO.setInterpretation(generateInterpretation(scoreResult.anxietyLevel, scoreResult.rawTotalScore));
            resultDTO.setSuggestions(getSuggestionsString(scoreResult.anxietyLevel));

            // 5. 设置维度得分
            resultDTO.setDimensionScores(calculateDimensionScores(contextAnswer, calcParams, optionScoreMap, reverseTopics));

            // 6. 设置因子分
            resultDTO.setFactorScores(calculateFactorScores(contextAnswer, optionScoreMap, reverseTopics));

            // 7. 设置症状详情
            resultDTO.setSymptomDetails(analyzeSymptoms(contextAnswer, optionScoreMap));

            // 8. 设置因子分析
            resultDTO.setFactorAnalysis(generateFactorAnalysis(resultDTO.getFactorScores()));

            // 9. 设置临床意义
            resultDTO.setClinicalSignificance(getClinicalSignificance(scoreResult.rawTotalScore));

            // 10. 设置建议列表
            resultDTO.setSuggestionList(getSuggestions(scoreResult.anxietyLevel, resultDTO.getFactorScores()));

            // 11. 设置用户信息
            if (lbsResults != null) {
                setUserInfo(resultDTO, lbsResults);
            }

            // 12. 计算进度条宽度和主题颜色
            resultDTO.calculateProgressWidth();
            resultDTO.setThemeColorByLevel();

            logger.info("HAMA计算结果: 原始总分={}, 焦虑程度={}", scoreResult.rawTotalScore, scoreResult.anxietyLevel);

            return resultDTO;

        } catch (Exception e) {
            logger.error("HAMA计算过程异常: ", e);
            throw new RuntimeException("HAMA计算失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据ContextVo构建选项ID到分值的映射
     * HAMA评分标准：A=0分，B=1分，C=2分，D=3分，E=4分
     */
    private Map<Long, Integer> buildOptionScoreMap(ContextVo contextVo) {
        Map<Long, Integer> optionScoreMap = new HashMap<>();

        if (contextVo.getTopics() == null) {
            return optionScoreMap;
        }

        for (TopicVo topic : contextVo.getTopics()) {
            if (topic.getOptions() == null) {
                continue;
            }

            for (OptionVo option : topic.getOptions()) {
                int score = determineScoreFromOption(option);
                optionScoreMap.put((long) option.getOptionId(), score);

                logger.debug("选项映射 - topicId: {}, optionId: {}, title: {}, 分值: {}",
                        topic.getTopicId(), option.getOptionId(), option.getTitle(), score);
            }
        }

        logger.info("构建选项映射完成，总共 {} 个选项", optionScoreMap.size());
        return optionScoreMap;
    }

    /**
     * 根据选项确定分值
     * HAMA规则：A.无症状=0分，B.轻度=1分，C.中等=2分，D.重度=3分，E.极重=4分
     */
    private int determineScoreFromOption(OptionVo option) {
        String title = option.getTitle();
        if (title == null || title.isEmpty()) {
            try {
                int order = Integer.parseInt(option.getOrderNum());
                return order - 1; // A=0分，B=1分，C=2分，D=3分，E=4分
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        if (title.contains("无症状") || title.startsWith("A.")) {
            return 0;
        } else if (title.contains("轻度") || title.startsWith("B.")) {
            return 1;
        } else if (title.contains("中等") || title.startsWith("C.")) {
            return 2;
        } else if (title.contains("重度") || title.startsWith("D.")) {
            return 3;
        } else if (title.contains("极重") || title.startsWith("E.")) {
            return 4;
        }

        try {
            int order = Integer.parseInt(option.getOrderNum());
            return order - 1;
        } catch (NumberFormatException e) {
            logger.warn("无法确定选项分值，optionId: {}, title: {}", option.getOptionId(), title);
            return 0;
        }
    }

    /**
     * 获取反向计分题目集合
     * HAMA通常没有反向计分题目
     */
    private Set<Long> getReverseTopics(ContextVo contextVo, HAMACalcParams calcParams) {
        Set<Long> reverseTopics = new HashSet<>();

        if (calcParams.getDescTopic() != null && !calcParams.getDescTopic().isEmpty()) {
            for (Integer topicId : calcParams.getDescTopic()) {
                reverseTopics.add(topicId.longValue());
            }
        }

        logger.debug("HAMA反向计分题目: {}", reverseTopics);
        return reverseTopics;
    }

    /**
     * 内部类：用于存储得分计算结果
     */
    private static class ScoreResult {
        int rawTotalScore;
        String anxietyLevel;

        ScoreResult(int rawTotalScore, String anxietyLevel) {
            this.rawTotalScore = rawTotalScore;
            this.anxietyLevel = anxietyLevel;
        }
    }

    /**
     * 计算原始得分
     */
    private ScoreResult calculateRawScores(ContextAnswerVo contextAnswer, HAMACalcParams calcParams,
                                           Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        // 将用户答案按topicId分组
        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        logger.info("开始计算HAMA原始得分，有效答案数量: {}", answerMap.size());

        // 计算原始总分
        int rawTotalScore = 0;

        for (Map.Entry<Long, AnswerVo> entry : answerMap.entrySet()) {
            Long topicId = entry.getKey();
            AnswerVo answer = entry.getValue();

            int optionScore = getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, reverseTopics);
            rawTotalScore += optionScore;

            logger.debug("题目得分 - topicId: {}, 选项IDs: {}, 得分: {}, 累计总分: {}",
                    topicId, answer.getOptionIds(), optionScore, rawTotalScore);
        }

        // 判断焦虑程度（HAMA标准）
        String anxietyLevel = determineAnxietyLevel(rawTotalScore);

        logger.info("HAMA计算完成 - 原始总分: {}, 焦虑程度: {}", rawTotalScore, anxietyLevel);

        return new ScoreResult(rawTotalScore, anxietyLevel);
    }

    /**
     * 获取选项分值
     */
    private int getOptionScore(String optionIds, Long topicId, Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        if (optionIds == null || optionIds.trim().isEmpty()) {
            return 0;
        }

        try {
            String[] options = optionIds.split(",");
            Long optionId = Long.parseLong(options[0].trim());

            Integer score = optionScoreMap.get(optionId);
            if (score == null) {
                logger.warn("选项ID {} 在映射表中未找到", optionId);
                return 0;
            }

            boolean isReverse = reverseTopics.contains(topicId);
            // HAMA反向计分：如果配置了反向，分数取反（通常不会配置）
            int finalScore = isReverse ? (4 - score) : score;

            logger.debug("选项计分 - topicId: {}, optionId: {}, 原始分值: {}, 反向计分: {}, 最终分值: {}",
                    topicId, optionId, score, isReverse, finalScore);

            return finalScore;

        } catch (NumberFormatException e) {
            logger.error("选项ID格式错误: {}", optionIds, e);
            return 0;
        }
    }

    /**
     * 判断焦虑程度（HAMA标准）
     */
    private String determineAnxietyLevel(int totalScore) {
        if (totalScore < 7) {
            return "无焦虑症状";
        } else if (totalScore <= 13) {
            return "可能有焦虑";
        } else if (totalScore <= 20) {
            return "肯定有焦虑";
        } else if (totalScore <= 28) {
            return "肯定有明显焦虑";
        } else {
            return "可能有严重焦虑";
        }
    }

    /**
     * 计算各维度得分
     * HAMA分为：精神性焦虑和躯体性焦虑
     */
    private Map<String, Integer> calculateDimensionScores(ContextAnswerVo contextAnswer, HAMACalcParams calcParams,
                                                          Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        Map<String, Integer> dimensionScores = new LinkedHashMap<>();

        // 精神性焦虑维度（第1,2,3,4,5,6,14题）
        int psychologicalAnxiety = calculateDimensionScore(Arrays.asList(43L, 44L, 45L, 46L, 47L, 48L, 56L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("精神性焦虑", psychologicalAnxiety);

        // 躯体性焦虑维度（第7,8,9,10,11,12,13题）
        int somaticAnxiety = calculateDimensionScore(Arrays.asList(49L, 50L, 51L, 52L, 53L, 54L, 55L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("躯体性焦虑", somaticAnxiety);

        // 总分
        int totalScore = calculateDimensionScore(
                Arrays.asList(43L, 44L, 45L, 46L, 47L, 48L, 49L, 50L, 51L, 52L, 53L, 54L, 55L, 56L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("总分", totalScore);

        return dimensionScores;
    }

    /**
     * 计算HAMA因子分
     * HAMA包含7个因子，每个因子包含2个条目
     */
    private Map<String, Double> calculateFactorScores(ContextAnswerVo contextAnswer,
                                                      Map<Long, Integer> optionScoreMap,
                                                      Set<Long> reverseTopics) {
        Map<String, Double> factorScores = new LinkedHashMap<>();

        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        // 因子1：焦虑心境 (题目1)
        double factor1 = getScoreForTopic(43L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("焦虑心境", factor1);

        // 因子2：紧张 (题目2)
        double factor2 = getScoreForTopic(44L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("紧张", factor2);

        // 因子3：害怕 (题目3)
        double factor3 = getScoreForTopic(45L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("害怕", factor3);

        // 因子4：失眠 (题目4)
        double factor4 = getScoreForTopic(46L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("失眠", factor4);

        // 因子5：认知功能 (题目5)
        double factor5 = getScoreForTopic(47L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("认知功能", factor5);

        // 因子6：抑郁心境 (题目6)
        double factor6 = getScoreForTopic(48L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("抑郁心境", factor6);

        // 因子7：躯体性焦虑-肌肉 (题目7)
        double factor7 = getScoreForTopic(49L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("肌肉症状", factor7);

        // 因子8：躯体性焦虑-感觉 (题目8)
        double factor8 = getScoreForTopic(50L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("感觉症状", factor8);

        // 因子9：心血管症状 (题目9)
        double factor9 = getScoreForTopic(51L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("心血管症状", factor9);

        // 因子10：呼吸症状 (题目10)
        double factor10 = getScoreForTopic(52L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("呼吸症状", factor10);

        // 因子11：胃肠道症状 (题目11)
        double factor11 = getScoreForTopic(53L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("胃肠道症状", factor11);

        // 因子12：泌尿系统症状 (题目12)
        double factor12 = getScoreForTopic(54L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("泌尿系统症状", factor12);

        // 因子13：植物神经症状 (题目13)
        double factor13 = getScoreForTopic(55L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("植物神经症状", factor13);

        // 因子14：会谈行为 (题目14)
        double factor14 = getScoreForTopic(56L, answerMap, optionScoreMap, reverseTopics) / 4.0 * 4;
        factorScores.put("会谈行为", factor14);

        return factorScores;
    }

    private int getScoreForTopic(Long topicId, Map<Long, AnswerVo> answerMap,
                                 Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        AnswerVo answer = answerMap.get(topicId);
        if (answer != null && answer.getOptionIds() != null) {
            return getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, reverseTopics);
        }
        return 0;
    }

    /**
     * 计算单个维度得分
     */
    private int calculateDimensionScore(List<Long> topicIds, ContextAnswerVo contextAnswer,
                                        Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        int score = 0;

        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        for (Long topicId : topicIds) {
            AnswerVo answer = answerMap.get(topicId);
            if (answer != null && answer.getOptionIds() != null) {
                score += getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, reverseTopics);
            }
        }

        return score;
    }

    /**
     * 分析具体症状表现
     */
    private Map<String, List<String>> analyzeSymptoms(ContextAnswerVo contextAnswer,
                                                      Map<Long, Integer> optionScoreMap) {
        Map<String, List<String>> symptoms = new LinkedHashMap<>();

        // 症状类别
        List<String> severeSymptoms = new ArrayList<>();  // 重度症状（得分≥3）
        List<String> moderateSymptoms = new ArrayList<>(); // 中度症状（得分=2）
        List<String> mildSymptoms = new ArrayList<>();    // 轻度症状（得分=1）

        // 题目ID与症状名称的映射
        Map<Long, String> symptomMapping = new HashMap<>();
        symptomMapping.put(43L, "焦虑心境");
        symptomMapping.put(44L, "紧张");
        symptomMapping.put(45L, "害怕");
        symptomMapping.put(46L, "失眠");
        symptomMapping.put(47L, "认知功能");
        symptomMapping.put(48L, "抑郁心境");
        symptomMapping.put(49L, "肌肉系统症状");
        symptomMapping.put(50L, "感觉系统症状");
        symptomMapping.put(51L, "心血管症状");
        symptomMapping.put(52L, "呼吸系统症状");
        symptomMapping.put(53L, "胃肠消化道症状");
        symptomMapping.put(54L, "泌尿系统症状");
        symptomMapping.put(55L, "植物神经症状");
        symptomMapping.put(56L, "会谈行为");

        for (AnswerVo answer : contextAnswer.getAnswers()) {
            if (answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty()) {
                Long topicId = answer.getTopicId();
                int score = getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, new HashSet<>());

                if (score >= 1 && symptomMapping.containsKey(topicId)) {
                    String symptomDesc = symptomMapping.get(topicId);

                    if (score >= 3) {
                        severeSymptoms.add(symptomDesc + "(重度)");
                    } else if (score == 2) {
                        moderateSymptoms.add(symptomDesc + "(中度)");
                    } else if (score == 1) {
                        mildSymptoms.add(symptomDesc + "(轻度)");
                    }
                }
            }
        }

        symptoms.put("重度症状", severeSymptoms);
        symptoms.put("中度症状", moderateSymptoms);
        symptoms.put("轻度症状", mildSymptoms);

        return symptoms;
    }

    /**
     * 生成因子分析
     */
    private String generateFactorAnalysis(Map<String, Double> factorScores) {
        StringBuilder analysis = new StringBuilder();

        // 找出得分最高的3个因子
        List<Map.Entry<String, Double>> sortedFactors = factorScores.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(3)
                .collect(Collectors.toList());

        if (!sortedFactors.isEmpty()) {
            analysis.append("您的焦虑症状主要表现在：");
            for (int i = 0; i < sortedFactors.size(); i++) {
                Map.Entry<String, Double> factor = sortedFactors.get(i);
                analysis.append(factor.getKey());
                if (i < sortedFactors.size() - 1) {
                    analysis.append("、");
                }
            }
            analysis.append("。");
        }

        return analysis.toString();
    }

    /**
     * 获取临床意义
     */
    private String getClinicalSignificance(int totalScore) {
        if (totalScore < 7) {
            return "HAMA总分低于7分，表明没有明显的焦虑症状，属于正常范围。";
        } else if (totalScore <= 13) {
            return "HAMA总分在7-13分之间，提示可能存在焦虑症状，建议关注情绪变化。";
        } else if (totalScore <= 20) {
            return "HAMA总分在14-20分之间，肯定有焦虑症状，建议进行专业心理评估。";
        } else if (totalScore <= 28) {
            return "HAMA总分在21-28分之间，肯定有明显焦虑，建议寻求专业治疗。";
        } else {
            return "HAMA总分超过28分，可能有严重焦虑症状，需要立即专业干预。";
        }
    }

    /**
     * 获取程度描述
     */
    private String getLevelDescription(String level) {
        switch (level) {
            case "无焦虑症状":
                return "您的HAMA总分低于7分，表明没有明显的焦虑症状。";
            case "可能有焦虑":
                return "您的HAMA总分在7-13分之间，提示可能存在焦虑症状。";
            case "肯定有焦虑":
                return "您的HAMA总分在14-20分之间，肯定存在焦虑症状。";
            case "肯定有明显焦虑":
                return "您的HAMA总分在21-28分之间，存在明显焦虑症状。";
            case "可能有严重焦虑":
                return "您的HAMA总分超过28分，可能存在严重焦虑症状。";
            default:
                return "";
        }
    }

    /**
     * 获取建议措施列表
     */
    private List<String> getSuggestions(String level, Map<String, Double> factorScores) {
        List<String> suggestions = new ArrayList<>();

        switch (level) {
            case "无焦虑症状":
                suggestions.add("继续保持良好的心态和生活习惯");
                suggestions.add("学习压力管理技巧，预防焦虑发生");
                suggestions.add("定期进行体育锻炼，保持身心健康");
                suggestions.add("培养兴趣爱好，丰富精神生活");
                break;

            case "可能有焦虑":
                suggestions.add("关注情绪变化，记录焦虑触发因素");
                suggestions.add("学习放松技巧，如深呼吸、渐进性肌肉放松");
                suggestions.add("保持规律作息，保证充足睡眠");
                suggestions.add("适当进行有氧运动，如散步、瑜伽");
                suggestions.add("减少咖啡因和酒精摄入");
                suggestions.add("如症状持续，建议进行专业心理评估");
                break;

            case "肯定有焦虑":
                suggestions.add("建议寻求专业心理咨询或心理治疗");
                suggestions.add("进行全面的心理评估和诊断");
                suggestions.add("学习认知行为疗法技巧");
                suggestions.add("建立规律的生活作息时间表");
                suggestions.add("练习暴露疗法，逐步面对恐惧情境");
                suggestions.add("考虑参加焦虑管理团体");
                suggestions.add("在医生指导下考虑药物治疗");
                break;

            case "肯定有明显焦虑":
                suggestions.add("强烈建议寻求专业精神科医生帮助");
                suggestions.add("进行全面的心理评估和诊断");
                suggestions.add("在医生指导下进行系统心理治疗");
                suggestions.add("可能需要药物治疗配合心理治疗");
                suggestions.add("建立强有力的社会支持系统");
                suggestions.add("学习危机应对策略");
                suggestions.add("避免重大生活变动，减少压力源");
                break;

            case "可能有严重焦虑":
                suggestions.add("立即寻求专业精神科医生的帮助");
                suggestions.add("进行紧急心理评估和诊断");
                suggestions.add("在医生指导下进行系统的心理治疗");
                suggestions.add("在医生指导下使用抗焦虑药物");
                suggestions.add("建立24小时支持系统");
                suggestions.add("学习危机干预策略");
                suggestions.add("避免单独行动，寻求亲友帮助");
                suggestions.add("如出现严重症状，及时就医");
                break;
        }

        // 根据因子分添加针对性建议
        if (factorScores != null) {
            addFactorSpecificSuggestions(suggestions, factorScores);
        }

        return suggestions;
    }

    /**
     * 根据因子分添加针对性建议
     */
    private void addFactorSpecificSuggestions(List<String> suggestions, Map<String, Double> factorScores) {
        // 找出得分较高的因子
        Map<String, Double> highScores = factorScores.entrySet().stream()
                .filter(entry -> entry.getValue() >= 2.0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<String, Double> entry : highScores.entrySet()) {
            String factor = entry.getKey();
            double score = entry.getValue();

            switch (factor) {
                case "失眠":
                    suggestions.add("针对失眠：建立规律的睡眠时间，避免睡前使用电子产品");
                    break;
                case "心血管症状":
                    suggestions.add("针对心血管症状：定期监测血压心率，避免刺激性食物");
                    break;
                case "胃肠道症状":
                    suggestions.add("针对胃肠道症状：注意饮食规律，避免刺激性食物");
                    break;
                case "肌肉症状":
                    suggestions.add("针对肌肉症状：进行肌肉放松训练，适当按摩");
                    break;
            }
        }
    }

    /**
     * 获取建议字符串
     */
    private String getSuggestionsString(String level) {
        List<String> suggestions = getSuggestions(level, new HashMap<>());
        return String.join("；", suggestions);
    }

    /**
     * 生成测评解读
     */
    private String generateInterpretation(String level, int totalScore) {
        StringBuilder interpretation = new StringBuilder();

        interpretation.append("根据汉密顿焦虑量表(HAMA)测评结果，您的总分为")
                .append(totalScore)
                .append("分，属于")
                .append(level)
                .append("。");

        switch (level) {
            case "无焦虑症状":
                interpretation.append("这表明您当前没有明显的焦虑症状，情绪状态良好。");
                break;
            case "可能有焦虑":
                interpretation.append("这表明您可能存在轻度焦虑症状，建议关注情绪变化，适当调整生活方式。");
                break;
            case "肯定有焦虑":
                interpretation.append("这表明您肯定存在焦虑症状，建议寻求专业心理帮助，进行系统评估和治疗。");
                break;
            case "肯定有明显焦虑":
                interpretation.append("这表明您存在明显焦虑症状，对日常生活有一定影响，建议立即寻求专业治疗。");
                break;
            case "可能有严重焦虑":
                interpretation.append("这表明您可能存在严重焦虑症状，需要立即专业干预和治疗。");
                break;
        }

        interpretation.append("HAMA是临床常用的焦虑评估工具，结果具有重要参考价值，但不能替代专业诊断。");

        return interpretation.toString();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(HAMAResultDTO resultDTO, LbsResultsVo lbsResults) {
        if (lbsResults.getNickName() != null) {
            resultDTO.setUserName(lbsResults.getUserName());
            resultDTO.setNickName(lbsResults.getNickName());
        } else if (lbsResults.getUserName() != null) {
            resultDTO.setUserName(lbsResults.getUserName());
            resultDTO.setNickName(lbsResults.getUserName());
        }

        resultDTO.setSex(lbsResults.getSex());
        resultDTO.setPhoneNumber(lbsResults.getPhoneNumber());

        if (lbsResults.getCommitTime() != null) {
            resultDTO.setCommitTime(lbsResults.getCommitTime().toString());
        }
    }

    /**
     * 将结果转换为JSON字符串
     */
    public String convertToJson(HAMAResultDTO resultDTO) throws Exception {
        return objectMapper.writeValueAsString(resultDTO);
    }

    /**
     * 从JSON字符串解析结果
     */
    public HAMAResultDTO parseFromJson(String json) throws Exception {
        return objectMapper.readValue(json, HAMAResultDTO.class);
    }

    /**
     * 验证答案完整性
     */
    public boolean validateAnswers(ContextAnswerVo contextAnswer) {
        if (contextAnswer == null || contextAnswer.getAnswers() == null) {
            return false;
        }

        List<AnswerVo> answers = contextAnswer.getAnswers();
        if (answers.size() < 14) {
            return false;
        }

        long validAnswers = answers.stream()
                .filter(a -> a.getOptionIds() != null && !a.getOptionIds().trim().isEmpty())
                .count();

        // HAMA应有14题，至少完成70%（10题）才认为有效
        return validAnswers >= 10;
    }

    /**
     * 兼容性方法：原有的calculate方法（返回Map）
     */
    public Map<String, Object> calculate(ContextAnswerVo contextAnswer, HAMACalcParams calcParams, ContextVo contextVo) {
        HAMAResultDTO resultDTO = calculateResult(contextAnswer, calcParams, contextVo, null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rawTotalScore", resultDTO.getRawTotalScore());
        result.put("standardScore", resultDTO.getStandardScore());
        result.put("anxietyLevel", resultDTO.getAnxietyLevel());
        result.put("interpretation", resultDTO.getInterpretation());
        result.put("suggestions", resultDTO.getSuggestions());
        result.put("dimensionScores", resultDTO.getDimensionScores());
        result.put("factorScores", resultDTO.getFactorScores());
        result.put("symptomDetails", resultDTO.getSymptomDetails());
        result.put("factorAnalysis", resultDTO.getFactorAnalysis());
        result.put("clinicalSignificance", resultDTO.getClinicalSignificance());

        return result;
    }
}