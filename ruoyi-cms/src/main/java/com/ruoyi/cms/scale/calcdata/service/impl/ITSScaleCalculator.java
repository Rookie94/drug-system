package com.ruoyi.cms.scale.calcdata.service.impl;

import com.ruoyi.cms.scale.calcdata.params.family.ITSCalcParams;
import com.ruoyi.cms.scale.domain.dto.ITSResultDTO;
import com.ruoyi.cms.scale.domain.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ITSScaleCalculator {

    private static final Logger logger = LoggerFactory.getLogger(ITSScaleCalculator.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 计算ITS测评结果并返回DTO
     */
    public ITSResultDTO calculateResult(ContextAnswerVo contextAnswer, ITSCalcParams calcParams,
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

            logger.debug("ITS选项映射构建完成，选项数量: {}", optionScoreMap.size());

            // 2. 计算原始得分
            ScoreResult scoreResult = calculateRawScores(contextAnswer, calcParams, optionScoreMap, reverseTopics);

            // 3. 创建结果DTO
            ITSResultDTO resultDTO = new ITSResultDTO();

            // 4. 设置基本得分信息
            resultDTO.setRawTotalScore(scoreResult.rawTotalScore);
            resultDTO.setStandardScore(scoreResult.standardScore);
            resultDTO.setTrustLevel(scoreResult.trustLevel);
            resultDTO.setLevelDescription(getLevelDescription(scoreResult.trustLevel, scoreResult.averageScore));
            resultDTO.setInterpretation(generateInterpretation(scoreResult.trustLevel, scoreResult.rawTotalScore));
            resultDTO.setSuggestions(getSuggestionsString(scoreResult.trustLevel));

            // 5. 计算平均分
            resultDTO.calculateAverageScore();

            // 6. 设置维度得分
            resultDTO.setDimensionScores(calculateDimensionScores(contextAnswer, calcParams, optionScoreMap, reverseTopics));

            // 7. 设置信任要素分析
            resultDTO.setTrustFactors(calculateTrustFactors(contextAnswer, optionScoreMap, reverseTopics));

            // 8. 设置信任分析
            resultDTO.setTrustAnalysis(analyzeTrustPatterns(contextAnswer, optionScoreMap));

            // 9. 设置建议列表
            resultDTO.setSuggestionList(getSuggestions(scoreResult.trustLevel, resultDTO.getTrustFactors()));

            // 10. 设置信任强度
            resultDTO.setTrustIntensity(getTrustIntensity(scoreResult.averageScore));

            // 11. 设置用户信息
            if (lbsResults != null) {
                setUserInfo(resultDTO, lbsResults);
            }

            // 12. 计算进度条宽度和主题颜色
            resultDTO.calculateProgressWidth();
            resultDTO.setThemeColorByLevel();

            logger.info("ITS计算结果: 原始总分={}, 平均分={}, 信任程度={}",
                    scoreResult.rawTotalScore, scoreResult.averageScore, scoreResult.trustLevel);

            return resultDTO;

        } catch (Exception e) {
            logger.error("ITS计算过程异常: ", e);
            throw new RuntimeException("ITS计算失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据ContextVo构建选项ID到分值的映射
     * ITS评分标准：A=1分，B=2分，C=3分，D=4分，E=5分，F=6分，G=7分
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
     * ITS规则：A.完全不同意=1分，B.部分不同意=2分，C.略微不同意=3分，D.中性=4分，E.略微同意=5分，F.部分同意=6分，G.完全同意=7分
     */
    private int determineScoreFromOption(OptionVo option) {
        String title = option.getTitle();
        if (title == null || title.isEmpty()) {
            try {
                return Integer.parseInt(option.getOrderNum());
            } catch (NumberFormatException e) {
                return 1;
            }
        }

        if (title.contains("完全不同意") || title.startsWith("A.")) {
            return 1;
        } else if (title.contains("部分不同意") || title.startsWith("B.")) {
            return 2;
        } else if (title.contains("略微不同意") || title.startsWith("C.")) {
            return 3;
        } else if (title.contains("中性") || title.startsWith("D.")) {
            return 4;
        } else if (title.contains("略微同意") || title.startsWith("E.")) {
            return 5;
        } else if (title.contains("部分同意") || title.startsWith("F.")) {
            return 6;
        } else if (title.contains("完全同意") || title.startsWith("G.")) {
            return 7;
        }

        try {
            return Integer.parseInt(option.getOrderNum());
        } catch (NumberFormatException e) {
            logger.warn("无法确定选项分值，optionId: {}, title: {}", option.getOptionId(), title);
            return 1;
        }
    }

    /**
     * 获取反向计分题目集合
     * ITS通常没有反向计分题目
     */
    private Set<Long> getReverseTopics(ContextVo contextVo, ITSCalcParams calcParams) {
        Set<Long> reverseTopics = new HashSet<>();

        if (calcParams.getDescTopic() != null && !calcParams.getDescTopic().isEmpty()) {
            for (Integer topicId : calcParams.getDescTopic()) {
                reverseTopics.add(topicId.longValue());
            }
        }

        logger.debug("ITS反向计分题目: {}", reverseTopics);
        return reverseTopics;
    }

    /**
     * 内部类：用于存储得分计算结果
     */
    private static class ScoreResult {
        int rawTotalScore;
        int standardScore;
        double averageScore;
        String trustLevel;

        ScoreResult(int rawTotalScore, int standardScore, double averageScore, String trustLevel) {
            this.rawTotalScore = rawTotalScore;
            this.standardScore = standardScore;
            this.averageScore = averageScore;
            this.trustLevel = trustLevel;
        }
    }

    /**
     * 计算原始得分
     */
    private ScoreResult calculateRawScores(ContextAnswerVo contextAnswer, ITSCalcParams calcParams,
                                           Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        // 将用户答案按topicId分组
        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        logger.info("开始计算ITS原始得分，有效答案数量: {}", answerMap.size());

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

        // 计算平均分
        double averageScore = answerMap.size() > 0 ? rawTotalScore / (double) answerMap.size() : 0;

        // 计算标准分（转换为0-100分制）
        // 原始分范围：18-126分，转换公式：(原始分-18) / (126-18) * 100
        int standardScore = 0;
        if (rawTotalScore >= 18) {
            standardScore = (int) Math.round(((rawTotalScore - 18) / 108.0) * 100);
        }

        // 判断信任程度（基于平均分）
        String trustLevel = determineTrustLevel(averageScore);

        logger.info("ITS计算完成 - 原始总分: {}, 平均分: {}, 标准分: {}, 信任程度: {}",
                rawTotalScore, averageScore, standardScore, trustLevel);

        return new ScoreResult(rawTotalScore, standardScore, averageScore, trustLevel);
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
            int finalScore = isReverse ? (8 - score) : score;  // 反向计分：1->7, 2->6, ..., 7->1

            logger.debug("选项计分 - topicId: {}, optionId: {}, 原始分值: {}, 反向计分: {}, 最终分值: {}",
                    topicId, optionId, score, isReverse, finalScore);

            return finalScore;

        } catch (NumberFormatException e) {
            logger.error("选项ID格式错误: {}", optionIds, e);
            return 0;
        }
    }

    /**
     * 判断信任程度（基于平均分）
     */
    private String determineTrustLevel(double averageScore) {
        if (averageScore <= 2.5) {
            return "非常低信任";
        } else if (averageScore <= 3.5) {
            return "较低信任";
        } else if (averageScore <= 5.0) {
            return "中等信任";
        } else if (averageScore <= 6.0) {
            return "较高信任";
        } else {
            return "非常高信任";
        }
    }

    /**
     * 获取信任强度
     */
    private String getTrustIntensity(double averageScore) {
        if (averageScore <= 2.5) {
            return "很弱";
        } else if (averageScore <= 3.5) {
            return "较弱";
        } else if (averageScore <= 5.0) {
            return "中等";
        } else if (averageScore <= 6.0) {
            return "较强";
        } else {
            return "很强";
        }
    }

    /**
     * 计算各维度得分
     * ITS通常可分为：可靠性、情感支持、诚实性、尊重等维度
     */
    private Map<String, Integer> calculateDimensionScores(ContextAnswerVo contextAnswer, ITSCalcParams calcParams,
                                                          Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        Map<String, Integer> dimensionScores = new LinkedHashMap<>();

        // 可靠性维度（第1,5,10,15题）
        int reliabilityScore = calculateDimensionScore(Arrays.asList(166L, 170L, 175L, 180L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("可靠性", reliabilityScore);

        // 情感支持维度（第2,7,13,18题）
        int emotionalSupportScore = calculateDimensionScore(Arrays.asList(167L, 172L, 178L, 183L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("情感支持", emotionalSupportScore);

        // 诚实性维度（第3,9,14题）
        int honestyScore = calculateDimensionScore(Arrays.asList(168L, 174L, 179L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("诚实性", honestyScore);

        // 尊重与公平维度（第4,6,11,16题）
        int respectScore = calculateDimensionScore(Arrays.asList(169L, 171L, 176L, 181L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("尊重与公平", respectScore);

        // 忠诚度维度（第8,12,17题）
        int loyaltyScore = calculateDimensionScore(Arrays.asList(173L, 177L, 182L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("忠诚度", loyaltyScore);

        // 总分
        int totalScore = calculateDimensionScore(
                Arrays.asList(166L, 167L, 168L, 169L, 170L, 171L, 172L, 173L, 174L, 175L, 176L, 177L, 178L, 179L, 180L, 181L, 182L, 183L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("总分", totalScore);

        return dimensionScores;
    }

    /**
     * 计算信任要素分析
     */
    private Map<String, Double> calculateTrustFactors(ContextAnswerVo contextAnswer,
                                                      Map<Long, Integer> optionScoreMap,
                                                      Set<Long> reverseTopics) {
        Map<String, Double> trustFactors = new LinkedHashMap<>();

        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        // 计算各信任要素的平均分
        // 帮助意愿
        double helpWillingness = calculateFactorAverage(Arrays.asList(166L, 170L, 173L), answerMap, optionScoreMap, reverseTopics);
        trustFactors.put("帮助意愿", helpWillingness);

        // 情感安全
        double emotionalSafety = calculateFactorAverage(Arrays.asList(167L, 172L, 178L), answerMap, optionScoreMap, reverseTopics);
        trustFactors.put("情感安全", emotionalSafety);

        // 保密性
        double confidentiality = calculateFactorAverage(Arrays.asList(168L, 171L, 181L), answerMap, optionScoreMap, reverseTopics);
        trustFactors.put("保密性", confidentiality);

        // 公平性
        double fairness = calculateFactorAverage(Arrays.asList(169L, 176L, 179L), answerMap, optionScoreMap, reverseTopics);
        trustFactors.put("公平性", fairness);

        // 诚实性
        double honesty = calculateFactorAverage(Arrays.asList(174L, 175L, 177L), answerMap, optionScoreMap, reverseTopics);
        trustFactors.put("诚实性", honesty);

        // 支持性
        double supportiveness = calculateFactorAverage(Arrays.asList(180L, 182L, 183L), answerMap, optionScoreMap, reverseTopics);
        trustFactors.put("支持性", supportiveness);

        return trustFactors;
    }

    private double calculateFactorAverage(List<Long> topicIds, Map<Long, AnswerVo> answerMap,
                                          Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        double total = 0;
        int count = 0;

        for (Long topicId : topicIds) {
            AnswerVo answer = answerMap.get(topicId);
            if (answer != null && answer.getOptionIds() != null) {
                int score = getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, reverseTopics);
                total += score;
                count++;
            }
        }

        return count > 0 ? total / count : 0;
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
     * 分析信任模式
     */
    private Map<String, List<String>> analyzeTrustPatterns(ContextAnswerVo contextAnswer,
                                                           Map<Long, Integer> optionScoreMap) {
        Map<String, List<String>> analysis = new LinkedHashMap<>();

        // 信任优势
        List<String> strengths = new ArrayList<>();
        // 信任不足
        List<String> weaknesses = new ArrayList<>();

        // 题目ID与信任要素的映射
        Map<Long, String> trustMapping = new HashMap<>();
        trustMapping.put(166L, "帮助可靠性");
        trustMapping.put(167L, "情感安全感");
        trustMapping.put(168L, "保密性");
        trustMapping.put(169L, "公平性");
        trustMapping.put(170L, "承诺履行");
        trustMapping.put(171L, "背后评价");
        trustMapping.put(172L, "利益考虑");
        trustMapping.put(173L, "困难支持");
        trustMapping.put(174L, "诚实性");
        trustMapping.put(175L, "无伤害意图");
        trustMapping.put(176L, "成就共享");
        trustMapping.put(177L, "无利用意图");
        trustMapping.put(178L, "理解能力");
        trustMapping.put(179L, "宽容性");
        trustMapping.put(180L, "真实接受");
        trustMapping.put(181L, "背后忠诚");
        trustMapping.put(182L, "关系重视");
        trustMapping.put(183L, "情感支持");

        for (AnswerVo answer : contextAnswer.getAnswers()) {
            if (answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty()) {
                Long topicId = answer.getTopicId();
                int score = getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, new HashSet<>());

                if (trustMapping.containsKey(topicId)) {
                    String trustElement = trustMapping.get(topicId);

                    if (score >= 6) {
                        strengths.add(trustElement);
                    } else if (score <= 3) {
                        weaknesses.add(trustElement);
                    }
                }
            }
        }

        analysis.put("信任优势", strengths);
        analysis.put("信任不足", weaknesses);

        return analysis;
    }

    /**
     * 获取程度描述
     */
    private String getLevelDescription(String level, double averageScore) {
        switch (level) {
            case "非常低信任":
                return String.format("您的信任平均分为%.1f分，信任程度非常低。表明您对该对象的信任感很弱，可能存在较严重的不信任问题。", averageScore);
            case "较低信任":
                return String.format("您的信任平均分为%.1f分，信任程度较低。表明您对该对象的信任感较弱，需要关注信任关系的建立。", averageScore);
            case "中等信任":
                return String.format("您的信任平均分为%.1f分，信任程度中等。表明您对该对象有一定程度的信任，但仍存在提升空间。", averageScore);
            case "较高信任":
                return String.format("您的信任平均分为%.1f分，信任程度较高。表明您对该对象有较强的信任感，这是一个健康的信任关系。", averageScore);
            case "非常高信任":
                return String.format("您的信任平均分为%.1f分，信任程度非常高。表明您对该对象有极强的信任感，这是一个非常稳固的信任关系。", averageScore);
            default:
                return String.format("您的信任平均分为%.1f分，信任程度为%s。", averageScore, level);
        }
    }

    /**
     * 获取建议措施列表
     */
    private List<String> getSuggestions(String level, Map<String, Double> trustFactors) {
        List<String> suggestions = new ArrayList<>();

        switch (level) {
            case "非常低信任":
                suggestions.add("重新评估您与该对象的关系是否值得继续");
                suggestions.add("尝试与对方进行开诚布公的沟通，表达您的疑虑");
                suggestions.add("观察对方的行为是否与承诺一致");
                suggestions.add("考虑建立适当的边界，保护自己的利益");
                suggestions.add("如关系中存在伤害，建议寻求专业咨询帮助");
                break;

            case "较低信任":
                suggestions.add("关注信任关系中存在的具体问题");
                suggestions.add("与对方讨论信任建立的具体方式");
                suggestions.add("从小事开始建立信任，逐步积累");
                suggestions.add("设立明确的期望和边界");
                suggestions.add("给予适当的信任测试机会，观察对方反应");
                break;

            case "中等信任":
                suggestions.add("继续加强已有的信任基础");
                suggestions.add("识别信任关系中的优势领域并加以巩固");
                suggestions.add("针对信任不足的方面进行改善");
                suggestions.add("保持开放沟通，及时解决小问题");
                suggestions.add("定期回顾信任关系的发展");
                break;

            case "较高信任":
                suggestions.add("维持并深化现有的信任关系");
                suggestions.add("在信任基础上建立更深层次的情感连接");
                suggestions.add("继续展现值得信赖的行为，巩固信任");
                suggestions.add("在信任基础上共同面对挑战");
                suggestions.add("珍视这份信任，避免做出损害信任的行为");
                break;

            case "非常高信任":
                suggestions.add("继续保持这种高质量的信任关系");
                suggestions.add("在高度信任的基础上建立更深入的合作");
                suggestions.add("将这种信任模式应用到其他关系中");
                suggestions.add("定期表达对这份信任的珍视和感激");
                suggestions.add("在信任基础上共同成长和发展");
                break;
        }

        // 根据信任要素分数添加针对性建议
        if (trustFactors != null) {
            addFactorSpecificSuggestions(suggestions, trustFactors);
        }

        return suggestions;
    }

    /**
     * 根据信任要素添加针对性建议
     */
    private void addFactorSpecificSuggestions(List<String> suggestions, Map<String, Double> trustFactors) {
        // 找出得分较低的要素
        Map<String, Double> lowScores = trustFactors.entrySet().stream()
                .filter(entry -> entry.getValue() < 4.0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Map.Entry<String, Double> entry : lowScores.entrySet()) {
            String factor = entry.getKey();
            double score = entry.getValue();

            switch (factor) {
                case "帮助意愿":
                    suggestions.add("针对帮助意愿：明确表达需要帮助的具体情况");
                    break;
                case "情感安全":
                    suggestions.add("针对情感安全：逐步建立情感表达的安全空间");
                    break;
                case "保密性":
                    suggestions.add("针对保密性：明确保密的重要性和具体范围");
                    break;
                case "公平性":
                    suggestions.add("针对公平性：讨论并建立公平的互动规则");
                    break;
                case "诚实性":
                    suggestions.add("针对诚实性：强调诚实沟通的重要性");
                    break;
                case "支持性":
                    suggestions.add("针对支持性：明确表达支持和鼓励的方式");
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

        interpretation.append("根据信任量表(ITS)测评结果，您的总分为")
                .append(totalScore)
                .append("分，属于")
                .append(level)
                .append("。");

        switch (level) {
            case "非常低信任":
                interpretation.append("这表明您与该对象之间的信任基础非常薄弱，可能存在较严重的关系问题。建议您认真考虑是否继续这段关系，或寻求专业帮助改善信任状况。");
                break;
            case "较低信任":
                interpretation.append("这表明您与该对象之间的信任度较低，需要投入时间和精力来建立信任。建议从小的承诺开始，逐步积累信任资本。");
                break;
            case "中等信任":
                interpretation.append("这表明您与该对象之间有一定程度的信任，但仍存在提升空间。这是一个可以继续发展和深化的信任关系。");
                break;
            case "较高信任":
                interpretation.append("这表明您与该对象之间有较强的信任感，这是一个健康稳固的关系。信任是人际关系的重要基础，值得珍惜和维护。");
                break;
            case "非常高信任":
                interpretation.append("这表明您与该对象之间有极强的信任感，这是一个非常优质的关系。这样的高度信任是建立深度合作和亲密关系的重要前提。");
                break;
        }

        interpretation.append("信任是人际关系的基石，良好的信任关系有助于提升生活质量和心理健康。");

        return interpretation.toString();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(ITSResultDTO resultDTO, LbsResultsVo lbsResults) {
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
    public String convertToJson(ITSResultDTO resultDTO) throws Exception {
        return objectMapper.writeValueAsString(resultDTO);
    }

    /**
     * 从JSON字符串解析结果
     */
    public ITSResultDTO parseFromJson(String json) throws Exception {
        return objectMapper.readValue(json, ITSResultDTO.class);
    }

    /**
     * 验证答案完整性
     */
    public boolean validateAnswers(ContextAnswerVo contextAnswer) {
        if (contextAnswer == null || contextAnswer.getAnswers() == null) {
            return false;
        }

        List<AnswerVo> answers = contextAnswer.getAnswers();
        if (answers.size() < 18) {
            return false;
        }

        long validAnswers = answers.stream()
                .filter(a -> a.getOptionIds() != null && !a.getOptionIds().trim().isEmpty())
                .count();

        // ITS应有18题，至少完成70%（13题）才认为有效
        return validAnswers >= 13;
    }

    /**
     * 兼容性方法：原有的calculate方法（返回Map）
     */
    public Map<String, Object> calculate(ContextAnswerVo contextAnswer, ITSCalcParams calcParams, ContextVo contextVo) {
        ITSResultDTO resultDTO = calculateResult(contextAnswer, calcParams, contextVo, null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rawTotalScore", resultDTO.getRawTotalScore());
        result.put("standardScore", resultDTO.getStandardScore());
        result.put("trustLevel", resultDTO.getTrustLevel());
        result.put("interpretation", resultDTO.getInterpretation());
        result.put("suggestions", resultDTO.getSuggestions());
        result.put("dimensionScores", resultDTO.getDimensionScores());
        result.put("trustFactors", resultDTO.getTrustFactors());
        result.put("trustAnalysis", resultDTO.getTrustAnalysis());
        result.put("averageScore", resultDTO.getAverageScore());
        result.put("trustIntensity", resultDTO.getTrustIntensity());

        return result;
    }
}