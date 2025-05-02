package com.ruoyi.cms.online.mapper;

import com.ruoyi.cms.online.domain.vo.ChatGroupVo;

import java.util.List;

/**
 * 聊天群组Mapper接口
 * 
 * @author admin
 * @date 2025-04-17
 */
public interface ChatMapper
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
}
