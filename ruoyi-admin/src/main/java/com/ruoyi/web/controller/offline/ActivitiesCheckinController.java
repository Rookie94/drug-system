package com.ruoyi.web.controller.offline;

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
import com.ruoyi.cms.offline.domain.ActivitiesCheckin;
import com.ruoyi.cms.offline.domain.vo.ActivitiesCheckinVo;
import com.ruoyi.cms.offline.service.IActivitiesCheckinService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动签到Controller
 * 
 * @author admin
 * @date 2025-05-09
 */
@RestController
@RequestMapping("/offline/checkin")
public class ActivitiesCheckinController extends BaseController
{
    @Autowired
    private IActivitiesCheckinService activitiesCheckinService;

    /**
     * 查询活动签到列表
     */
    @PreAuthorize("@ss.hasPermi('offline:checkin:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivitiesCheckinVo activitiesCheckin)
    {
        startPage();
        List<ActivitiesCheckinVo> list = activitiesCheckinService.selectActivitiesCheckinList(activitiesCheckin);
        return getDataTable(list);
    }

    /**
     * 导出活动签到列表
     */
    @PreAuthorize("@ss.hasPermi('offline:checkin:export')")
    @Log(title = "活动签到", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivitiesCheckinVo activitiesCheckin)
    {
        List<ActivitiesCheckinVo> list = activitiesCheckinService.selectActivitiesCheckinList(activitiesCheckin);
        ExcelUtil<ActivitiesCheckinVo> util = new ExcelUtil<ActivitiesCheckinVo>(ActivitiesCheckinVo.class);
        util.exportExcel(response, list, "活动签到数据");
    }

    /**
     * 获取活动签到详细信息
     */
    @PreAuthorize("@ss.hasPermi('offline:checkin:query')")
    @GetMapping(value = "/{checkinId}")
    public AjaxResult getInfo(@PathVariable("checkinId") Long checkinId)
    {
        return success(activitiesCheckinService.selectActivitiesCheckinByCheckinId(checkinId));
    }

    /**
     * 新增活动签到
     */
    @PreAuthorize("@ss.hasPermi('offline:checkin:add')")
    @Log(title = "活动签到", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivitiesCheckin activitiesCheckin)
    {
        return toAjax(activitiesCheckinService.insertActivitiesCheckin(activitiesCheckin));
    }

    /**
     * 修改活动签到
     */
    @PreAuthorize("@ss.hasPermi('offline:checkin:edit')")
    @Log(title = "活动签到", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivitiesCheckin activitiesCheckin)
    {
        return toAjax(activitiesCheckinService.updateActivitiesCheckin(activitiesCheckin));
    }

    /**
     * 删除活动签到
     */
    @PreAuthorize("@ss.hasPermi('offline:checkin:remove')")
    @Log(title = "活动签到", businessType = BusinessType.DELETE)
	@DeleteMapping("/{checkinIds}")
    public AjaxResult remove(@PathVariable Long[] checkinIds)
    {
        return toAjax(activitiesCheckinService.deleteActivitiesCheckinByCheckinIds(checkinIds));
    }
}
