package com.ruoyi.web.controller.scale;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.vo.LbsTopicsVo;
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
import com.ruoyi.cms.scale.domain.LbsTopics;
import com.ruoyi.cms.scale.service.ILbsTopicsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 量表题目Controller
 * 
 * @author admin
 * @date 2025-05-05
 */
@RestController
@RequestMapping("/scale/topics")
public class LbsTopicsController extends BaseController
{
    @Autowired
    private ILbsContextsService lbsContextsService;

    @Autowired
    private ILbsTopicsService lbsTopicsService;

    /**
     * 查询量表题目列表
     */
    @PreAuthorize("@ss.hasPermi('scale:topics:list')")
    @GetMapping("/list")
    public TableDataInfo list(LbsTopicsVo lbsTopics)
    {
        startPage();
        List<LbsTopics> list = lbsTopicsService.selectLbsTopicsList(lbsTopics);
        return getDataTable(list);
    }

    /**
     * 导出量表题目列表
     */
    @PreAuthorize("@ss.hasPermi('scale:topics:export')")
    @Log(title = "量表题目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LbsTopicsVo lbsTopics)
    {
        List<LbsTopics> list = lbsTopicsService.selectLbsTopicsList(lbsTopics);
        ExcelUtil<LbsTopics> util = new ExcelUtil<LbsTopics>(LbsTopics.class);
        util.exportExcel(response, list, "量表题目数据");
    }

    /**
     * 获取量表题目详细信息
     */
    @PreAuthorize("@ss.hasPermi('scale:topics:query')")
    @GetMapping(value = "/{topicId}")
    public AjaxResult getInfo(@PathVariable("topicId") Long topicId)
    {
        return success(lbsTopicsService.selectLbsTopicsByTopicId(topicId));
    }

    /**
     * 新增量表题目
     */
    @PreAuthorize("@ss.hasPermi('scale:topics:add')")
    @Log(title = "量表题目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LbsTopics lbsTopics)
    {
        return toAjax(lbsTopicsService.insertLbsTopics(lbsTopics));
    }

    /**
     * 修改量表题目
     */
    @PreAuthorize("@ss.hasPermi('scale:topics:edit')")
    @Log(title = "量表题目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LbsTopics lbsTopics)
    {
        return toAjax(lbsTopicsService.updateLbsTopics(lbsTopics));
    }

    /**
     * 删除量表题目
     */
    @PreAuthorize("@ss.hasPermi('scale:topics:remove')")
    @Log(title = "量表题目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{topicIds}")
    public AjaxResult remove(@PathVariable Long[] topicIds)
    {
        return toAjax(lbsTopicsService.deleteLbsTopicsByTopicIds(topicIds));
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('scale:topics:list')")
    @GetMapping("/contextTree")
    public AjaxResult contextTree(LbsContexts lbsContexts)
    {
        return success(lbsContextsService.selectLbsContextsTreeList(lbsContexts));
    }

}
