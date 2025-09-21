package com.ruoyi.web.controller.etl;

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
import com.ruoyi.cms.etl.domain.EtlSyncdata;
import com.ruoyi.cms.etl.service.IEtlSyncdataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据清洗Controller
 * 
 * @author admin
 * @date 2025-09-21
 */
@RestController
@RequestMapping("/etl/syncdata")
public class EtlSyncdataController extends BaseController
{
    @Autowired
    private IEtlSyncdataService etlSyncdataService;

    /**
     * 查询数据清洗列表
     */
    @PreAuthorize("@ss.hasPermi('etl:syncdata:list')")
    @GetMapping("/list")
    public TableDataInfo list(EtlSyncdata etlSyncdata)
    {
        startPage();
        List<EtlSyncdata> list = etlSyncdataService.selectEtlSyncdataList(etlSyncdata);
        return getDataTable(list);
    }

    /**
     * 导出数据清洗列表
     */
    @PreAuthorize("@ss.hasPermi('etl:syncdata:export')")
    @Log(title = "数据清洗", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EtlSyncdata etlSyncdata)
    {
        List<EtlSyncdata> list = etlSyncdataService.selectEtlSyncdataList(etlSyncdata);
        ExcelUtil<EtlSyncdata> util = new ExcelUtil<EtlSyncdata>(EtlSyncdata.class);
        util.exportExcel(response, list, "数据清洗数据");
    }

    /**
     * 获取数据清洗详细信息
     */
    @PreAuthorize("@ss.hasPermi('etl:syncdata:query')")
    @GetMapping(value = "/{syncId}")
    public AjaxResult getInfo(@PathVariable("syncId") Long syncId)
    {
        return success(etlSyncdataService.selectEtlSyncdataBySyncId(syncId));
    }

    /**
     * 新增数据清洗
     */
    @PreAuthorize("@ss.hasPermi('etl:syncdata:add')")
    @Log(title = "数据清洗", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EtlSyncdata etlSyncdata)
    {
        return toAjax(etlSyncdataService.insertEtlSyncdata(etlSyncdata));
    }

    /**
     * 修改数据清洗
     */
    @PreAuthorize("@ss.hasPermi('etl:syncdata:edit')")
    @Log(title = "数据清洗", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EtlSyncdata etlSyncdata)
    {
        return toAjax(etlSyncdataService.updateEtlSyncdata(etlSyncdata));
    }

    /**
     * 删除数据清洗
     */
    @PreAuthorize("@ss.hasPermi('etl:syncdata:remove')")
    @Log(title = "数据清洗", businessType = BusinessType.DELETE)
	@DeleteMapping("/{syncIds}")
    public AjaxResult remove(@PathVariable Long[] syncIds)
    {
        return toAjax(etlSyncdataService.deleteEtlSyncdataBySyncIds(syncIds));
    }
}
