package com.ruoyi.web.controller.job;

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
import com.ruoyi.cms.job.domain.ResJob;
import com.ruoyi.cms.job.service.IResJobService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 招聘信息Controller
 * 
 * @author admin
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/job/zhaopin")
public class ResJobController extends BaseController
{
    @Autowired
    private IResJobService resJobService;

    /**
     * 查询招聘信息列表
     */
    @PreAuthorize("@ss.hasPermi('job:zhaopin:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResJob resJob)
    {
        startPage();
        List<ResJob> list = resJobService.selectResJobList(resJob);
        return getDataTable(list);
    }

    /**
     * 导出招聘信息列表
     */
    @PreAuthorize("@ss.hasPermi('job:zhaopin:export')")
    @Log(title = "招聘信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResJob resJob)
    {
        List<ResJob> list = resJobService.selectResJobList(resJob);
        ExcelUtil<ResJob> util = new ExcelUtil<ResJob>(ResJob.class);
        util.exportExcel(response, list, "招聘信息数据");
    }

    /**
     * 获取招聘信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('job:zhaopin:query')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Integer jobId)
    {
        return success(resJobService.selectResJobByJobId(jobId));
    }

    /**
     * 新增招聘信息
     */
    @PreAuthorize("@ss.hasPermi('job:zhaopin:add')")
    @Log(title = "招聘信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResJob resJob)
    {
        return toAjax(resJobService.insertResJob(resJob));
    }

    /**
     * 修改招聘信息
     */
    @PreAuthorize("@ss.hasPermi('job:zhaopin:edit')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResJob resJob)
    {
        return toAjax(resJobService.updateResJob(resJob));
    }

    /**
     * 删除招聘信息
     */
    @PreAuthorize("@ss.hasPermi('job:zhaopin:remove')")
    @Log(title = "招聘信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Integer[] jobIds)
    {
        return toAjax(resJobService.deleteResJobByJobIds(jobIds));
    }
}
