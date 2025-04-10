package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.job.domain.ResJobinfo;
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
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通知公告Controller
 *
 * @author admin
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService sysNoticeService;

    /**
     * 查询通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysNotice sysNotice)
    {
        startPage();
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
        return getDataTable(list);
    }

    /**
     * 导出通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:export')")
    @Log(title = "通知公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNotice sysNotice)
    {
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
        ExcelUtil<SysNotice> util = new ExcelUtil<SysNotice>(SysNotice.class);
        util.exportExcel(response, list, "通知公告数据");
    }

    /**
     * 获取通知公告详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable("noticeId") Long noticeId)
    {
        return success(sysNoticeService.selectSysNoticeByNoticeId(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysNotice sysNotice)
    {
        return toAjax(sysNoticeService.insertSysNotice(sysNotice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysNotice sysNotice)
    {
        return toAjax(sysNoticeService.updateSysNotice(sysNotice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(sysNoticeService.deleteSysNoticeByNoticeIds(noticeIds));
    }
    /**
     * 查询已审核工作列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list/{noticeIds}")
    public List<Integer> list(@PathVariable Long[] noticeIds)
    {
        startPage();
        List<Integer> list = sysNoticeService.selectApporedByIds(noticeIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysNotice sysNotice)
    {
        return toAjax(sysNoticeService.updateStatus(sysNotice));
    }

    /**
     * 审批专家
     */
    @PreAuthorize("@ss.hasPermi('system:notice:appor')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/appor/{ids}")
    public AjaxResult appor(@PathVariable Long[] ids)
    {
        return toAjax(sysNoticeService.apporByIds(ids));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('system:notice:unappor')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(sysNoticeService.unApporByIds(ids));
    }


}
