package com.ruoyi.cms.myboard.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class DailyLoginDataVO {
    private List<String> dates;
    private List<Integer> counts;
}