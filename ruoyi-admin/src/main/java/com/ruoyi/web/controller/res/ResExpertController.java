package com.ruoyi.web.controller.res;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.cms.res.domain.ResOrginfo;
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
import com.ruoyi.cms.res.domain.ResExpert;
import com.ruoyi.cms.res.service.IResExpertService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 戒治专家Controller
 * 
 * @author admin
 * @date 2025-04-09
 */
@RestController
@RequestMapping("/res/expert")
public class ResExpertController extends BaseController
{
    @Autowired
    private IResExpertService resExpertService;

    /**
     * 查询戒治专家列表
     */
    @PreAuthorize("@ss.hasPermi('res:expert:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResExpert resExpert)
    {
        startPage();
        List<ResExpert> list = resExpertService.selectResExpertList(resExpert);
        return getDataTable(list);
    }

    /**
     * 导出戒治专家列表
     */
    @PreAuthorize("@ss.hasPermi('res:expert:export')")
    @Log(title = "戒治专家", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResExpert resExpert)
    {
        List<ResExpert> list = resExpertService.selectResExpertList(resExpert);
        ExcelUtil<ResExpert> util = new ExcelUtil<ResExpert>(ResExpert.class);
        util.exportExcel(response, list, "戒治专家数据");
    }

    /**
     * 获取戒治专家详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:expert:query')")
    @GetMapping(value = "/{expertid}")
    public AjaxResult getInfo(@PathVariable("expertid") Long expertid)
    {
        return success(resExpertService.selectResExpertByExpertid(expertid));
    }

    /**
     * 新增戒治专家
     */
    @PreAuthorize("@ss.hasPermi('res:expert:add')")
    @Log(title = "戒治专家", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResExpert resExpert)
    {
        return toAjax(resExpertService.insertResExpert(resExpert));
    }

    /**
     * 修改戒治专家
     */
    @PreAuthorize("@ss.hasPermi('res:expert:edit')")
    @Log(title = "戒治专家", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResExpert resExpert)
    {
        return toAjax(resExpertService.updateResExpert(resExpert));
    }

    /**
     * 删除戒治专家
     */
    @PreAuthorize("@ss.hasPermi('res:expert:remove')")
    @Log(title = "戒治专家", businessType = BusinessType.DELETE)
	@DeleteMapping("/{expertids}")
    public AjaxResult remove(@PathVariable Long[] expertids)
    {
        return toAjax(resExpertService.deleteResExpertByExpertids(expertids));
    }

    /**
     * 查询已审核专家列表
     */
    @PreAuthorize("@ss.hasPermi('res:expert:list')")
    @GetMapping("/list/{expertids}")
    public List<Integer> list(@PathVariable Long[] expertids)
    {
        startPage();
        List<Integer> list = resExpertService.selectApporedByIds(expertids);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:expert:edit')")
    @Log(title = "戒治专家", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResExpert resExpert)
    {
        return toAjax(resExpertService.updateStatus(resExpert));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('res:expert:appor')")
    @Log(title = "戒治专家", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(resExpertService.apporByIds(apporParams));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('res:expert:unappor')")
    @Log(title = "戒治专家", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resExpertService.unApporByIds(ids));
    }


}
