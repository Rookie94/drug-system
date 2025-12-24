package com.ruoyi.cms.scale.calcdata.params.family;

import lombok.Data;
import java.util.List;

@Data
public class LSIACalcParams {
    private Double rate = 1.0;  // LSIA不需要标准分转换，保持1.0
    private List<Integer> descTopic;  // 反向计分题目ID
}