package com.ruoyi.web.controller.offline;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.res.domain.ResSlider;
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
import com.ruoyi.cms.offline.domain.vo.ActivitiesLiveVo;
import com.ruoyi.cms.offline.domain.ActivitiesLive;
import com.ruoyi.cms.offline.service.IActivitiesLiveService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * ActivitiesLiveController
 * 
 * @author admin
 * @date 2025-05-09
 */
@RestController
@RequestMapping("/offline/live")
public class ActivitiesLiveController extends BaseController
{
    @Autowired
    private IActivitiesLiveService activitiesLiveService;

    /**
     * 查询现场资讯列表
     */
    @PreAuthorize("@ss.hasPermi('offline:live:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivitiesLiveVo activitiesLive)
    {
        startPage();
        List<ActivitiesLiveVo> list = activitiesLiveService.selectActivitiesLiveList(activitiesLive);
        return getDataTable(list);
    }

    /**
     * 导出现场资讯列表
     */
    @PreAuthorize("@ss.hasPermi('offline:live:export')")
    @Log(title = "现场资讯", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivitiesLiveVo activitiesLive)
    {
        List<ActivitiesLiveVo> list = activitiesLiveService.selectActivitiesLiveList(activitiesLive);
        ExcelUtil<ActivitiesLiveVo> util = new ExcelUtil<ActivitiesLiveVo>(ActivitiesLiveVo.class);
        util.exportExcel(response, list, "现场资讯数据");
    }

    /**
     * 获取现场资讯详细信息
     */
    @PreAuthorize("@ss.hasPermi('offline:live:query')")
    @GetMapping(value = "/{liveId}")
    public AjaxResult getInfo(@PathVariable("liveId") Long liveId)
    {
        return success(activitiesLiveService.selectActivitiesLiveByLiveId(liveId));
    }

    /**
     * 新增现场资讯
     */
    @PreAuthorize("@ss.hasPermi('offline:live:add')")
    @Log(title = "现场资讯", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivitiesLive activitiesLive)
    {
        return toAjax(activitiesLiveService.insertActivitiesLive(activitiesLive));
    }

    /**
     * 修改现场资讯
     */
    @PreAuthorize("@ss.hasPermi('offline:live:edit')")
    @Log(title = "现场资讯", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivitiesLive activitiesLive)
    {
        return toAjax(activitiesLiveService.updateActivitiesLive(activitiesLive));
    }

    /**
     * 删除现场资讯
     */
    @PreAuthorize("@ss.hasPermi('offline:live:remove')")
    @Log(title = "现场资讯", businessType = BusinessType.DELETE)
	@DeleteMapping("/{liveIds}")
    public AjaxResult remove(@PathVariable Long[] liveIds)
    {
        return toAjax(activitiesLiveService.deleteActivitiesLiveByLiveIds(liveIds));
    }

    /**
     * 查询已审核处方列表
     */
    @PreAuthorize("@ss.hasPermi('res:live:list')")
    @GetMapping("/list/{liveIds}")
    public List<Integer> list(@PathVariable Long[] liveIds)
    {
        startPage();
        List<Integer> list = activitiesLiveService.selectApporedByIds(liveIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:live:edit')")
    @Log(title = "现场资讯", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ActivitiesLive activitiesLive)
    {
        return toAjax(activitiesLiveService.updateStatus(activitiesLive));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('res:live:appor')")
    @Log(title = "现场资讯", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(activitiesLiveService.apporByIds(apporParams));
    }

    /**
     * 反审批
     */
    @PreAuthorize("@ss.hasPermi('res:live:unappor')")
    @Log(title = "现场资讯", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(activitiesLiveService.unApporByIds(ids));
    }


}
