package com.ruoyi.web.controller.system;

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
import com.ruoyi.system.domain.SerialNo;
import com.ruoyi.system.service.ISerialNoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流水号管理Controller
 * 
 * @author admin
 * @date 2025-05-16
 */
@RestController
@RequestMapping("/system/serialno")
public class SerialNoController extends BaseController
{
    @Autowired
    private ISerialNoService serialNoService;

    /**
     * 查询流水号管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:serialno:list')")
    @GetMapping("/list")
    public TableDataInfo list(SerialNo serialNo)
    {
        startPage();
        List<SerialNo> list = serialNoService.selectSerialNoList(serialNo);
        return getDataTable(list);
    }

    /**
     * 导出流水号管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:serialno:export')")
    @Log(title = "流水号管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SerialNo serialNo)
    {
        List<SerialNo> list = serialNoService.selectSerialNoList(serialNo);
        ExcelUtil<SerialNo> util = new ExcelUtil<SerialNo>(SerialNo.class);
        util.exportExcel(response, list, "流水号管理数据");
    }

    /**
     * 获取流水号管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:serialno:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(serialNoService.selectSerialNoById(id));
    }

    /**
     * 新增流水号管理
     */
    @PreAuthorize("@ss.hasPermi('system:serialno:add')")
    @Log(title = "流水号管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SerialNo serialNo)
    {
        return toAjax(serialNoService.insertSerialNo(serialNo));
    }

    /**
     * 修改流水号管理
     */
    @PreAuthorize("@ss.hasPermi('system:serialno:edit')")
    @Log(title = "流水号管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SerialNo serialNo)
    {
        return toAjax(serialNoService.updateSerialNo(serialNo));
    }

    /**
     * 删除流水号管理
     */
    @PreAuthorize("@ss.hasPermi('system:serialno:remove')")
    @Log(title = "流水号管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(serialNoService.deleteSerialNoByIds(ids));
    }
}
