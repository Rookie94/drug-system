package com.ruoyi.cms.scale.calcdata.params.standard;

import lombok.Data;
import java.util.List;

@Data
public class HAMACalcParams {
    private Double rate = 1.0;  // HAMA不需要转换率，直接使用原始分
    private List<Integer> descTopic;  // 反向计分题目ID（HAMA通常没有反向计分）
}