package com.ruoyi.cms.myboard.mapper;

import com.ruoyi.cms.myboard.domain.vo.UserStatisticsVo;


import java.util.List;
import java.util.Map;

/**
 * 人员统计分析Mapper接口
 *
 * @author ruoyi
 * @date 2025-12-09
 */
public interface UserStatisticsMapper
{
    /**
     * 查询用户总数和各类型人数
     *
     * @param userStatisticsVo 查询条件
     * @return 用户统计数量
     */
    Map<String, Object> selectUserCounts(UserStatisticsVo userStatisticsVo);

    /**
     * 查询用户类型统计
     *
     * @param userStatisticsVo 查询条件
     * @return 用户类型统计列表
     */
    List<Map<String, Object>> selectUserTypeStatistics(UserStatisticsVo userStatisticsVo);

    /**
     * 查询每日注册统计
     *
     * @param userStatisticsVo 查询条件
     * @return 每日注册统计列表
     */
    List<Map<String, Object>> selectDailyRegisterStatistics(UserStatisticsVo userStatisticsVo);

    /**
     * 查询每周注册统计
     *
     * @param userStatisticsVo 查询条件
     * @return 每周注册统计列表
     */
    List<Map<String, Object>> selectWeeklyRegisterStatistics(UserStatisticsVo userStatisticsVo);

    /**
     * 查询每月注册统计
     *
     * @param userStatisticsVo 查询条件
     * @return 每月注册统计列表
     */
    List<Map<String, Object>> selectMonthlyRegisterStatistics(UserStatisticsVo userStatisticsVo);

    /**
     * 查询每日登录统计
     *
     * @param userStatisticsVo 查询条件
     * @return 每日登录统计列表
     */
    List<Map<String, Object>> selectDailyLoginStatistics(UserStatisticsVo userStatisticsVo);
}