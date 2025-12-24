package com.ruoyi.cms.scale.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ITSResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本得分信息
    private Integer rawTotalScore;      // 原始总分 (18-126分)
    private Integer standardScore;      // 标准分 (0-100分)
    private String trustLevel;          // 信任程度
    private String levelDescription;    // 程度描述
    private String interpretation;      // 结果解读
    private String suggestions;         // 建议措施

    // 维度得分（ITS可能的维度划分）
    private Map<String, Integer> dimensionScores;

    // 关键信任要素分析
    private Map<String, Double> trustFactors;

    // 信任优势与不足
    private Map<String, List<String>> trustAnalysis;

    // 建议措施列表
    private List<String> suggestionList;

    // 用户信息
    private String userName;
    private String nickName;
    private String sex;
    private String phoneNumber;
    private String commitTime;

    // 额外信息
    private String reportTitle = "人际信任测评报告";
    private String scaleName = "信任量表(ITS)";
    private String evaluationTime;
    private String evaluator;

    // 关系对象信息
    private String targetPerson;  // 测评对象
    private String relationship;  // 关系类型

    // 用于模板展示的进度条宽度
    private Integer progressWidth;

    // 主题颜色
    private String themeColor;

    // 平均分
    private Double averageScore;

    // 信任强度
    private String trustIntensity;

    public ITSResultDTO() {
    }

    public void calculateProgressWidth() {
        if (standardScore != null) {
            this.progressWidth = Math.min(standardScore, 100);
        }
    }

    public void setThemeColorByLevel() {
        if (trustLevel != null) {
            switch (trustLevel) {
                case "非常低信任":
                    this.themeColor = "#F44336"; // 红色
                    break;
                case "较低信任":
                    this.themeColor = "#FF9800"; // 橙色
                    break;
                case "中等信任":
                    this.themeColor = "#4a90e2"; // 蓝色
                    break;
                case "较高信任":
                    this.themeColor = "#4CAF50"; // 绿色
                    break;
                case "非常高信任":
                    this.themeColor = "#2196F3"; // 深蓝色
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

    public void calculateAverageScore() {
        if (rawTotalScore != null && rawTotalScore > 0) {
            this.averageScore = rawTotalScore / 18.0;
        }
    }
}