package com.ruoyi.cms.online.mapper;

import java.util.List;

import com.ruoyi.cms.online.domain.ChatMessage;
import com.ruoyi.cms.online.domain.vo.ChatMessageTreeVo;
import com.ruoyi.cms.online.domain.vo.ChatMessageVo;
import org.apache.ibatis.annotations.Param;

/**
 * 留言板Mapper接口
 *
 * @author admin
 * @date 2025-05-07
 */
public interface ChatMessageMapper
{
    /**
     * 查询留言板
     *
     * @param messageId 留言板主键
     * @return 留言板
     */
    public ChatMessageVo selectChatMessageByMessageId(Long messageId);

    public List<ChatMessageTreeVo> selectMessageTree(@Param("groupId") Long groupId);

    /**
     * 查询留言板列表
     *
     * @param chatMessage 留言板
     * @return 留言板集合
     */
    public List<ChatMessageVo> selectChatMessageList(ChatMessageVo chatMessage);

    /**
     * 查询子留言
     *
     * @param parentMessageId 父留言ID
     * @return 子留言集合
     */
    public List<ChatMessageTreeVo> selectChildMessages(Long parentMessageId);

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
     * 更新留言状态
     *
     * @param chatMessage 留言板
     * @return 结果
     */
    public int updateMessageStatus(ChatMessage chatMessage);

    /**
     * 删除留言板
     *
     * @param messageId 留言板主键
     * @return 结果
     */
    public int deleteChatMessageByMessageId(Long messageId);

    /**
     * 批量删除留言板
     *
     * @param messageIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatMessageByMessageIds(Long[] messageIds);
}