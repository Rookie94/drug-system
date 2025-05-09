package com.ruoyi.web.controller.offline;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.offline.domain.ActivitiesTmsdata;
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
import com.ruoyi.cms.offline.domain.vo.ActivitiesTmsdataVo;
import com.ruoyi.cms.offline.service.IActivitiesTmsdataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 现场经颅磁Controller
 * 
 * @author admin
 * @date 2025-05-09
 */
@RestController
@RequestMapping("/offline/tmsdata")
public class ActivitiesTmsdataController extends BaseController
{
    @Autowired
    private IActivitiesTmsdataService activitiesTmsdataService;

    /**
     * 查询现场经颅磁列表
     */
    @PreAuthorize("@ss.hasPermi('offline:tmsdata:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivitiesTmsdataVo activitiesTmsdata)
    {
        startPage();
        List<ActivitiesTmsdataVo> list = activitiesTmsdataService.selectActivitiesTmsdataList(activitiesTmsdata);
        return getDataTable(list);
    }

    /**
     * 导出现场经颅磁列表
     */
    @PreAuthorize("@ss.hasPermi('offline:tmsdata:export')")
    @Log(title = "现场经颅磁", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivitiesTmsdataVo activitiesTmsdata)
    {
        List<ActivitiesTmsdataVo> list = activitiesTmsdataService.selectActivitiesTmsdataList(activitiesTmsdata);
        ExcelUtil<ActivitiesTmsdataVo> util = new ExcelUtil<ActivitiesTmsdataVo>(ActivitiesTmsdataVo.class);
        util.exportExcel(response, list, "现场经颅磁数据");
    }

    /**
     * 获取现场经颅磁详细信息
     */
    @PreAuthorize("@ss.hasPermi('offline:tmsdata:query')")
    @GetMapping(value = "/{analyzeId}")
    public AjaxResult getInfo(@PathVariable("analyzeId") Long analyzeId)
    {
        return success(activitiesTmsdataService.selectActivitiesTmsdataByAnalyzeId(analyzeId));
    }

    /**
     * 新增现场经颅磁
     */
    @PreAuthorize("@ss.hasPermi('offline:tmsdata:add')")
    @Log(title = "现场经颅磁", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivitiesTmsdata activitiesTmsdata)
    {
        return toAjax(activitiesTmsdataService.insertActivitiesTmsdata(activitiesTmsdata));
    }

    /**
     * 修改现场经颅磁
     */
    @PreAuthorize("@ss.hasPermi('offline:tmsdata:edit')")
    @Log(title = "现场经颅磁", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivitiesTmsdata activitiesTmsdata)
    {
        return toAjax(activitiesTmsdataService.updateActivitiesTmsdata(activitiesTmsdata));
    }

    /**
     * 删除现场经颅磁
     */
    @PreAuthorize("@ss.hasPermi('offline:tmsdata:remove')")
    @Log(title = "现场经颅磁", businessType = BusinessType.DELETE)
	@DeleteMapping("/{analyzeIds}")
    public AjaxResult remove(@PathVariable Long[] analyzeIds)
    {
        return toAjax(activitiesTmsdataService.deleteActivitiesTmsdataByAnalyzeIds(analyzeIds));
    }
}
