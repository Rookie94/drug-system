package com.ruoyi.cms.online.service.impl;

import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.domain.vo.ChatGroupMenbersVo;
import com.ruoyi.cms.online.mapper.ChatGroupMenbersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.ruoyi.cms.online.domain.vo.ChatGroupVo;
import com.ruoyi.cms.online.mapper.ChatMapper;
import com.ruoyi.cms.online.service.IChatService;
import com.ruoyi.common.utils.DateUtils;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 聊天群组Service业务层处理
 * 
 * @author admin
 * @date 2025-04-17
 */
@Service
public class ChatServiceImpl implements IChatService
{
    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatGroupMenbersMapper chatGroupMenbersMapper;

    /**
     * 查询聊天群组
     * 
     * @param groupId 聊天群组主键
     * @return 聊天群组
     */
    @Override
    public ChatGroupVo selectChatGroupByGroupId(Long groupId)
    {
        ChatGroupVo vo=chatMapper.selectChatGroupByGroupId(groupId);
        ChatGroupMenbersVo chatGroupMenbers=new ChatGroupMenbersVo();
        chatGroupMenbers.setGroupId(vo.getGroupId());
        chatGroupMenbers.setUserId(getUserId());
        List<ChatGroupMenbersVo> mbrList=chatGroupMenbersMapper.selectChatGroupMenbersList(chatGroupMenbers);
        if(mbrList!=null && mbrList.size()>0){
            if (mbrList.get(0).getStatus().equals("0")==false){
                //已禁止入群
                vo.setJoinedGroup("3");
            }
            else{
                //已申请待审批
                if( (mbrList.get(0).getAppored().equals("0"))){
                    vo.setJoinedGroup("1");
                }
                else{
                    //已入群
                    vo.setJoinedGroup("2");
                }
            }
        }
        else{
            //未入群
            vo.setJoinedGroup("0");
        }
        return vo;
    }

    /**
     * 查询聊天群组列表
     * 
     * @param chatGroup 聊天群组
     * @return 聊天群组
     */
    @Override
    public List<ChatGroupVo> selectChatGroupList(ChatGroupVo chatGroup)
    {
        List<ChatGroupVo> list=chatMapper.selectChatGroupList(chatGroup);
        for (ChatGroupVo vo : list) {
            ChatGroupMenbersVo chatGroupMenbers=new ChatGroupMenbersVo();
            chatGroupMenbers.setGroupId(vo.getGroupId());
            chatGroupMenbers.setUserId(getUserId());
            List<ChatGroupMenbersVo> mbrList=chatGroupMenbersMapper.selectChatGroupMenbersList(chatGroupMenbers);
            if(mbrList!=null && mbrList.size()>0){
                if (mbrList.get(0).getStatus().equals("0")==false){
                    //已禁止入群
                    vo.setJoinedGroup("3");
                }
                else{
                    //已申请待审批
                    if( (mbrList.get(0).getAppored().equals("0"))){
                        vo.setJoinedGroup("1");
                    }
                    else{
                        //已入群
                        vo.setJoinedGroup("2");
                    }
                }
            }
            else{
                //未入群
                vo.setJoinedGroup("0");
            }
        }
        return list;
    }

    /**
     * 申请入群
     *
     * @param chatGroupMenbers 聊天群组
     * @return 结果
     */
    @Override
    public int joinGroup(ChatGroupMenbers chatGroupMenbers)
    {
        chatGroupMenbers.setDeptId(getDeptId());
        chatGroupMenbers.setCreateBy(getUsername());
        chatGroupMenbers.setCreateTime(DateUtils.getNowDate());
        return chatGroupMenbersMapper.insertChatGroupMenbers(chatGroupMenbers);
    }

}
