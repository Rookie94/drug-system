package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.res.domain.ResArticles;
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
import com.ruoyi.system.domain.SysArea;
import com.ruoyi.system.service.ISysAreaService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 行政区域Controller
 * 
 * @author admin
 * @date 2025-05-08
 */
@RestController
@RequestMapping("/system/area")
public class SysAreaController extends BaseController
{
    @Autowired
    private ISysAreaService sysAreaService;

    /**
     * 查询行政区域列表
     */
    @PreAuthorize("@ss.hasPermi('system:area:list')")
    @GetMapping("/list")
    public AjaxResult list(SysArea sysArea)
    {
        sysArea.setId(16L);
        List<SysArea> list = sysAreaService.selectSysAreaWithChild(sysArea);
        return success(list);
    }

    /**
     * 导出行政区域列表
     */
    @PreAuthorize("@ss.hasPermi('system:area:export')")
    @Log(title = "行政区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysArea sysArea)
    {
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        ExcelUtil<SysArea> util = new ExcelUtil<SysArea>(SysArea.class);
        util.exportExcel(response, list, "行政区域数据");
    }

    /**
     * 获取行政区域详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:area:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysAreaService.selectSysAreaById(id));
    }

    /**
     * 新增行政区域
     */
    @PreAuthorize("@ss.hasPermi('system:area:add')")
    @Log(title = "行政区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysArea sysArea)
    {
        return toAjax(sysAreaService.insertSysArea(sysArea));
    }

    /**
     * 修改行政区域
     */
    @PreAuthorize("@ss.hasPermi('system:area:edit')")
    @Log(title = "行政区域", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysArea sysArea)
    {
        return toAjax(sysAreaService.updateSysArea(sysArea));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:area:edit')")
    @Log(title = "资讯发布", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysArea sysArea)
    {
        return toAjax(sysAreaService.updateStatus(sysArea));
    }

    /**
     * 删除行政区域
     */
    @PreAuthorize("@ss.hasPermi('system:area:remove')")
    @Log(title = "行政区域", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysAreaService.deleteSysAreaByIds(ids));
    }
}
