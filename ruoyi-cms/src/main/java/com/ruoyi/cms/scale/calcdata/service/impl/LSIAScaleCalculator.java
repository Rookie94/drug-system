package com.ruoyi.cms.scale.calcdata.service.impl;

import com.ruoyi.cms.scale.calcdata.params.family.LSIACalcParams;
import com.ruoyi.cms.scale.domain.dto.LSIAResultDTO;
import com.ruoyi.cms.scale.domain.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LSIAScaleCalculator {

    private static final Logger logger = LoggerFactory.getLogger(LSIAScaleCalculator.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 计算LSIA测评结果并返回DTO
     */
    public LSIAResultDTO calculateResult(ContextAnswerVo contextAnswer, LSIACalcParams calcParams,
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

            logger.debug("LSIA选项映射构建完成，选项数量: {}, 反向计分题目: {}",
                    optionScoreMap.size(), reverseTopics.size());

            // 2. 计算原始得分
            ScoreResult scoreResult = calculateRawScores(contextAnswer, calcParams, optionScoreMap, reverseTopics);

            // 3. 创建结果DTO
            LSIAResultDTO resultDTO = new LSIAResultDTO();

            // 4. 设置基本得分信息
            resultDTO.setRawTotalScore(scoreResult.rawTotalScore);
            resultDTO.setStandardScore(scoreResult.standardScore);
            resultDTO.setSatisfactionLevel(scoreResult.satisfactionLevel);
            resultDTO.setLevelDescription(getLevelDescription(scoreResult.satisfactionLevel));
            resultDTO.setInterpretation(generateInterpretation(scoreResult.satisfactionLevel, scoreResult.standardScore));
            resultDTO.setSuggestions(getSuggestionsString(scoreResult.satisfactionLevel));

            // 5. 设置维度得分
            resultDTO.setDimensionScores(calculateDimensionScores(contextAnswer, calcParams, optionScoreMap, reverseTopics));

            // 6. 设置建议列表
            resultDTO.setSuggestionList(getSuggestions(scoreResult.satisfactionLevel));

            // 7. 设置满意度详情
            resultDTO.setSatisfactionDetails(analyzeSatisfaction(contextAnswer, optionScoreMap, reverseTopics));

            // 8. 设置影响程度
            resultDTO.setImpactDegree(getImpactDegree(scoreResult.standardScore));

            // 9. 设置用户信息
            if (lbsResults != null) {
                setUserInfo(resultDTO, lbsResults);
            }

            // 10. 计算进度条宽度和主题颜色
            resultDTO.calculateProgressWidth();
            resultDTO.setThemeColorByLevel();

            logger.info("LSIA计算结果: 原始总分={}, 标准分={}, 满意度程度={}",
                    scoreResult.rawTotalScore, scoreResult.standardScore, scoreResult.satisfactionLevel);

            return resultDTO;

        } catch (Exception e) {
            logger.error("LSIA计算过程异常: ", e);
            throw new RuntimeException("LSIA计算失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据ContextVo构建选项ID到分值的映射
     * LSIA计分规则:
     * 正向题: 同意=2分, 不确定=1分, 不同意=0分
     * 反向题: 同意=0分, 不确定=1分, 不同意=2分
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
     * LSIA规则: A.同意=2分, C.不确定=1分, B.不同意=0分
     */
    private int determineScoreFromOption(OptionVo option) {
        String title = option.getTitle();
        if (title == null || title.isEmpty()) {
            try {
                return Integer.parseInt(option.getOrderNum());
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        // 清理标题字符串
        title = title.trim();

        if (title.startsWith("A.") || title.contains("同意")) {
            return 2;
        } else if (title.startsWith("C.") || title.contains("不确定")) {
            return 1;
        } else if (title.startsWith("B.") || title.contains("不同意")) {
            return 0;
        }

        try {
            return Integer.parseInt(option.getOrderNum());
        } catch (NumberFormatException e) {
            logger.warn("无法确定选项分值，optionId: {}, title: {}", option.getOptionId(), title);
            return 0;
        }
    }

    /**
     * 获取反向计分题目集合
     * LSIA反向计分题目（常见）：3,5,7,10,14,17,18,20题
     * 对应topicId: 59,61,63,66,70,73,74,76
     */
    private Set<Long> getReverseTopics(ContextVo contextVo, LSIACalcParams calcParams) {
        Set<Long> reverseTopics = new HashSet<>();

        if (calcParams.getDescTopic() != null && !calcParams.getDescTopic().isEmpty()) {
            for (Integer topicId : calcParams.getDescTopic()) {
                reverseTopics.add(topicId.longValue());
            }
        } else {
            Long[] defaultReverseTopics = {59L, 61L, 63L, 66L, 70L, 73L, 74L, 76L};
            reverseTopics.addAll(Arrays.asList(defaultReverseTopics));
        }

        logger.debug("LSIA反向计分题目: {}", reverseTopics);
        return reverseTopics;
    }

    /**
     * 内部类：用于存储得分计算结果
     */
    private static class ScoreResult {
        int rawTotalScore;
        int standardScore;
        String satisfactionLevel;

        ScoreResult(int rawTotalScore, int standardScore, String satisfactionLevel) {
            this.rawTotalScore = rawTotalScore;
            this.standardScore = standardScore;
            this.satisfactionLevel = satisfactionLevel;
        }
    }

    /**
     * 计算原始得分
     */
    private ScoreResult calculateRawScores(ContextAnswerVo contextAnswer, LSIACalcParams calcParams,
                                           Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        double rate = calcParams.getRate();

        // 将用户答案按topicId分组
        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        logger.info("开始计算LSIA原始得分，有效答案数量: {}", answerMap.size());

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

        // LSIA直接使用原始总分作为标准分（不需要转换）
        int standardScore = rawTotalScore;

        // 判断满意度程度
        String satisfactionLevel = determineSatisfactionLevel(standardScore);

        logger.info("LSIA计算完成 - 原始总分: {}, 标准分: {}, 满意度程度: {}",
                rawTotalScore, standardScore, satisfactionLevel);

        return new ScoreResult(rawTotalScore, standardScore, satisfactionLevel);
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
            int finalScore = isReverse ? (2 - score) : score;

            logger.debug("选项计分 - topicId: {}, optionId: {}, 原始分值: {}, 反向计分: {}, 最终分值: {}",
                    topicId, optionId, score, isReverse, finalScore);

            return finalScore;

        } catch (NumberFormatException e) {
            logger.error("选项ID格式错误: {}", optionIds, e);
            return 0;
        }
    }

    /**
     * 判断满意度程度（LSIA标准，总分0-40分）
     */
    private String determineSatisfactionLevel(int standardScore) {
        if (standardScore >= 31) {
            return "非常满意";
        } else if (standardScore >= 21) {
            return "一般满意";
        } else if (standardScore >= 11) {
            return "不太满意";
        } else {
            return "非常不满意";
        }
    }

    /**
     * 计算各维度得分
     */
    private Map<String, Integer> calculateDimensionScores(ContextAnswerVo contextAnswer, LSIACalcParams calcParams,
                                                          Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        Map<String, Integer> dimensionScores = new LinkedHashMap<>();

        // 对过去的满意度（第1,3,5,12,13,14,17,18题）
        int pastSatisfaction = calculateDimensionScore(Arrays.asList(57L, 59L, 61L, 68L, 69L, 70L, 73L, 74L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("过去满意度", pastSatisfaction);

        // 对现在的满意度（第2,4,6,7,8,9,10,11,15,16,19题）
        int presentSatisfaction = calculateDimensionScore(Arrays.asList(58L, 60L, 62L, 63L, 64L, 65L, 66L, 67L, 71L, 72L, 75L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("现在满意度", presentSatisfaction);

        // 对未来期望（第20题）
        int futureExpectation = calculateDimensionScore(Arrays.asList(76L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("未来期望", futureExpectation);

        // 总体满意度指数
        int totalScore = calculateDimensionScore(
                Arrays.asList(57L, 58L, 59L, 60L, 61L, 62L, 63L, 64L, 65L, 66L, 67L, 68L, 69L, 70L, 71L, 72L, 73L, 74L, 75L, 76L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("总体满意度", totalScore);

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
     * 分析具体满意度表现
     */
    private Map<String, List<String>> analyzeSatisfaction(ContextAnswerVo contextAnswer,
                                                          Map<Long, Integer> optionScoreMap,
                                                          Set<Long> reverseTopics) {
        Map<String, List<String>> satisfaction = new LinkedHashMap<>();

        // 将满意度分类
        List<String> positiveAspects = new ArrayList<>();
        List<String> areasForImprovement = new ArrayList<>();

        // 题目与描述的映射
        Map<Long, String[]> questionMapping = new HashMap<>();
        questionMapping.put(57L, new String[]{"positive", "对衰老持积极态度"});
        questionMapping.put(58L, new String[]{"positive", "把握生活机遇"});
        questionMapping.put(59L, new String[]{"negative", "当前情绪状态"});
        questionMapping.put(60L, new String[]{"positive", "幸福感保持"});
        questionMapping.put(61L, new String[]{"negative", "生活期望落差"});
        questionMapping.put(62L, new String[]{"positive", "当前生活满意度"});
        questionMapping.put(63L, new String[]{"negative", "活动兴趣"});
        questionMapping.put(64L, new String[]{"positive", "未来期待"});
        questionMapping.put(65L, new String[]{"positive", "活动乐趣"});
        questionMapping.put(66L, new String[]{"negative", "衰老感受"});
        questionMapping.put(67L, new String[]{"positive", "接受衰老"});
        questionMapping.put(68L, new String[]{"positive", "对过去的满足"});
        questionMapping.put(69L, new String[]{"positive", "接受过去"});
        questionMapping.put(70L, new String[]{"negative", "决策评价"});
        questionMapping.put(71L, new String[]{"positive", "外貌满意度"});
        questionMapping.put(72L, new String[]{"positive", "未来规划"});
        questionMapping.put(73L, new String[]{"negative", "未满足的期望"});
        questionMapping.put(74L, new String[]{"negative", "失败感受"});
        questionMapping.put(75L, new String[]{"positive", "期望实现"});
        questionMapping.put(76L, new String[]{"negative", "对普通人生活的看法"});

        // 检查每题得分，低分（≤1分）表示需要改进的方面，高分（≥2分）表示积极方面
        for (AnswerVo answer : contextAnswer.getAnswers()) {
            if (answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty()) {
                Long topicId = answer.getTopicId();
                int score = getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, reverseTopics);

                if (questionMapping.containsKey(topicId)) {
                    String[] questionInfo = questionMapping.get(topicId);
                    String aspectType = questionInfo[0];
                    String aspectDesc = questionInfo[1];

                    if (score <= 1) {
                        // 低分，需要改进
                        if ("negative".equals(aspectType)) {
                            positiveAspects.add(aspectDesc + "（改善明显）");
                        } else {
                            areasForImprovement.add(aspectDesc + "（有待提升）");
                        }
                    } else if (score >= 2) {
                        // 高分，积极方面
                        if ("positive".equals(aspectType)) {
                            positiveAspects.add(aspectDesc + "（表现良好）");
                        } else {
                            areasForImprovement.add(aspectDesc + "（需要关注）");
                        }
                    }
                }
            }
        }

        satisfaction.put("积极方面", positiveAspects);
        satisfaction.put("改进方向", areasForImprovement);

        return satisfaction;
    }

    /**
     * 获取程度描述
     */
    private String getLevelDescription(String level) {
        switch (level) {
            case "非常满意":
                return "您的生活满意度非常高，对过去、现在和未来都持积极态度";
            case "一般满意":
                return "您的生活满意度处于中等水平，有良好的适应能力";
            case "不太满意":
                return "您的生活满意度较低，存在一些需要调整的方面";
            case "非常不满意":
                return "您的生活满意度很低，建议关注心理健康和生活质量";
            default:
                return "";
        }
    }

    /**
     * 获取影响程度
     */
    private String getImpactDegree(int standardScore) {
        if (standardScore >= 31) {
            return "积极影响";
        } else if (standardScore >= 21) {
            return "中度积极";
        } else if (standardScore >= 11) {
            return "轻度消极";
        } else {
            return "显著消极";
        }
    }

    /**
     * 获取建议措施列表
     */
    private List<String> getSuggestions(String level) {
        List<String> suggestions = new ArrayList<>();

        switch (level) {
            case "非常满意":
                suggestions.add("继续保持积极的生活态度和乐观心态");
                suggestions.add("培养兴趣爱好，丰富精神生活");
                suggestions.add("建立并维护良好的社会支持网络");
                suggestions.add("定期进行自我反思和成长规划");
                suggestions.add("帮助他人提升生活满意度");
                break;

            case "一般满意":
                suggestions.add("关注生活中的积极方面，培养感恩心态");
                suggestions.add("设定切实可行的短期和长期目标");
                suggestions.add("加强社交活动，建立有意义的人际关系");
                suggestions.add("培养新的兴趣爱好或学习新技能");
                suggestions.add("练习正念冥想，提高对当下的感知");
                suggestions.add("定期进行体育锻炼，保持身心健康");
                suggestions.add("考虑参加生活满意度提升工作坊");
                break;

            case "不太满意":
                suggestions.add("寻求心理咨询或生活教练的帮助");
                suggestions.add("建立每日感恩清单，记录积极事件");
                suggestions.add("设定小而具体的目标，逐步改善生活");
                suggestions.add("改善睡眠质量和饮食习惯");
                suggestions.add("增加社交互动，减少孤立感");
                suggestions.add("探索新的职业或生活方向");
                suggestions.add("参加志愿活动，增加生活意义感");
                suggestions.add("学习压力管理和情绪调节技巧");
                break;

            case "非常不满意":
                suggestions.add("立即寻求专业心理咨询或治疗");
                suggestions.add("进行全面的生活状况评估");
                suggestions.add("建立强有力的社会支持系统");
                suggestions.add("制定详细的生活改善计划");
                suggestions.add("考虑参加团体治疗或支持小组");
                suggestions.add("在专业人士指导下进行认知行为疗法");
                suggestions.add("关注基本需求的满足（睡眠、营养、运动）");
                suggestions.add("逐步建立日常生活的结构和规律");
                suggestions.add("如有需要，考虑药物辅助治疗");
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
            case "非常满意":
                interpretation.append("根据生活满意度量表(LSIA)测评结果，您的得分为")
                        .append(standardScore)
                        .append("分，处于31-40分之间，属于非常满意水平。")
                        .append("这表明您对过去的生活经历、当前的状况以及未来的展望都持非常积极的态度。")
                        .append("您有较高的生活满足感和幸福感，能够很好地适应生活的变化和挑战。")
                        .append("建议您继续保持这种积极的生活态度，并可以考虑帮助他人提升生活满意度。");
                break;
            case "一般满意":
                interpretation.append("根据生活满意度量表(LSIA)测评结果，您的得分为")
                        .append(standardScore)
                        .append("分，处于21-30分之间，属于一般满意水平。")
                        .append("这表明您对生活总体上持积极态度，但在某些方面仍有提升空间。")
                        .append("您具备良好的适应能力，能够平衡生活的不同方面。")
                        .append("建议您关注生活中的积极事件，加强社交联系，进一步提升生活满意度。");
                break;
            case "不太满意":
                interpretation.append("根据生活满意度量表(LSIA)测评结果，您的得分为")
                        .append(standardScore)
                        .append("分，处于11-20分之间，属于不太满意水平。")
                        .append("这表明您对生活的某些方面存在不满意，可能需要调整心态或改变生活方式。")
                        .append("生活中可能有一些挑战或不如意的地方影响了您的整体满意度。")
                        .append("建议您寻求适当的支持，制定改善计划，逐步提升生活质量。");
                break;
            case "非常不满意":
                interpretation.append("根据生活满意度量表(LSIA)测评结果，您的得分为")
                        .append(standardScore)
                        .append("分，处于0-10分之间，属于非常不满意水平。")
                        .append("这表明您对生活的多个方面都感到不满意，可能存在较大的心理压力或生活困扰。")
                        .append("这种情况可能会影响您的心理健康和整体幸福感。")
                        .append("强烈建议您寻求专业心理咨询或治疗，进行系统的评估和干预。");
                break;
        }

        return interpretation.toString();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(LSIAResultDTO resultDTO, LbsResultsVo lbsResults) {
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
    public String convertToJson(LSIAResultDTO resultDTO) throws Exception {
        return objectMapper.writeValueAsString(resultDTO);
    }

    /**
     * 从JSON字符串解析结果
     */
    public LSIAResultDTO parseFromJson(String json) throws Exception {
        return objectMapper.readValue(json, LSIAResultDTO.class);
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
    public Map<String, Object> calculate(ContextAnswerVo contextAnswer, LSIACalcParams calcParams, ContextVo contextVo) {
        LSIAResultDTO resultDTO = calculateResult(contextAnswer, calcParams, contextVo, null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rawTotalScore", resultDTO.getRawTotalScore());
        result.put("standardScore", resultDTO.getStandardScore());
        result.put("satisfactionLevel", resultDTO.getSatisfactionLevel());
        result.put("interpretation", resultDTO.getInterpretation());
        result.put("suggestions", resultDTO.getSuggestions());
        result.put("dimensionScores", resultDTO.getDimensionScores());
        result.put("satisfactionDetails", resultDTO.getSatisfactionDetails());
        result.put("impactDegree", resultDTO.getImpactDegree());

        return result;
    }
}