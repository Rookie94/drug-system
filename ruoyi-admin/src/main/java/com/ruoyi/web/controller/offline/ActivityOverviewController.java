package com.ruoyi.web.controller.offline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.cms.offline.domain.vo.ActivityOverviewVo;
import com.ruoyi.cms.offline.service.IActivityOverviewService;

@RestController
@RequestMapping("/offline/overview")
public class ActivityOverviewController extends BaseController {

    @Autowired
    private IActivityOverviewService activityOverviewService;

    /**
     * 获取活动总览信息
     */
    @PreAuthorize("@ss.hasPermi('offline:overview:list')")
    @GetMapping("/{activityId}")
    public AjaxResult getOverview(@PathVariable("activityId") Integer activityId) {
        ActivityOverviewVo overview = activityOverviewService.getActivityOverview(activityId);
        if (overview == null) {
            return AjaxResult.error("活动不存在或已被删除");
        }
        return AjaxResult.success(overview);
    }
}