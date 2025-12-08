package com.ruoyi.cms.myboard.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ModuleTrendDataVO {
    private List<String> dates;
    private List<ModuleTrendSeriesVO> series;
}