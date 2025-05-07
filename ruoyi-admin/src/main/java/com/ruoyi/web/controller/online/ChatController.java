package com.ruoyi.web.controller.online;

import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.service.IChatGroupMenbersService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import com.ruoyi.cms.online.domain.vo.ChatGroupVo;
import com.ruoyi.cms.online.service.IChatService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ResApporParam;

import static com.ruoyi.common.utils.SecurityUtils.*;
import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 聊天群组Controller
 * 
 * @author admin
 * @date 2025-04-17
 */
@RestController
@RequestMapping("/online/chat")
public class ChatController extends BaseController
{
    @Autowired
    private IChatService chatService;

    @Autowired
    private IChatGroupMenbersService chatGroupMenbersService;

    /**
     * 查询聊天群组列表
     */
    @PreAuthorize("@ss.hasPermi('online:chat:list')")
    @GetMapping("/list")
    public TableDataInfo list(ChatGroupVo chatGroup)
    {
        startPage();
        List<ChatGroupVo> list = chatService.selectChatGroupList(chatGroup);
        return getDataTable(list);
    }

    /**
     * 获取聊天群组详细信息
     */
    @PreAuthorize("@ss.hasPermi('online:chat:query')")
    @GetMapping(value = "/{groupId}")
    public AjaxResult getInfo(@PathVariable("groupId") Long groupId)
    {
        return success(chatService.selectChatGroupByGroupId(groupId));
    }


    /**
     * 新增聊天群组
     */
    @PreAuthorize("@ss.hasPermi('online:chat:query')")
    @Log(title = "聊天功能", businessType = BusinessType.INSERT)
    @PostMapping(value = "/joingroup")
    public AjaxResult add(@RequestBody ChatGroup chatGroup)
    {
        ChatGroupMenbers chatGroupMenbers=new ChatGroupMenbers();
        chatGroupMenbers.setGroupId(chatGroup.getGroupId());
        chatGroupMenbers.setUserId(getUserId());
        chatGroupMenbers.setRemark(chatGroup.getRemark());
        if (!chatGroupMenbersService.checkGroupUserUnique(chatGroupMenbers))
        {
            return error("申请入群失败，群组已存在账号");
        }
        return toAjax(chatService.joinGroup(chatGroupMenbers));
    }

}
