package com.ruoyi.cms.online.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ruoyi.cms.online.domain.ChatMessage;
import com.ruoyi.cms.online.domain.vo.ChatMessageTreeVo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.online.mapper.ChatMessageMapper;
import com.ruoyi.cms.online.domain.vo.ChatMessageVo;
import com.ruoyi.cms.online.service.IChatMessageService;

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
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ChatMessageVo> selectChatMessageList(ChatMessageVo chatMessage)
    {
        return chatMessageMapper.selectChatMessageList(chatMessage);
    }

    /**
     * 查询留言明细
     *
     * @param parentMessageId 主留言Id
     * messagesList 留言清单
     * @return 留言板树
     */
    @Override
    public List<ChatMessageTreeVo> selectChatMessageTree(Long parentMessageId, List<ChatMessageTreeVo> messagesList)
    {
        Map<Long, ChatMessageTreeVo> messageMap = new HashMap<>();
        for (ChatMessageTreeVo msg : messagesList) {
            messageMap.put(msg.getMessageId(), msg);
        }
        List<ChatMessageTreeVo> rootMessages = new ArrayList<>();
        for (ChatMessageTreeVo msg : messagesList) {
            Long parentId = msg.getParentMessageId();
            if (parentId == null) {
                // 顶级留言，直接加入根列表
                rootMessages.add(msg);
            } else {
                // 非顶级，找到父节点并添加到其子列表
                ChatMessageTreeVo parentMsg = messageMap.get(parentId);
                if (parentMsg != null) {
                    parentMsg.getChildren().add(msg);
                }
            }
        }
        return rootMessages;
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
     * 批量删除留言板
     * 
     * @param messageIds 需要删除的留言板主键
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
