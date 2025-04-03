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
import com.ruoyi.cms.res.domain.ResArticles;
import com.ruoyi.cms.res.service.IResArticlesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 资讯发布Controller
 * 
 * @author admin
 * @date 2025-04-02
 */
@RestController
@RequestMapping("/res/articles")
public class ResArticlesController extends BaseController
{
    @Autowired
    private IResArticlesService resArticlesService;

    /**
     * 查询资讯发布列表
     */
    @PreAuthorize("@ss.hasPermi('res:articles:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResArticles resArticles)
    {
        startPage();
        List<ResArticles> list = resArticlesService.selectResArticlesList(resArticles);
        return getDataTable(list);
    }

    /**
     * 导出资讯发布列表
     */
    @PreAuthorize("@ss.hasPermi('res:articles:export')")
    @Log(title = "资讯发布", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResArticles resArticles)
    {
        List<ResArticles> list = resArticlesService.selectResArticlesList(resArticles);
        ExcelUtil<ResArticles> util = new ExcelUtil<ResArticles>(ResArticles.class);
        util.exportExcel(response, list, "资讯发布数据");
    }

    /**
     * 获取资讯发布详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:articles:query')")
    @GetMapping(value = "/{articleId}")
    public AjaxResult getInfo(@PathVariable("articleId") Long articleId)
    {
        return success(resArticlesService.selectResArticlesByArticleId(articleId));
    }

    /**
     * 新增资讯发布
     */
    @PreAuthorize("@ss.hasPermi('res:articles:add')")
    @Log(title = "资讯发布", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResArticles resArticles)
    {
        return toAjax(resArticlesService.insertResArticles(resArticles));
    }

    /**
     * 修改资讯发布
     */
    @PreAuthorize("@ss.hasPermi('res:articles:edit')")
    @Log(title = "资讯发布", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResArticles resArticles)
    {
        return toAjax(resArticlesService.updateResArticles(resArticles));
    }

    /**
     * 删除资讯发布
     */
    @PreAuthorize("@ss.hasPermi('res:articles:remove')")
    @Log(title = "资讯发布", businessType = BusinessType.DELETE)
	@DeleteMapping("/{articleIds}")
    public AjaxResult remove(@PathVariable Long[] articleIds)
    {
        return toAjax(resArticlesService.deleteResArticlesByArticleIds(articleIds));
    }
}
