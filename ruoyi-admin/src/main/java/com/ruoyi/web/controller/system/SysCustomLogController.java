package com.ruoyi.system.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysCustomLog;
import com.ruoyi.system.service.ISysCustomLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自定义日志 信息操作处理
 */
@RestController
@RequestMapping("/system/customLog")
public class SysCustomLogController extends BaseController {

    @Autowired
    private ISysCustomLogService customLogService;

    /**
     * 查询自定义日志列表 (带分页)
     */
    @GetMapping("/list")
    @Log(title = "自定义日志", businessType = BusinessType.OTHER)
    public TableDataInfo list(SysCustomLog sysCustomLog) {
        startPage();
        List<SysCustomLog> list = customLogService.selectCustomLogList(sysCustomLog);
        return getDataTable(list);
    }

    /**
     * 获取自定义日志详细信息
     */
    @GetMapping(value = "/{logId}")
    @Log(title = "自定义日志", businessType = BusinessType.OTHER)
    public AjaxResult getInfo(@PathVariable("logId") Long logId) {
        return AjaxResult.success(customLogService.selectCustomLogById(logId));
    }

    /**
     * 删除自定义日志
     */
    @DeleteMapping("/{logIds}")
    @Log(title = "自定义日志", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long[] logIds) {
        return toAjax(customLogService.deleteCustomLogByIds(logIds));
    }
}