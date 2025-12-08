package com.ruoyi.cms.myboard.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class TimeDistributionDataVO {
    private List<String> hours;
    private List<Integer> counts;
}