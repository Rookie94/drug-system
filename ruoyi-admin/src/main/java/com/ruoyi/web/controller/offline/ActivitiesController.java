package com.ruoyi.web.controller.offline;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.job.domain.ResJobinfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
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
import com.ruoyi.cms.offline.domain.Activities;
import com.ruoyi.cms.offline.service.IActivitiesService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 活动发布Controller
 *
 * @author admin
 * @date 2025-04-25
 */
@RestController
@RequestMapping("/offline/activities")
public class ActivitiesController extends BaseController
{
    @Autowired
    private IActivitiesService activitiesService;

    /**
     * 查询活动发布列表
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:list')")
    @GetMapping("/list")
    public TableDataInfo list(Activities activities)
    {
        startPage();
        List<Activities> list = activitiesService.selectActivitiesWithChild(activities);
        return getDataTable(list);
    }

    /**
     * 导出活动发布列表
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:export')")
    @Log(title = "活动发布", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Activities activities)
    {
        List<Activities> list = activitiesService.selectActivitiesList(activities);
        ExcelUtil<Activities> util = new ExcelUtil<Activities>(Activities.class);
        util.exportExcel(response, list, "活动发布数据");
    }

    /**
     * 获取活动发布详细信息
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:query')")
    @GetMapping(value = "/{activityId}")
    public AjaxResult getInfo(@PathVariable("activityId") Long activityId)
    {
        return success(activitiesService.selectActivitiesByActivityId(activityId));
    }

    /**
     * 新增活动发布
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:add')")
    @Log(title = "活动发布", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Activities activities)
    {
        Date startTime=activities.getStartTime();
        Date endTime=activities.getEndTime();
        Date signDeadline=activities.getSignDeadline();
        if(startTime==null){
            return error("活动开始时间不能为空");
        }
        if(endTime==null){
            return error("活动结束时间不能为空");
        }
        if (!endTime.after(startTime)) {
            return error("结束时间必须晚于开始时间");
        }
        if(activities.getParentActivityId()==0){
            if(signDeadline==null){
                return error("报名截止时间不能为空");
            }
            // 报名截止时间不能晚于结束时间
            if (signDeadline.after(endTime)) {
                return error("报名截止时间不能晚于活动结束时间");
            }

            // 报名截止时间不能晚于开始时间（通常报名应该在活动开始前截止）
            //if (!signDeadline.before(startTime)) {
            //    return error("报名截止时间必须早于活动开始时间");
            //}

            // 可选：报名截止时间不能早于当前时间（如果这是创建新活动）
            //if (signDeadline.before(new Date())) {
            //    return error("报名截止时间不能早于当前时间");
            //}
        }
        return toAjax(activitiesService.insertActivities(activities));
    }

    /**
     * 修改活动发布
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:edit')")
    @Log(title = "活动发布", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Activities activities)
    {
        Date startTime=activities.getStartTime();
        Date endTime=activities.getEndTime();
        Date signDeadline=activities.getSignDeadline();
        if(startTime==null){
            return error("活动开始时间不能为空");
        }
        if(endTime==null){
            return error("活动结束时间不能为空");
        }
        if (!endTime.after(startTime)) {
            return error("结束时间必须晚于开始时间");
        }
        if(activities.getParentActivityId()==0){
            if(signDeadline==null){
                return error("报名截止时间不能为空");
            }
            // 报名截止时间不能晚于结束时间
            if (signDeadline.after(endTime)) {
                return error("报名截止时间不能晚于活动结束时间");
            }

            // 报名截止时间不能晚于开始时间（通常报名应该在活动开始前截止）
            //if (!signDeadline.before(startTime)) {
            //    return error("报名截止时间必须早于活动开始时间");
            //}

            // 可选：报名截止时间不能早于当前时间（如果这是创建新活动）
            //if (signDeadline.before(new Date())) {
            //    return error("报名截止时间不能早于当前时间");
            //}
        }
        return toAjax(activitiesService.updateActivities(activities));
    }

    /**
     * 删除活动发布
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:remove')")
    @Log(title = "活动发布", businessType = BusinessType.DELETE)
    @DeleteMapping("/{activityIds}")
    public AjaxResult remove(@PathVariable Long[] activityIds)
    {
        return toAjax(activitiesService.deleteActivitiesByActivityIds(activityIds));
    }

    /**
     * 查询已审核工作列表
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:list')")
    @GetMapping("/list/{activityIds}")
    public List<Integer> list(@PathVariable Long[] activityIds)
    {
        startPage();
        List<Integer> list = activitiesService.selectApporedByIds(activityIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:edit')")
    @Log(title = "活动发布", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody Activities activities)
    {
        return toAjax(activitiesService.updateStatus(activities));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:appor')")
    @Log(title = "活动发布", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(activitiesService.apporByIds(apporParams));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('offline:activities:unappor')")
    @Log(title = "活动发布", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(activitiesService.unApporByIds(ids));
    }

}
