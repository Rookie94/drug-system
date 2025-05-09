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
import com.ruoyi.cms.res.domain.ResCase;
import com.ruoyi.cms.res.service.IResCaseService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 戒治案例Controller
 * 
 * @author admin
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/res/case")
public class ResCaseController extends BaseController
{
    @Autowired
    private IResCaseService resCaseService;

    /**
     * 查询戒治案例列表
     */
    @PreAuthorize("@ss.hasPermi('res:case:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResCase resCase)
    {
        startPage();
        List<ResCase> list = resCaseService.selectResCaseList(resCase);
        return getDataTable(list);
    }

    /**
     * 导出戒治案例列表
     */
    @PreAuthorize("@ss.hasPermi('res:case:export')")
    @Log(title = "戒治案例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResCase resCase)
    {
        List<ResCase> list = resCaseService.selectResCaseList(resCase);
        ExcelUtil<ResCase> util = new ExcelUtil<ResCase>(ResCase.class);
        util.exportExcel(response, list, "戒治案例数据");
    }

    /**
     * 获取戒治案例详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:case:query')")
    @GetMapping(value = "/{caseid}")
    public AjaxResult getInfo(@PathVariable("caseid") Long caseid)
    {
        return success(resCaseService.selectResCaseByCaseid(caseid));
    }

    /**
     * 新增戒治案例
     */
    @PreAuthorize("@ss.hasPermi('res:case:add')")
    @Log(title = "戒治案例", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResCase resCase)
    {
        return toAjax(resCaseService.insertResCase(resCase));
    }

    /**
     * 修改戒治案例
     */
    @PreAuthorize("@ss.hasPermi('res:case:edit')")
    @Log(title = "戒治案例", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResCase resCase)
    {
        return toAjax(resCaseService.updateResCase(resCase));
    }

    /**
     * 删除戒治案例
     */
    @PreAuthorize("@ss.hasPermi('res:case:remove')")
    @Log(title = "戒治案例", businessType = BusinessType.DELETE)
	@DeleteMapping("/{caseids}")
    public AjaxResult remove(@PathVariable Long[] caseids)
    {
        return toAjax(resCaseService.deleteResCaseByCaseids(caseids));
    }

    /**
     * 查询已审核案例列表
     */
    @PreAuthorize("@ss.hasPermi('res:case:list')")
    @GetMapping("/list/{caseids}")
    public List<Integer> list(@PathVariable Long[] caseids)
    {
        startPage();
        List<Integer> list = resCaseService.selectApporedByIds(caseids);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:case:edit')")
    @Log(title = "戒治案例", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResCase resCase)
    {
        return toAjax(resCaseService.updateStatus(resCase));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('res:case:appor')")
    @Log(title = "戒治案例", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(resCaseService.apporByIds(apporParams));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('res:case:unappor')")
    @Log(title = "戒治案例", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resCaseService.unApporByIds(ids));
    }

}
