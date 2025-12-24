package com.ruoyi.cms.scale.calcdata.service.impl;

import com.ruoyi.cms.scale.calcdata.params.family.ASLECCalcParams;
import com.ruoyi.cms.scale.domain.dto.ASLECResultDTO;
import com.ruoyi.cms.scale.domain.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ASLECScaleCalculator {

    private static final Logger logger = LoggerFactory.getLogger(ASLECScaleCalculator.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 计算ASLEC测评结果并返回DTO
     */
    public ASLECResultDTO calculateResult(ContextAnswerVo contextAnswer, ASLECCalcParams calcParams,
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

            logger.debug("ASLEC选项映射构建完成，选项数量: {}", optionScoreMap.size());

            // 2. 计算原始得分
            ScoreResult scoreResult = calculateRawScores(contextAnswer, optionScoreMap);

            // 3. 创建结果DTO
            ASLECResultDTO resultDTO = new ASLECResultDTO();

            // 4. 设置基本得分信息
            resultDTO.setRawTotalScore(scoreResult.rawTotalScore);
            resultDTO.setStressLevel(scoreResult.stressLevel);
            resultDTO.setLevelDescription(getLevelDescription(scoreResult.stressLevel));
            resultDTO.setInterpretation(generateInterpretation(scoreResult.stressLevel, scoreResult.rawTotalScore));
            resultDTO.setSuggestions(getSuggestionsString(scoreResult.stressLevel));

            // 5. 设置维度得分
            resultDTO.setDimensionScores(calculateDimensionScores(contextAnswer, optionScoreMap, calcParams, contextVo));

            // 6. 设置建议列表
            resultDTO.setSuggestionList(getSuggestions(scoreResult.stressLevel));

            // 7. 设置高压力事件详情 - 修正：传入contextVo参数
            resultDTO.setHighStressEvents(analyzeHighStressEvents(contextAnswer, optionScoreMap, calcParams, contextVo));

            // 8. 设置影响程度
            resultDTO.setImpactDegree(getImpactDegree(scoreResult.stressLevel));

            // 9. 设置用户信息
            if (lbsResults != null) {
                setUserInfo(resultDTO, lbsResults);
            }

            // 10. 计算进度条宽度和主题颜色
            resultDTO.calculateProgressWidth();
            resultDTO.setThemeColorByLevel();

            logger.info("ASLEC计算结果: 原始总分={}, 压力水平={}",
                    scoreResult.rawTotalScore, scoreResult.stressLevel);

            return resultDTO;

        } catch (Exception e) {
            logger.error("ASLEC计算过程异常: ", e);
            throw new RuntimeException("ASLEC计算失败: " + e.getMessage(), e);
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
     */
    private int determineScoreFromOption(OptionVo option) {
        String title = option.getTitle();
        if (title == null || title.isEmpty()) {
            return 0;
        }

        // ASLEC计分规则
        if (title.contains("未发生过") || title.startsWith("A.")) {
            return 0;
        } else if (title.contains("影响没有") || title.startsWith("B.")) {
            return 1;
        } else if (title.contains("影响轻度") || title.startsWith("C.")) {
            return 2;
        } else if (title.contains("影响中度") || title.startsWith("D.")) {
            return 3;
        } else if (title.contains("影响重度") || title.startsWith("E.")) {
            return 4;
        } else if (title.contains("影响极重") || title.startsWith("F.")) {
            return 5;
        }

        try {
            return Integer.parseInt(option.getOrderNum()) - 1;
        } catch (NumberFormatException e) {
            logger.warn("无法确定选项分值，optionId: {}, title: {}", option.getOptionId(), title);
            return 0;
        }
    }

    /**
     * 内部类：用于存储得分计算结果
     */
    private static class ScoreResult {
        int rawTotalScore;
        String stressLevel;

        ScoreResult(int rawTotalScore, String stressLevel) {
            this.rawTotalScore = rawTotalScore;
            this.stressLevel = stressLevel;
        }
    }

    /**
     * 计算原始得分
     */
    private ScoreResult calculateRawScores(ContextAnswerVo contextAnswer,
                                           Map<Long, Integer> optionScoreMap) {
        // 将用户答案按topicId分组
        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        logger.info("开始计算ASLEC原始得分，有效答案数量: {}", answerMap.size());

        // 计算原始总分
        int rawTotalScore = 0;

        for (Map.Entry<Long, AnswerVo> entry : answerMap.entrySet()) {
            Long topicId = entry.getKey();
            AnswerVo answer = entry.getValue();

            int optionScore = getOptionScore(answer.getOptionIds(), optionScoreMap);
            rawTotalScore += optionScore;

            logger.debug("题目得分 - topicId: {}, 选项IDs: {}, 得分: {}, 累计总分: {}",
                    topicId, answer.getOptionIds(), optionScore, rawTotalScore);
        }

        // 判断压力水平
        String stressLevel = determineStressLevel(rawTotalScore);

        logger.info("ASLEC计算完成 - 原始总分: {}, 压力水平: {}", rawTotalScore, stressLevel);

        return new ScoreResult(rawTotalScore, stressLevel);
    }

    /**
     * 获取选项分值
     */
    private int getOptionScore(String optionIds, Map<Long, Integer> optionScoreMap) {
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

            return score;

        } catch (NumberFormatException e) {
            logger.error("选项ID格式错误: {}", optionIds, e);
            return 0;
        }
    }

    /**
     * 判断压力水平
     */
    private String determineStressLevel(int rawTotalScore) {
        if (rawTotalScore <= 10) {
            return "无压力";
        } else if (rawTotalScore <= 25) {
            return "轻度压力";
        } else if (rawTotalScore <= 45) {
            return "中度压力";
        } else if (rawTotalScore <= 80) {
            return "重度压力";
        } else {
            return "极重度压力";
        }
    }

    /**
     * 计算各维度得分 - 修正：添加contextVo参数
     */
    private Map<String, Integer> calculateDimensionScores(ContextAnswerVo contextAnswer,
                                                          Map<Long, Integer> optionScoreMap,
                                                          ASLECCalcParams calcParams,
                                                          ContextVo contextVo) {
        Map<String, Integer> dimensionScores = new LinkedHashMap<>();

        // 默认维度划分
        Map<String, List<Long>> dimensionMappings = getDefaultDimensionMappings();

        // 如果参数中有自定义维度，则使用自定义维度
        updateDimensionMappings(dimensionMappings, calcParams);

        // 计算每个维度得分
        for (Map.Entry<String, List<Long>> entry : dimensionMappings.entrySet()) {
            int dimensionScore = calculateDimensionScore(entry.getValue(), contextAnswer, optionScoreMap);
            dimensionScores.put(entry.getKey(), dimensionScore);
        }

        // 添加总分
        dimensionScores.put("总分", calculateDimensionScore(getAllTopicIds(dimensionMappings),
                contextAnswer, optionScoreMap));

        return dimensionScores;
    }

    /**
     * 更新维度映射
     */
    private void updateDimensionMappings(Map<String, List<Long>> dimensionMappings, ASLECCalcParams calcParams) {
        if (calcParams.getInterpersonalDimension() != null && !calcParams.getInterpersonalDimension().isEmpty()) {
            dimensionMappings.put("人际关系", calcParams.getInterpersonalDimension().stream()
                    .map(Long::longValue).collect(Collectors.toList()));
        }
        if (calcParams.getLearningPressureDimension() != null && !calcParams.getLearningPressureDimension().isEmpty()) {
            dimensionMappings.put("学习压力", calcParams.getLearningPressureDimension().stream()
                    .map(Long::longValue).collect(Collectors.toList()));
        }
        if (calcParams.getPunishmentDimension() != null && !calcParams.getPunishmentDimension().isEmpty()) {
            dimensionMappings.put("受惩罚", calcParams.getPunishmentDimension().stream()
                    .map(Long::longValue).collect(Collectors.toList()));
        }
        if (calcParams.getLossDimension() != null && !calcParams.getLossDimension().isEmpty()) {
            dimensionMappings.put("丧失", calcParams.getLossDimension().stream()
                    .map(Long::longValue).collect(Collectors.toList()));
        }
        if (calcParams.getHealthAdaptationDimension() != null && !calcParams.getHealthAdaptationDimension().isEmpty()) {
            dimensionMappings.put("健康适应", calcParams.getHealthAdaptationDimension().stream()
                    .map(Long::longValue).collect(Collectors.toList()));
        }
        if (calcParams.getOtherDimension() != null && !calcParams.getOtherDimension().isEmpty()) {
            dimensionMappings.put("其他", calcParams.getOtherDimension().stream()
                    .map(Long::longValue).collect(Collectors.toList()));
        }
    }

    /**
     * 获取默认维度映射
     */
    private Map<String, List<Long>> getDefaultDimensionMappings() {
        Map<String, List<Long>> mappings = new LinkedHashMap<>();

        mappings.put("人际关系", Arrays.asList(139L, 140L, 142L, 146L, 150L, 151L, 152L, 153L, 155L, 156L, 157L, 162L));
        mappings.put("学习压力", Arrays.asList(141L, 144L, 147L, 148L, 160L, 163L));
        mappings.put("受惩罚", Arrays.asList(154L, 157L, 159L, 161L, 162L));
        mappings.put("丧失", Arrays.asList(149L, 150L, 151L));
        mappings.put("健康适应", Arrays.asList(143L, 158L));
        mappings.put("其他", Arrays.asList(145L, 152L, 164L, 165L));

        return mappings;
    }

    /**
     * 获取所有题目ID
     */
    private List<Long> getAllTopicIds(Map<String, List<Long>> dimensionMappings) {
        List<Long> allIds = new ArrayList<>();
        for (List<Long> ids : dimensionMappings.values()) {
            allIds.addAll(ids);
        }
        return allIds.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 计算单个维度得分
     */
    private int calculateDimensionScore(List<Long> topicIds, ContextAnswerVo contextAnswer,
                                        Map<Long, Integer> optionScoreMap) {
        int score = 0;

        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        for (Long topicId : topicIds) {
            AnswerVo answer = answerMap.get(topicId);
            if (answer != null && answer.getOptionIds() != null) {
                score += getOptionScore(answer.getOptionIds(), optionScoreMap);
            }
        }

        return score;
    }

    /**
     * 分析高压力事件 - 修正：添加contextVo参数
     */
    private Map<String, List<String>> analyzeHighStressEvents(ContextAnswerVo contextAnswer,
                                                              Map<Long, Integer> optionScoreMap,
                                                              ASLECCalcParams calcParams,
                                                              ContextVo contextVo) {
        Map<String, List<String>> highStressEvents = new LinkedHashMap<>();

        // 获取题目名称映射 - 使用传入的contextVo参数
        Map<Long, String> topicNameMap = getTopicNameMap(contextVo);

        // 检查每题的得分，如果得分≥4分（重度或极重影响），则视为高压力事件
        List<String> highStressList = new ArrayList<>();

        for (AnswerVo answer : contextAnswer.getAnswers()) {
            if (answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty()) {
                Long topicId = answer.getTopicId();
                int score = getOptionScore(answer.getOptionIds(), optionScoreMap);

                if (score >= 4) {
                    String topicName = topicNameMap.get(topicId);
                    if (topicName != null) {
                        String level = score == 4 ? "重度影响" : "极重影响";
                        highStressList.add(topicName + "（" + level + "）");
                    }
                }
            }
        }

        highStressEvents.put("高压力事件", highStressList);

        return highStressEvents;
    }

    /**
     * 获取题目名称映射 - 修正方法
     */
    private Map<Long, String> getTopicNameMap(ContextVo contextVo) {
        Map<Long, String> topicNameMap = new HashMap<>();
        if (contextVo != null && contextVo.getTopics() != null) {
            for (TopicVo topic : contextVo.getTopics()) {
                try {
                    Long topicId = Long.parseLong(topic.getTopicId());
                    topicNameMap.put(topicId, topic.getTitle());
                } catch (NumberFormatException e) {
                    logger.warn("题目ID格式错误: {}", topic.getTopicId());
                }
            }
        }
        return topicNameMap;
    }

    /**
     * 获取程度描述
     */
    private String getLevelDescription(String level) {
        switch (level) {
            case "无压力":
                return "您近期遇到的生活事件压力在正常范围内";
            case "轻度压力":
                return "您近期遇到的生活事件有轻度压力，需要适当关注";
            case "中度压力":
                return "您近期遇到的生活事件有中度压力，建议进行心理调适";
            case "重度压力":
                return "您近期遇到的生活事件有重度压力，建议寻求专业帮助";
            case "极重度压力":
                return "您近期遇到的生活事件有极重度压力，建议立即寻求专业心理支持";
            default:
                return "";
        }
    }

    /**
     * 获取影响程度
     */
    private String getImpactDegree(String stressLevel) {
        switch (stressLevel) {
            case "无压力":
                return "无影响";
            case "轻度压力":
                return "轻度影响";
            case "中度压力":
                return "中度影响";
            case "重度压力":
                return "重度影响";
            case "极重度压力":
                return "极重影响";
            default:
                return "";
        }
    }

    /**
     * 获取建议措施列表
     */
    private List<String> getSuggestions(String level) {
        List<String> suggestions = new ArrayList<>();

        switch (level) {
            case "无压力":
                suggestions.add("继续保持良好的心态和应对方式");
                suggestions.add("学习压力管理技巧，为未来可能遇到的压力做准备");
                suggestions.add("建立良好的社会支持系统");
                suggestions.add("保持规律的生活作息");
                suggestions.add("培养积极乐观的心态");
                break;

            case "轻度压力":
                suggestions.add("学习放松技巧，如深呼吸、渐进性肌肉放松");
                suggestions.add("适当进行体育锻炼，释放压力");
                suggestions.add("与朋友、家人分享感受，寻求支持");
                suggestions.add("合理安排时间，避免过度疲劳");
                suggestions.add("培养兴趣爱好，转移注意力");
                suggestions.add("学习问题解决技巧，应对具体生活事件");
                suggestions.add("如压力持续，可寻求心理咨询");
                break;

            case "中度压力":
                suggestions.add("建议寻求心理咨询或辅导");
                suggestions.add("学习认知行为疗法技巧，改变负面思维");
                suggestions.add("建立规律的生活作息时间表");
                suggestions.add("练习正念冥想，提高当下意识");
                suggestions.add("参加压力管理团体或工作坊");
                suggestions.add("学习情绪调节技巧");
                suggestions.add("与信任的人倾诉，寻求情感支持");
                suggestions.add("避免重大决策，待情绪稳定后再做决定");
                break;

            case "重度压力":
            case "极重度压力":
                suggestions.add("立即寻求专业心理帮助");
                suggestions.add("进行全面的心理评估和诊断");
                suggestions.add("在专业指导下进行心理治疗");
                suggestions.add("建立强有力的社会支持系统");
                suggestions.add("学习危机应对策略");
                suggestions.add("必要时在医生指导下考虑药物治疗");
                suggestions.add("避免独处，保持与家人朋友的联系");
                suggestions.add("定期复查，监测心理状态");
                suggestions.add("学习自我关怀技巧，善待自己");
                suggestions.add("建立安全计划，应对紧急情况");
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
    private String generateInterpretation(String level, int rawTotalScore) {
        StringBuilder interpretation = new StringBuilder();

        switch (level) {
            case "无压力":
                interpretation.append("根据青少年生活事件量表(ASLEC)测评结果，您的总分为")
                        .append(rawTotalScore)
                        .append("分，处于0-10分之间，属于正常压力范围。")
                        .append("这表明您近期遇到的生活事件对您造成的心理压力在正常范围内，您具备良好的应对能力。")
                        .append("建议您继续保持积极的心态和健康的应对方式。");
                break;
            case "轻度压力":
                interpretation.append("根据青少年生活事件量表(ASLEC)测评结果，您的总分为")
                        .append(rawTotalScore)
                        .append("分，处于11-25分之间，提示存在轻度压力。")
                        .append("这表明近期发生的一些生活事件对您造成了一定程度的心理影响，但程度较轻。")
                        .append("建议您关注自己的情绪变化，学习适当的压力管理技巧，如症状持续可寻求专业帮助。")
                        .append("请注意，本测评结果仅供参考，不能作为临床诊断依据。");
                break;
            case "中度压力":
                interpretation.append("根据青少年生活事件量表(ASLEC)测评结果，您的总分为")
                        .append(rawTotalScore)
                        .append("分，处于26-45分之间，提示存在中度压力。")
                        .append("这表明近期发生的生活事件对您造成了显著的心理影响，可能影响到您的情绪状态和生活质量。")
                        .append("建议您及时寻求心理咨询或辅导，学习有效的应对策略。")
                        .append("及时干预将有助于您更好地应对当前状况。");
                break;
            case "重度压力":
                interpretation.append("根据青少年生活事件量表(ASLEC)测评结果，您的总分为")
                        .append(rawTotalScore)
                        .append("分，处于46-80分之间，提示存在重度压力。")
                        .append("这表明近期发生的生活事件对您造成了严重的心理影响，可能需要专业干预。")
                        .append("建议您尽快寻求专业心理帮助，进行系统的评估和干预。")
                        .append("专业的心理支持将有助于您缓解压力，恢复心理健康。");
                break;
            case "极重度压力":
                interpretation.append("根据青少年生活事件量表(ASLEC)测评结果，您的总分为")
                        .append(rawTotalScore)
                        .append("分，高于80分，提示存在极重度压力。")
                        .append("这表明近期发生的生活事件对您造成了极其严重的心理影响，强烈建议您立即寻求专业心理帮助。")
                        .append("重度压力需要专业干预，及时治疗可以有效缓解症状，提高生活质量。");
                break;
        }

        return interpretation.toString();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(ASLECResultDTO resultDTO, LbsResultsVo lbsResults) {
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
    public String convertToJson(ASLECResultDTO resultDTO) throws Exception {
        return objectMapper.writeValueAsString(resultDTO);
    }

    /**
     * 从JSON字符串解析结果
     */
    public ASLECResultDTO parseFromJson(String json) throws Exception {
        return objectMapper.readValue(json, ASLECResultDTO.class);
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

        return validAnswers >= 20;
    }

    /**
     * 兼容性方法：原有的calculate方法（返回Map）
     */
    public Map<String, Object> calculate(ContextAnswerVo contextAnswer, ASLECCalcParams calcParams, ContextVo contextVo) {
        ASLECResultDTO resultDTO = calculateResult(contextAnswer, calcParams, contextVo, null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rawTotalScore", resultDTO.getRawTotalScore());
        result.put("stressLevel", resultDTO.getStressLevel());
        result.put("interpretation", resultDTO.getInterpretation());
        result.put("suggestions", resultDTO.getSuggestions());
        result.put("dimensionScores", resultDTO.getDimensionScores());
        result.put("highStressEvents", resultDTO.getHighStressEvents());
        result.put("impactDegree", resultDTO.getImpactDegree());

        return result;
    }
}