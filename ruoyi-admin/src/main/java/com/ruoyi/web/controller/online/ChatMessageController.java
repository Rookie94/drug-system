package com.ruoyi.web.controller.online;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.online.domain.vo.ChatMessageVo;
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
import com.ruoyi.cms.online.domain.ChatMessage;
import com.ruoyi.cms.online.service.IChatMessageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 留言板Controller
 * 
 * @author admin
 * @date 2025-05-07
 */
@RestController
@RequestMapping("/online/msg")
public class ChatMessageController extends BaseController
{
    @Autowired
    private IChatMessageService chatMessageService;

    /**
     * 查询留言板列表
     */
    @PreAuthorize("@ss.hasPermi('online:chat:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatMessageVo chatMessage)
    {
        startPage();
        List<ChatMessageVo> list = chatMessageService.selectChatMessageList(chatMessage);
        return getDataTable(list);
    }

    /**
     * 导出留言板列表
     */
    @PreAuthorize("@ss.hasPermi('online:chat:export')")
    @Log(title = "留言板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ChatMessageVo chatMessage)
    {
        List<ChatMessageVo> list = chatMessageService.selectChatMessageList(chatMessage);
        ExcelUtil<ChatMessageVo> util = new ExcelUtil<ChatMessageVo>(ChatMessageVo.class);
        util.exportExcel(response, list, "留言板数据");
    }

    /**
     * 获取留言板详细信息
     */
    @PreAuthorize("@ss.hasPermi('online:chat:query')")
    @GetMapping(value = "/{messageId}")
    public AjaxResult getInfo(@PathVariable("messageId") Long messageId)
    {
        return success(chatMessageService.selectChatMessageByMessageId(messageId));
    }

    /**
     * 新增留言板
     */
    @PreAuthorize("@ss.hasPermi('online:chat:add')")
    @Log(title = "留言板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ChatMessage chatMessage)
    {
        return toAjax(chatMessageService.insertChatMessage(chatMessage));
    }

    /**
     * 修改留言板
     */
    @PreAuthorize("@ss.hasPermi('online:chat:edit')")
    @Log(title = "留言板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ChatMessage chatMessage)
    {
        return toAjax(chatMessageService.updateChatMessage(chatMessage));
    }

    /**
     * 删除留言板
     */
    @PreAuthorize("@ss.hasPermi('online:chat:remove')")
    @Log(title = "留言板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{messageIds}")
    public AjaxResult remove(@PathVariable Long[] messageIds)
    {
        return toAjax(chatMessageService.deleteChatMessageByMessageIds(messageIds));
    }

    /**
     * 发送留言信息
     */
    @Log(title = "留言板", businessType = BusinessType.INSERT)
    @PostMapping("/SendMessage")
    public AjaxResult SendMessage(@RequestBody ChatMessage chatMessage)
    {
        return toAjax(chatMessageService.insertChatMessage(chatMessage));
    }

}
