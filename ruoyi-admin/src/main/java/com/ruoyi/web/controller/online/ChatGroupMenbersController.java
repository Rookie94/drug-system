package com.ruoyi.web.controller.online;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.domain.vo.ChatGroupMenbersVo;
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
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.service.IChatGroupMenbersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 群工作人员Controller
 * 
 * @author admin
 * @date 2025-04-30
 */
@RestController
@RequestMapping("/online/groupmbrs")
public class ChatGroupMenbersController extends BaseController
{
    @Autowired
    private IChatGroupMenbersService chatGroupMenbersService;

    /**
     * 查询群工作人员列表
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatGroupMenbersVo chatGroupMenbers)
    {
        startPage();
        List<ChatGroupMenbersVo> list = chatGroupMenbersService.selectChatGroupMenbersList(chatGroupMenbers);
        return getDataTable(list);
    }

    /**
     * 导出群工作人员列表
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:export')")
    @Log(title = "群工作人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatGroupMenbersVo chatGroupMenbers)
    {
        List<ChatGroupMenbersVo> list = chatGroupMenbersService.selectChatGroupMenbersList(chatGroupMenbers);
        ExcelUtil<ChatGroupMenbersVo> util = new ExcelUtil<ChatGroupMenbersVo>(ChatGroupMenbersVo.class);
        util.exportExcel(response, list, "群工作人员数据");
    }

    /**
     * 获取群工作人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:query')")
    @GetMapping(value = "/{mbrId}")
    public AjaxResult getInfo(@PathVariable("mbrId") Long mbrId)
    {
        return success(chatGroupMenbersService.selectChatGroupMenbersByMbrId(mbrId));
    }

    /**
     * 新增群工作人员
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:add')")
    @Log(title = "群工作人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatGroupMenbers chatGroupMenbers)
    {
        return toAjax(chatGroupMenbersService.insertChatGroupMenbers(chatGroupMenbers));
    }

    /**
     * 修改群工作人员
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:edit')")
    @Log(title = "群工作人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatGroupMenbers chatGroupMenbers)
    {
        return toAjax(chatGroupMenbersService.updateChatGroupMenbers(chatGroupMenbers));
    }

    /**
     * 删除群工作人员
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:remove')")
    @Log(title = "群工作人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{mbrIds}")
    public AjaxResult remove(@PathVariable Long[] mbrIds)
    {
        return toAjax(chatGroupMenbersService.deleteChatGroupMenbersByMbrIds(mbrIds));
    }

    /**
     * 查询已审核工作列表
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:list')")
    @GetMapping("/list/{groupIds}")
    public List<Integer> list(@PathVariable Long[] groupIds)
    {
        startPage();
        List<Integer> list = chatGroupMenbersService.selectApporedByIds(groupIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:edit')")
    @Log(title = "群工作人员", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ChatGroupMenbers chatGroupMenbers)
    {
        return toAjax(chatGroupMenbersService.updateStatus(chatGroupMenbers));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:appor')")
    @Log(title = "群工作人员", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(chatGroupMenbersService.apporByIds(apporParams));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('online:groupmbrs:unappor')")
    @Log(title = "群工作人员", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(chatGroupMenbersService.unApporByIds(ids));
    }

}
