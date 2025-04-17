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
import com.ruoyi.cms.job.domain.ResSkill;
import com.ruoyi.cms.job.service.IResSkillService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

import com.ruoyi.system.domain.ResApporParam;

/**
 * 技能信息Controller
 *
 * @author admin
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/job/skill")
public class ResSkillController extends BaseController
{
    @Autowired
    private IResSkillService resSkillService;

    /**
     * 查询技能信息列表
     */
    @PreAuthorize("@ss.hasPermi('job:skill:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResSkill resSkill)
    {
        startPage();
        List<ResSkill> list = resSkillService.selectResSkillList(resSkill);
        return getDataTable(list);
    }

    /**
     * 导出技能信息列表
     */
    @PreAuthorize("@ss.hasPermi('job:skill:export')")
    @Log(title = "技能信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResSkill resSkill)
    {
        List<ResSkill> list = resSkillService.selectResSkillList(resSkill);
        ExcelUtil<ResSkill> util = new ExcelUtil<ResSkill>(ResSkill.class);
        util.exportExcel(response, list, "技能信息数据");
    }

    /**
     * 获取技能信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('job:skill:query')")
    @GetMapping(value = "/{skillid}")
    public AjaxResult getInfo(@PathVariable("skillid") Long skillid)
    {
        return success(resSkillService.selectResSkillBySkillid(skillid));
    }

    /**
     * 新增技能信息
     */
    @PreAuthorize("@ss.hasPermi('job:skill:add')")
    @Log(title = "技能信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResSkill resSkill)
    {
        return toAjax(resSkillService.insertResSkill(resSkill));
    }

    /**
     * 修改技能信息
     */
    @PreAuthorize("@ss.hasPermi('job:skill:edit')")
    @Log(title = "技能信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResSkill resSkill)
    {
        return toAjax(resSkillService.updateResSkill(resSkill));
    }

    /**
     * 删除技能信息
     */
    @PreAuthorize("@ss.hasPermi('job:skill:remove')")
    @Log(title = "技能信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{skillids}")
    public AjaxResult remove(@PathVariable Long[] skillids)
    {
        return toAjax(resSkillService.deleteResSkillBySkillids(skillids));
    }

    /**
     * 查询已审核工作列表
     */
    @PreAuthorize("@ss.hasPermi('job:skill:list')")
    @GetMapping("/list/{skillids}")
    public List<Integer> list(@PathVariable Long[] skillids)
    {
        startPage();
        List<Integer> list = resSkillService.selectApporedByIds(skillids);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('job:skill:edit')")
    @Log(title = "技能信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResSkill resSkill)
    {
        return toAjax(resSkillService.updateStatus(resSkill));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('job:skill:appor')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(resSkillService.apporByIds(apporParams));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('job:skill:unappor')")
    @Log(title = "技能信息", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resSkillService.unApporByIds(ids));
    }

}
