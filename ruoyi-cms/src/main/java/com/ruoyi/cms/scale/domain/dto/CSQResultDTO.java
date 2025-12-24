package com.ruoyi.cms.scale.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class CSQResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本得分信息
    private Integer rawTotalScore;      // 原始总分
    private Map<String, Integer> factorScores;  // 各因子得分
    private Map<String, Double> factorRatios;   // 各因子比例
    private String copingStyle;         // 主要应对方式
    private String styleDescription;    // 应对方式描述
    private String interpretation;      // 结果解读
    private String suggestions;         // 建议措施

    // 应对方式详情
    private Map<String, List<String>> styleDetails;

    // 建议措施列表
    private List<String> suggestionList;

    // 用户信息
    private String userName;
    private String nickName;
    private String sex;
    private String phoneNumber;
    private String commitTime;

    // 额外信息
    private String reportTitle = "应付方式问卷测试报告";
    private String scaleName = "应付方式问卷(CSQ)";
    private String evaluationTime;
    private String evaluator;

    // 用于模板展示的进度条宽度（针对主要因子）
    private Integer progressWidth;

    // 主题颜色
    private String themeColor;

    // 应对方式倾向
    private String copingTendency;

    // 各因子解释
    private Map<String, String> factorInterpretations;

    public CSQResultDTO() {
    }

    public void calculateProgressWidth() {
        if (factorScores != null && !factorScores.isEmpty()) {
            // 使用主要因子的得分作为进度（得分范围0-12，转换为百分比）
            int maxScore = 0;
            for (Integer score : factorScores.values()) {
                if (score > maxScore) {
                    maxScore = score;
                }
            }
            this.progressWidth = Math.min((int)((maxScore / 12.0) * 100), 100);
        }
    }

    public void setThemeColorByCopingStyle() {
        if (copingStyle != null) {
            switch (copingStyle) {
                case "成熟型":
                    this.themeColor = "#4CAF50"; // 绿色
                    break;
                case "混合型":
                    this.themeColor = "#4a90e2"; // 蓝色
                    break;
                case "不成熟型":
                    this.themeColor = "#FF9800"; // 橙色
                    break;
                case "不适当型":
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