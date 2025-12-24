package com.ruoyi.cms.scale.calcdata.service.impl;

import com.ruoyi.cms.scale.calcdata.params.standard.SDSCalcParams;
import com.ruoyi.cms.scale.domain.dto.SDSResultDTO;
import com.ruoyi.cms.scale.domain.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SDSScaleCalculator {

    private static final Logger logger = LoggerFactory.getLogger(SDSScaleCalculator.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 计算SDS测评结果并返回DTO
     */
    public SDSResultDTO calculateResult(ContextAnswerVo contextAnswer, SDSCalcParams calcParams,
                                        ContextVo contextVo, LbsResultsVo lbsResults) {
        // 检查输入参数
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
            // 1. 使用ContextVo构建选项映射
            Map<Long, Integer> optionScoreMap = buildOptionScoreMap(contextVo);
            Set<Long> reverseTopics = getReverseTopics(contextVo, calcParams);

            logger.debug("SDS选项映射构建完成，选项数量: {}, 反向计分题目: {}",
                    optionScoreMap.size(), reverseTopics.size());

            // 2. 计算原始得分
            ScoreResult scoreResult = calculateRawScores(contextAnswer, calcParams, optionScoreMap, reverseTopics);

            // 3. 创建结果DTO
            SDSResultDTO resultDTO = new SDSResultDTO();

            // 4. 设置基本得分信息
            resultDTO.setRawTotalScore(scoreResult.rawTotalScore);
            resultDTO.setStandardScore(scoreResult.standardScore);
            resultDTO.setDepressionLevel(scoreResult.depressionLevel);
            resultDTO.setLevelDescription(getLevelDescription(scoreResult.depressionLevel));
            resultDTO.setInterpretation(generateInterpretation(scoreResult.depressionLevel, scoreResult.standardScore));
            resultDTO.setSuggestions(getSuggestionsString(scoreResult.depressionLevel));

            // 5. 设置维度得分
            resultDTO.setDimensionScores(calculateDimensionScores(contextAnswer, calcParams, optionScoreMap, reverseTopics));

            // 6. 设置建议列表
            resultDTO.setSuggestionList(getSuggestions(scoreResult.depressionLevel));

            // 7. 设置用户信息（如果提供了lbsResults）
            if (lbsResults != null) {
                setUserInfo(resultDTO, lbsResults);
            }

            // 8. 计算进度条宽度和主题颜色
            resultDTO.calculateProgressWidth();
            resultDTO.setThemeColorByLevel();

            logger.info("SDS计算结果: 原始总分={}, 标准分={}, 抑郁程度={}",
                    scoreResult.rawTotalScore, scoreResult.standardScore, scoreResult.depressionLevel);

            return resultDTO;

        } catch (Exception e) {
            logger.error("SDS计算过程异常: ", e);
            throw new RuntimeException("SDS计算失败: " + e.getMessage(), e);
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
                // 根据选项标题或顺序确定分值
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
     * SDS规则：A.没有或很少时间=1分，B.小部分时间=2分，C.相当多时间=3分，D.绝大部分或全部时间=4分
     */
    private int determineScoreFromOption(OptionVo option) {
        String title = option.getTitle();
        if (title == null || title.isEmpty()) {
            // 如果没有标题，按选项顺序计分
            return Integer.parseInt(option.getOrderNum());
        }

        // 根据选项标题判断分值
        if (title.contains("没有或很少时间") || title.startsWith("A.")) {
            return 1;
        } else if (title.contains("小部分时间") || title.startsWith("B.")) {
            return 2;
        } else if (title.contains("相当多时间") || title.startsWith("C.")) {
            return 3;
        } else if (title.contains("绝大部分") || title.startsWith("D.")) {
            return 4;
        }

        // 默认按选项顺序
        try {
            return Integer.parseInt(option.getOrderNum());
        } catch (NumberFormatException e) {
            logger.warn("无法确定选项分值，optionId: {}, title: {}", option.getOptionId(), title);
            return 1;
        }
    }

    /**
     * 获取反向计分题目集合
     * SDS反向计分题目：第2,5,6,11,12,14,16,17,18,20题（对应topicId 4,7,8,13,14,16,17,18,19,20）
     */
    private Set<Long> getReverseTopics(ContextVo contextVo, SDSCalcParams calcParams) {
        Set<Long> reverseTopics = new HashSet<>();

        // 优先使用calcParams中的反向计分题目配置
        if (calcParams.getDescTopic() != null && !calcParams.getDescTopic().isEmpty()) {
            for (Integer topicId : calcParams.getDescTopic()) {
                reverseTopics.add(topicId.longValue());
            }
        } else {
            // 如果没有配置，使用默认的SDS反向计分题目
            // 注意：这里根据你的JSON，topicId是从3开始的
            Long[] defaultReverseTopics = {4L, 7L, 8L, 13L, 14L, 16L, 17L, 18L, 19L, 20L};
            reverseTopics.addAll(Arrays.asList(defaultReverseTopics));
        }

        logger.debug("反向计分题目: {}", reverseTopics);
        return reverseTopics;
    }

    /**
     * 内部类：用于存储得分计算结果
     */
    private static class ScoreResult {
        int rawTotalScore;
        int standardScore;
        String depressionLevel;

        ScoreResult(int rawTotalScore, int standardScore, String depressionLevel) {
            this.rawTotalScore = rawTotalScore;
            this.standardScore = standardScore;
            this.depressionLevel = depressionLevel;
        }
    }

    /**
     * 计算原始得分
     */
    private ScoreResult calculateRawScores(ContextAnswerVo contextAnswer, SDSCalcParams calcParams,
                                           Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        double rate = calcParams.getRate();

        // 将用户答案按topicId分组
        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        logger.info("开始计算SDS原始得分，有效答案数量: {}", answerMap.size());

        // 计算原始总分
        int rawTotalScore = 0;

        for (Map.Entry<Long, AnswerVo> entry : answerMap.entrySet()) {
            Long topicId = entry.getKey();
            AnswerVo answer = entry.getValue();

            // 获取选项分值
            int optionScore = getOptionScore(answer.getOptionIds(), topicId, optionScoreMap, reverseTopics);
            rawTotalScore += optionScore;

            logger.debug("题目得分 - topicId: {}, 选项IDs: {}, 得分: {}, 累计总分: {}",
                    topicId, answer.getOptionIds(), optionScore, rawTotalScore);
        }

        // 计算标准分（原始总分×1.25，四舍五入）
        double standardScoreDouble = rawTotalScore * rate;
        int standardScore = (int) Math.round(standardScoreDouble);

        // 判断抑郁程度
        String depressionLevel = determineDepressionLevel(standardScore);

        logger.info("SDS计算完成 - 原始总分: {}, 标准分: {}, 抑郁程度: {}",
                rawTotalScore, standardScore, depressionLevel);

        return new ScoreResult(rawTotalScore, standardScore, depressionLevel);
    }

    /**
     * 获取选项分值
     */
    private int getOptionScore(String optionIds, Long topicId, Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        if (optionIds == null || optionIds.trim().isEmpty()) {
            return 0;
        }

        try {
            // 选项ID可能是多个（逗号分隔），取第一个（假设单选）
            String[] options = optionIds.split(",");
            Long optionId = Long.parseLong(options[0].trim());

            // 从映射表中获取分值
            Integer score = optionScoreMap.get(optionId);
            if (score == null) {
                logger.warn("选项ID {} 在映射表中未找到", optionId);
                return 0;
            }

            // 检查是否为反向计分题目
            boolean isReverse = reverseTopics.contains(topicId);
            int finalScore = isReverse ? (5 - score) : score;  // 反向计分：1->4, 2->3, 3->2, 4->1

            logger.debug("选项计分 - topicId: {}, optionId: {}, 原始分值: {}, 反向计分: {}, 最终分值: {}",
                    topicId, optionId, score, isReverse, finalScore);

            return finalScore;

        } catch (NumberFormatException e) {
            logger.error("选项ID格式错误: {}", optionIds, e);
            return 0;
        }
    }

    /**
     * 判断抑郁程度
     */
    private String determineDepressionLevel(int standardScore) {
        if (standardScore < 53) {
            return "无抑郁";
        } else if (standardScore <= 62) {
            return "轻度抑郁";
        } else if (standardScore <= 72) {
            return "中度抑郁";
        } else {
            return "重度抑郁";
        }
    }

    /**
     * 计算各维度得分
     */
    private Map<String, Integer> calculateDimensionScores(ContextAnswerVo contextAnswer, SDSCalcParams calcParams,
                                                          Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        Map<String, Integer> dimensionScores = new LinkedHashMap<>();

        // SDS量表的维度划分
        // 精神性-情感症状（第1,3,4,7,8,9,10题）
        int psychologicalScore = calculateDimensionScore(Arrays.asList(3L, 5L, 6L, 9L, 10L, 11L, 12L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("精神性-情感症状", psychologicalScore);

        // 躯体性障碍（第2,5,6,11,12,14,16,17,18,20题）
        int physicalScore = calculateDimensionScore(Arrays.asList(4L, 7L, 8L, 13L, 14L, 16L, 18L, 19L, 20L, 22L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("躯体性障碍", physicalScore);

        // 精神运动性障碍（第13,15,19题）
        int psychomotorScore = calculateDimensionScore(Arrays.asList(15L, 17L, 21L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("精神运动性障碍", psychomotorScore);

        // 总体抑郁指数（所有题目）
        int totalScore = calculateDimensionScore(Arrays.asList(3L,4L,5L,6L,7L,8L,9L,10L,11L,12L,13L,14L,15L,16L,17L,18L,19L,20L,21L,22L),
                contextAnswer, optionScoreMap, reverseTopics);
        dimensionScores.put("总体抑郁指数", totalScore);

        return dimensionScores;
    }

    /**
     * 计算单个维度得分
     */
    private int calculateDimensionScore(List<Long> topicIds, ContextAnswerVo contextAnswer,
                                        Map<Long, Integer> optionScoreMap, Set<Long> reverseTopics) {
        int score = 0;

        // 将用户答案按topicId分组
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
     * 获取程度描述
     */
    private String getLevelDescription(String level) {
        switch (level) {
            case "无抑郁":
                return "您的情绪状态良好，处于正常范围";
            case "轻度抑郁":
                return "您的情绪状态提示可能存在轻度抑郁倾向，建议适当关注和调适";
            case "中度抑郁":
                return "您的情绪状态提示可能存在中度抑郁症状，建议寻求专业帮助";
            case "重度抑郁":
                return "您的情绪状态提示可能存在重度抑郁症状，建议立即寻求专业帮助";
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
            case "无抑郁":
                suggestions.add("继续保持健康的生活方式，保持良好的心态");
                suggestions.add("维持良好的社交关系和家庭关系");
                suggestions.add("定期进行体育锻炼，增强身体素质");
                suggestions.add("培养兴趣爱好，丰富精神生活");
                suggestions.add("学会压力管理和情绪调节技巧");
                break;

            case "轻度抑郁":
                suggestions.add("保持规律作息，保证充足睡眠");
                suggestions.add("适当进行户外活动和体育锻炼，如散步、慢跑等");
                suggestions.add("与亲友保持良好沟通，分享内心感受");
                suggestions.add("培养积极的兴趣爱好，转移注意力");
                suggestions.add("尝试放松训练，如深呼吸、冥想等");
                suggestions.add("如症状持续2周以上，建议寻求心理咨询");
                suggestions.add("避免过度使用酒精或药物来缓解情绪");
                break;

            case "中度抑郁":
                suggestions.add("强烈建议寻求专业心理咨询或心理治疗");
                suggestions.add("在专业人士指导下进行系统的心理评估");
                suggestions.add("建立规律的生活节奏和作息时间");
                suggestions.add("避免单独长期独处，增加社交活动");
                suggestions.add("记录情绪变化日记，帮助识别情绪模式");
                suggestions.add("考虑参加心理教育团体或支持小组");
                suggestions.add("在医生指导下考虑药物治疗的可能性");
                break;

            case "重度抑郁":
                suggestions.add("立即寻求专业精神科医生的帮助");
                suggestions.add("进行全面的心理评估和诊断");
                suggestions.add("在医生指导下进行系统的心理治疗");
                suggestions.add("考虑在医生指导下使用抗抑郁药物");
                suggestions.add("建立支持系统，寻求亲友的帮助和支持");
                suggestions.add("避免做出重大生活决策，减少压力源");
                suggestions.add("如出现自杀念头，请立即联系心理危机干预热线");
                suggestions.add("定期复查，监测病情变化和治疗效果");
                break;
        }

        return suggestions;
    }

    /**
     * 获取建议字符串（用于JSON存储）
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
            case "无抑郁":
                interpretation.append("根据抑郁自评量表(SDS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，低于53分，属于正常范围。这表明您当前的情绪状态良好，没有明显的抑郁症状。")
                        .append("建议您继续保持积极的生活方式，关注心理健康，定期进行情绪自评。");
                break;
            case "轻度抑郁":
                interpretation.append("根据抑郁自评量表(SDS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，处于53-62分之间，提示存在轻度抑郁症状。")
                        .append("这可能表现为情绪低落、兴趣减退、睡眠障碍等，但程度较轻，对日常生活影响有限。")
                        .append("建议您适当调整生活节奏，保持积极心态，多与亲友交流，如症状持续可寻求专业帮助。")
                        .append("请注意，本测评结果仅供参考，不能作为临床诊断依据。");
                break;
            case "中度抑郁":
                interpretation.append("根据抑郁自评量表(SDS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，处于63-72分之间，提示存在中度抑郁症状。")
                        .append("这可能表现为明显的情绪低落、兴趣丧失、精力减退、睡眠和食欲改变等，对日常生活和工作有一定影响。")
                        .append("建议您寻求专业心理咨询或心理治疗，及时进行干预和调整。")
                        .append("专业的心理评估和治疗可以帮助您更好地应对当前的情绪困扰。");
                break;
            case "重度抑郁":
                interpretation.append("根据抑郁自评量表(SDS)测评结果，您的标准分为")
                        .append(standardScore)
                        .append("分，高于72分，提示存在重度抑郁症状。")
                        .append("这可能表现为严重的情绪低落、绝望感、自我评价过低、明显的睡眠和食欲障碍，甚至可能出现消极念头。")
                        .append("强烈建议您尽快寻求专业精神科医生的帮助，进行系统的评估和治疗。")
                        .append("抑郁症是一种可治疗的疾病，及时的专业干预非常重要。");
                break;
        }

        return interpretation.toString();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(SDSResultDTO resultDTO, LbsResultsVo lbsResults) {
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
    public String convertToJson(SDSResultDTO resultDTO) throws Exception {
        return objectMapper.writeValueAsString(resultDTO);
    }

    /**
     * 从JSON字符串解析结果
     */
    public SDSResultDTO parseFromJson(String json) throws Exception {
        return objectMapper.readValue(json, SDSResultDTO.class);
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

        // 检查是否所有题目都有答案
        long validAnswers = answers.stream()
                .filter(a -> a.getOptionIds() != null && !a.getOptionIds().trim().isEmpty())
                .count();

        // SDS应有20题，至少完成80%（16题）才认为有效
        return validAnswers >= 16;
    }

    /**
     * 兼容性方法：原有的calculate方法（返回Map）
     */
    public Map<String, Object> calculate(ContextAnswerVo contextAnswer, SDSCalcParams calcParams, ContextVo contextVo) {
        SDSResultDTO resultDTO = calculateResult(contextAnswer, calcParams, contextVo, null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rawTotalScore", resultDTO.getRawTotalScore());
        result.put("standardScore", resultDTO.getStandardScore());
        result.put("depressionLevel", resultDTO.getDepressionLevel());
        result.put("interpretation", resultDTO.getInterpretation());
        result.put("suggestions", resultDTO.getSuggestions());
        result.put("dimensionScores", resultDTO.getDimensionScores());

        return result;
    }
}