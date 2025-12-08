package com.ruoyi.cms.myboard.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 人员统计分析VO
 *
 * @author ruoyi
 * @date 2025-12-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserStatisticsVO", description = "人员统计分析VO")
public class UserStatisticsVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("人员类型")
    private String userType;

    @ApiModelProperty("归属部门")
    private String deptId;

    // 统计结果字段
    @ApiModelProperty("用户总数")
    private Integer totalUsers;

    @ApiModelProperty("警官人数")
    private Integer policeCount;

    @ApiModelProperty("出所人员人数")
    private Integer releasedCount;

    @ApiModelProperty("社康社戒人员总数")
    private Integer rehabCount;

    @ApiModelProperty("游客及其它人员总数")
    private Integer otherCount;

    @ApiModelProperty("用户类型数据")
    private List<ChartDataVo> userTypeData;

    @ApiModelProperty("每日注册数据")
    private ChartDataVo dailyRegisterData;

    @ApiModelProperty("每周注册数据")
    private ChartDataVo weeklyRegisterData;

    @ApiModelProperty("每月注册数据")
    private ChartDataVo monthlyRegisterData;

    @ApiModelProperty("每日登录数据")
    private ChartDataVo dailyLoginData;
}