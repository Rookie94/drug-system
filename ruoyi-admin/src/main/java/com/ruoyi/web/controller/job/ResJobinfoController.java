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
import com.ruoyi.cms.job.domain.ResJobinfo;
import com.ruoyi.cms.job.service.IResJobinfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 招聘信息Controller
 *
 * @author admin
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/job/jobinfo")
public class ResJobinfoController extends BaseController
{
    @Autowired
    private IResJobinfoService resJobinfoService;

    /**
     * 查询招聘信息列表
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResJobinfo resJobinfo)
    {
        startPage();
        List<ResJobinfo> list = resJobinfoService.selectResJobinfoList(resJobinfo);
        return getDataTable(list);
    }

    /**
     * 导出招聘信息列表
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:export')")
    @Log(title = "招聘信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResJobinfo resJobinfo)
    {
        List<ResJobinfo> list = resJobinfoService.selectResJobinfoList(resJobinfo);
        ExcelUtil<ResJobinfo> util = new ExcelUtil<ResJobinfo>(ResJobinfo.class);
        util.exportExcel(response, list, "招聘信息数据");
    }

    /**
     * 获取招聘信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:query')")
    @GetMapping(value = "/{jobid}")
    public AjaxResult getInfo(@PathVariable("jobid") Long jobid)
    {
        return success(resJobinfoService.selectResJobinfoByJobid(jobid));
    }

    /**
     * 新增招聘信息
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:add')")
    @Log(title = "招聘信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResJobinfo resJobinfo)
    {
        return toAjax(resJobinfoService.insertResJobinfo(resJobinfo));
    }

    /**
     * 修改招聘信息
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:edit')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResJobinfo resJobinfo)
    {
        return toAjax(resJobinfoService.updateResJobinfo(resJobinfo));
    }

    /**
     * 删除招聘信息
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:remove')")
    @Log(title = "招聘信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobids}")
    public AjaxResult remove(@PathVariable Long[] jobids)
    {
        return toAjax(resJobinfoService.deleteResJobinfoByJobids(jobids));
    }

    /**
     * 查询已审核工作列表
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:list')")
    @GetMapping("/list/{jobids}")
    public List<Integer> list(@PathVariable Long[] jobids)
    {
        startPage();
        List<Integer> list = resJobinfoService.selectApporedByIds(jobids);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:edit')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResJobinfo resJobInfo)
    {
        return toAjax(resJobinfoService.updateStatus(resJobInfo));
    }

    /**
     * 审批专家
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:appor')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PostMapping("/appor/{ids}")
    public AjaxResult appor(@PathVariable Long[] ids)
    {
        return toAjax(resJobinfoService.apporByIds(ids));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('job:jobinfo:unappor')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resJobinfoService.unApporByIds(ids));
    }


}
