package com.ruoyi.cms.myboard.service;

import com.ruoyi.cms.myboard.domain.vo.UserStatisticsVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 人员统计分析Service接口
 *
 * @author ruoyi
 * @date 2025-12-09
 */
public interface IUserStatisticsService
{
    /**
     * 查询人员统计数据
     *
     * @param userStatisticsVo 查询条件
     * @return 统计数据
     */
    UserStatisticsVo selectUserStatistics(UserStatisticsVo userStatisticsVo);

    /**
     * 导出人员统计数据
     *
     * @param response 响应对象
     * @param userStatisticsVo 查询条件
     */
    void exportUserStatistics(HttpServletResponse response, UserStatisticsVo userStatisticsVo);
}