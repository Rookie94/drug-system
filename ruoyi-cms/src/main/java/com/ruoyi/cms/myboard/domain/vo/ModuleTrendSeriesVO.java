package com.ruoyi.cms.myboard.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ModuleTrendSeriesVO {
    private String name;
    private List<Integer> data;
}