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
import com.ruoyi.cms.res.domain.ResSlider;
import com.ruoyi.cms.res.service.IResSliderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 轮播图Controller
 * 
 * @author admin
 * @date 2025-10-23
 */
@RestController
@RequestMapping("/res/slider")
public class ResSliderController extends BaseController
{
    @Autowired
    private IResSliderService resSliderService;

    /**
     * 查询轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('res:slider:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResSlider resSlider)
    {
        startPage();
        List<ResSlider> list = resSliderService.selectResSliderList(resSlider);
        return getDataTable(list);
    }

    /**
     * 导出轮播图列表
     */
    @PreAuthorize("@ss.hasPermi('res:slider:export')")
    @Log(title = "轮播图", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResSlider resSlider)
    {
        List<ResSlider> list = resSliderService.selectResSliderList(resSlider);
        ExcelUtil<ResSlider> util = new ExcelUtil<ResSlider>(ResSlider.class);
        util.exportExcel(response, list, "轮播图数据");
    }

    /**
     * 获取轮播图详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:slider:query')")
    @GetMapping(value = "/{sliderId}")
    public AjaxResult getInfo(@PathVariable("sliderId") Long sliderId)
    {
        return success(resSliderService.selectResSliderBySliderId(sliderId));
    }

    /**
     * 新增轮播图
     */
    @PreAuthorize("@ss.hasPermi('res:slider:add')")
    @Log(title = "轮播图", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResSlider resSlider)
    {
        return toAjax(resSliderService.insertResSlider(resSlider));
    }

    /**
     * 修改轮播图
     */
    @PreAuthorize("@ss.hasPermi('res:slider:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResSlider resSlider)
    {
        return toAjax(resSliderService.updateResSlider(resSlider));
    }

    /**
     * 删除轮播图
     */
    @PreAuthorize("@ss.hasPermi('res:slider:remove')")
    @Log(title = "轮播图", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sliderIds}")
    public AjaxResult remove(@PathVariable Long[] sliderIds)
    {
        return toAjax(resSliderService.deleteResSliderBySliderIds(sliderIds));
    }

    /**
     * 查询已审核处方列表
     */
    @PreAuthorize("@ss.hasPermi('res:slider:list')")
    @GetMapping("/list/{sliderIds}")
    public List<Integer> list(@PathVariable Long[] sliderIds)
    {
        startPage();
        List<Integer> list = resSliderService.selectApporedByIds(sliderIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:slider:edit')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResSlider resSlider)
    {
        return toAjax(resSliderService.updateStatus(resSlider));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('res:slider:appor')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(resSliderService.apporByIds(apporParams));
    }

    /**
     * 反审批
     */
    @PreAuthorize("@ss.hasPermi('res:slider:unappor')")
    @Log(title = "轮播图", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resSliderService.unApporByIds(ids));
    }

}
