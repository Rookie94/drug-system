package com.ruoyi.web.controller.survey;

import java.util.List;

import com.ruoyi.cms.survey.domain.vo.DocResultsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.survey.domain.DocResults;
import com.ruoyi.cms.survey.service.IDocResultsService;

import javax.servlet.http.HttpServletResponse;

/**
 * 问卷答案结果jsonController
 *
 * @author Shure
 * @date 2021-10-18
 */
@RestController
@RequestMapping("/survey/docs")
public class DocResultsController extends BaseController {
    @Autowired
    private IDocResultsService docResultsService;

    /**
     * 查询问卷答案结果json列表
     */
    @PreAuthorize("@ss.hasPermi('survey:docs:list')")
    @GetMapping("/list")
    public TableDataInfo list(DocResultsVo docResultsVo) {
        startPage();
        List<DocResultsVo> list = docResultsService.selectDocResultsList(docResultsVo);
        return getDataTable(list);
    }

    /**
     * 导出问卷答案结果json列表
     */
    @PreAuthorize("@ss.hasPermi('survey:docs:export')")
    @Log(title = "问卷答案结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DocResultsVo docResultsVo) {
        List<DocResultsVo> list = docResultsService.selectDocResultsList(docResultsVo);
        ExcelUtil<DocResultsVo> util = new ExcelUtil<DocResultsVo>(DocResultsVo.class);
        util.exportExcel(response, list, "问卷填报明细");
    }

    /**
     * 获取问卷答案结果json详细信息
     */
    @PreAuthorize("@ss.hasPermi('survey:docs:query')")
    @GetMapping(value = "/{resultId}")
    public AjaxResult getInfo(@PathVariable("resultId") Long resultId) {
        return AjaxResult.success(docResultsService.selectDocResultsById(resultId));
    }

    /**
     * 新增问卷答案结果json
     */
    @PreAuthorize("@ss.hasPermi('survey:docs:add')")
    @Log(title = "问卷答案结果json", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DocResults docResults) {
        return toAjax(docResultsService.insertDocResults(docResults));
    }

    /**
     * 修改问卷答案结果json
     */
    @PreAuthorize("@ss.hasPermi('survey:docs:edit')")
    @Log(title = "问卷答案结果json", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DocResults docResults) {
        return toAjax(docResultsService.updateDocResults(docResults));
    }

    /**
     * 删除问卷答案结果json
     */
    @PreAuthorize("@ss.hasPermi('survey:docs:remove')")
    @Log(title = "问卷答案结果json", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resultIds}")
    public AjaxResult remove(@PathVariable Long[] resultIds) {
        return toAjax(docResultsService.deleteDocResultsByIds(resultIds));
    }
}
