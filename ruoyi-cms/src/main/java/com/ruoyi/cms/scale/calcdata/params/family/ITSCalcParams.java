package com.ruoyi.cms.scale.calcdata.params.family;

import lombok.Data;
import java.util.List;

@Data
public class ITSCalcParams {
    private Double rate = 1.0;  // 信任指数转换率
    private List<Integer> descTopic;  // 反向计分题目ID
    private Integer totalQuestions = 18;  // 总题目数
    private Integer maxScorePerQuestion = 7;  // 每道题最大得分
}