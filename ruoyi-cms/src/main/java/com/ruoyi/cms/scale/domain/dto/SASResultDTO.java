package com.ruoyi.cms.scale.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class SASResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本得分信息
    private Integer rawTotalScore;      // 原始总分 (20-80分)
    private Integer standardScore;      // 标准分 (25-100分)
    private String anxietyLevel;        // 焦虑程度
    private String levelDescription;    // 程度描述
    private String interpretation;      // 结果解读
    private String suggestions;         // 建议措施

    // 维度得分
    private Map<String, Integer> dimensionScores;

    // 建议措施列表
    private List<String> suggestionList;

    // 用户信息
    private String userName;
    private String nickName;
    private String sex;
    private String phoneNumber;
    private String commitTime;

    // 额外信息
    private String reportTitle = "焦虑自评测试报告";
    private String scaleName = "焦虑自评量表(SAS)";
    private String evaluationTime;
    private String evaluator;

    // 用于模板展示的进度条宽度
    private Integer progressWidth;

    // 主题颜色
    private String themeColor;

    // 焦虑症状详情
    private Map<String, List<String>> symptomDetails;

    // 影响程度
    private String impactDegree;

    public SASResultDTO() {
    }

    public void calculateProgressWidth() {
        if (standardScore != null) {
            this.progressWidth = Math.min(standardScore, 100);
        }
    }

    public void setThemeColorByLevel() {
        if (anxietyLevel != null) {
            switch (anxietyLevel) {
                case "无焦虑":
                    this.themeColor = "#4CAF50"; // 绿色
                    break;
                case "轻度焦虑":
                    this.themeColor = "#4a90e2"; // 蓝色
                    break;
                case "中度焦虑":
                    this.themeColor = "#FF9800"; // 橙色
                    break;
                case "重度焦虑":
                    this.themeColor = "#F44336"; // 红色
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