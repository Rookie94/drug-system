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
public class SASMobileStrategy implements ITemplateStrategy {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(SASMobileStrategy.class);

    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults) {
        try {
            // 1. 检查测评结果是否为空
            if (lbsResults == null) {
                // 返回默认的加载模板
                return renderLoadingTemplate();
            }

            // 2. 解析jsonReport字段中的SAS结果
            String jsonReport = lbsResults.getJsonReport();
            if (jsonReport == null || jsonReport.trim().isEmpty()) {
                // 如果没有报告数据，返回加载模板
                return renderLoadingTemplate();
            }

            // 3. 尝试解析JSON结果
            Map<String, Object> resultMap;
            try {
                resultMap = parseResultJson(jsonReport);
            } catch (Exception e) {
                // JSON解析失败，可能数据格式不正确，返回加载模板
                return renderLoadingTemplate();
            }

            // 4. 检查是否包含必要的字段
            if (!hasRequiredFields(resultMap)) {
                // 缺少必要字段，返回加载模板
                return renderLoadingTemplate();
            }

            // 5. 准备Thymeleaf上下文数据
            Context context = prepareTemplateContext(resultMap);

            try {
                logger.info("Processing SAS mobile template: mobile/standard/sas");
                logger.info("Context variables: {}", context.getVariableNames());

                // 尝试处理模板
                String result = templateEngine.process("mobile/standard/sas", context);
                logger.info("SAS template processed successfully");
                return result;
            } catch (TemplateInputException e) {
                logger.error("TemplateInputException - Template not found or cannot be read: ", e);
                throw e;
            } catch (TemplateProcessingException e) {
                logger.error("TemplateProcessingException - Error in template content: ", e);
                logger.error("Template line info: {}", e.getTemplateName() + ":" + e.getLine());
                throw e;
            } catch (Exception e) {
                logger.error("Unexpected error processing SAS template: ", e);
                e.printStackTrace();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 发生异常时返回加载模板
            return renderLoadingTemplate();
        }
    }

    /**
     * 渲染默认的加载模板
     */
    private String renderLoadingTemplate() {
        try {
            // 创建一个空的上下文，因为默认模板不需要参数
            Context context = new Context();
            // 返回默认的加载模板
            return templateEngine.process("mobile/default/index", context);
        } catch (Exception e) {
            // 如果加载模板渲染失败，返回一个简单的加载提示
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
        // 将JSON字符串解析为Map
        Map<String, Object> resultMap = objectMapper.readValue(jsonReport, Map.class);

        // 确保所有必需的字段都有默认值
        ensureRequiredFields(resultMap);

        return resultMap;
    }

    /**
     * 检查是否包含必要的字段
     */
    private boolean hasRequiredFields(Map<String, Object> resultMap) {
        // 检查关键字段是否存在
        boolean hasStandardScore = resultMap.containsKey("standardScore") && resultMap.get("standardScore") != null;
        boolean hasAnxietyLevel = resultMap.containsKey("anxietyLevel") && resultMap.get("anxietyLevel") != null;

        // 只要包含标准分和焦虑程度就认为数据有效
        return hasStandardScore && hasAnxietyLevel;
    }

    /**
     * 确保所有必需的字段都有值
     */
    private void ensureRequiredFields(Map<String, Object> resultMap) {
        // 标准分
        if (!resultMap.containsKey("standardScore") || resultMap.get("standardScore") == null) {
            resultMap.put("standardScore", 0);
        }

        // 原始总分
        if (!resultMap.containsKey("rawTotalScore") || resultMap.get("rawTotalScore") == null) {
            resultMap.put("rawTotalScore", 0);
        }

        // 焦虑程度
        if (!resultMap.containsKey("anxietyLevel") || resultMap.get("anxietyLevel") == null) {
            resultMap.put("anxietyLevel", "无焦虑");
        }

        // 结果解读
        if (!resultMap.containsKey("interpretation") || resultMap.get("interpretation") == null) {
            resultMap.put("interpretation", "");
        }

        // 建议措施
        if (!resultMap.containsKey("suggestions") || resultMap.get("suggestions") == null) {
            resultMap.put("suggestions", "");
        }

        // 建议列表
        if (!resultMap.containsKey("suggestionList") || resultMap.get("suggestionList") == null) {
            resultMap.put("suggestionList", new ArrayList<>());
        }

        // 维度得分
        if (!resultMap.containsKey("dimensionScores") || resultMap.get("dimensionScores") == null) {
            resultMap.put("dimensionScores", new HashMap<>());
        }

        // 程度描述
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
        Integer standardScore = (Integer) resultMap.get("standardScore");
        Integer rawTotalScore = (Integer) resultMap.get("rawTotalScore");
        String anxietyLevel = (String) resultMap.get("anxietyLevel");
        String interpretation = (String) resultMap.get("interpretation");
        String suggestions = (String) resultMap.get("suggestions");
        List<String> suggestionList = (List<String>) resultMap.get("suggestionList");
        Map<String, Object> dimensionScores = (Map<String, Object>) resultMap.get("dimensionScores");
        String levelDescription = (String) resultMap.get("levelDescription");

        // 计算进度条宽度
        int progressWidth = Math.min(standardScore, 100);

        // 获取焦虑程度的颜色配置
        Map<String, String> levelColors = getLevelColors(anxietyLevel);

        // 准备建议列表（如果没有，从suggestions字符串解析）
        List<String> finalSuggestionList = prepareSuggestionList(suggestionList, suggestions);

        // 将数据放入上下文
        context.setVariable("standardScore", standardScore);
        context.setVariable("rawTotalScore", rawTotalScore);
        context.setVariable("anxietyLevel", anxietyLevel);
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

        // 如果没有程度描述，则生成一个
        if (levelDescription == null || levelDescription.trim().isEmpty()) {
            context.setVariable("levelDescription", getLevelDescription(anxietyLevel, standardScore));
        } else {
            context.setVariable("levelDescription", levelDescription);
        }

        return context;
    }

    /**
     * 准备建议列表
     */
    private List<String> prepareSuggestionList(List<String> suggestionList, String suggestions) {
        // 如果已经有建议列表，直接使用
        if (suggestionList != null && !suggestionList.isEmpty()) {
            return suggestionList;
        }

        // 否则从suggestions字符串解析
        List<String> result = new ArrayList<>();
        if (suggestions != null && !suggestions.trim().isEmpty()) {
            // 尝试不同的分隔符
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

        // 如果仍然为空，添加默认建议
        if (result.isEmpty()) {
            result.add("保持规律作息，保证充足睡眠");
            result.add("学习放松技巧，如深呼吸、渐进性肌肉放松");
            result.add("适当进行有氧运动，如散步、瑜伽、游泳");
            result.add("练习正念冥想，提高当下意识");
            result.add("减少咖啡因和酒精摄入");
        }

        return result;
    }

    /**
     * 根据焦虑程度获取颜色配置
     */
    private Map<String, String> getLevelColors(String anxietyLevel) {
        Map<String, String> colors = new HashMap<>();

        switch (anxietyLevel) {
            case "无焦虑":
                colors.put("gradient", "linear-gradient(135deg, #4CAF50, #8BC34A)");
                colors.put("icon", "😊");
                colors.put("score", "#4CAF50");
                colors.put("progress", "linear-gradient(90deg, #4CAF50, #8BC34A)");
                colors.put("border", "#4CAF50");
                colors.put("levelBg", "#E8F5E9");
                colors.put("suggestionBg", "#E8F5E9");
                break;
            case "轻度焦虑":
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "😐");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#e8f4fd");
                colors.put("suggestionBg", "#e8f4fd");
                break;
            case "中度焦虑":
                colors.put("gradient", "linear-gradient(135deg, #FF9800, #FFB74D)");
                colors.put("icon", "😟");
                colors.put("score", "#FF9800");
                colors.put("progress", "linear-gradient(90deg, #FF9800, #FFB74D)");
                colors.put("border", "#FF9800");
                colors.put("levelBg", "#FFF3E0");
                colors.put("suggestionBg", "#FFF3E0");
                break;
            case "重度焦虑":
                colors.put("gradient", "linear-gradient(135deg, #F44336, #E57373)");
                colors.put("icon", "😨");
                colors.put("score", "#F44336");
                colors.put("progress", "linear-gradient(90deg, #F44336, #E57373)");
                colors.put("border", "#F44336");
                colors.put("levelBg", "#FFEBEE");
                colors.put("suggestionBg", "#FFEBEE");
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
            case "无焦虑":
                return "您的情绪状态良好，处于正常范围。标准分" + score + "分，表明没有明显的焦虑症状。";
            case "轻度焦虑":
                return "您的焦虑症状处于轻度水平，标准分" + score + "分。建议关注情绪变化，适当进行心理调适。";
            case "中度焦虑":
                return "您的焦虑症状处于中度水平，标准分" + score + "分。建议寻求专业心理咨询或心理治疗。";
            case "重度焦虑":
                return "您的焦虑症状处于重度水平，标准分" + score + "分。建议尽快寻求专业精神科医生的帮助。";
            default:
                return "您的焦虑症状处于" + anxietyLevel + "水平，标准分" + score + "分。";
        }
    }
}