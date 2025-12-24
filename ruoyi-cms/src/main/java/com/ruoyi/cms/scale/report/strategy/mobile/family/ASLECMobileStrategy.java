package com.ruoyi.cms.scale.report.strategy.mobile.family;

import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.report.ITemplateStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component
public class ASLECMobileStrategy implements ITemplateStrategy {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(ASLECMobileStrategy.class);

    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults) {
        try {
            // 1. 检查测评结果是否为空
            if (lbsResults == null) {
                return renderLoadingTemplate();
            }

            // 2. 解析jsonReport字段中的ASLEC结果
            String jsonReport = lbsResults.getJsonReport();
            if (jsonReport == null || jsonReport.trim().isEmpty()) {
                return renderLoadingTemplate();
            }

            // 3. 尝试解析JSON结果
            Map<String, Object> resultMap;
            try {
                resultMap = parseResultJson(jsonReport);
            } catch (Exception e) {
                return renderLoadingTemplate();
            }

            // 4. 检查是否包含必要的字段
            if (!hasRequiredFields(resultMap)) {
                return renderLoadingTemplate();
            }

            // 5. 准备Thymeleaf上下文数据
            Context context = prepareTemplateContext(resultMap);

            try {
                logger.info("Processing ASLEC mobile template: mobile/family/aslec");
                String result = templateEngine.process("mobile/family/aslec", context);
                logger.info("ASLEC template processed successfully");
                return result;
            } catch (Exception e) {
                logger.error("Unexpected error processing ASLEC template: ", e);
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return renderLoadingTemplate();
        }
    }

    /**
     * 渲染默认的加载模板
     */
    private String renderLoadingTemplate() {
        try {
            Context context = new Context();
            return templateEngine.process("mobile/default/index", context);
        } catch (Exception e) {
            return generateSimpleLoadingHtml();
        }
    }

    /**
     * 生成简单的加载HTML（备用）
     */
    private String generateSimpleLoadingHtml() {
        return "<div style='font-family: -apple-system, BlinkMacSystemFont, \"Helvetica Neue\", sans-serif; padding: 40px 20px; text-align: center;'>" +
                "<div style='color: #4a90e2; font-size: 24px; margin-bottom: 20px;'>⌛</div>" +
                "<h3 style='color: #2c3e50; margin-bottom: 10px;'>报告正在生成中...</h3>" +
                "<p style='color: #7f8c8d;'>请稍候，系统正在处理您的测评结果</p>" +
                "</div>";
    }

    /**
     * 解析JSON结果
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseResultJson(String jsonReport) throws Exception {
        Map<String, Object> resultMap = objectMapper.readValue(jsonReport, Map.class);
        ensureRequiredFields(resultMap);
        return resultMap;
    }

    /**
     * 检查是否包含必要的字段
     */
    private boolean hasRequiredFields(Map<String, Object> resultMap) {
        boolean hasRawTotalScore = resultMap.containsKey("rawTotalScore") && resultMap.get("rawTotalScore") != null;
        boolean hasStressLevel = resultMap.containsKey("stressLevel") && resultMap.get("stressLevel") != null;
        return hasRawTotalScore && hasStressLevel;
    }

    /**
     * 确保所有必需的字段都有值
     */
    private void ensureRequiredFields(Map<String, Object> resultMap) {
        if (!resultMap.containsKey("rawTotalScore") || resultMap.get("rawTotalScore") == null) {
            resultMap.put("rawTotalScore", 0);
        }

        if (!resultMap.containsKey("stressLevel") || resultMap.get("stressLevel") == null) {
            resultMap.put("stressLevel", "无压力");
        }

        if (!resultMap.containsKey("interpretation") || resultMap.get("interpretation") == null) {
            resultMap.put("interpretation", "");
        }

        if (!resultMap.containsKey("suggestions") || resultMap.get("suggestions") == null) {
            resultMap.put("suggestions", "");
        }

        if (!resultMap.containsKey("suggestionList") || resultMap.get("suggestionList") == null) {
            resultMap.put("suggestionList", new ArrayList<>());
        }

        if (!resultMap.containsKey("dimensionScores") || resultMap.get("dimensionScores") == null) {
            resultMap.put("dimensionScores", new HashMap<>());
        }

        if (!resultMap.containsKey("levelDescription") || resultMap.get("levelDescription") == null) {
            resultMap.put("levelDescription", "");
        }

        if (!resultMap.containsKey("highStressEvents") || resultMap.get("highStressEvents") == null) {
            resultMap.put("highStressEvents", new HashMap<>());
        }
    }

    /**
     * 准备模板上下文数据
     */
    private Context prepareTemplateContext(Map<String, Object> resultMap) {
        Context context = new Context();

        // 从resultMap中提取数据
        Integer rawTotalScore = (Integer) resultMap.get("rawTotalScore");
        String stressLevel = (String) resultMap.get("stressLevel");
        String interpretation = (String) resultMap.get("interpretation");
        String suggestions = (String) resultMap.get("suggestions");
        List<String> suggestionList = (List<String>) resultMap.get("suggestionList");
        Map<String, Object> dimensionScores = (Map<String, Object>) resultMap.get("dimensionScores");
        String levelDescription = (String) resultMap.get("levelDescription");
        Map<String, Object> highStressEvents = (Map<String, Object>) resultMap.get("highStressEvents");

        // 计算进度条宽度
        int progressWidth = Math.min((int) (rawTotalScore * 100.0 / 135), 100);

        // 获取压力水平的颜色配置
        Map<String, String> levelColors = getLevelColors(stressLevel);

        // 准备建议列表
        List<String> finalSuggestionList = prepareSuggestionList(suggestionList, suggestions);

        // 准备高压力事件列表
        List<String> highStressList = new ArrayList<>();
        if (highStressEvents != null && highStressEvents.containsKey("高压力事件")) {
            highStressList = (List<String>) highStressEvents.get("高压力事件");
        }

        // 将数据放入上下文
        context.setVariable("rawTotalScore", rawTotalScore);
        context.setVariable("stressLevel", stressLevel);
        context.setVariable("interpretation", interpretation);
        context.setVariable("progressWidth", progressWidth);
        context.setVariable("levelDescription", levelDescription);

        // 颜色相关变量
        context.setVariable("gradientBg", levelColors.get("gradient"));
        context.setVariable("icon", levelColors.get("icon"));
        context.setVariable("scoreColor", levelColors.get("score"));
        context.setVariable("progressGradient", levelColors.get("progress"));
        context.setVariable("borderColor", levelColors.get("border"));
        context.setVariable("levelBgColor", levelColors.get("levelBg"));
        context.setVariable("suggestionBgColor", levelColors.get("suggestionBg"));

        // 建议相关
        context.setVariable("suggestionList", finalSuggestionList);

        // 维度得分
        context.setVariable("dimensionScores", dimensionScores);
        context.setVariable("hasDimensions", dimensionScores != null && !dimensionScores.isEmpty());

        // 高压力事件
        context.setVariable("highStressEvents", highStressList);
        context.setVariable("hasHighStressEvents", highStressList != null && !highStressList.isEmpty());

        // 如果没有程度描述，则生成一个
        if (levelDescription == null || levelDescription.trim().isEmpty()) {
            context.setVariable("levelDescription", getLevelDescription(stressLevel, rawTotalScore));
        } else {
            context.setVariable("levelDescription", levelDescription);
        }

        // 分数范围说明
        context.setVariable("scoreRange", "0-135分");

        return context;
    }

    /**
     * 准备建议列表
     */
    private List<String> prepareSuggestionList(List<String> suggestionList, String suggestions) {
        if (suggestionList != null && !suggestionList.isEmpty()) {
            return suggestionList;
        }

        List<String> result = new ArrayList<>();
        if (suggestions != null && !suggestions.trim().isEmpty()) {
            String[] items;
            if (suggestions.contains("\n")) {
                items = suggestions.split("\n");
            } else if (suggestions.contains("；")) {
                items = suggestions.split("；");
            } else if (suggestions.contains(";")) {
                items = suggestions.split(";");
            } else {
                items = new String[]{suggestions};
            }

            for (String item : items) {
                String trimmed = item.trim();
                if (!trimmed.isEmpty()) {
                    result.add(trimmed);
                }
            }
        }

        if (result.isEmpty()) {
            result.add("学习放松技巧，如深呼吸、渐进性肌肉放松");
            result.add("与信任的人分享感受，寻求支持");
            result.add("合理安排时间，避免过度疲劳");
            result.add("培养兴趣爱好，转移注意力");
            result.add("学习问题解决技巧，应对具体生活事件");
        }

        return result;
    }

    /**
     * 根据压力水平获取颜色配置
     */
    private Map<String, String> getLevelColors(String stressLevel) {
        Map<String, String> colors = new HashMap<>();

        switch (stressLevel) {
            case "无压力":
                colors.put("gradient", "linear-gradient(135deg, #4CAF50, #8BC34A)");
                colors.put("icon", "😊");
                colors.put("score", "#4CAF50");
                colors.put("progress", "linear-gradient(90deg, #4CAF50, #8BC34A)");
                colors.put("border", "#4CAF50");
                colors.put("levelBg", "#E8F5E9");
                colors.put("suggestionBg", "#E8F5E9");
                break;
            case "轻度压力":
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "😐");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#e8f4fd");
                colors.put("suggestionBg", "#e8f4fd");
                break;
            case "中度压力":
                colors.put("gradient", "linear-gradient(135deg, #FF9800, #FFB74D)");
                colors.put("icon", "😟");
                colors.put("score", "#FF9800");
                colors.put("progress", "linear-gradient(90deg, #FF9800, #FFB74D)");
                colors.put("border", "#FF9800");
                colors.put("levelBg", "#FFF3E0");
                colors.put("suggestionBg", "#FFF3E0");
                break;
            case "重度压力":
                colors.put("gradient", "linear-gradient(135deg, #F44336, #E57373)");
                colors.put("icon", "😨");
                colors.put("score", "#F44336");
                colors.put("progress", "linear-gradient(90deg, #F44336, #E57373)");
                colors.put("border", "#F44336");
                colors.put("levelBg", "#FFEBEE");
                colors.put("suggestionBg", "#FFEBEE");
                break;
            case "极重度压力":
                colors.put("gradient", "linear-gradient(135deg, #9C27B0, #BA68C8)");
                colors.put("icon", "😰");
                colors.put("score", "#9C27B0");
                colors.put("progress", "linear-gradient(90deg, #9C27B0, #BA68C8)");
                colors.put("border", "#9C27B0");
                colors.put("levelBg", "#F3E5F5");
                colors.put("suggestionBg", "#F3E5F5");
                break;
            default:
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "📊");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#f8f9fa");
                colors.put("suggestionBg", "#f8f9fa");
        }

        return colors;
    }

    /**
     * 获取压力程度描述
     */
    private String getLevelDescription(String stressLevel, int score) {
        switch (stressLevel) {
            case "无压力":
                return "您的压力水平在正常范围内，总分" + score + "分。这表明您近期遇到的生活事件对您造成的心理压力较小，您具备良好的应对能力。";
            case "轻度压力":
                return "您的压力水平处于轻度状态，总分" + score + "分。建议关注自己的情绪变化，学习适当的压力管理技巧。";
            case "中度压力":
                return "您的压力水平处于中度状态，总分" + score + "分。建议寻求心理咨询或辅导，学习有效的应对策略。";
            case "重度压力":
                return "您的压力水平处于重度状态，总分" + score + "分。建议尽快寻求专业心理帮助，进行系统的评估和干预。";
            case "极重度压力":
                return "您的压力水平处于极重度状态，总分" + score + "分。强烈建议您立即寻求专业心理帮助。";
            default:
                return "您的压力水平处于" + stressLevel + "状态，总分" + score + "分。";
        }
    }
}