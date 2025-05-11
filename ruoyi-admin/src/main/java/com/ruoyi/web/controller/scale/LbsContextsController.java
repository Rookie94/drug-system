package com.ruoyi.web.controller.scale;

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
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.service.ILbsContextsService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 量表目录Controller
 * 
 * @author admin
 * @date 2025-05-05
 */
@RestController
@RequestMapping("/scale/contexts")
public class LbsContextsController extends BaseController
{
    @Autowired
    private ILbsContextsService lbsContextsService;

    /**
     * 查询量表目录列表
     */
    @PreAuthorize("@ss.hasPermi('scale:contexts:list')")
    @GetMapping("/list")
    public AjaxResult list(LbsContexts lbsContexts)
    {
        List<LbsContexts> list = lbsContextsService.selectLbsContextsList(lbsContexts);
        return success(list);
    }

    /**
     * 导出量表目录列表
     */
    @PreAuthorize("@ss.hasPermi('scale:contexts:export')")
    @Log(title = "量表目录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LbsContexts lbsContexts)
    {
        List<LbsContexts> list = lbsContextsService.selectLbsContextsList(lbsContexts);
        ExcelUtil<LbsContexts> util = new ExcelUtil<LbsContexts>(LbsContexts.class);
        util.exportExcel(response, list, "量表目录数据");
    }

    /**
     * 获取量表目录详细信息
     */
    @PreAuthorize("@ss.hasPermi('scale:contexts:query')")
    @GetMapping(value = "/{contextId}")
    public AjaxResult getInfo(@PathVariable("contextId") Integer contextId)
    {
        return success(lbsContextsService.selectLbsContextsByContextId(contextId));
    }

    /**
     * 新增量表目录
     */
    @PreAuthorize("@ss.hasPermi('scale:contexts:add')")
    @Log(title = "量表目录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LbsContexts lbsContexts)
    {
        return toAjax(lbsContextsService.insertLbsContexts(lbsContexts));
    }

    /**
     * 修改量表目录
     */
    @PreAuthorize("@ss.hasPermi('scale:contexts:edit')")
    @Log(title = "量表目录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LbsContexts lbsContexts)
    {
        return toAjax(lbsContextsService.updateLbsContexts(lbsContexts));
    }

    /**
     * 删除量表目录
     */
    @PreAuthorize("@ss.hasPermi('scale:contexts:remove')")
    @Log(title = "量表目录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{contextIds}")
    public AjaxResult remove(@PathVariable Integer[] contextIds)
    {
        return toAjax(lbsContextsService.deleteLbsContextsByContextIds(contextIds));
    }

    /**
     * 量表转JSON
     */
    @PreAuthorize("@ss.hasPermi('scale:contexts:generalJson')")
    @Log(title = "量表目录", businessType = BusinessType.UPDATE)
    @GetMapping("/generalJson/{contextIds}")
    public AjaxResult generalJson(@PathVariable Integer[] contextIds)
    {
        return toAjax(lbsContextsService.generalJsonByContextIds(contextIds));
    }
}
