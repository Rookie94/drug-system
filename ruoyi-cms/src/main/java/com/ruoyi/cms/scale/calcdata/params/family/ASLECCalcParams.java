package com.ruoyi.cms.scale.calcdata.params.family;

import lombok.Data;
import java.util.List;

@Data
public class ASLECCalcParams {
    // ASLEC参数 - 维度划分
    private List<Long> interpersonalDimension;   // 人际关系维度题目ID（1-12题）
    private List<Long> learningPressureDimension; // 学习压力维度题目ID（13-18题）
    private List<Long> punishmentDimension;      // 受惩罚维度题目ID（19-23题）
    private List<Long> lossDimension;           // 丧失维度题目ID（24-27题）
    private List<Long> healthAdaptationDimension; // 健康适应维度题目ID（28-29题）
    private List<Long> otherDimension;          // 其他维度题目ID（30-33题）
}