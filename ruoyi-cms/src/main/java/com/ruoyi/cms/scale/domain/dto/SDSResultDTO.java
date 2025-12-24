package com.ruoyi.cms.scale.domain.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * SDS测评结果数据传输对象
 */
@Data
public class SDSResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 基本得分信息
    private Integer rawTotalScore;      // 原始总分 (20-80分)
    private Integer standardScore;      // 标准分 (25-100分)
    private String depressionLevel;     // 抑郁程度
    private String levelDescription;    // 程度描述
    private String interpretation;      // 结果解读
    private String suggestions;         // 建议措施

    // 维度得分
    private Map<String, Integer> dimensionScores;

    // 建议措施列表
    private List<String> suggestionList;

    // 用户信息（用于模板展示）
    private String userName;            // 用户名
    private String nickName;            // 昵称
    private String sex;                 // 性别
    private String phoneNumber;         // 手机号
    private String commitTime;          // 提交时间

    // 额外信息
    private String reportTitle = "抑郁自评测试报告";
    private String scaleName = "抑郁自评量表(SDS)";
    private String evaluationTime;      // 测评时间
    private String evaluator;           // 测评人

    // 用于模板展示的进度条宽度
    private Integer progressWidth;

    // 主题颜色
    private String themeColor;

    // 构造函数
    public SDSResultDTO() {
    }

    /**
     * 计算并设置进度条宽度
     */
    public void calculateProgressWidth() {
        if (standardScore != null) {
            this.progressWidth = Math.min(standardScore, 100);
        }
    }

    /**
     * 根据抑郁程度设置主题颜色
     */
    public void setThemeColorByLevel() {
        if (depressionLevel != null) {
            switch (depressionLevel) {
                case "无抑郁":
                    this.themeColor = "#4CAF50"; // 绿色
                    break;
                case "轻度抑郁":
                    this.themeColor = "#4a90e2"; // 蓝色
                    break;
                case "中度抑郁":
                    this.themeColor = "#FF9800"; // 橙色
                    break;
                case "重度抑郁":
                    this.themeColor = "#F44336"; // 红色
                    break;
                default:
                    this.themeColor = "#4a90e2"; // 蓝色
            }
        } else {
            this.themeColor = "#4a90e2";
        }
    }

    /**
     * 获取完整的报告标题
     */
    public String getFullReportTitle() {
        return reportTitle + " - " + scaleName;
    }
}