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
public class CSQMobileStrategy implements ITemplateStrategy {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(CSQMobileStrategy.class);

    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults) {
        try {
            if (lbsResults == null) {
                return renderLoadingTemplate();
            }

            String jsonReport = lbsResults.getJsonReport();
            if (jsonReport == null || jsonReport.trim().isEmpty()) {
                return renderLoadingTemplate();
            }

            Map<String, Object> resultMap;
            try {
                resultMap = parseResultJson(jsonReport);
            } catch (Exception e) {
                logger.error("CSQ结果JSON解析失败: ", e);
                return renderLoadingTemplate();
            }

            if (!hasRequiredFields(resultMap)) {
                return renderLoadingTemplate();
            }

            Context context = prepareTemplateContext(resultMap);

            try {
                // 使用正确的模板路径：mobile/family/csq
                logger.info("Processing CSQ mobile template: mobile/family/csq");
                String result = templateEngine.process("mobile/family/csq", context);
                logger.info("CSQ template processed successfully");
                return result;
            } catch (Exception e) {
                logger.error("Unexpected error processing CSQ template: ", e);
                return renderLoadingTemplate();
            }

        } catch (Exception e) {
            logger.error("CSQ模板处理异常: ", e);
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
            logger.error("加载模板渲染失败: ", e);
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
        boolean hasCopingStyle = resultMap.containsKey("copingStyle") && resultMap.get("copingStyle") != null;
        boolean hasFactorScores = resultMap.containsKey("factorScores") && resultMap.get("factorScores") != null;
        return hasCopingStyle && hasFactorScores;
    }

    /**
     * 确保所有必需的字段都有值
     */
    private void ensureRequiredFields(Map<String, Object> resultMap) {
        if (!resultMap.containsKey("rawTotalScore") || resultMap.get("rawTotalScore") == null) {
            resultMap.put("rawTotalScore", 0);
        }

        if (!resultMap.containsKey("copingStyle") || resultMap.get("copingStyle") == null) {
            resultMap.put("copingStyle", "未知");
        }

        if (!resultMap.containsKey("styleDescription") || resultMap.get("styleDescription") == null) {
            resultMap.put("styleDescription", "");
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

        if (!resultMap.containsKey("factorScores") || resultMap.get("factorScores") == null) {
            resultMap.put("factorScores", new HashMap<>());
        }

        if (!resultMap.containsKey("factorRatios") || resultMap.get("factorRatios") == null) {
            resultMap.put("factorRatios", new HashMap<>());
        }

        if (!resultMap.containsKey("copingTendency") || resultMap.get("copingTendency") == null) {
            resultMap.put("copingTendency", "未知");
        }
    }

    /**
     * 准备模板上下文数据
     */
    @SuppressWarnings("unchecked")
    private Context prepareTemplateContext(Map<String, Object> resultMap) {
        Context context = new Context();

        // 从resultMap中提取数据
        Integer rawTotalScore = (Integer) resultMap.get("rawTotalScore");
        String copingStyle = (String) resultMap.get("copingStyle");
        String interpretation = (String) resultMap.get("interpretation");
        String suggestions = (String) resultMap.get("suggestions");
        List<String> suggestionList = (List<String>) resultMap.get("suggestionList");
        Map<String, Object> factorScores = (Map<String, Object>) resultMap.get("factorScores");
        Map<String, Object> factorRatios = (Map<String, Object>) resultMap.get("factorRatios");
        String styleDescription = (String) resultMap.get("styleDescription");
        String copingTendency = (String) resultMap.get("copingTendency");

        // 确保factorRatios不为null
        if (factorRatios == null) {
            factorRatios = new HashMap<>();
        }

        // 计算进度条宽度
        int progressWidth = calculateProgressWidth(factorScores);

        // 获取应对方式的颜色配置
        Map<String, String> styleColors = getStyleColors(copingStyle);

        // 准备建议列表
        List<String> finalSuggestionList = prepareSuggestionList(suggestionList, suggestions);

        // 准备因子数据用于图表展示
        List<Map<String, Object>> factorChartData = prepareFactorChartData(factorScores, factorRatios);

        // 将数据放入上下文
        context.setVariable("rawTotalScore", rawTotalScore);
        context.setVariable("copingStyle", copingStyle);
        context.setVariable("interpretation", interpretation);
        context.setVariable("progressWidth", progressWidth);
        context.setVariable("styleDescription", styleDescription);
        context.setVariable("copingTendency", copingTendency);

        // 颜色相关变量
        context.setVariable("gradientBg", styleColors.get("gradient"));
        context.setVariable("icon", styleColors.get("icon"));
        context.setVariable("scoreColor", styleColors.get("score"));
        context.setVariable("progressGradient", styleColors.get("progress"));
        context.setVariable("borderColor", styleColors.get("border"));
        context.setVariable("styleBgColor", styleColors.get("styleBg"));
        context.setVariable("suggestionBgColor", styleColors.get("suggestionBg"));

        // 建议相关
        context.setVariable("suggestionList", finalSuggestionList);

        // 因子得分相关 - 确保不为null
        context.setVariable("factorScores", factorScores != null ? factorScores : new HashMap<>());
        context.setVariable("factorRatios", factorRatios);
        context.setVariable("factorChartData", factorChartData);
        context.setVariable("hasFactors", factorScores != null && !factorScores.isEmpty());
        context.setVariable("hasFactorChartData", factorChartData != null && !factorChartData.isEmpty());

        // 如果没有程度描述，则生成一个
        if (styleDescription == null || styleDescription.trim().isEmpty()) {
            context.setVariable("styleDescription", getStyleDescription(copingStyle));
        }

        return context;
    }

    /**
     * 计算进度条宽度
     */
    private int calculateProgressWidth(Map<String, Object> factorScores) {
        if (factorScores == null || factorScores.isEmpty()) {
            return 50;
        }

        int maxScore = 0;
        for (Object scoreObj : factorScores.values()) {
            if (scoreObj instanceof Integer) {
                int score = (Integer) scoreObj;
                if (score > maxScore) {
                    maxScore = score;
                }
            } else if (scoreObj instanceof Number) {
                int score = ((Number) scoreObj).intValue();
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }

        // 因子得分范围通常0-12，转换为百分比
        return Math.min((int)((maxScore / 12.0) * 100), 100);
    }

    /**
     * 准备因子图表数据
     */
    private List<Map<String, Object>> prepareFactorChartData(Map<String, Object> factorScores, Map<String, Object> factorRatios) {
        List<Map<String, Object>> chartData = new ArrayList<>();

        if (factorScores != null && factorRatios != null) {
            for (Map.Entry<String, Object> entry : factorScores.entrySet()) {
                String factorName = entry.getKey();
                Object scoreObj = entry.getValue();
                Object ratioObj = factorRatios.get(factorName);

                if (scoreObj != null && ratioObj != null) {
                    Map<String, Object> factorData = new HashMap<>();
                    factorData.put("name", factorName);
                    factorData.put("score", scoreObj);
                    factorData.put("ratio", ratioObj);

                    // 根据得分设置颜色
                    int score = 0;
                    if (scoreObj instanceof Integer) {
                        score = (Integer) scoreObj;
                    } else if (scoreObj instanceof Number) {
                        score = ((Number) scoreObj).intValue();
                    }

                    if (score >= 8) {
                        factorData.put("color", "#4CAF50");
                    } else if (score >= 4) {
                        factorData.put("color", "#4a90e2");
                    } else {
                        factorData.put("color", "#FF9800");
                    }

                    chartData.add(factorData);
                }
            }
        }

        return chartData;
    }

    /**
     * 根据应对方式获取颜色配置
     */
    private Map<String, String> getStyleColors(String copingStyle) {
        Map<String, String> colors = new HashMap<>();

        switch (copingStyle) {
            case "成熟型":
                colors.put("gradient", "linear-gradient(135deg, #4CAF50, #8BC34A)");
                colors.put("icon", "🌟");
                colors.put("score", "#4CAF50");
                colors.put("progress", "linear-gradient(90deg, #4CAF50, #8BC34A)");
                colors.put("border", "#4CAF50");
                colors.put("styleBg", "#E8F5E9");
                colors.put("suggestionBg", "#E8F5E9");
                break;
            case "混合型":
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "⚖️");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("styleBg", "#e8f4fd");
                colors.put("suggestionBg", "#e8f4fd");
                break;
            case "不成熟型":
                colors.put("gradient", "linear-gradient(135deg, #FF9800, #FFB74D)");
                colors.put("icon", "⚠️");
                colors.put("score", "#FF9800");
                colors.put("progress", "linear-gradient(90deg, #FF9800, #FFB74D)");
                colors.put("border", "#FF9800");
                colors.put("styleBg", "#FFF3E0");
                colors.put("suggestionBg", "#FFF3E0");
                break;
            case "不适当型":
                colors.put("gradient", "linear-gradient(135deg, #F44336, #E57373)");
                colors.put("icon", "🚨");
                colors.put("score", "#F44336");
                colors.put("progress", "linear-gradient(90deg, #F44336, #E57373)");
                colors.put("border", "#F44336");
                colors.put("styleBg", "#FFEBEE");
                colors.put("suggestionBg", "#FFEBEE");
                break;
            default:
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "📊");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("styleBg", "#f8f9fa");
                colors.put("suggestionBg", "#f8f9fa");
        }

        return colors;
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
            result.add("学习压力管理技巧");
            result.add("建立积极的社会支持系统");
            result.add("培养问题解决能力");
            result.add("学习情绪调节方法");
            result.add("定期进行自我评估");
        }

        return result;
    }

    /**
     * 获取应对方式描述
     */
    private String getStyleDescription(String copingStyle) {
        switch (copingStyle) {
            case "成熟型":
                return "您倾向于使用成熟的应对策略，善于解决问题和寻求帮助。";
            case "混合型":
                return "您的应对方式较为平衡，结合了多种策略。";
            case "不成熟型":
                return "您较多使用不成熟的应对方式，建议学习更有效的压力管理策略。";
            case "不适当型":
                return "您的应对方式可能不太适应当前情境，建议寻求专业指导。";
            default:
                return "您的应对方式需要进一步评估。";
        }
    }
}