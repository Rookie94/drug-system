package com.ruoyi.cms.online.service;

import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.domain.vo.ChatGroupVo;

import java.util.List;

/**
 * 聊天群组Service接口
 * 
 * @author admin
 * @date 2025-04-17
 */
public interface IChatService
{
    /**
     * 查询聊天群组
     * 
     * @param groupId 聊天群组主键
     * @return 聊天群组
     */
    public ChatGroupVo selectChatGroupByGroupId(Long groupId);

    /**
     * 查询聊天群组列表
     * 
     * @param chatGroup 聊天群组
     * @return 聊天群组集合
     */
    public List<ChatGroupVo> selectChatGroupList(ChatGroupVo chatGroup);

    /**
     * 申请入群
     *
     * @param chatGroupMenbers 聊天群组
     * @return 结果
     */
    public int joinGroup(ChatGroupMenbers chatGroupMenbers);

}
