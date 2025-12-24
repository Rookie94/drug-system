package com.ruoyi.cms.scale.report.strategy.mobile.standard;

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
public class HAMAMobileStrategy implements ITemplateStrategy {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(HAMAMobileStrategy.class);

    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults) {
        try {
            // 1. 检查测评结果是否为空
            if (lbsResults == null) {
                return renderLoadingTemplate();
            }

            // 2. 解析jsonReport字段中的HAMA结果
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
                logger.info("Processing HAMA mobile template: mobile/standard/hama");

                // 尝试处理模板
                String result = templateEngine.process("mobile/standard/hama", context);
                logger.info("HAMA template processed successfully");
                return result;
            } catch (TemplateInputException e) {
                logger.error("TemplateInputException - Template not found or cannot be read: ", e);
                throw e;
            } catch (TemplateProcessingException e) {
                logger.error("TemplateProcessingException - Error in template content: ", e);
                logger.error("Template line info: {}", e.getTemplateName() + ":" + e.getLine());
                throw e;
            } catch (Exception e) {
                logger.error("Unexpected error processing HAMA template: ", e);
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
        boolean hasAnxietyLevel = resultMap.containsKey("anxietyLevel") && resultMap.get("anxietyLevel") != null;
        return hasRawTotalScore && hasAnxietyLevel;
    }

    /**
     * 确保所有必需的字段都有值
     */
    private void ensureRequiredFields(Map<String, Object> resultMap) {
        if (!resultMap.containsKey("rawTotalScore") || resultMap.get("rawTotalScore") == null) {
            resultMap.put("rawTotalScore", 0);
        }

        if (!resultMap.containsKey("anxietyLevel") || resultMap.get("anxietyLevel") == null) {
            resultMap.put("anxietyLevel", "无焦虑症状");
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
    }

    /**
     * 准备模板上下文数据
     */
    private Context prepareTemplateContext(Map<String, Object> resultMap) {
        Context context = new Context();

        // 从resultMap中提取数据
        Integer rawTotalScore = (Integer) resultMap.get("rawTotalScore");
        String anxietyLevel = (String) resultMap.get("anxietyLevel");
        String interpretation = (String) resultMap.get("interpretation");
        String suggestions = (String) resultMap.get("suggestions");
        List<String> suggestionList = (List<String>) resultMap.get("suggestionList");
        Map<String, Object> dimensionScores = (Map<String, Object>) resultMap.get("dimensionScores");
        String levelDescription = (String) resultMap.get("levelDescription");

        // 计算进度条宽度（HAMA总分56分，转换为百分比）
        int progressWidth = rawTotalScore != null ? (int) Math.min((rawTotalScore * 100.0 / 56), 100) : 0;

        // 获取焦虑程度的颜色配置
        Map<String, String> levelColors = getLevelColors(anxietyLevel != null ? anxietyLevel : "无焦虑症状");

        // 准备建议列表
        List<String> finalSuggestionList = prepareSuggestionList(suggestionList, suggestions);

        // 将数据放入上下文
        context.setVariable("rawTotalScore", rawTotalScore != null ? rawTotalScore : 0);
        context.setVariable("anxietyLevel", anxietyLevel != null ? anxietyLevel : "无焦虑症状");
        context.setVariable("interpretation", interpretation != null ? interpretation : "");
        context.setVariable("progressWidth", progressWidth);

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
            context.setVariable("levelDescription", getLevelDescription(anxietyLevel != null ? anxietyLevel : "无焦虑症状", rawTotalScore != null ? rawTotalScore : 0));
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
            result.add("建议进行专业心理评估");
            result.add("学习放松技巧，缓解焦虑");
            result.add("保持规律作息，保证睡眠");
            result.add("适当体育锻炼，增强体质");
            result.add("寻求社会支持，分享感受");
        }

        return result;
    }

    /**
     * 根据焦虑程度获取颜色配置
     */
    private Map<String, String> getLevelColors(String anxietyLevel) {
        Map<String, String> colors = new HashMap<>();

        switch (anxietyLevel) {
            case "无焦虑症状":
                colors.put("gradient", "linear-gradient(135deg, #4CAF50, #8BC34A)");
                colors.put("icon", "😊");
                colors.put("score", "#4CAF50");
                colors.put("progress", "linear-gradient(90deg, #4CAF50, #8BC34A)");
                colors.put("border", "#4CAF50");
                colors.put("levelBg", "#E8F5E9");
                colors.put("suggestionBg", "#E8F5E9");
                break;
            case "可能有焦虑":
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "😐");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#e8f4fd");
                colors.put("suggestionBg", "#e8f4fd");
                break;
            case "肯定有焦虑":
                colors.put("gradient", "linear-gradient(135deg, #FF9800, #FFB74D)");
                colors.put("icon", "😟");
                colors.put("score", "#FF9800");
                colors.put("progress", "linear-gradient(90deg, #FF9800, #FFB74D)");
                colors.put("border", "#FF9800");
                colors.put("levelBg", "#FFF3E0");
                colors.put("suggestionBg", "#FFF3E0");
                break;
            case "肯定有明显焦虑":
                colors.put("gradient", "linear-gradient(135deg, #F44336, #E57373)");
                colors.put("icon", "😨");
                colors.put("score", "#F44336");
                colors.put("progress", "linear-gradient(90deg, #F44336, #E57373)");
                colors.put("border", "#F44336");
                colors.put("levelBg", "#FFEBEE");
                colors.put("suggestionBg", "#FFEBEE");
                break;
            case "可能有严重焦虑":
                colors.put("gradient", "linear-gradient(135deg, #9C27B0, #BA68C8)");
                colors.put("icon", "😱");
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
     * 获取焦虑程度描述
     */
    private String getLevelDescription(String anxietyLevel, int score) {
        switch (anxietyLevel) {
            case "无焦虑症状":
                return "HAMA总分" + score + "分，低于7分，属于正常范围，没有明显的焦虑症状。";
            case "可能有焦虑":
                return "HAMA总分" + score + "分，在7-13分之间，提示可能存在焦虑症状，建议关注情绪变化。";
            case "肯定有焦虑":
                return "HAMA总分" + score + "分，在14-20分之间，肯定存在焦虑症状，建议进行专业评估。";
            case "肯定有明显焦虑":
                return "HAMA总分" + score + "分，在21-28分之间，存在明显焦虑症状，建议寻求专业治疗。";
            case "可能有严重焦虑":
                return "HAMA总分" + score + "分，超过28分，可能存在严重焦虑症状，需要立即专业干预。";
            default:
                return "HAMA总分" + score + "分，" + anxietyLevel + "。";
        }
    }
}