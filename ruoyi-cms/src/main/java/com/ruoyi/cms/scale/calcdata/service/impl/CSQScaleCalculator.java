package com.ruoyi.cms.scale.calcdata.service.impl;

import com.ruoyi.cms.scale.calcdata.params.family.CSQCalcParams;
import com.ruoyi.cms.scale.domain.dto.CSQResultDTO;
import com.ruoyi.cms.scale.domain.vo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CSQScaleCalculator {

    private static final Logger logger = LoggerFactory.getLogger(CSQScaleCalculator.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 计算CSQ测评结果并返回DTO
     */
    public CSQResultDTO calculateResult(ContextAnswerVo contextAnswer, CSQCalcParams calcParams,
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
            Map<Long, Double> optionScoreMap = buildOptionScoreMap(contextVo);

            logger.debug("CSQ选项映射构建完成，选项数量: {}", optionScoreMap.size());

            // 2. 计算各因子得分
            FactorScores factorScores = calculateFactorScores(contextAnswer, calcParams, optionScoreMap);

            // 3. 创建结果DTO
            CSQResultDTO resultDTO = new CSQResultDTO();

            // 4. 设置基本得分信息
            resultDTO.setRawTotalScore(factorScores.totalScore);
            resultDTO.setFactorScores(factorScores.factorScores);
            resultDTO.setFactorRatios(factorScores.factorRatios);
            resultDTO.setCopingStyle(factorScores.mainCopingStyle);
            resultDTO.setStyleDescription(getStyleDescription(factorScores.mainCopingStyle));
            resultDTO.setInterpretation(generateInterpretation(factorScores));
            resultDTO.setSuggestions(getSuggestionsString(factorScores.mainCopingStyle));

            // 5. 设置应对方式详情
            resultDTO.setStyleDetails(analyzeCopingStyles(factorScores));

            // 6. 设置建议列表
            resultDTO.setSuggestionList(getSuggestions(factorScores.mainCopingStyle));

            // 7. 设置应对方式倾向
            resultDTO.setCopingTendency(getCopingTendency(factorScores));

            // 8. 设置因子解释
            resultDTO.setFactorInterpretations(getFactorInterpretations(factorScores.factorScores));

            // 9. 设置用户信息
            if (lbsResults != null) {
                setUserInfo(resultDTO, lbsResults);
            }

            // 10. 计算进度条宽度和主题颜色
            resultDTO.calculateProgressWidth();
            resultDTO.setThemeColorByCopingStyle();

            logger.info("CSQ计算结果: 原始总分={}, 主要应对方式={}",
                    factorScores.totalScore, factorScores.mainCopingStyle);

            return resultDTO;

        } catch (Exception e) {
            logger.error("CSQ计算过程异常: ", e);
            throw new RuntimeException("CSQ计算失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据ContextVo构建选项ID到分值的映射
     * CSQ计分规则：A.是=1分，B.否=0分，C.有效=1分，D.比较有效=0.5分，E.无效=0分
     */
    private Map<Long, Double> buildOptionScoreMap(ContextVo contextVo) {
        Map<Long, Double> optionScoreMap = new HashMap<>();

        if (contextVo.getTopics() == null) {
            return optionScoreMap;
        }

        for (TopicVo topic : contextVo.getTopics()) {
            if (topic.getOptions() == null) {
                continue;
            }

            for (OptionVo option : topic.getOptions()) {
                double score = determineScoreFromOption(option);
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
    private double determineScoreFromOption(OptionVo option) {
        String title = option.getTitle();
        if (title == null || title.isEmpty()) {
            return 0;
        }

        if (title.contains("A.是") || title.startsWith("A.")) {
            return 1.0;
        } else if (title.contains("B.否") || title.startsWith("B.")) {
            return 0.0;
        } else if (title.contains("C.有效") || title.startsWith("C.")) {
            return 1.0;
        } else if (title.contains("D.比较有效") || title.startsWith("D.")) {
            return 0.5;
        } else if (title.contains("E.无效") || title.startsWith("E.")) {
            return 0.0;
        }

        return 0.0;
    }

    /**
     * 内部类：用于存储因子得分计算结果
     */
    private static class FactorScores {
        int totalScore;
        Map<String, Integer> factorScores;
        Map<String, Double> factorRatios;
        String mainCopingStyle;

        FactorScores(int totalScore, Map<String, Integer> factorScores,
                     Map<String, Double> factorRatios, String mainCopingStyle) {
            this.totalScore = totalScore;
            this.factorScores = factorScores;
            this.factorRatios = factorRatios;
            this.mainCopingStyle = mainCopingStyle;
        }
    }

    /**
     * 计算各因子得分
     */
    private FactorScores calculateFactorScores(ContextAnswerVo contextAnswer, CSQCalcParams calcParams,
                                               Map<Long, Double> optionScoreMap) {
        // 定义CSQ的6个因子（题目划分可根据实际调整）
        Map<String, List<Integer>> factorMapping = new LinkedHashMap<>();
        factorMapping.put("解决问题", Arrays.asList(1, 2, 3, 5, 8, 9, 11, 14, 15, 17, 20, 21, 22, 24, 25, 28, 29, 31, 33, 34, 35, 36));
        factorMapping.put("自责", Arrays.asList(4, 12, 13, 16, 18, 19, 23, 26, 27, 30, 32, 37));
        factorMapping.put("求助", Arrays.asList(38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49));
        factorMapping.put("幻想", Arrays.asList(50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61));
        factorMapping.put("退避", Arrays.asList(6, 7, 10, 62));
        factorMapping.put("合理化", Arrays.asList(64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75));

        // 将用户答案按topicId分组
        Map<Long, AnswerVo> answerMap = contextAnswer.getAnswers().stream()
                .filter(answer -> answer.getOptionIds() != null && !answer.getOptionIds().trim().isEmpty())
                .collect(Collectors.toMap(AnswerVo::getTopicId, answer -> answer));

        logger.info("开始计算CSQ因子得分，有效答案数量: {}", answerMap.size());

        // 计算各因子得分
        Map<String, Integer> factorScores = new LinkedHashMap<>();
        int totalScore = 0;

        for (Map.Entry<String, List<Integer>> factorEntry : factorMapping.entrySet()) {
            String factorName = factorEntry.getKey();
            List<Integer> topicNumbers = factorEntry.getValue();

            int factorScore = 0;

            for (Integer topicNumber : topicNumbers) {
                // 根据题目序号找到对应的topicId（假设topicId与顺序对应）
                Long topicId = findTopicIdByNumber(topicNumber, contextAnswer.getAnswers());
                if (topicId != null) {
                    AnswerVo answer = answerMap.get(topicId);
                    if (answer != null) {
                        factorScore += getOptionScore(answer.getOptionIds(), optionScoreMap);
                    }
                }
            }

            factorScores.put(factorName, factorScore);
            totalScore += factorScore;

            logger.debug("因子得分 - {}: {}分", factorName, factorScore);
        }

        // 计算各因子比例
        Map<String, Double> factorRatios = calculateFactorRatios(factorScores, totalScore);

        // 确定主要应对方式
        String mainCopingStyle = determineMainCopingStyle(factorScores);

        return new FactorScores(totalScore, factorScores, factorRatios, mainCopingStyle);
    }

    /**
     * 根据题目序号找到topicId
     */
    private Long findTopicIdByNumber(int topicNumber, List<AnswerVo> answers) {
        // 这里需要根据实际的题目ID映射关系来调整
        // 假设题目ID从77开始连续增加
        return 77L + topicNumber - 1;
    }

    /**
     * 获取选项分值
     */
    private int getOptionScore(String optionIds, Map<Long, Double> optionScoreMap) {
        if (optionIds == null || optionIds.trim().isEmpty()) {
            return 0;
        }

        try {
            String[] options = optionIds.split(",");
            Long optionId = Long.parseLong(options[0].trim());

            Double score = optionScoreMap.get(optionId);
            if (score == null) {
                logger.warn("选项ID {} 在映射表中未找到", optionId);
                return 0;
            }

            // CSQ得分需要取整数部分
            return (int) Math.round(score);

        } catch (NumberFormatException e) {
            logger.error("选项ID格式错误: {}", optionIds, e);
            return 0;
        }
    }

    /**
     * 计算各因子比例
     */
    private Map<String, Double> calculateFactorRatios(Map<String, Integer> factorScores, int totalScore) {
        Map<String, Double> ratios = new LinkedHashMap<>();

        if (totalScore > 0) {
            for (Map.Entry<String, Integer> entry : factorScores.entrySet()) {
                double ratio = (double) entry.getValue() / totalScore * 100;
                ratios.put(entry.getKey(), Math.round(ratio * 10) / 10.0); // 保留一位小数
            }
        }

        return ratios;
    }

    /**
     * 确定主要应对方式
     */
    private String determineMainCopingStyle(Map<String, Integer> factorScores) {
        // 找出得分最高的因子
        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> entry : factorScores.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }

        if (maxEntry == null) {
            return "未知";
        }

        String mainFactor = maxEntry.getKey();

        // 根据主要因子判断应对方式类型
        if (mainFactor.equals("解决问题") || mainFactor.equals("求助")) {
            return "成熟型";
        } else if (mainFactor.equals("自责") || mainFactor.equals("幻想") || mainFactor.equals("退避")) {
            return "不成熟型";
        } else if (mainFactor.equals("合理化")) {
            return "混合型";
        } else {
            return "混合型";
        }
    }

    /**
     * 分析应对方式详情
     */
    private Map<String, List<String>> analyzeCopingStyles(FactorScores factorScores) {
        Map<String, List<String>> styleDetails = new LinkedHashMap<>();

        List<String> strongFactors = new ArrayList<>();
        List<String> moderateFactors = new ArrayList<>();
        List<String> weakFactors = new ArrayList<>();

        // 根据得分判断各因子的强度（得分≥6分为强，3-5分为中等，≤2分为弱）
        for (Map.Entry<String, Integer> entry : factorScores.factorScores.entrySet()) {
            int score = entry.getValue();
            if (score >= 6) {
                strongFactors.add(entry.getKey() + "（" + score + "分）");
            } else if (score >= 3) {
                moderateFactors.add(entry.getKey() + "（" + score + "分）");
            } else {
                weakFactors.add(entry.getKey() + "（" + score + "分）");
            }
        }

        styleDetails.put("强势应对方式", strongFactors);
        styleDetails.put("中等应对方式", moderateFactors);
        styleDetails.put("弱势应对方式", weakFactors);

        return styleDetails;
    }

    /**
     * 获取应对方式倾向
     */
    private String getCopingTendency(FactorScores factorScores) {
        int matureScore = factorScores.factorScores.getOrDefault("解决问题", 0) +
                factorScores.factorScores.getOrDefault("求助", 0);
        int immatureScore = factorScores.factorScores.getOrDefault("自责", 0) +
                factorScores.factorScores.getOrDefault("幻想", 0) +
                factorScores.factorScores.getOrDefault("退避", 0);
        int mixedScore = factorScores.factorScores.getOrDefault("合理化", 0);

        if (matureScore > immatureScore && matureScore > mixedScore) {
            return "成熟应对倾向";
        } else if (immatureScore > matureScore && immatureScore > mixedScore) {
            return "不成熟应对倾向";
        } else {
            return "混合应对倾向";
        }
    }

    /**
     * 获取因子解释
     */
    private Map<String, String> getFactorInterpretations(Map<String, Integer> factorScores) {
        Map<String, String> interpretations = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : factorScores.entrySet()) {
            String factor = entry.getKey();
            int score = entry.getValue();

            String interpretation = "";
            switch (factor) {
                case "解决问题":
                    if (score >= 8) interpretation = "善于主动解决问题，应对能力强";
                    else if (score >= 4) interpretation = "具有一定的解决问题能力";
                    else interpretation = "解决问题能力有待提高";
                    break;
                case "自责":
                    if (score >= 8) interpretation = "倾向于自我批评，可能产生负面情绪";
                    else if (score >= 4) interpretation = "适度自我反思";
                    else interpretation = "较少自责倾向";
                    break;
                case "求助":
                    if (score >= 8) interpretation = "善于寻求社会支持";
                    else if (score >= 4) interpretation = "适度寻求帮助";
                    else interpretation = "较少主动求助";
                    break;
                case "幻想":
                    if (score >= 8) interpretation = "较多使用幻想来逃避现实";
                    else if (score >= 4) interpretation = "适度使用幻想应对";
                    else interpretation = "较少使用幻想";
                    break;
                case "退避":
                    if (score >= 8) interpretation = "倾向于回避问题";
                    else if (score >= 4) interpretation = "适度回避";
                    else interpretation = "较少回避问题";
                    break;
                case "合理化":
                    if (score >= 8) interpretation = "较多使用合理化防御机制";
                    else if (score >= 4) interpretation = "适度使用合理化";
                    else interpretation = "较少使用合理化";
                    break;
            }

            interpretations.put(factor, interpretation);
        }

        return interpretations;
    }

    /**
     * 获取应对方式描述
     */
    private String getStyleDescription(String copingStyle) {
        switch (copingStyle) {
            case "成熟型":
                return "您主要采用成熟的问题解决和求助方式应对压力，这是积极的应对策略";
            case "混合型":
                return "您的应对方式较为复杂，结合了成熟和不成熟的方式";
            case "不成熟型":
                return "您倾向于使用自责、幻想或退避等不成熟的应对方式";
            case "不适当型":
                return "您的应对方式可能不太适应当前的压力情境";
            default:
                return "您的应对方式需要进一步评估";
        }
    }

    /**
     * 获取建议措施列表
     */
    private List<String> getSuggestions(String copingStyle) {
        List<String> suggestions = new ArrayList<>();

        switch (copingStyle) {
            case "成熟型":
                suggestions.add("继续保持积极的问题解决态度");
                suggestions.add("继续维持良好的人际支持网络");
                suggestions.add("学习更多元化的压力管理技巧");
                suggestions.add("帮助他人提高应对能力");
                suggestions.add("定期进行压力自我评估");
                break;

            case "混合型":
                suggestions.add("强化解决问题和求助的成熟应对方式");
                suggestions.add("减少自责和幻想等不成熟应对方式的使用");
                suggestions.add("学习认知重构技巧，改变不合理信念");
                suggestions.add("培养情绪调节能力");
                suggestions.add("寻求专业心理咨询指导");
                suggestions.add("建立更有效的问题解决策略");
                break;

            case "不成熟型":
                suggestions.add("学习问题解决技巧和策略");
                suggestions.add("建立积极的求助模式");
                suggestions.add("减少自责和自我批评");
                suggestions.add("学习现实检验技巧，减少幻想");
                suggestions.add("建立积极的社会支持系统");
                suggestions.add("寻求专业心理咨询帮助");
                suggestions.add("学习压力管理和情绪调节技巧");
                suggestions.add("培养积极应对压力的信心");
                break;

            case "不适当型":
                suggestions.add("立即寻求专业心理咨询评估");
                suggestions.add("学习适合的压力应对策略");
                suggestions.add("建立社会支持系统");
                suggestions.add("进行系统的心理调适训练");
                suggestions.add("培养自我觉察能力");
                suggestions.add("学习情绪管理和压力应对技巧");
                suggestions.add("定期进行心理状态评估");
                suggestions.add("考虑参加压力管理团体辅导");
                break;
        }

        return suggestions;
    }

    /**
     * 获取建议字符串
     */
    private String getSuggestionsString(String copingStyle) {
        List<String> suggestions = getSuggestions(copingStyle);
        return String.join("；", suggestions);
    }

    /**
     * 生成测评解读
     */
    private String generateInterpretation(FactorScores factorScores) {
        StringBuilder interpretation = new StringBuilder();

        interpretation.append("根据应付方式问卷(CSQ)测评结果，您的总分是")
                .append(factorScores.totalScore)
                .append("分，主要应对方式为")
                .append(factorScores.mainCopingStyle)
                .append("。");

        // 添加各因子得分情况
        interpretation.append("具体来说，");
        for (Map.Entry<String, Integer> entry : factorScores.factorScores.entrySet()) {
            interpretation.append(entry.getKey())
                    .append("得分")
                    .append(entry.getValue())
                    .append("分");

            double ratio = factorScores.factorRatios.get(entry.getKey());
            interpretation.append("（占比")
                    .append(String.format("%.1f", ratio))
                    .append("%），");
        }

        interpretation.append("这反映了您的压力应对模式特点。");

        // 根据主要应对方式添加针对性解读
        switch (factorScores.mainCopingStyle) {
            case "成熟型":
                interpretation.append("您倾向于使用成熟的应对策略，这有助于有效处理压力情境，促进问题解决和适应能力的发展。");
                break;
            case "混合型":
                interpretation.append("您的应对方式较为复杂，既有成熟的一面，也有需要改进的地方，建议有意识地强化积极应对方式。");
                break;
            case "不成熟型":
                interpretation.append("您较多使用不成熟的应对方式，这可能增加心理压力，影响问题解决效率，建议学习更有效的应对策略。");
                break;
        }

        interpretation.append("本测评结果仅供参考，如需深入评估，请咨询专业心理咨询师。");

        return interpretation.toString();
    }

    /**
     * 设置用户信息
     */
    private void setUserInfo(CSQResultDTO resultDTO, LbsResultsVo lbsResults) {
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
    public String convertToJson(CSQResultDTO resultDTO) throws Exception {
        return objectMapper.writeValueAsString(resultDTO);
    }

    /**
     * 从JSON字符串解析结果
     */
    public CSQResultDTO parseFromJson(String json) throws Exception {
        return objectMapper.readValue(json, CSQResultDTO.class);
    }

    /**
     * 验证答案完整性
     */
    public boolean validateAnswers(ContextAnswerVo contextAnswer) {
        if (contextAnswer == null || contextAnswer.getAnswers() == null) {
            return false;
        }

        List<AnswerVo> answers = contextAnswer.getAnswers();
        if (answers.size() < 60) {
            return false;
        }

        long validAnswers = answers.stream()
                .filter(a -> a.getOptionIds() != null && !a.getOptionIds().trim().isEmpty())
                .count();

        return validAnswers >= 48;
    }

    /**
     * 兼容性方法：原有的calculate方法（返回Map）
     */
    public Map<String, Object> calculate(ContextAnswerVo contextAnswer, CSQCalcParams calcParams, ContextVo contextVo) {
        CSQResultDTO resultDTO = calculateResult(contextAnswer, calcParams, contextVo, null);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("rawTotalScore", resultDTO.getRawTotalScore());
        result.put("factorScores", resultDTO.getFactorScores());
        result.put("factorRatios", resultDTO.getFactorRatios());
        result.put("copingStyle", resultDTO.getCopingStyle());
        result.put("interpretation", resultDTO.getInterpretation());
        result.put("suggestions", resultDTO.getSuggestions());
        result.put("styleDetails", resultDTO.getStyleDetails());
        result.put("copingTendency", resultDTO.getCopingTendency());
        result.put("factorInterpretations", resultDTO.getFactorInterpretations());

        return result;
    }
}