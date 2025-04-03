package com.ruoyi.web.controller.res;

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
import com.ruoyi.cms.res.domain.ResCategory;
import com.ruoyi.cms.res.service.IResCategoryService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 资源分类Controller
 *
 * @author admin
 * @date 2025-03-28
 */
@RestController
@RequestMapping("/res/category")
public class ResCategoryController extends BaseController
{
    @Autowired
    private IResCategoryService resCategoryService;

    /**
     * 查询资源分类列表
     */
    @PreAuthorize("@ss.hasPermi('res:category:list')")
    @GetMapping("/list")
    public AjaxResult list(ResCategory resCategory)
    {
        List<ResCategory> list = resCategoryService.selectResCategoryList(resCategory);
        return success(list);
    }

    /**
     * 导出资源分类列表
     */
    @PreAuthorize("@ss.hasPermi('res:category:export')")
    @Log(title = "资源分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResCategory resCategory)
    {
        List<ResCategory> list = resCategoryService.selectResCategoryList(resCategory);
        ExcelUtil<ResCategory> util = new ExcelUtil<ResCategory>(ResCategory.class);
        util.exportExcel(response, list, "资源分类数据");
    }

    /**
     * 获取资源分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(resCategoryService.selectResCategoryById(id));
    }

    /**
     * 新增资源分类
     */
    @PreAuthorize("@ss.hasPermi('res:category:add')")
    @Log(title = "资源分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResCategory resCategory)
    {
        return toAjax(resCategoryService.insertResCategory(resCategory));
    }

    /**
     * 修改资源分类
     */
    @PreAuthorize("@ss.hasPermi('res:category:edit')")
    @Log(title = "资源分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResCategory resCategory)
    {
        return toAjax(resCategoryService.updateResCategory(resCategory));
    }

    /**
     * 删除资源分类
     */
    @PreAuthorize("@ss.hasPermi('res:category:remove')")
    @Log(title = "资源分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(resCategoryService.deleteResCategoryByIds(ids));
    }
}
