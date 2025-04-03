package com.ruoyi.web.controller.wxsys;

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
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.wxsys.service.IMiniAppUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 小程序用户Controller
 * 
 * @author 盖子
 * @date 2024-11-22
 */
@RestController
@RequestMapping("/wxsys/miniappUser")
public class MiniAppUserController extends BaseController
{
    @Autowired
    private IMiniAppUserService miniAppUserService;

    /**
     * 查询小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniappUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(MiniAppUser miniAppUser)
    {
        startPage();
        List<MiniAppUser> list = miniAppUserService.selectMiniAppUserList(miniAppUser);
        return getDataTable(list);
    }

    /**
     * 导出小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniappUser:export')")
    @Log(title = "小程序用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MiniAppUser miniAppUser)
    {
        List<MiniAppUser> list = miniAppUserService.selectMiniAppUserList(miniAppUser);
        ExcelUtil<MiniAppUser> util = new ExcelUtil<MiniAppUser>(MiniAppUser.class);
        util.exportExcel(response, list, "小程序用户数据");
    }

    /**
     * 获取小程序用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniappUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(miniAppUserService.selectMiniAppUserById(id));
    }

    /**
     * 新增小程序用户
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniappUser:add')")
    @Log(title = "小程序用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MiniAppUser miniAppUser)
    {
        return toAjax(miniAppUserService.insertMiniAppUser(miniAppUser));
    }

    /**
     * 修改小程序用户
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniappUser:edit')")
    @Log(title = "小程序用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MiniAppUser miniAppUser)
    {
        return toAjax(miniAppUserService.updateMiniAppUser(miniAppUser));
    }

    /**
     * 删除小程序用户
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniappUser:remove')")
    @Log(title = "小程序用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(miniAppUserService.deleteMiniAppUserByIds(ids));
    }

    /**
     * 查询小程序用户列表
     */
    @PreAuthorize("@ss.hasPermi('wxsys:miniappUser:list')")
    @GetMapping("/innerJoinList")
    public TableDataInfo innerJoinList(MiniAppUser miniAppUser)
    {
        startPage();
        List<MiniAppUser> list = miniAppUserService.selectMiniAppUserInnerJoinMiniAppList(miniAppUser);
        return getDataTable(list);
    }
}
