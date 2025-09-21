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
import com.ruoyi.cms.etl.domain.EtlDatasource;
import com.ruoyi.cms.etl.service.IEtlDatasourceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据源管理Controller
 * 
 * @author admin
 * @date 2025-09-21
 */
@RestController
@RequestMapping("/etl/datasource")
public class EtlDatasourceController extends BaseController
{
    @Autowired
    private IEtlDatasourceService etlDatasourceService;

    /**
     * 查询数据源管理列表
     */
    @PreAuthorize("@ss.hasPermi('etl:datasource:list')")
    @GetMapping("/list")
    public TableDataInfo list(EtlDatasource etlDatasource)
    {
        startPage();
        List<EtlDatasource> list = etlDatasourceService.selectEtlDatasourceList(etlDatasource);
        return getDataTable(list);
    }

    /**
     * 导出数据源管理列表
     */
    @PreAuthorize("@ss.hasPermi('etl:datasource:export')")
    @Log(title = "数据源管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EtlDatasource etlDatasource)
    {
        List<EtlDatasource> list = etlDatasourceService.selectEtlDatasourceList(etlDatasource);
        ExcelUtil<EtlDatasource> util = new ExcelUtil<EtlDatasource>(EtlDatasource.class);
        util.exportExcel(response, list, "数据源管理数据");
    }

    /**
     * 获取数据源管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('etl:datasource:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable("configId") Long configId)
    {
        return success(etlDatasourceService.selectEtlDatasourceByConfigId(configId));
    }

    /**
     * 新增数据源管理
     */
    @PreAuthorize("@ss.hasPermi('etl:datasource:add')")
    @Log(title = "数据源管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EtlDatasource etlDatasource)
    {
        return toAjax(etlDatasourceService.insertEtlDatasource(etlDatasource));
    }

    /**
     * 修改数据源管理
     */
    @PreAuthorize("@ss.hasPermi('etl:datasource:edit')")
    @Log(title = "数据源管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EtlDatasource etlDatasource)
    {
        return toAjax(etlDatasourceService.updateEtlDatasource(etlDatasource));
    }

    /**
     * 删除数据源管理
     */
    @PreAuthorize("@ss.hasPermi('etl:datasource:remove')")
    @Log(title = "数据源管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        return toAjax(etlDatasourceService.deleteEtlDatasourceByConfigIds(configIds));
    }
}
