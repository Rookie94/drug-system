package com.ruoyi.cms.myboard.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 图表数据VO
 *
 * @author ruoyi
 * @date 2025-12-09
 */
@Data
@ApiModel(value = "ChartDataVO", description = "图表数据VO")
public class ChartDataVo
{
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("值")
    private Integer value;

    @ApiModelProperty("日期列表（用于折线图、柱状图）")
    private List<String> dates;

    @ApiModelProperty("周列表")
    private List<String> weeks;

    @ApiModelProperty("月列表")
    private List<String> months;

    @ApiModelProperty("数量列表（用于折线图、柱状图）")
    private List<Integer> counts;
}