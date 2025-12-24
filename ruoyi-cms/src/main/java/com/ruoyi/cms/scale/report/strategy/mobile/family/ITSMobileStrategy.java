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

import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.exceptions.TemplateInputException;

import java.util.*;

@Component
public class ITSMobileStrategy implements ITemplateStrategy {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(ITSMobileStrategy.class);

    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults) {
        try {
            // 1. 检查测评结果是否为空
            if (lbsResults == null) {
                return renderLoadingTemplate();
            }

            // 2. 解析jsonReport字段中的ITS结果
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
                logger.info("Processing ITS mobile template: mobile/family/its");

                // 尝试处理模板
                String result = templateEngine.process("mobile/family/its", context);
                logger.info("ITS template processed successfully");
                return result;
            } catch (TemplateInputException e) {
                logger.error("TemplateInputException - Template not found or cannot be read: ", e);
                throw e;
            } catch (TemplateProcessingException e) {
                logger.error("TemplateProcessingException - Error in template content: ", e);
                logger.error("Template line info: {}", e.getTemplateName() + ":" + e.getLine());
                throw e;
            } catch (Exception e) {
                logger.error("Unexpected error processing ITS template: ", e);
                e.printStackTrace();
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
        boolean hasTrustLevel = resultMap.containsKey("trustLevel") && resultMap.get("trustLevel") != null;
        return hasRawTotalScore && hasTrustLevel;
    }

    /**
     * 确保所有必需的字段都有值
     */
    private void ensureRequiredFields(Map<String, Object> resultMap) {
        if (!resultMap.containsKey("rawTotalScore") || resultMap.get("rawTotalScore") == null) {
            resultMap.put("rawTotalScore", 0);
        }

        if (!resultMap.containsKey("standardScore") || resultMap.get("standardScore") == null) {
            resultMap.put("standardScore", 0);
        }

        if (!resultMap.containsKey("trustLevel") || resultMap.get("trustLevel") == null) {
            resultMap.put("trustLevel", "中等信任");
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

        if (!resultMap.containsKey("averageScore") || resultMap.get("averageScore") == null) {
            resultMap.put("averageScore", 4.0);
        }

        if (!resultMap.containsKey("levelDescription") || resultMap.get("levelDescription") == null) {
            resultMap.put("levelDescription", "");
        }
    }

    /**
     * 准备模板上下文数据
     */
    private Context prepareTemplateContext(Map<String, Object> resultMap) {
        Context context = new Context();

        // 从resultMap中提取数据
        Integer rawTotalScore = (Integer) resultMap.get("rawTotalScore");
        Integer standardScore = (Integer) resultMap.get("standardScore");
        String trustLevel = (String) resultMap.get("trustLevel");
        String interpretation = (String) resultMap.get("interpretation");
        String suggestions = (String) resultMap.get("suggestions");
        List<String> suggestionList = (List<String>) resultMap.get("suggestionList");
        Map<String, Object> dimensionScores = (Map<String, Object>) resultMap.get("dimensionScores");
        Double averageScore = (Double) resultMap.get("averageScore");
        String levelDescription = (String) resultMap.get("levelDescription");

        // 计算进度条宽度
        int progressWidth = standardScore != null ? Math.min(standardScore, 100) : 50;

        // 获取信任程度的颜色配置
        Map<String, String> levelColors = getLevelColors(trustLevel != null ? trustLevel : "中等信任");

        // 准备建议列表
        List<String> finalSuggestionList = prepareSuggestionList(suggestionList, suggestions);

        // 将数据放入上下文
        context.setVariable("rawTotalScore", rawTotalScore != null ? rawTotalScore : 0);
        context.setVariable("standardScore", standardScore != null ? standardScore : 50);
        context.setVariable("trustLevel", trustLevel != null ? trustLevel : "中等信任");
        context.setVariable("interpretation", interpretation != null ? interpretation : "");
        context.setVariable("progressWidth", progressWidth);
        context.setVariable("averageScore", averageScore != null ? String.format("%.1f", averageScore) : "4.0");

        // 传递所有样式变量
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
        context.setVariable("dimensionScores", dimensionScores != null ? dimensionScores : new HashMap<>());
        context.setVariable("hasDimensions", dimensionScores != null && !dimensionScores.isEmpty());

        // 如果没有程度描述，则生成一个
        if (levelDescription == null || levelDescription.trim().isEmpty()) {
            context.setVariable("levelDescription", getLevelDescription(trustLevel != null ? trustLevel : "中等信任",
                    averageScore != null ? averageScore : 4.0));
        } else {
            context.setVariable("levelDescription", levelDescription);
        }

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
            result.add("信任是人际关系的基础，建议加强沟通");
            result.add("从小的承诺开始，逐步建立信任");
            result.add("保持一致性，言行一致是信任的关键");
            result.add("给予对方适当的信任机会");
            result.add("定期回顾和调整信任关系");
        }

        return result;
    }

    /**
     * 根据信任程度获取颜色配置
     */
    private Map<String, String> getLevelColors(String trustLevel) {
        Map<String, String> colors = new HashMap<>();

        switch (trustLevel) {
            case "非常低信任":
                colors.put("gradient", "linear-gradient(135deg, #F44336, #E57373)");
                colors.put("icon", "😟");
                colors.put("score", "#F44336");
                colors.put("progress", "linear-gradient(90deg, #F44336, #E57373)");
                colors.put("border", "#F44336");
                colors.put("levelBg", "#FFEBEE");
                colors.put("suggestionBg", "#FFEBEE");
                break;
            case "较低信任":
                colors.put("gradient", "linear-gradient(135deg, #FF9800, #FFB74D)");
                colors.put("icon", "😐");
                colors.put("score", "#FF9800");
                colors.put("progress", "linear-gradient(90deg, #FF9800, #FFB74D)");
                colors.put("border", "#FF9800");
                colors.put("levelBg", "#FFF3E0");
                colors.put("suggestionBg", "#FFF3E0");
                break;
            case "中等信任":
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "😊");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#e8f4fd");
                colors.put("suggestionBg", "#e8f4fd");
                break;
            case "较高信任":
                colors.put("gradient", "linear-gradient(135deg, #4CAF50, #8BC34A)");
                colors.put("icon", "😄");
                colors.put("score", "#4CAF50");
                colors.put("progress", "linear-gradient(90deg, #4CAF50, #8BC34A)");
                colors.put("border", "#4CAF50");
                colors.put("levelBg", "#E8F5E9");
                colors.put("suggestionBg", "#E8F5E9");
                break;
            case "非常高信任":
                colors.put("gradient", "linear-gradient(135deg, #2196F3, #64B5F6)");
                colors.put("icon", "🤗");
                colors.put("score", "#2196F3");
                colors.put("progress", "linear-gradient(90deg, #2196F3, #64B5F6)");
                colors.put("border", "#2196F3");
                colors.put("levelBg", "#E3F2FD");
                colors.put("suggestionBg", "#E3F2FD");
                break;
            default:
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "🤝");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#f8f9fa");
                colors.put("suggestionBg", "#f8f9fa");
        }

        return colors;
    }

    /**
     * 获取信任程度描述
     */
    private String getLevelDescription(String trustLevel, double averageScore) {
        switch (trustLevel) {
            case "非常低信任":
                return String.format("信任平均分%.1f分，信任程度非常低。表明信任基础薄弱，需要重点关注。", averageScore);
            case "较低信任":
                return String.format("信任平均分%.1f分，信任程度较低。表明有一定信任但需加强。", averageScore);
            case "中等信任":
                return String.format("信任平均分%.1f分，信任程度中等。表明存在基本的信任关系。", averageScore);
            case "较高信任":
                return String.format("信任平均分%.1f分，信任程度较高。表明有良好的信任基础。", averageScore);
            case "非常高信任":
                return String.format("信任平均分%.1f分，信任程度非常高。表明有非常稳固的信任关系。", averageScore);
            default:
                return String.format("信任平均分%.1f分，信任程度为%s。", averageScore, trustLevel);
        }
    }
}