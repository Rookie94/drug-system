package com.ruoyi.wxapp.Controller.msg;

import com.ruoyi.cms.offline.domain.Activities;
import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.domain.ChatMessage;
import com.ruoyi.cms.online.domain.vo.ChatGroupMenbersVo;
import com.ruoyi.cms.online.domain.vo.ChatMessageTreeVo;
import com.ruoyi.cms.online.domain.vo.ChatMessageVo;
import com.ruoyi.cms.online.service.IChatGroupMenbersService;
import com.ruoyi.cms.online.service.IChatGroupService;
import com.ruoyi.cms.online.service.IChatMessageService;
import com.ruoyi.cms.online.service.IChatService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/msg")
@ResponseBody
public class MsgBoardController extends BaseController {

    @Autowired
    private IChatGroupService chatGroupService;

    @Autowired
    private IChatService chatService;

    @Autowired
    private IChatGroupMenbersService chatGroupMenbersService;

    @Autowired
    private IChatMessageService chatMessageService;

    @GetMapping("/getGroup")
    public TableDataInfo getGroupList(ChatGroup chatGroup)
    {
        startPage();
        chatGroup.setAppored("2");
        chatGroup.setStatus("0");
        List<ChatGroup> list = chatGroupService.selectChatGroupList(chatGroup);
        return getDataTable(list);
    }

    @GetMapping(value = "/getGroupInfo/{groupId}")
    public AjaxResult getGroupInfo(@PathVariable("groupId") Long groupId)
    {
        return success(chatGroupService.selectChatGroupByGroupId(groupId));
    }


    @Log(title = "聊天功能", businessType = BusinessType.INSERT)
    @PostMapping(value = "/joinGroup")
    public AjaxResult joinGroup(@RequestBody ChatGroup chatGroup)
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

    @GetMapping("/getMenberState")
    public AjaxResult getMenberState(ChatGroupMenbersVo chatGroupMenbers)
    {
        chatGroupMenbers.setUserId(getUserId());
        List<ChatGroupMenbersVo> list = chatGroupMenbersService.selectChatGroupMenbersList(chatGroupMenbers);
        if(list==null || list.size()==0)
        {
            return error("没有群组访问权限");
        }
        ChatGroupMenbersVo menbersVo=list.get(0);
        if(menbersVo.getAppored().equals("0")){
            return error("入群申请审批中");
        }
        if(menbersVo.getStatus().equals("1")){
            return error("已禁止加入群聊");
        }
        else{
            return success("ok");
        }
    }

    /**
     * 查询留言信息
     */
    @GetMapping("/getMessage")
    public TableDataInfo getMessage(ChatMessageVo chatMessage)
    {
        startPage();
        LoginUser loginUser=getLoginUser();
        SysUser user=loginUser.getUser();
        if(user.getUserType().equals("00")==false){
            chatMessage.setUserId(user.getUserId());
            chatMessage.setDeptId(user.getDeptId());
        }
        List<ChatMessageVo> list = chatMessageService.selectChatMessageList(chatMessage);
        return getDataTable(list);
    }

    /**
     * 查询留言板树结构
     */
    @GetMapping("/getMessageTree/{parentMessageId}")
    public AjaxResult getMessageTree(@PathVariable("parentMessageId") Long parentMessageId)
    {
        List<ChatMessageTreeVo> tree = chatMessageService.selectChatMessageTree(parentMessageId);
        return success(tree);
    }

    /**
     * 发送留言信息
     */
    @Log(title = "留言板", businessType = BusinessType.INSERT)
    @PostMapping("/sendMessage")
    public AjaxResult SendMessage(@RequestBody ChatMessage chatMessage)
    {
        // 设置当前用户ID
        chatMessage.setUserId(getUserId());
        // 如果是回复，设置主留言状态为已回复
        if (chatMessage.getParentMessageId() != null) {
            chatMessageService.updateMainMessageStatus(chatMessage.getParentMessageId(), "1");
        }
        return toAjax(chatMessageService.insertChatMessage(chatMessage));
    }

    /**
     * 删除留言消息
     */
    @Log(title = "留言板", businessType = BusinessType.DELETE)
    @PostMapping("/removeMessage/{messageId}")
    public AjaxResult remove(@PathVariable Long messageId)
    {
        return toAjax(chatMessageService.deleteChatMessageTree(messageId));
    }

}