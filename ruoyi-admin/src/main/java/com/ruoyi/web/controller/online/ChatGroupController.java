package com.ruoyi.web.controller.online;

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
import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.service.IChatGroupService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 聊天群组Controller
 * 
 * @author admin
 * @date 2025-04-17
 */
@RestController
@RequestMapping("/online/group")
public class ChatGroupController extends BaseController
{
    @Autowired
    private IChatGroupService chatGroupService;

    /**
     * 查询聊天群组列表
     */
    @PreAuthorize("@ss.hasPermi('online:group:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatGroup chatGroup)
    {
        startPage();
        List<ChatGroup> list = chatGroupService.selectChatGroupList(chatGroup);
        return getDataTable(list);
    }

    /**
     * 导出聊天群组列表
     */
    @PreAuthorize("@ss.hasPermi('online:group:export')")
    @Log(title = "聊天群组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatGroup chatGroup)
    {
        List<ChatGroup> list = chatGroupService.selectChatGroupList(chatGroup);
        ExcelUtil<ChatGroup> util = new ExcelUtil<ChatGroup>(ChatGroup.class);
        util.exportExcel(response, list, "聊天群组数据");
    }

    /**
     * 获取聊天群组详细信息
     */
    @PreAuthorize("@ss.hasPermi('online:group:query')")
    @GetMapping(value = "/{groupId}")
    public AjaxResult getInfo(@PathVariable("groupId") Long groupId)
    {
        return success(chatGroupService.selectChatGroupByGroupId(groupId));
    }

    /**
     * 新增聊天群组
     */
    @PreAuthorize("@ss.hasPermi('online:group:add')")
    @Log(title = "聊天群组", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatGroup chatGroup)
    {
        return toAjax(chatGroupService.insertChatGroup(chatGroup));
    }

    /**
     * 修改聊天群组
     */
    @PreAuthorize("@ss.hasPermi('online:group:edit')")
    @Log(title = "聊天群组", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatGroup chatGroup)
    {
        return toAjax(chatGroupService.updateChatGroup(chatGroup));
    }

    /**
     * 删除聊天群组
     */
    @PreAuthorize("@ss.hasPermi('online:group:remove')")
    @Log(title = "聊天群组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{groupIds}")
    public AjaxResult remove(@PathVariable Long[] groupIds)
    {
        return toAjax(chatGroupService.deleteChatGroupByGroupIds(groupIds));
    }

    /**
     * 查询已审核工作列表
     */
    @PreAuthorize("@ss.hasPermi('online:group:list')")
    @GetMapping("/list/{groupIds}")
    public List<Integer> list(@PathVariable Long[] groupIds)
    {
        startPage();
        List<Integer> list = chatGroupService.selectApporedByIds(groupIds);
        return list;
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('online:group:edit')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody ChatGroup chatGroup)
    {
        return toAjax(chatGroupService.updateStatus(chatGroup));
    }

    /**
     * 批量审批
     */
    @PreAuthorize("@ss.hasPermi('online:group:appor')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PostMapping("/appor")
    public AjaxResult appor(@RequestBody ResApporParam apporParams)
    {
        return toAjax(chatGroupService.apporByIds(apporParams));
    }

    /**
     * 反审批专家
     */
    @PreAuthorize("@ss.hasPermi('online:group:unappor')")
    @Log(title = "招聘信息", businessType = BusinessType.UPDATE)
    @PostMapping("/unappor/{ids}")
    public AjaxResult unappor(@PathVariable Long[] ids)
    {
        return toAjax(chatGroupService.unApporByIds(ids));
    }

}
