package com.ruoyi.cms.online.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.online.domain.ChatMessage;
import com.ruoyi.cms.online.domain.vo.ChatMessageTreeVo;
import com.ruoyi.cms.online.domain.vo.ChatMessageVo;
import com.ruoyi.cms.online.mapper.ChatMessageMapper;
import com.ruoyi.cms.online.service.IChatMessageService;
import com.ruoyi.common.utils.DateUtils;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 留言板Service业务层处理
 *
 * @author admin
 * @date 2025-05-07
 */
@Service
public class ChatMessageServiceImpl implements IChatMessageService
{
    @Autowired
    private ChatMessageMapper chatMessageMapper;

    /**
     * 查询留言板
     *
     * @param messageId 留言板主键
     * @return 留言板
     */
    @Override
    public ChatMessageVo selectChatMessageByMessageId(Long messageId)
    {
        return chatMessageMapper.selectChatMessageByMessageId(messageId);
    }

    /**
     * 查询留言板列表
     *
     * @param chatMessage 留言板
     * @return 留言板
     */
    @Override
    public List<ChatMessageVo> selectChatMessageList(ChatMessageVo chatMessage)
    {
        return chatMessageMapper.selectChatMessageList(chatMessage);
    }

    /**
     * 查询留言树结构
     *
     * @param parentMessageId 主留言ID
     * @return 留言树结构
     */
    @Override
    public List<ChatMessageTreeVo> selectChatMessageTree(Long parentMessageId) {
        // 获取一级回复
        List<ChatMessageTreeVo> firstLevelReplies = chatMessageMapper.selectChildMessages(parentMessageId);

        // 递归获取所有子回复
        for (ChatMessageTreeVo reply : firstLevelReplies) {
            List<ChatMessageTreeVo> children = selectChatMessageTree(reply.getMessageId());
            reply.setReplies(children);
        }

        return firstLevelReplies;
    }

    /**
     * 新增留言板
     *
     * @param chatMessage 留言板
     * @return 结果
     */
    @Override
    public int insertChatMessage(ChatMessage chatMessage)
    {
        String message = chatMessage.getMessage();
        if (StringUtils.isEmpty(chatMessage.getTitle()) && StringUtils.isNotEmpty(message)) {
            if (message.length() > 15) {
                chatMessage.setTitle(message.substring(0, 15) + "...");
            } else {
                chatMessage.setTitle(message);
            }
        }
        chatMessage.setUserId(getUserId());
        chatMessage.setDeptId(getDeptId());
        chatMessage.setCreateBy(getUsername());
        chatMessage.setCreateTime(DateUtils.getNowDate());
        return chatMessageMapper.insertChatMessage(chatMessage);
    }

    /**
     * 修改留言板
     *
     * @param chatMessage 留言板
     * @return 结果
     */
    @Override
    public int updateChatMessage(ChatMessage chatMessage)
    {
        chatMessage.setUpdateBy(getUsername());
        chatMessage.setUpdateTime(DateUtils.getNowDate());
        return chatMessageMapper.updateChatMessage(chatMessage);
    }

    /**
     * 更新主留言状态
     *
     * @param messageId 留言ID
     * @param status 状态
     * @return 结果
     */
    @Override
    public int updateMainMessageStatus(Long messageId, String status) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId(messageId);
        chatMessage.setStatus(status);
        chatMessage.setUpdateTime(DateUtils.getNowDate());
        return chatMessageMapper.updateMessageStatus(chatMessage);
    }

    /**
     * 批量删除留言板
     *
     * @param messageIds 需要删除的留言板主键集合
     * @return 结果
     */
    @Override
    public int deleteChatMessageByMessageIds(Long[] messageIds)
    {
        return chatMessageMapper.deleteChatMessageByMessageIds(messageIds);
    }

    /**
     * 删除留言板信息
     *
     * @param messageId 留言板主键
     * @return 结果
     */
    @Override
    public int deleteChatMessageByMessageId(Long messageId)
    {
        return chatMessageMapper.deleteChatMessageByMessageId(messageId);
    }
}