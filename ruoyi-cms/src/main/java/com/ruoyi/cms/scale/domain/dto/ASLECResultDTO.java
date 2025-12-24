package com.ruoyi.cms.scale.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ASLECResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本得分信息
    private Integer rawTotalScore;        // 原始总分 (0-135分)
    private String stressLevel;           // 压力水平
    private String levelDescription;      // 程度描述
    private String interpretation;        // 结果解读
    private String suggestions;           // 建议措施

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
    private String reportTitle = "青少年生活事件测评报告";
    private String scaleName = "青少年生活事件量表(ASLEC)";
    private String evaluationTime;
    private String evaluator;

    // 用于模板展示的进度条宽度
    private Integer progressWidth;

    // 主题颜色
    private String themeColor;

    // 高压力事件详情
    private Map<String, List<String>> highStressEvents;

    // 影响程度
    private String impactDegree;

    public ASLECResultDTO() {
    }

    public void calculateProgressWidth() {
        if (rawTotalScore != null) {
            // ASLEC总分0-135分，映射到0-100%
            this.progressWidth = (int) Math.min((rawTotalScore * 100.0 / 135), 100);
        }
    }

    public void setThemeColorByLevel() {
        if (stressLevel != null) {
            switch (stressLevel) {
                case "无压力":
                    this.themeColor = "#4CAF50"; // 绿色
                    break;
                case "轻度压力":
                    this.themeColor = "#4a90e2"; // 蓝色
                    break;
                case "中度压力":
                    this.themeColor = "#FF9800"; // 橙色
                    break;
                case "重度压力":
                    this.themeColor = "#F44336"; // 红色
                    break;
                case "极重度压力":
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