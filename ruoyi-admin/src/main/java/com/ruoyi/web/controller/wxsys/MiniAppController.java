package com.ruoyi.web.controller.wxsys;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
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
import com.ruoyi.common.core.domain.entity.MiniApp;
import com.ruoyi.wxsys.service.IMiniAppService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 小程序信息Controller
 * 
 * @author 盖子
 * @date 2024-11-22
 */
@RestController
@RequestMapping("/wxsys/miniapp")
public class MiniAppController extends BaseController
{
    @Autowired
    private IMiniAppService miniAppService;

    /**
     * 查询小程序信息列表
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniapp:list')")
    @GetMapping("/list")
    public TableDataInfo list(MiniApp miniApp)
    {
        startPage();
        List<MiniApp> list = miniAppService.selectMiniAppList(miniApp);
        return getDataTable(list);
    }

    /**
     * 导出小程序信息列表
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniapp:export')")
    @Log(title = "小程序信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MiniApp miniApp)
    {
        List<MiniApp> list = miniAppService.selectMiniAppList(miniApp);
        ExcelUtil<MiniApp> util = new ExcelUtil<MiniApp>(MiniApp.class);
        util.exportExcel(response, list, "小程序信息数据");
    }

    /**
     * 获取小程序信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniapp:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(miniAppService.selectMiniAppById(id));
    }

    /**
     * 新增小程序信息
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniapp:add')")
    @Log(title = "小程序信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MiniApp miniApp)
    {
        return toAjax(miniAppService.insertMiniApp(miniApp));
    }

    /**
     * 修改小程序信息
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniapp:edit')")
    @Log(title = "小程序信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MiniApp miniApp)
    {
        return toAjax(miniAppService.updateMiniApp(miniApp));
    }

    /**
     * 删除小程序信息
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniapp:remove')")
    @Log(title = "小程序信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(miniAppService.deleteMiniAppByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('wxsys:miniapp:list')")
    @GetMapping("/listSelect")
    public AjaxResult listSelect()
    {
        JSONArray data = miniAppService.listSelect();
        return success(data);
    }
}
