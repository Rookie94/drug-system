package com.ruoyi.cms.scale.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class LSIAResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本得分信息
    private Integer rawTotalScore;      // 原始总分 (0-40分)
    private Integer standardScore;      // 标准分 (与原始总分相同)
    private String satisfactionLevel;   // 满意度程度
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
    private String reportTitle = "生活满意度测试报告";
    private String scaleName = "生活满意度量表(LSIA)";
    private String evaluationTime;
    private String evaluator;

    // 用于模板展示的进度条宽度
    private Integer progressWidth;

    // 主题颜色
    private String themeColor;

    // 满意度详情
    private Map<String, List<String>> satisfactionDetails;

    // 影响程度
    private String impactDegree;

    public LSIAResultDTO() {
    }

    public void calculateProgressWidth() {
        if (standardScore != null) {
            // LSIA总分40分，转换为百分比
            this.progressWidth = (int) (standardScore * 100.0 / 40);
        }
    }

    public void setThemeColorByLevel() {
        if (satisfactionLevel != null) {
            switch (satisfactionLevel) {
                case "非常满意":
                    this.themeColor = "#4CAF50"; // 绿色
                    break;
                case "一般满意":
                    this.themeColor = "#4a90e2"; // 蓝色
                    break;
                case "不太满意":
                    this.themeColor = "#FF9800"; // 橙色
                    break;
                case "非常不满意":
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