package com.ruoyi.cms.scale.calcdata.service.impl;

import com.ruoyi.cms.scale.calcdata.params.standard.SASCalcParams;
import com.ruoyi.cms.scale.domain.dto.SASResultDTO;
import com.ruoyi.cms.scale.domain.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SASScaleCalculator {

    private static final Logger logger = LoggerFactory.getLogger(SASScaleCalculator.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 计算SAS测评结果并返回DTO
     */
    public SASResultDTO calculateResult(ContextAnswerVo contextAnswer, SASCalcParams calcParams,
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

            logger.debug("SAS选项映射构建完成，选项数量: {}, 反向计分题目: {}",
                    optionScoreMap.size(), reverseTopics.size());

            // 2. 计算原始得分
            ScoreResult scoreResult = calculateRawScores(contextAnswer, calcParams, optionScoreMap, reverseTopics);

            // 3. 创建结果DTO
            SASResultDTO resultDTO = new SASResultDTO();

            // 4. 设置基本得分信息
            resultDTO.setRawTotalScore(scoreResult.rawTotalScore);
            resultDTO.setStandardScore(scoreResult.standardScore);
            resultDTO.setAnxietyLevel(scoreResult.anxietyLevel);
            resultDTO.setLevelDescription(getLevelDescription(scoreResult.anxietyLevel));
            resultDTO.setInterpretation(generateInterpretation(scoreResult.anxietyLevel, scoreResult.standardScore));
            resultDTO.setSuggestions(getSuggestionsString(scoreResult.anxietyLevel));

            // 5. 设置维度得分
            resultDTO.setDimensionScores(calculateDimensionScores(contextAnswer, calcParams, optionScoreMap, reverseTopics));

            // 6. 设置建议列表
            resultDTO.setSuggestionList(getSuggestions(scoreResult.anxietyLevel));

            // 7. 设置症状详情
            resultDTO.setSymptomDetails(analyzeSymptoms(contextAnswer, optionScoreMap, reverseTopics));

            // 8. 设置影响程度
            resultDTO.setImpactDegree(getImpactDegree(scoreResult.standardScore));

            // 9. 设置用户信息
            if (lbsResults != null) {
                setUserInfo(resultDTO, lbsResults);
            }

            // 10. 计算进度条宽度和主题颜色
            resultDTO.calculateProgressWidth();
            resultDTO.setThemeColorByLevel();

            logger.info("SAS计算结果: 原始总分={}, 标准分={}, 焦虑程度={}",
                    scoreResult.rawTotalScore, scoreResult.standardScore, scoreResult.anxietyLevel);

            return resultDTO;

        } catch (Exception e) {
            logger.error("SAS计算过程异常: ", e);
            throw new RuntimeException("SAS计算失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据ContextVo构建选项ID到分值的映射
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
     * SAS规则：A.没有或很少时间=1分，B.小部分时间=2分，C.相当多时间=3分，D.绝大部分或全部时间=4分
     */
    private int determineScoreFromOption(OptionVo option) {
        String title = option.getTitle();
        if (title == null || title.isEmpty()) {
            return Integer.parseInt(option.getOrderNum());
        }

        if (title.contains("没有或很少时间") || title.startsWith("A.")) {
            return 1;
        } else if (title.contains("小部分时间") || title.startsWith("B.")) {
            return 2;
        } else if (title.contains("相当多时间") || title.startsWith("C.")) {
            return 3;
        } else if (title.contains("绝大部分") || title.startsWith("D.")) {
            return 4;
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
     * SAS反向计分题目：第5,9,13,17,19题（对应topicId 27,31,35,39,41）
     */
    private Set<Long> getReverseTopics(ContextVo contextVo, SASCalcParams calcParams) {
        Set<Long> reverseTopics = new HashSet<>();

        if (calcParams.getDescTopic() != null && !calcParams.getDescTopic().isEmpty()) {
            for (Integer topicId : calcParams.getDescTopic()) {
                reverseTopics.add(topicId.longValue());
            }
        } else {
            Long[] defaultReverseTopics = {27L, 31L, 35L, 39L, 41L};
            reverseTopics.addAll(Arrays.asList(defaultReverseTopics));
        }

        logger.debug("SAS反向计分题目: {}", reverseTopics);
        return reverseTopics;
    }

    /**
     * 内部类：用于存储得分计算结果
     */
    private static class ScoreResult {
        int rawTotalScore;
        int standardScore;
        String anxietyLevel;

        ScoreResult(int rawTotalScore, int standardScore, String anxietyLevel) {
            this.rawTotalScore = rawTotalScore;
            this.standardScore = standardScore;
            this.anxietyLevel = anxietyLevel;
        }
    }

    /**
     * 计算原始得分
     */
    private ScoreResult calculateRawScores(ContextAnswerVo contextAnswer, SASCalcParams calcParams,
                                           Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        double rate = calcParams.getRate();

        // 将用户答案按topicId分组
        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        logger.info("开始计算SAS原始得分，有效答案数量: {}", answerMap.size());

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

        // 计算标准分（原始总分×1.25，取整数部分）
        double standardScoreDouble = rawTotalScore * rate;
        int standardScore = (int) standardScoreDouble; // SAS标准取整数部分，不是四舍五入

        // 判断焦虑程度
        String anxietyLevel = determineAnxietyLevel(standardScore);

        logger.info("SAS计算完成 - 原始总分: {}, 标准分: {}, 焦虑程度: {}",
                rawTotalScore, standardScore, anxietyLevel);

        return new ScoreResult(rawTotalScore, standardScore, anxietyLevel);
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
            int finalScore = isReverse ? (5 - score) : score;

            logger.debug("选项计分 - topicId: {}, optionId: {}, 原始分值: {}, 反向计分: {}, 最终分值: {}",
                    topicId, optionId, score, isReverse, finalScore);

            return finalScore;

        } catch (NumberFormatException e) {
            logger.error("选项ID格式错误: {}", optionIds, e);
            return 0;
        }
    }

    /**
     * 判断焦虑程度（SAS标准）
     */
    private String determineAnxietyLevel(int standardScore) {
        if (standardScore < 50) {
            return "无焦虑";
        } else if (standardScore <= 59) {
            return "轻度焦虑";
        } else if (standardScore <= 69) {
            return "中度焦虑";
        } else {
            return "重度焦虑";
        }
    }

    /**
     * 计算各维度得分
     */
    private Map<String, Integer> calculateDimensionScores(ContextAnswerVo contextAnswer, SASCalcParams calcParams,
                                                          Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        Map<String, Integer> dimensionScores = new LinkedHashMap<>();

        // 焦虑性情绪（第1,2,3,4,5,6,20题）
        int emotionalAnxiety = calculateDimensionScore(Arrays.asList(23L, 24L, 25L, 26L, 27L, 28L, 42L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("焦虑性情绪", emotionalAnxiety);

        // 躯体性焦虑（第7,8,9,10,11,12,13,14,15,16题）
        int physicalAnxiety = calculateDimensionScore(Arrays.asList(29L, 30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("躯体性焦虑", physicalAnxiety);

        // 其他焦虑症状（第17,18,19题）
        int otherAnxiety = calculateDimensionScore(Arrays.asList(39L, 40L, 41L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("其他焦虑症状", otherAnxiety);

        // 总体焦虑指数
        int totalScore = calculateDimensionScore(
                Arrays.asList(23L, 24L, 25L, 26L, 27L, 28L, 29L, 30L, 31L, 32L, 33L, 34L, 35L, 36L, 37L, 38L, 39L, 40L, 41L, 42L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("总体焦虑指数", totalScore);

        return dimensionScores;
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
                                                      Map<Long, Integer> optionScoreMap,
                                                      Set<Long> reverseTopics) {
        Map<String, List<String>> symptoms = new LinkedHashMap<>();

        // 将症状分类
        List<String> emotionalSymptoms = new ArrayList<>();
        List<String> physicalSymptoms = new ArrayList<>();
        List<String> cognitiveSymptoms = new ArrayList<>();

        // 症状与题目ID的映射
        Map<Long, String[]> symptomMapping = new HashMap<>();
        symptomMapping.put(23L, new String[]{"emotional", "紧张着急"});
        symptomMapping.put(24L, new String[]{"emotional", "无故害怕"});
        symptomMapping.put(25L, new String[]{"emotional", "心里烦乱惊恐"});
        symptomMapping.put(26L, new String[]{"emotional", "将要发疯"});
        symptomMapping.put(27L, new String[]{"emotional", "消极思维"});
        symptomMapping.put(28L, new String[]{"physical", "手脚发抖"});
        symptomMapping.put(29L, new String[]{"physical", "头痛颈痛背痛"});
        symptomMapping.put(30L, new String[]{"physical", "衰弱疲乏"});
        symptomMapping.put(31L, new String[]{"emotional", "不能安静"});
        symptomMapping.put(32L, new String[]{"physical", "心跳加快"});
        symptomMapping.put(33L, new String[]{"physical", "头晕"});
        symptomMapping.put(34L, new String[]{"physical", "晕倒感"});
        symptomMapping.put(35L, new String[]{"physical", "呼吸困难"});
        symptomMapping.put(36L, new String[]{"physical", "手脚麻木刺痛"});
        symptomMapping.put(37L, new String[]{"physical", "胃痛消化不良"});
        symptomMapping.put(38L, new String[]{"physical", "尿频"});
        symptomMapping.put(39L, new String[]{"physical", "手脚干燥温暖"});
        symptomMapping.put(40L, new String[]{"physical", "脸红发热"});
        symptomMapping.put(41L, new String[]{"emotional", "睡眠困难"});
        symptomMapping.put(42L, new String[]{"emotional", "做恶梦"});

        // 检查每题的得分，如果得分≥3分（相当多时间或更多），则视为存在该症状
        for (AnswerVo answer : contextAnswer.getAnswers()) {
            if (answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty()) {
                Long topicId = answer.getTopicId();
                int score = getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, reverseTopics);

                if (score >= 3 && symptomMapping.containsKey(topicId)) {
                    String[] symptomInfo = symptomMapping.get(topicId);
                    String symptomType = symptomInfo[0];
                    String symptomDesc = symptomInfo[1];

                    switch (symptomType) {
                        case "emotional":
                            emotionalSymptoms.add(symptomDesc);
                            break;
                        case "physical":
                            physicalSymptoms.add(symptomDesc);
                            break;
                        case "cognitive":
                            cognitiveSymptoms.add(symptomDesc);
                            break;
                    }
                }
            }
        }

        symptoms.put("情绪症状", emotionalSymptoms);
        symptoms.put("躯体症状", physicalSymptoms);
        symptoms.put("认知症状", cognitiveSymptoms);

        return symptoms;
    }

    /**
     * 获取程度描述
     */
    private String getLevelDescription(String level) {
        switch (level) {
            case "无焦虑":
                return "您的焦虑水平在正常范围内";
            case "轻度焦虑":
                return "您存在轻度焦虑症状，建议适当调整";
            case "中度焦虑":
                return "您存在中度焦虑症状，建议寻求专业帮助";
            case "重度焦虑":
                return "您存在重度焦虑症状，建议立即寻求专业帮助";
            default:
                return "";
        }
    }

    /**
     * 获取影响程度
     */
    private String getImpactDegree(int standardScore) {
        if (standardScore < 50) {
            return "无影响";
        } else if (standardScore <= 59) {
            return "轻度影响";
        } else if (standardScore <= 69) {
            return "中度影响";
        } else {
            return "重度影响";
        }
    }

    /**
     * 获取建议措施列表
     */
    private List<String> getSuggestions(String level) {
        List<String> suggestions = new ArrayList<>();

        switch (level) {
            case "无焦虑":
                suggestions.add("继续保持良好的心态和生活习惯");
                suggestions.add("学习压力管理技巧，预防焦虑发生");
                suggestions.add("定期进行体育锻炼，保持身心健康");
                suggestions.add("维持良好的人际关系和社会支持");
                suggestions.add("培养兴趣爱好，丰富精神生活");
                break;

            case "轻度焦虑":
                suggestions.add("学习放松技巧，如深呼吸、渐进性肌肉放松");
                suggestions.add("保持规律作息，保证充足睡眠");
                suggestions.add("适当进行有氧运动，如散步、瑜伽、游泳");
                suggestions.add("练习正念冥想，提高当下意识");
                suggestions.add("减少咖啡因和酒精摄入");
                suggestions.add("制定合理的工作和生活计划");
                suggestions.add("如症状持续，建议寻求心理咨询");
                break;

            case "中度焦虑":
                suggestions.add("建议寻求专业心理咨询或治疗");
                suggestions.add("学习认知行为疗法技巧，改变负面思维");
                suggestions.add("建立规律的生活作息时间表");
                suggestions.add("练习暴露疗法，逐步面对恐惧情境");
                suggestions.add("保持适度的体育锻炼");
                suggestions.add("考虑参加焦虑管理团体");
                suggestions.add("在医生指导下考虑药物治疗");
                suggestions.add("避免回避行为，逐步面对焦虑源");
                break;

            case "重度焦虑":
                suggestions.add("立即寻求专业精神科医生帮助");
                suggestions.add("进行全面的心理评估和诊断");
                suggestions.add("在医生指导下进行系统心理治疗");
                suggestions.add("可能需要药物治疗配合心理治疗");
                suggestions.add("建立强有力的社会支持系统");
                suggestions.add("学习危机应对策略");
                suggestions.add("避免重大生活变动，减少压力源");
                suggestions.add("定期复查，监测治疗效果");
                suggestions.add("如出现严重症状，及时就医");
                break;
        }

        return suggestions;
    }

    /**
     * 获取建议字符串
     */
    private String getSuggestionsString(String level) {
        List<String> suggestions = getSuggestions(level);
        return String.join("；", suggestions);
    }

    /**
     * 生成测评解读
     */
    private String generateInterpretation(String level, int standardScore) {
        StringBuilder interpretation = new StringBuilder();

        switch (level) {
            case "无焦虑":
                interpretation.append("根据焦虑自评量表(SAS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，低于50分，属于正常范围。这表明您当前的焦虑水平在正常范围内，没有明显的焦虑症状。")
                        .append("建议您继续保持健康的生活方式，关注心理健康，定期进行自我评估。");
                break;
            case "轻度焦虑":
                interpretation.append("根据焦虑自评量表(SAS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，处于50-59分之间，提示存在轻度焦虑症状。")
                        .append("这可能表现为偶尔的紧张不安、担心或身体不适，但程度较轻，对日常生活影响有限。")
                        .append("建议您学习放松技巧，适当调整生活节奏，如症状持续可寻求专业帮助。")
                        .append("请注意，本测评结果仅供参考，不能作为临床诊断依据。");
                break;
            case "中度焦虑":
                interpretation.append("根据焦虑自评量表(SAS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，处于60-69分之间，提示存在中度焦虑症状。")
                        .append("这可能表现为频繁的紧张、担心、烦躁，伴有明显的身体症状如心悸、肌肉紧张等，对日常生活和工作有一定影响。")
                        .append("建议您寻求专业心理咨询或治疗，及时进行干预。")
                        .append("焦虑是可以有效管理的，专业帮助将有助于您更好地应对当前状况。");
                break;
            case "重度焦虑":
                interpretation.append("根据焦虑自评量表(SAS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，高于69分，提示存在重度焦虑症状。")
                        .append("这可能表现为持续的强烈焦虑、恐慌发作、回避行为，严重影响日常生活和社会功能。")
                        .append("强烈建议您尽快寻求专业精神科医生的帮助，进行系统的评估和治疗。")
                        .append("重度焦虑需要专业干预，及时治疗可以有效改善症状，提高生活质量。");
                break;
        }

        return interpretation.toString();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(SASResultDTO resultDTO, LbsResultsVo lbsResults) {
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
    public String convertToJson(SASResultDTO resultDTO) throws Exception {
        return objectMapper.writeValueAsString(resultDTO);
    }

    /**
     * 从JSON字符串解析结果
     */
    public SASResultDTO parseFromJson(String json) throws Exception {
        return objectMapper.readValue(json, SASResultDTO.class);
    }

    /**
     * 验证答案完整性
     */
    public boolean validateAnswers(ContextAnswerVo contextAnswer) {
        if (contextAnswer == null || contextAnswer.getAnswers() == null) {
            return false;
        }

        List<AnswerVo> answers = contextAnswer.getAnswers();
        if (answers.size() < 20) {
            return false;
        }

        long validAnswers = answers.stream()
                .filter(a -> a.getOptionIds() != null && !a.getOptionIds().trim().isEmpty())
                .count();

        return validAnswers >= 16;
    }

    /**
     * 兼容性方法：原有的calculate方法（返回Map）
     */
    public Map<String, Object> calculate(ContextAnswerVo contextAnswer, SASCalcParams calcParams, ContextVo contextVo) {
        SASResultDTO resultDTO = calculateResult(contextAnswer, calcParams, contextVo, null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rawTotalScore", resultDTO.getRawTotalScore());
        result.put("standardScore", resultDTO.getStandardScore());
        result.put("anxietyLevel", resultDTO.getAnxietyLevel());
        result.put("interpretation", resultDTO.getInterpretation());
        result.put("suggestions", resultDTO.getSuggestions());
        result.put("dimensionScores", resultDTO.getDimensionScores());
        result.put("symptomDetails", resultDTO.getSymptomDetails());
        result.put("impactDegree", resultDTO.getImpactDegree());

        return result;
    }
}