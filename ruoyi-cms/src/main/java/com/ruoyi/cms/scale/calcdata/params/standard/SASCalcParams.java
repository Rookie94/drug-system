package com.ruoyi.cms.scale.calcdata.params.standard;

import lombok.Data;
import java.util.List;

@Data
public class SASCalcParams {
    private Double rate = 1.25;  // 标准分转换率
    private List<Integer> descTopic;  // 反向计分题目ID
}