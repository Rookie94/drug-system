package com.ruoyi.web.controller.res;

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
import com.ruoyi.cms.res.domain.BaseOrginfo;
import com.ruoyi.cms.res.service.IBaseOrginfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 戒治机构Controller
 * 
 * @author admin
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/res/orginfo")
public class BaseOrginfoController extends BaseController
{
    @Autowired
    private IBaseOrginfoService baseOrginfoService;

    /**
     * 查询戒治机构列表
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseOrginfo baseOrginfo)
    {
        startPage();
        List<BaseOrginfo> list = baseOrginfoService.selectBaseOrginfoList(baseOrginfo);
        return getDataTable(list);
    }

    /**
     * 导出戒治机构列表
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:export')")
    @Log(title = "戒治机构", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseOrginfo baseOrginfo)
    {
        List<BaseOrginfo> list = baseOrginfoService.selectBaseOrginfoList(baseOrginfo);
        ExcelUtil<BaseOrginfo> util = new ExcelUtil<BaseOrginfo>(BaseOrginfo.class);
        util.exportExcel(response, list, "戒治机构数据");
    }

    /**
     * 获取戒治机构详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:query')")
    @GetMapping(value = "/{orgid}")
    public AjaxResult getInfo(@PathVariable("orgid") Long orgid)
    {
        return success(baseOrginfoService.selectBaseOrginfoByOrgid(orgid));
    }

    /**
     * 新增戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:add')")
    @Log(title = "戒治机构", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseOrginfo baseOrginfo)
    {
        return toAjax(baseOrginfoService.insertBaseOrginfo(baseOrginfo));
    }

    /**
     * 修改戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:edit')")
    @Log(title = "戒治机构", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseOrginfo baseOrginfo)
    {
        return toAjax(baseOrginfoService.updateBaseOrginfo(baseOrginfo));
    }

    /**
     * 删除戒治机构
     */
    @PreAuthorize("@ss.hasPermi('res:orginfo:remove')")
    @Log(title = "戒治机构", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orgids}")
    public AjaxResult remove(@PathVariable Long[] orgids)
    {
        return toAjax(baseOrginfoService.deleteBaseOrginfoByOrgids(orgids));
    }
}
