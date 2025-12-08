package com.ruoyi.cms.myboard.mapper;

import com.ruoyi.cms.myboard.domain.vo.AccessLogVO;
import com.ruoyi.cms.myboard.domain.vo.ResStatisticsQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ResStatisticsMapper {

    /**
     * 查询访问日志
     */
    List<AccessLogVO> selectAccessLogs(@Param("query") ResStatisticsQueryVO queryVO);

    /**
     * 统计按日期访问量
     */
    List<Map<String, Object>> selectDailyAccessData(@Param("query") ResStatisticsQueryVO queryVO);

    /**
     * 统计按用户类型访问量
     */
    List<Map<String, Object>> selectUserTypeAccessData(@Param("query") ResStatisticsQueryVO queryVO);

    /**
     * 统计按资源分类访问量
     */
    List<Map<String, Object>> selectModuleAccessData(@Param("query") ResStatisticsQueryVO queryVO);

    /**
     * 统计热门文章
     */
    List<Map<String, Object>> selectTopArticles(@Param("query") ResStatisticsQueryVO queryVO);
}