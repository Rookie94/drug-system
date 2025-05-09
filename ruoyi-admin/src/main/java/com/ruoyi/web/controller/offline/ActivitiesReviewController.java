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
import com.ruoyi.cms.offline.domain.vo.ActivitiesReviewVo;
import com.ruoyi.cms.offline.domain.ActivitiesReview;
import com.ruoyi.cms.offline.service.IActivitiesReviewService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动评价Controller
 * 
 * @author admin
 * @date 2025-05-09
 */
@RestController
@RequestMapping("/offline/review")
public class ActivitiesReviewController extends BaseController
{
    @Autowired
    private IActivitiesReviewService activitiesReviewService;

    /**
     * 查询活动评价列表
     */
    @PreAuthorize("@ss.hasPermi('offline:review:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivitiesReviewVo activitiesReview)
    {
        startPage();
        List<ActivitiesReviewVo> list = activitiesReviewService.selectActivitiesReviewList(activitiesReview);
        return getDataTable(list);
    }

    /**
     * 导出活动评价列表
     */
    @PreAuthorize("@ss.hasPermi('offline:review:export')")
    @Log(title = "活动评价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivitiesReviewVo activitiesReview)
    {
        List<ActivitiesReviewVo> list = activitiesReviewService.selectActivitiesReviewList(activitiesReview);
        ExcelUtil<ActivitiesReviewVo> util = new ExcelUtil<ActivitiesReviewVo>(ActivitiesReviewVo.class);
        util.exportExcel(response, list, "活动评价数据");
    }

    /**
     * 获取活动评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('offline:review:query')")
    @GetMapping(value = "/{reviewId}")
    public AjaxResult getInfo(@PathVariable("reviewId") Long reviewId)
    {
        return success(activitiesReviewService.selectActivitiesReviewByReviewId(reviewId));
    }

    /**
     * 新增活动评价
     */
    @PreAuthorize("@ss.hasPermi('offline:review:add')")
    @Log(title = "活动评价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivitiesReview activitiesReview)
    {
        return toAjax(activitiesReviewService.insertActivitiesReview(activitiesReview));
    }

    /**
     * 修改活动评价
     */
    @PreAuthorize("@ss.hasPermi('offline:review:edit')")
    @Log(title = "活动评价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivitiesReview activitiesReview)
    {
        return toAjax(activitiesReviewService.updateActivitiesReview(activitiesReview));
    }

    /**
     * 删除活动评价
     */
    @PreAuthorize("@ss.hasPermi('offline:review:remove')")
    @Log(title = "活动评价", businessType = BusinessType.DELETE)
	@DeleteMapping("/{reviewIds}")
    public AjaxResult remove(@PathVariable Long[] reviewIds)
    {
        return toAjax(activitiesReviewService.deleteActivitiesReviewByReviewIds(reviewIds));
    }
}
