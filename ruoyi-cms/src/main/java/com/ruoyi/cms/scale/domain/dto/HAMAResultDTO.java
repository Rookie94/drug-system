package com.ruoyi.cms.scale.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class HAMAResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本得分信息
    private Integer rawTotalScore;      // 原始总分 (0-56分)
    private Integer standardScore;      // HAMA标准分（同原始分）
    private String anxietyLevel;        // 焦虑程度
    private String levelDescription;    // 程度描述
    private String interpretation;      // 结果解读
    private String suggestions;         // 建议措施

    // 维度得分（HAMA分为精神性焦虑和躯体性焦虑）
    private Map<String, Integer> dimensionScores;

    // 因子分（HAMA的7个因子）
    private Map<String, Double> factorScores;

    // 症状详情
    private Map<String, List<String>> symptomDetails;

    // 建议措施列表
    private List<String> suggestionList;

    // 用户信息
    private String userName;
    private String nickName;
    private String sex;
    private String phoneNumber;
    private String commitTime;

    // 额外信息
    private String reportTitle = "汉密顿焦虑量表报告";
    private String scaleName = "汉密顿焦虑量表(HAMA)";
    private String evaluationTime;
    private String evaluator;

    // 用于模板展示的进度条宽度
    private Integer progressWidth;

    // 主题颜色
    private String themeColor;

    // 因子分析描述
    private String factorAnalysis;

    // 临床意义
    private String clinicalSignificance;

    public HAMAResultDTO() {
    }

    public void calculateProgressWidth() {
        if (standardScore != null) {
            // HAMA总分56分，转换为100%比例
            this.progressWidth = (int) Math.min((standardScore * 100.0 / 56), 100);
        }
    }

    public void setThemeColorByLevel() {
        if (anxietyLevel != null) {
            switch (anxietyLevel) {
                case "无焦虑症状":
                    this.themeColor = "#4CAF50"; // 绿色
                    break;
                case "可能有焦虑":
                    this.themeColor = "#4a90e2"; // 蓝色
                    break;
                case "肯定有焦虑":
                    this.themeColor = "#FF9800"; // 橙色
                    break;
                case "肯定有明显焦虑":
                    this.themeColor = "#F44336"; // 红色
                    break;
                case "可能有严重焦虑":
                    this.themeColor = "#9C27B0"; // 紫色
                    break;
                default:
                    this.themeColor = "#4a90e2";
            }
        } else {
            this.themeColor = "#4a90e2";
        }
    }

    public String getFullReportTitle() {
        return reportTitle + " - " + scaleName;
    }
}