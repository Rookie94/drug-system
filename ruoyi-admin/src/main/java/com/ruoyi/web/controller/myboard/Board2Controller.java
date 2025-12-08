package com.ruoyi.web.controller.myboard;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.cms.myboard.domain.vo.ResStatisticsDataVO;
import com.ruoyi.cms.myboard.domain.vo.ResStatisticsQueryVO;
import com.ruoyi.cms.myboard.service.IResStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 资源访问统计分析
 */
@RestController
@RequestMapping("/myboard/board2")
public class Board2Controller extends BaseController {

    @Autowired
    private IResStatisticsService resStatisticsService;

    /**
     * 获取统计分析数据
     */
    @PreAuthorize("@ss.hasPermi('myboard:board2:list')")
    @PostMapping("/getStatisticsData")
    public AjaxResult getStatisticsData(@RequestBody ResStatisticsQueryVO queryVO) {
        ResStatisticsDataVO data = resStatisticsService.getStatisticsData(queryVO);
        return AjaxResult.success(data);
    }

    /**
     * 导出统计数据
     */
    @PreAuthorize("@ss.hasPermi('myboard:board2:export')")
    @PostMapping("/export")
    public void export(@RequestBody ResStatisticsQueryVO queryVO, HttpServletResponse response) {
        resStatisticsService.exportStatisticsData(queryVO, response);
    }
}