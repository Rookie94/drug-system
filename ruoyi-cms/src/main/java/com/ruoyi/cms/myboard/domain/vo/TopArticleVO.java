package com.ruoyi.cms.myboard.domain.vo;

import lombok.Data;

@Data
public class TopArticleVO {
    private Long id;
    private String title;
    private String module;
    private Integer views;
    private String date;
}