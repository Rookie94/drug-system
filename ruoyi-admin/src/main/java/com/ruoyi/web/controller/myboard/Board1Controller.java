package com.ruoyi.web.controller.myboard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.cms.myboard.domain.vo.UserStatisticsVo;
import com.ruoyi.cms.myboard.service.IUserStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 人员统计分析Controller
 *
 * @author ruoyi
 * @date 2025-12-09
 */
@Api(tags = "人员统计分析管理")
@RestController
@RequestMapping("/myboard/board1")
public class Board1Controller extends BaseController
{
    @Autowired
    private IUserStatisticsService userStatisticsService;

    /**
     * 查询人员统计列表
     */
    @ApiOperation("查询人员统计数据")
    @PreAuthorize("@ss.hasPermi('myboard:board1:list')")
    @GetMapping("/list")
    public AjaxResult list(UserStatisticsVo userStatisticsVo)
    {
        // 处理统计数据
        UserStatisticsVo result = userStatisticsService.selectUserStatistics(userStatisticsVo);
        return AjaxResult.success(result);
    }

    /**
     * 导出人员统计数据
     */
    @ApiOperation("导出人员统计数据")
    @PreAuthorize("@ss.hasPermi('myboard:board1:export')")
    @Log(title = "人员统计", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, UserStatisticsVo userStatisticsVo)
    {
        // 导出逻辑实现
        userStatisticsService.exportUserStatistics(response, userStatisticsVo);
    }
}
