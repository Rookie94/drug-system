package com.ruoyi.web.controller.res;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.ResApporParam;
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
import com.ruoyi.cms.res.domain.ResNews;
import com.ruoyi.cms.res.service.IResNewsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 戒毒资讯Controller
 * 
 * @author admin
 * @date 2025-04-10
 */
@RestController
@RequestMapping("/res/news")
public class ResNewsController extends BaseController
{
    @Autowired
    private IResNewsService resNewsService;

    /**
     * 查询戒毒资讯列表
     */
    @PreAuthorize("@ss.hasPermi('res:news:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResNews resNews)
    {
        startPage();
        List<ResNews> list = resNewsService.selectResNewsList(resNews);
        return getDataTable(list);
    }

    /**
     * 导出戒毒资讯列表
     */
    @PreAuthorize("@ss.hasPermi('res:news:export')")
    @Log(title = "戒毒资讯", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResNews resNews)
    {
        List<ResNews> list = resNewsService.selectResNewsList(resNews);
        ExcelUtil<ResNews> util = new ExcelUtil<ResNews>(ResNews.class);
        util.exportExcel(response, list, "戒毒资讯数据");
    }

    /**
     * 获取戒毒资讯详细信息
     */
    @PreAuthorize("@ss.hasPermi('res:news:query')")
    @GetMapping(value = "/{newsid}")
    public AjaxResult getInfo(@PathVariable("newsid") Long newsid)
    {
        return success(resNewsService.selectResNewsByNewsid(newsid));
    }

    /**
     * 新增戒毒资讯
     */
    @PreAuthorize("@ss.hasPermi('res:news:add')")
    @Log(title = "戒毒资讯", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResNews resNews)
    {
        return toAjax(resNewsService.insertResNews(resNews));
    }

    /**
     * 修改戒毒资讯
     */
    @PreAuthorize("@ss.hasPermi('res:news:edit')")
    @Log(title = "戒毒资讯", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResNews resNews)
    {
        return toAjax(resNewsService.updateResNews(resNews));
    }

    /**
     * 删除戒毒资讯
     */
    @PreAuthorize("@ss.hasPermi('res:news:remove')")
    @Log(title = "戒毒资讯", businessType = BusinessType.DELETE)
	@DeleteMapping("/{newsids}")
    public AjaxResult remove(@PathVariable Long[] newsids)
    {
        return toAjax(resNewsService.deleteResNewsByNewsids(newsids));
    }

    /**
     * 查询已审核资讯列表
     */
    @PreAuthorize("@ss.hasPermi('res:news:list')")
    @GetMapping("/list/{newsids}")
    public List<Integer> list(@PathVariable Long[] newsids)
    {
        startPage();
        List<Integer> list = resNewsService.selectApporedByIds(newsids);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('res:news:edit')")
    @Log(title = "戒毒资讯", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ResNews resNews)
    {
        return toAjax(resNewsService.updateStatus(resNews));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('res:news:appor')")
    @Log(title = "戒毒资讯", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(resNewsService.apporByIds(apporParams));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('res:news:unappor')")
    @Log(title = "戒毒资讯", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(resNewsService.unApporByIds(ids));
    }


}
