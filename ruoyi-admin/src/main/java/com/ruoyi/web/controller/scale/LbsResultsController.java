package com.ruoyi.web.controller.scale;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.LbsResults;
import com.ruoyi.cms.scale.service.ILbsContextsService;
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
import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.cms.scale.service.ILbsResultsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 测评报告Controller
 * 
 * @author admin
 * @date 2025-05-16
 */
@RestController
@RequestMapping("/scale/report")
public class LbsResultsController extends BaseController
{

    @Autowired
    private ILbsContextsService lbsContextsService;

    @Autowired
    private ILbsResultsService lbsResultsService;

    /**
     * 查询测评报告列表
     */
    @PreAuthorize("@ss.hasPermi('scale:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(LbsResultsVo lbsResults)
    {
        startPage();
        if(lbsResults.getContextId()!=null){
            Long contextId=lbsResults.getContextId();
            LbsContexts contexts=lbsContextsService.selectLbsContextsByContextId(contextId);
            if(contexts.getParentContextId()==0)
            {
                lbsResults.setContextId(null);
            }
        }
        List<LbsResultsVo> list = lbsResultsService.selectLbsResultsList(lbsResults);
        return getDataTable(list);
    }

    /**
     * 导出测评报告列表
     */
    @PreAuthorize("@ss.hasPermi('scale:report:export')")
    @Log(title = "测评报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LbsResultsVo lbsResults)
    {
        List<LbsResultsVo> list = lbsResultsService.selectLbsResultsList(lbsResults);
        ExcelUtil<LbsResultsVo> util = new ExcelUtil<LbsResultsVo>(LbsResultsVo.class);
        util.exportExcel(response, list, "测评报告数据");
    }

    /**
     * 获取测评报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('scale:report:query')")
    @GetMapping(value = "/{resultId}")
    public AjaxResult getInfo(@PathVariable("resultId") Long resultId)
    {
        return success(lbsResultsService.selectLbsResultsByResultId(resultId));
    }

    /**
     * 新增测评报告
     */
    @PreAuthorize("@ss.hasPermi('scale:report:add')")
    @Log(title = "测评报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LbsResults lbsResults)
    {
        return toAjax(lbsResultsService.insertLbsResults(lbsResults));
    }

    /**
     * 修改测评报告
     */
    @PreAuthorize("@ss.hasPermi('scale:report:edit')")
    @Log(title = "测评报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LbsResults lbsResults)
    {
        return toAjax(lbsResultsService.updateLbsResults(lbsResults));
    }

    /**
     * 删除测评报告
     */
    @PreAuthorize("@ss.hasPermi('scale:report:remove')")
    @Log(title = "测评报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{resultIds}")
    public AjaxResult remove(@PathVariable Long[] resultIds)
    {
        return toAjax(lbsResultsService.deleteLbsResultsByResultIds(resultIds));
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('scale:report:list')")
    @GetMapping("/contextTree")
    public AjaxResult contextTree(LbsContexts lbsContexts)
    {
        return success(lbsContextsService.selectLbsContextsTreeList(lbsContexts));
    }

}
