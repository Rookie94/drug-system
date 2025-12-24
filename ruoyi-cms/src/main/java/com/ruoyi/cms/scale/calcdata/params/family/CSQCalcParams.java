package com.ruoyi.cms.scale.calcdata.params.family;

import lombok.Data;
import java.util.List;

@Data
public class CSQCalcParams {
    private Double rate = 1.0;  // CSQ不需要标准分转换
    private List<Integer> descTopic;  // 反向计分题目ID（根据CSQ需要）

    // CSQ的6个应对方式因子
    private List<Integer> problemSolving;     // 解决问题
    private List<Integer> selfBlame;         // 自责
    private List<Integer> helpSeeking;       // 求助
    private List<Integer> fantasy;           // 幻想
    private List<Integer> retreat;           // 退避
    private List<Integer> rationalization;   // 合理化
}