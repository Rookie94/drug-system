package com.ruoyi.web.controller.res;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
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
import com.ruoyi.cms.res.domain.ResOrginfo;
import com.ruoyi.cms.res.service.IResOrginfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 戒治机构Controller
 * 
 * @author ruoyi
 * @date 2025-04-08
 */
@RestController
@RequestMapping("/res/orginfo")
public class ResOrginfoController extends BaseController
{
    @Autowired
    private IResOrginfoService resOrginfoService;

    /**
     * 查询戒治机构列表
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResOrginfo resOrginfo)
    {
        startPage();
        List<ResOrginfo> list = resOrginfoService.selectResOrginfoList(resOrginfo);
        return getDataTable(list);
    }

    /**
     * 查询戒治机构列表
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:list')")
    @GetMapping("/list/{orgids}")
    public List<Integer> list(@PathVariable Long[] orgids)
    {
        startPage();
        List<Integer> list = resOrginfoService.selectApporedOrgByOrgids(orgids);
        return list;
    }

    /**
     * 导出戒治机构列表
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:export')")
    @Log(title = "戒治机构", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResOrginfo resOrginfo)
    {
        List<ResOrginfo> list = resOrginfoService.selectResOrginfoList(resOrginfo);
        ExcelUtil<ResOrginfo> util = new ExcelUtil<ResOrginfo>(ResOrginfo.class);
        util.exportExcel(response, list, "戒治机构数据");
    }

    /**
     * 获取戒治机构详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:query')")
    @GetMapping(value = "/{orgid}")
    public AjaxResult getInfo(@PathVariable("orgid") Long orgid)
    {
        return success(resOrginfoService.selectResOrginfoByOrgid(orgid));
    }

    /**
     * 新增戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:add')")
    @Log(title = "戒治机构", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResOrginfo resOrginfo)
    {
        return toAjax(resOrginfoService.insertResOrginfo(resOrginfo));
    }

    /**
     * 修改戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:edit')")
    @Log(title = "戒治机构", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResOrginfo resOrginfo)
    {
        return toAjax(resOrginfoService.updateResOrginfo(resOrginfo));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:edit')")
    @Log(title = "戒治机构", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResOrginfo resOrginfo)
    {
        return toAjax(resOrginfoService.updateStatus(resOrginfo));
    }

    /**
     * 删除戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:remove')")
    @Log(title = "戒治机构", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orgids}")
    public AjaxResult remove(@PathVariable Long[] orgids)
    {
        return toAjax(resOrginfoService.deleteResOrginfoByOrgids(orgids));
    }

    /**
     * 审批戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:appor')")
    @Log(title = "戒治机构", businessType = BusinessType.UPDATE)
    @PostMapping("/appor/{orgids}")
    public AjaxResult appor(@PathVariable Long[] orgids)
    {
        return toAjax(resOrginfoService.apporResOrginfoByOrgids(orgids));
    }

    /**
     * 审批戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:unappor')")
    @Log(title = "戒治机构", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{orgids}")
    public AjaxResult unappor(@PathVariable Long[] orgids)
    {
        return toAjax(resOrginfoService.unApporResOrginfoByOrgids(orgids));
    }

}
