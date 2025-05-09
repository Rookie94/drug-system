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
import com.ruoyi.cms.res.domain.ResRxdata;
import com.ruoyi.cms.res.service.IResRxdataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 戒治处方Controller
 * 
 * @author admin
 * @date 2025-05-08
 */
@RestController
@RequestMapping("/res/rxdata")
public class ResRxdataController extends BaseController
{
    @Autowired
    private IResRxdataService resRxdataService;

    /**
     * 查询戒治处方列表
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResRxdata resRxdata)
    {
        startPage();
        List<ResRxdata> list = resRxdataService.selectResRxdataList(resRxdata);
        return getDataTable(list);
    }

    /**
     * 导出戒治处方列表
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:export')")
    @Log(title = "戒治处方", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResRxdata resRxdata)
    {
        List<ResRxdata> list = resRxdataService.selectResRxdataList(resRxdata);
        ExcelUtil<ResRxdata> util = new ExcelUtil<ResRxdata>(ResRxdata.class);
        util.exportExcel(response, list, "戒治处方数据");
    }

    /**
     * 获取戒治处方详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:query')")
    @GetMapping(value = "/{rxId}")
    public AjaxResult getInfo(@PathVariable("rxId") Long rxId)
    {
        return success(resRxdataService.selectResRxdataByRxId(rxId));
    }

    /**
     * 新增戒治处方
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:add')")
    @Log(title = "戒治处方", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResRxdata resRxdata)
    {
        return toAjax(resRxdataService.insertResRxdata(resRxdata));
    }

    /**
     * 修改戒治处方
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:edit')")
    @Log(title = "戒治处方", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResRxdata resRxdata)
    {
        return toAjax(resRxdataService.updateResRxdata(resRxdata));
    }

    /**
     * 删除戒治处方
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:remove')")
    @Log(title = "戒治处方", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rxIds}")
    public AjaxResult remove(@PathVariable Long[] rxIds)
    {
        return toAjax(resRxdataService.deleteResRxdataByRxIds(rxIds));
    }

    /**
     * 查询已审核处方列表
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:list')")
    @GetMapping("/list/{rxIds}")
    public List<Integer> list(@PathVariable Long[] rxIds)
    {
        startPage();
        List<Integer> list = resRxdataService.selectApporedByIds(rxIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:edit')")
    @Log(title = "戒治处方", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResRxdata resRxdata)
    {
        return toAjax(resRxdataService.updateStatus(resRxdata));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:appor')")
    @Log(title = "戒治处方", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(resRxdataService.apporByIds(apporParams));
    }

    /**
     * 反审批
     */
    @PreAuthorize("@ss.hasPermi('res:rxdata:unappor')")
    @Log(title = "戒治处方", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resRxdataService.unApporByIds(ids));
    }

}
