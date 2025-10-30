package com.ruoyi.web.controller.res;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.ResApporParam;
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
import com.ruoyi.cms.res.domain.ResTech;
import com.ruoyi.cms.res.service.IResTechService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 戒治技术宣传Controller
 * 
 * @author admin
 * @date 2025-10-29
 */
@RestController
@RequestMapping("/res/tech")
public class ResTechController extends BaseController
{
    @Autowired
    private IResTechService resTechService;

    /**
     * 查询戒治技术宣传列表
     */
    @PreAuthorize("@ss.hasPermi('res:tech:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResTech resTech)
    {
        startPage();
        List<ResTech> list = resTechService.selectResTechList(resTech);
        return getDataTable(list);
    }

    /**
     * 导出戒治技术宣传列表
     */
    @PreAuthorize("@ss.hasPermi('res:tech:export')")
    @Log(title = "戒治技术宣传", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResTech resTech)
    {
        List<ResTech> list = resTechService.selectResTechList(resTech);
        ExcelUtil<ResTech> util = new ExcelUtil<ResTech>(ResTech.class);
        util.exportExcel(response, list, "戒治技术宣传数据");
    }

    /**
     * 获取戒治技术宣传详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:tech:query')")
    @GetMapping(value = "/{techId}")
    public AjaxResult getInfo(@PathVariable("techId") Long techId)
    {
        return success(resTechService.selectResTechByTechId(techId));
    }

    /**
     * 新增戒治技术宣传
     */
    @PreAuthorize("@ss.hasPermi('res:tech:add')")
    @Log(title = "戒治技术宣传", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResTech resTech)
    {
        return toAjax(resTechService.insertResTech(resTech));
    }

    /**
     * 修改戒治技术宣传
     */
    @PreAuthorize("@ss.hasPermi('res:tech:edit')")
    @Log(title = "戒治技术宣传", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResTech resTech)
    {
        return toAjax(resTechService.updateResTech(resTech));
    }

    /**
     * 删除戒治技术宣传
     */
    @PreAuthorize("@ss.hasPermi('res:tech:remove')")
    @Log(title = "戒治技术宣传", businessType = BusinessType.DELETE)
	@DeleteMapping("/{techIds}")
    public AjaxResult remove(@PathVariable Long[] techIds)
    {
        return toAjax(resTechService.deleteResTechByTechIds(techIds));
    }

    /**
     * 查询已审核处方列表
     */
    @PreAuthorize("@ss.hasPermi('res:tech:list')")
    @GetMapping("/list/{techIds}")
    public List<Integer> list(@PathVariable Long[] techIds)
    {
        startPage();
        List<Integer> list = resTechService.selectApporedByIds(techIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:tech:edit')")
    @Log(title = "戒治技术宣传", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResTech resTech)
    {
        return toAjax(resTechService.updateStatus(resTech));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('res:tech:appor')")
    @Log(title = "戒治技术宣传", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(resTechService.apporByIds(apporParams));
    }

    /**
     * 反审批
     */
    @PreAuthorize("@ss.hasPermi('res:tech:unappor')")
    @Log(title = "戒治技术宣传", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resTechService.unApporByIds(ids));
    }

}
