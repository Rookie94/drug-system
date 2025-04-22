package com.ruoyi.cms.online.mapper;

import java.util.List;

import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 聊天群组Mapper接口
 * 
 * @author admin
 * @date 2025-04-17
 */
public interface ChatGroupMapper 
{
    /**
     * 查询聊天群组
     * 
     * @param groupId 聊天群组主键
     * @return 聊天群组
     */
    public ChatGroup selectChatGroupByGroupId(Long groupId);

    /**
     * 查询聊天群组列表
     * 
     * @param chatGroup 聊天群组
     * @return 聊天群组集合
     */
    public List<ChatGroup> selectChatGroupList(ChatGroup chatGroup);

    /**
     * 新增聊天群组
     * 
     * @param chatGroup 聊天群组
     * @return 结果
     */
    public int insertChatGroup(ChatGroup chatGroup);

    /**
     * 修改聊天群组
     * 
     * @param chatGroup 聊天群组
     * @return 结果
     */
    public int updateChatGroup(ChatGroup chatGroup);

    /**
     * 删除聊天群组
     * 
     * @param groupId 聊天群组主键
     * @return 结果
     */
    public int deleteChatGroupByGroupId(Long groupId);

    /**
     * 批量删除聊天群组
     * 
     * @param groupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatGroupByGroupIds(Long[] groupIds);

    /**
     * 修改技能状态
     *
     * @param chatGroup 技能
     * @return 结果
     */
    public int updateStatus(ChatGroup chatGroup);

    /**
     * 批量审批
     *
     * @param apporParams 主键集合
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);

    /**
     * 批量反审批技能
     *
     * @param ids 需要删除的技能主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] ids);

    /**
     * 反审批技能信息
     *
     * @param ids 技能主键
     * @return 结果
     */
    public int unApporById(Long ids);

    /**
     * 查询已审核的单据清单
     *
     * @param ids 技能主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids);

}
