package com.ruoyi.cms.online.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ResApporParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.online.mapper.ChatGroupMapper;
import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.service.IChatGroupService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 聊天群组Service业务层处理
 * 
 * @author admin
 * @date 2025-04-17
 */
@Service
public class ChatGroupServiceImpl implements IChatGroupService 
{
    @Autowired
    private ChatGroupMapper chatGroupMapper;

    /**
     * 查询聊天群组
     * 
     * @param groupId 聊天群组主键
     * @return 聊天群组
     */
    @Override
    public ChatGroup selectChatGroupByGroupId(Long groupId)
    {
        return chatGroupMapper.selectChatGroupByGroupId(groupId);
    }

    /**
     * 查询聊天群组列表
     * 
     * @param chatGroup 聊天群组
     * @return 聊天群组
     */
    @Override
    public List<ChatGroup> selectChatGroupList(ChatGroup chatGroup)
    {
        return chatGroupMapper.selectChatGroupList(chatGroup);
    }

    /**
     * 新增聊天群组
     * 
     * @param chatGroup 聊天群组
     * @return 结果
     */
    @Override
    public int insertChatGroup(ChatGroup chatGroup)
    {
        chatGroup.setCreateBy(getUsername());
        chatGroup.setCreateTime(DateUtils.getNowDate());
        return chatGroupMapper.insertChatGroup(chatGroup);
    }

    /**
     * 修改聊天群组
     * 
     * @param chatGroup 聊天群组
     * @return 结果
     */
    @Override
    public int updateChatGroup(ChatGroup chatGroup)
    {
        chatGroup.setUpdateBy(getUsername());
        chatGroup.setUpdateTime(DateUtils.getNowDate());
        return chatGroupMapper.updateChatGroup(chatGroup);
    }

    /**
     * 批量删除聊天群组
     * 
     * @param groupIds 需要删除的聊天群组主键
     * @return 结果
     */
    @Override
    public int deleteChatGroupByGroupIds(Long[] groupIds)
    {
        return chatGroupMapper.deleteChatGroupByGroupIds(groupIds);
    }

    /**
     * 删除聊天群组信息
     * 
     * @param groupId 聊天群组主键
     * @return 结果
     */
    @Override
    public int deleteChatGroupByGroupId(Long groupId)
    {
        return chatGroupMapper.deleteChatGroupByGroupId(groupId);
    }

    /**
     * 修改技能状态
     *
     * @param chatGroup 技能
     * @return 结果
     */
    public int updateStatus(ChatGroup chatGroup)
    {
        if(chatGroup.getStatus()=="0"){
            chatGroup.setStatus("1");
        }
        else{
            chatGroup.setStatus("0");
        }
        chatGroup.setUpdateBy(getUsername());
        chatGroup.setUpdateTime(DateUtils.getNowDate());
        return chatGroupMapper.updateStatus(chatGroup);
    }

    /**
     * 批量审批
     *
     * @param apporParams 审批参数
     * @return 结果
     */
    @Override
    public int apporByIds(ResApporParam apporParams)
    {
        apporParams.setApporBy(getUsername());
        apporParams.setApporTime(DateUtils.getNowDate());
        return chatGroupMapper.apporByIds(apporParams);
    }

    /**
     * 反审批技能
     *
     * @param id 技能主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return chatGroupMapper.unApporById(id);
    }

    /**
     * 批量反审批技能
     *
     * @param ids 需要删除的技能主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return chatGroupMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 技能主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return chatGroupMapper.selectApporedByIds(ids);
    }

}
