package com.ruoyi.cms.online.service;

import java.util.List;

import com.ruoyi.cms.online.domain.ChatMessage;
import com.ruoyi.cms.online.domain.vo.ChatMessageTreeVo;
import com.ruoyi.cms.online.domain.vo.ChatMessageVo;

/**
 * 留言板Service接口
 * 
 * @author admin
 * @date 2025-05-07
 */
public interface IChatMessageService 
{
    /**
     * 查询留言板
     * 
     * @param messageId 留言板主键
     * @return 留言板
     */
    public ChatMessageVo selectChatMessageByMessageId(Long messageId);

    /**
     * 查询留言板列表
     * 
     * @param chatMessage 留言板
     * @return 留言板集合
     */
    public List<ChatMessageVo> selectChatMessageList(ChatMessageVo chatMessage);

    /**
     * 查询留言明细
     *
     * @param parentMessageId 主留言Id
     * messagesList 留言清单
     * @return 留言板
     */
    public List<ChatMessageTreeVo> selectChatMessageTree(Long parentMessageId, List<ChatMessageTreeVo> messagesList);

    /**
     * 新增留言板
     * 
     * @param chatMessage 留言板
     * @return 结果
     */
    public int insertChatMessage(ChatMessage chatMessage);

    /**
     * 修改留言板
     * 
     * @param chatMessage 留言板
     * @return 结果
     */
    public int updateChatMessage(ChatMessage chatMessage);

    /**
     * 批量删除留言板
     * 
     * @param messageIds 需要删除的留言板主键集合
     * @return 结果
     */
    public int deleteChatMessageByMessageIds(Long[] messageIds);

    /**
     * 删除留言板信息
     * 
     * @param messageId 留言板主键
     * @return 结果
     */
    public int deleteChatMessageByMessageId(Long messageId);
}
