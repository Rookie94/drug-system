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
     * 查询留言树结构
     *
     * @param parentMessageId 主留言ID
     * @return 留言树结构
     */
    public List<ChatMessageTreeVo> selectChatMessageTree(Long parentMessageId);

    /**
     * 查询子留言id
     *
     * @param parentMessageId 父留言ID
     * @return 子留言集合
     */
    public List<Long> selectChildMessageIds(Long parentMessageId);

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
     * 更新主留言状态
     *
     * @param messageId 留言ID
     * @param status 状态
     * @return 结果
     */
    public int updateMainMessageStatus(Long messageId, String status);

    /**
     * 批量删除留言板
     *
     * @param messageId 需要删除的留言板主键集合
     * @return 结果
     */
    public int deleteChatMessageByMessageId(Long messageId);

    /**
     * 批量删除留言板
     *
     * @param messageIds 需要删除的留言板主键集合
     * @return 结果
     */
    public int deleteChatMessageByMessageIds(Long[] messageIds);

    /**
     * 删除留言树信息
     *
     * @param rootMessageId 留言板主键
     * @return 结果
     */

    public int deleteChatMessageTree(Long rootMessageId);

}