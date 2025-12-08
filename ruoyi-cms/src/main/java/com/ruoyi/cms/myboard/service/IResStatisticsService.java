package com.ruoyi.cms.myboard.service;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.cms.myboard.domain.vo.ResStatisticsDataVO;
import com.ruoyi.cms.myboard.domain.vo.ResStatisticsQueryVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 资源访问统计分析服务接口
 */
public interface IResStatisticsService {

    /**
     * 获取统计分析数据
     *
     * @param queryVO 查询参数
     * @return 统计数据
     */
    ResStatisticsDataVO getStatisticsData(ResStatisticsQueryVO queryVO);

    /**
     * 导出统计数据
     *
     * @param queryVO 查询参数
     * @param response HTTP响应
     */
    void exportStatisticsData(ResStatisticsQueryVO queryVO, HttpServletResponse response);
}