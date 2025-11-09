package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysResLog;
import com.ruoyi.system.service.ISysResLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 资源日志Controller
 * 
 * @author admin
 * @date 2025-11-09
 */
@RestController
@RequestMapping("/system/reslog")
public class SysResLogController extends BaseController
{
    @Autowired
    private ISysResLogService sysResLogService;

    /**
     * 查询资源日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:reslog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysResLog sysResLog)
    {
        startPage();
        List<SysResLog> list = sysResLogService.selectSysResLogList(sysResLog);
        return getDataTable(list);
    }

    /**
     * 导出资源日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:reslog:export')")
    @Log(title = "资源日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysResLog sysResLog)
    {
        List<SysResLog> list = sysResLogService.selectSysResLogList(sysResLog);
        ExcelUtil<SysResLog> util = new ExcelUtil<SysResLog>(SysResLog.class);
        util.exportExcel(response, list, "资源日志数据");
    }

    /**
     * 获取资源日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:reslog:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(sysResLogService.selectSysResLogByLogId(logId));
    }

    /**
     * 新增资源日志
     */
    @PreAuthorize("@ss.hasPermi('system:reslog:add')")
    @Log(title = "资源日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysResLog sysResLog)
    {
        return toAjax(sysResLogService.insertSysResLog(sysResLog));
    }

    /**
     * 修改资源日志
     */
    @PreAuthorize("@ss.hasPermi('system:reslog:edit')")
    @Log(title = "资源日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysResLog sysResLog)
    {
        return toAjax(sysResLogService.updateSysResLog(sysResLog));
    }

    /**
     * 删除资源日志
     */
    @PreAuthorize("@ss.hasPermi('system:reslog:remove')")
    @Log(title = "资源日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(sysResLogService.deleteSysResLogByLogIds(logIds));
    }
}
