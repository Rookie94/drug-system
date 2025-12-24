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
public class LSIAMobileStrategy implements ITemplateStrategy {

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LoggerFactory.getLogger(LSIAMobileStrategy.class);

    @Override
    public String getTemplate(Long contextId, String deviceType, LbsResultsVo lbsResults) {
        logger.info("LSIA移动端报告生成开始 - contextId: {}, deviceType: {}", contextId, deviceType);

        try {
            // 1. 检查测评结果是否为空
            if (lbsResults == null) {
                logger.warn("测评结果为空，返回加载模板");
                // 返回默认的加载模板
                return renderLoadingTemplate();
            }

            // 2. 解析jsonReport字段中的LSIA结果
            String jsonReport = lbsResults.getJsonReport();
            if (jsonReport == null || jsonReport.trim().isEmpty()) {
                logger.warn("JSON报告数据为空，返回加载模板");
                // 如果没有报告数据，返回加载模板
                return renderLoadingTemplate();
            }

            logger.debug("开始解析JSON报告数据，长度: {}", jsonReport.length());

            // 3. 尝试解析JSON结果
            Map<String, Object> resultMap;
            try {
                resultMap = parseResultJson(jsonReport);
                logger.info("JSON报告解析成功，包含字段数: {}", resultMap.size());
            } catch (Exception e) {
                logger.error("JSON报告解析失败，返回加载模板，错误信息: {}", e.getMessage(), e);
                // JSON解析失败，可能数据格式不正确，返回加载模板
                return renderLoadingTemplate();
            }

            // 4. 检查是否包含必要的字段
            if (!hasRequiredFields(resultMap)) {
                logger.warn("JSON报告缺少必要字段，返回加载模板。包含的字段: {}", resultMap.keySet());
                // 缺少必要字段，返回加载模板
                return renderLoadingTemplate();
            }

            logger.info("JSON报告包含所有必要字段，开始准备模板上下文");

            // 5. 准备Thymeleaf上下文数据
            Context context = prepareTemplateContext(resultMap);
            logger.debug("模板上下文准备完成，包含变量: {}", context.getVariableNames());

            try {
                logger.info("开始处理LSIA移动端模板: mobile/family/lsia");

                // 尝试处理模板
                String result = templateEngine.process("mobile/family/lsia", context);
                logger.info("LSIA移动端模板处理成功，生成HTML长度: {}", result.length());
                return result;
            } catch (TemplateInputException e) {
                logger.error("模板输入异常 - 模板未找到或无法读取: ", e);
                logger.error("模板异常详情: {}:{}", e.getTemplateName(), e.getLine());
                throw e;
            } catch (TemplateProcessingException e) {
                logger.error("模板处理异常 - 模板内容错误: ", e);
                logger.error("模板处理异常详情: {}:{}", e.getTemplateName(), e.getLine());
                throw e;
            } catch (Exception e) {
                logger.error("处理LSIA模板时发生未预期异常: ", e);
                logger.error("异常类型: {}, 异常消息: {}", e.getClass().getName(), e.getMessage());
                throw e;
            }

        } catch (Exception e) {
            logger.error("LSIA移动端报告生成过程中发生异常，返回加载模板", e);
            // 发生异常时返回加载模板
            return renderLoadingTemplate();
        } finally {
            logger.info("LSIA移动端报告生成结束 - contextId: {}", contextId);
        }
    }

    /**
     * 渲染默认的加载模板
     */
    private String renderLoadingTemplate() {
        try {
            logger.info("渲染默认加载模板");
            // 创建一个空的上下文，因为默认模板不需要参数
            Context context = new Context();
            // 返回默认的加载模板
            return templateEngine.process("mobile/default/index", context);
        } catch (Exception e) {
            logger.error("渲染默认加载模板失败，返回简单HTML", e);
            // 如果加载模板渲染失败，返回一个简单的加载提示
            return generateSimpleLoadingHtml();
        }
    }

    /**
     * 生成简单的加载HTML（备用）
     */
    private String generateSimpleLoadingHtml() {
        logger.warn("生成简单加载HTML作为后备方案");
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
        logger.debug("开始解析JSON字符串: {}", jsonReport.substring(0, Math.min(100, jsonReport.length())) + "...");

        // 将JSON字符串解析为Map
        Map<String, Object> resultMap = objectMapper.readValue(jsonReport, Map.class);
        logger.debug("JSON解析成功，获取到Map类型: {}", resultMap.getClass().getName());

        // 确保所有必需的字段都有默认值
        ensureRequiredFields(resultMap);
        logger.debug("必要字段验证完成");

        return resultMap;
    }

    /**
     * 检查是否包含必要的字段
     */
    private boolean hasRequiredFields(Map<String, Object> resultMap) {
        logger.debug("检查必要字段，当前字段数: {}", resultMap.size());

        // 检查关键字段是否存在
        boolean hasStandardScore = resultMap.containsKey("standardScore") && resultMap.get("standardScore") != null;
        boolean hasSatisfactionLevel = resultMap.containsKey("satisfactionLevel") && resultMap.get("satisfactionLevel") != null;

        logger.debug("字段检查结果 - standardScore: {}, satisfactionLevel: {}", hasStandardScore, hasSatisfactionLevel);

        // 只要包含总分和满意度程度就认为数据有效
        return hasStandardScore && hasSatisfactionLevel;
    }

    /**
     * 确保所有必需的字段都有值
     */
    private void ensureRequiredFields(Map<String, Object> resultMap) {
        logger.debug("开始确保必要字段都有值");

        int updatedCount = 0;

        // 标准分
        if (!resultMap.containsKey("standardScore") || resultMap.get("standardScore") == null) {
            resultMap.put("standardScore", 0);
            updatedCount++;
            logger.debug("添加默认standardScore: 0");
        }

        // 原始总分
        if (!resultMap.containsKey("rawTotalScore") || resultMap.get("rawTotalScore") == null) {
            resultMap.put("rawTotalScore", 0);
            updatedCount++;
            logger.debug("添加默认rawTotalScore: 0");
        }

        // 满意度程度
        if (!resultMap.containsKey("satisfactionLevel") || resultMap.get("satisfactionLevel") == null) {
            resultMap.put("satisfactionLevel", "一般满意");
            updatedCount++;
            logger.debug("添加默认satisfactionLevel: 一般满意");
        }

        // 结果解读
        if (!resultMap.containsKey("interpretation") || resultMap.get("interpretation") == null) {
            resultMap.put("interpretation", "");
            updatedCount++;
            logger.debug("添加默认interpretation: 空字符串");
        }

        // 建议措施
        if (!resultMap.containsKey("suggestions") || resultMap.get("suggestions") == null) {
            resultMap.put("suggestions", "");
            updatedCount++;
            logger.debug("添加默认suggestions: 空字符串");
        }

        // 建议列表
        if (!resultMap.containsKey("suggestionList") || resultMap.get("suggestionList") == null) {
            resultMap.put("suggestionList", new ArrayList<>());
            updatedCount++;
            logger.debug("添加默认suggestionList: 空列表");
        }

        // 维度得分
        if (!resultMap.containsKey("dimensionScores") || resultMap.get("dimensionScores") == null) {
            resultMap.put("dimensionScores", new HashMap<>());
            updatedCount++;
            logger.debug("添加默认dimensionScores: 空Map");
        }

        // 程度描述
        if (!resultMap.containsKey("levelDescription") || resultMap.get("levelDescription") == null) {
            resultMap.put("levelDescription", "");
            updatedCount++;
            logger.debug("添加默认levelDescription: 空字符串");
        }

        logger.debug("确保必要字段完成，共更新{}个字段", updatedCount);
    }

    /**
     * 准备模板上下文数据
     */
    private Context prepareTemplateContext(Map<String, Object> resultMap) {
        logger.info("开始准备模板上下文数据");

        Context context = new Context();

        // 从resultMap中提取数据
        Integer standardScore = (Integer) resultMap.get("standardScore");
        Integer rawTotalScore = (Integer) resultMap.get("rawTotalScore");
        String satisfactionLevel = (String) resultMap.get("satisfactionLevel");
        String interpretation = (String) resultMap.get("interpretation");
        String suggestions = (String) resultMap.get("suggestions");
        List<String> suggestionList = (List<String>) resultMap.get("suggestionList");
        Map<String, Object> dimensionScores = (Map<String, Object>) resultMap.get("dimensionScores");
        String levelDescription = (String) resultMap.get("levelDescription");

        logger.debug("提取的数据 - standardScore: {}, satisfactionLevel: {}, 维度数: {}, 建议数: {}",
                standardScore, satisfactionLevel,
                dimensionScores != null ? dimensionScores.size() : 0,
                suggestionList != null ? suggestionList.size() : 0);

        // 计算进度条宽度（LSIA总分40分，转换为百分比）
        int progressWidth = standardScore * 100 / 40;
        logger.debug("计算进度条宽度: {}% (标准分: {})", progressWidth, standardScore);

        // 获取满意度程度的颜色配置
        Map<String, String> levelColors = getLevelColors(satisfactionLevel);
        logger.debug("获取颜色配置 - 主题颜色: {}", levelColors.get("score"));

        // 准备建议列表（如果没有，从suggestions字符串解析）
        List<String> finalSuggestionList = prepareSuggestionList(suggestionList, suggestions);
        logger.debug("最终建议列表大小: {}", finalSuggestionList.size());

        // 将数据放入上下文
        context.setVariable("standardScore", standardScore);
        context.setVariable("rawTotalScore", rawTotalScore);
        context.setVariable("satisfactionLevel", satisfactionLevel);
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
            String generatedDescription = getLevelDescription(satisfactionLevel, standardScore);
            context.setVariable("levelDescription", generatedDescription);
            logger.debug("生程度度描述: {}", generatedDescription);
        } else {
            context.setVariable("levelDescription", levelDescription);
            logger.debug("使用已有程度描述");
        }

        logger.info("模板上下文数据准备完成，共设置{}个变量", context.getVariableNames().size());

        return context;
    }

    /**
     * 准备建议列表
     */
    private List<String> prepareSuggestionList(List<String> suggestionList, String suggestions) {
        logger.debug("开始准备建议列表，原始列表大小: {}, 建议字符串长度: {}",
                suggestionList != null ? suggestionList.size() : 0,
                suggestions != null ? suggestions.length() : 0);

        // 如果已经有建议列表，直接使用
        if (suggestionList != null && !suggestionList.isEmpty()) {
            logger.debug("使用现有建议列表，大小: {}", suggestionList.size());
            return suggestionList;
        }

        // 否则从suggestions字符串解析
        List<String> result = new ArrayList<>();
        if (suggestions != null && !suggestions.trim().isEmpty()) {
            logger.debug("从字符串解析建议列表");
            // 尝试不同的分隔符
            String[] items;
            if (suggestions.contains("\n")) {
                items = suggestions.split("\n");
                logger.debug("使用换行符分割建议，分割数: {}", items.length);
            } else if (suggestions.contains("；")) {
                items = suggestions.split("；");
                logger.debug("使用中文分号分割建议，分割数: {}", items.length);
            } else if (suggestions.contains(";")) {
                items = suggestions.split(";");
                logger.debug("使用英文分号分割建议，分割数: {}", items.length);
            } else {
                items = new String[]{suggestions};
                logger.debug("未找到分隔符，将整个字符串作为一条建议");
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
            logger.debug("建议列表为空，添加默认建议");
            result.add("关注生活中的积极方面，培养感恩心态");
            result.add("加强社交活动，建立有意义的人际关系");
            result.add("设定切实可行的短期和长期目标");
            result.add("培养新的兴趣爱好或学习新技能");
            result.add("定期进行体育锻炼，保持身心健康");
        }

        logger.debug("最终建议列表大小: {}", result.size());
        return result;
    }

    /**
     * 根据满意度程度获取颜色配置
     */
    private Map<String, String> getLevelColors(String satisfactionLevel) {
        logger.debug("获取满意度级别颜色配置: {}", satisfactionLevel);

        Map<String, String> colors = new HashMap<>();

        switch (satisfactionLevel) {
            case "非常满意":
                colors.put("gradient", "linear-gradient(135deg, #4CAF50, #8BC34A)");
                colors.put("icon", "😊");
                colors.put("score", "#4CAF50");
                colors.put("progress", "linear-gradient(90deg, #4CAF50, #8BC34A)");
                colors.put("border", "#4CAF50");
                colors.put("levelBg", "#E8F5E9");
                colors.put("suggestionBg", "#E8F5E9");
                logger.debug("使用非常满意配色方案 (绿色)");
                break;
            case "一般满意":
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "😐");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#e8f4fd");
                colors.put("suggestionBg", "#e8f4fd");
                logger.debug("使用一般满意配色方案 (蓝色)");
                break;
            case "不太满意":
                colors.put("gradient", "linear-gradient(135deg, #FF9800, #FFB74D)");
                colors.put("icon", "😟");
                colors.put("score", "#FF9800");
                colors.put("progress", "linear-gradient(90deg, #FF9800, #FFB74D)");
                colors.put("border", "#FF9800");
                colors.put("levelBg", "#FFF3E0");
                colors.put("suggestionBg", "#FFF3E0");
                logger.debug("使用不太满意配色方案 (橙色)");
                break;
            case "非常不满意":
                colors.put("gradient", "linear-gradient(135deg, #F44336, #E57373)");
                colors.put("icon", "😨");
                colors.put("score", "#F44336");
                colors.put("progress", "linear-gradient(90deg, #F44336, #E57373)");
                colors.put("border", "#F44336");
                colors.put("levelBg", "#FFEBEE");
                colors.put("suggestionBg", "#FFEBEE");
                logger.debug("使用非常不满意配色方案 (红色)");
                break;
            default:
                colors.put("gradient", "linear-gradient(135deg, #4a90e2, #6a89cc)");
                colors.put("icon", "📊");
                colors.put("score", "#4a90e2");
                colors.put("progress", "linear-gradient(90deg, #4a90e2, #6a89cc)");
                colors.put("border", "#4a90e2");
                colors.put("levelBg", "#f8f9fa");
                colors.put("suggestionBg", "#f8f9fa");
                logger.debug("使用默认配色方案 (蓝色)");
        }

        return colors;
    }

    /**
     * 获取满意度程度描述
     */
    private String getLevelDescription(String satisfactionLevel, int score) {
        logger.debug("生成满意度级别描述: {} (分数: {})", satisfactionLevel, score);

        String description;

        switch (satisfactionLevel) {
            case "非常满意":
                description = "您的生活满意度非常高，标准分" + score + "分（满分40分），表明您对生活的多个方面都感到非常满意。";
                break;
            case "一般满意":
                description = "您的生活满意度处于中等水平，标准分" + score + "分，表明您对生活总体上持积极态度，但仍有提升空间。";
                break;
            case "不太满意":
                description = "您的生活满意度较低，标准分" + score + "分，表明您对生活的某些方面存在不满意，可能需要调整心态或改变生活方式。";
                break;
            case "非常不满意":
                description = "您的生活满意度很低，标准分" + score + "分，表明您对生活的多个方面都感到不满意，建议关注心理健康和生活质量。";
                break;
            default:
                description = "您的生活满意度处于" + satisfactionLevel + "水平，标准分" + score + "分。";
        }

        logger.debug("生成描述: {}", description);
        return description;
    }
}