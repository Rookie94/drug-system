package com.ruoyi.cms.myboard.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("资源统计查询参数")
public class ResStatisticsQueryVO {

    @ApiModelProperty(value = "开始日期（格式：yyyy-MM-dd）")
    private String startDate;

    @ApiModelProperty(value = "结束日期（格式：yyyy-MM-dd）")
    private String endDate;

    @ApiModelProperty("用户类型")
    private String userType;

    @ApiModelProperty("模块筛选")
    private String moduleFilter;
}