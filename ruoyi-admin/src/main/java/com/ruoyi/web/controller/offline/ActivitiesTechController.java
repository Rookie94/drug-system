package com.ruoyi.web.controller.offline;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.offline.domain.ActivitiesTech;
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
import com.ruoyi.cms.offline.domain.vo.ActivitiesTechVo;
import com.ruoyi.cms.offline.service.IActivitiesTechService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动戒治技术资料Controller
 * 
 * @author admin
 * @date 2025-05-10
 */
@RestController
@RequestMapping("/offline/tech")
public class ActivitiesTechController extends BaseController
{
    @Autowired
    private IActivitiesTechService activitiesTechService;

    /**
     * 查询活动戒治技术资料列表
     */
    @PreAuthorize("@ss.hasPermi('offline:tech:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivitiesTechVo activitiesTech)
    {
        startPage();
        List<ActivitiesTechVo> list = activitiesTechService.selectActivitiesTechList(activitiesTech);
        return getDataTable(list);
    }

    /**
     * 导出活动戒治技术资料列表
     */
    @PreAuthorize("@ss.hasPermi('offline:tech:export')")
    @Log(title = "活动戒治技术资料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivitiesTechVo activitiesTech)
    {
        List<ActivitiesTechVo> list = activitiesTechService.selectActivitiesTechList(activitiesTech);
        ExcelUtil<ActivitiesTechVo> util = new ExcelUtil<ActivitiesTechVo>(ActivitiesTechVo.class);
        util.exportExcel(response, list, "活动戒治技术资料数据");
    }

    /**
     * 获取活动戒治技术资料详细信息
     */
    @PreAuthorize("@ss.hasPermi('offline:tech:query')")
    @GetMapping(value = "/{techId}")
    public AjaxResult getInfo(@PathVariable("techId") Long techId)
    {
        return success(activitiesTechService.selectActivitiesTechByTechId(techId));
    }

    /**
     * 新增活动戒治技术资料
     */
    @PreAuthorize("@ss.hasPermi('offline:tech:add')")
    @Log(title = "活动戒治技术资料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivitiesTech activitiesTech)
    {
        return toAjax(activitiesTechService.insertActivitiesTech(activitiesTech));
    }

    /**
     * 修改活动戒治技术资料
     */
    @PreAuthorize("@ss.hasPermi('offline:tech:edit')")
    @Log(title = "活动戒治技术资料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivitiesTech activitiesTech)
    {
        return toAjax(activitiesTechService.updateActivitiesTech(activitiesTech));
    }

    /**
     * 删除活动戒治技术资料
     */
    @PreAuthorize("@ss.hasPermi('offline:tech:remove')")
    @Log(title = "活动戒治技术资料", businessType = BusinessType.DELETE)
	@DeleteMapping("/{techIds}")
    public AjaxResult remove(@PathVariable Long[] techIds)
    {
        return toAjax(activitiesTechService.deleteActivitiesTechByTechIds(techIds));
    }
}
