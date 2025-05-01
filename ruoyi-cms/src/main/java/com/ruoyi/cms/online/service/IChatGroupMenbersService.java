package com.ruoyi.cms.online.service;

import java.util.List;

import com.ruoyi.cms.job.domain.ResJobinfo;
import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.domain.vo.ChatGroupMenbersVo;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 群工作人员Service接口
 *
 * @author admin
 * @date 2025-04-30
 */
public interface IChatGroupMenbersService
{
    /**
     * 查询群工作人员
     *
     * @param mbrId 群工作人员主键
     * @return 群工作人员
     */
    public ChatGroupMenbersVo selectChatGroupMenbersByMbrId(Long mbrId);

    /**
     * 查询聊天群组列表
     *
     * @param chatGroup 聊天群组
     * @return 聊天群组集合
     */
    public List<ChatGroup> selectChatGroupList(ChatGroup chatGroup);

    /**
     * 校验群组成员是否唯一
     *
     * @param chatGroupMenbers 角色信息
     * @return 结果
     */
    public boolean checkGroupUserUnique(ChatGroupMenbers chatGroupMenbers);

    /**
     * 查询群工作人员列表
     *
     * @param chatGroupMenbers 群工作人员
     * @return 群工作人员集合
     */
    public List<ChatGroupMenbersVo> selectChatGroupMenbersList(ChatGroupMenbersVo chatGroupMenbers);

    /**
     * 新增群工作人员
     *
     * @param chatGroupMenbers 群工作人员
     * @return 结果
     */
    public int insertChatGroupMenbers(ChatGroupMenbers chatGroupMenbers);

    /**
     * 修改群工作人员
     *
     * @param chatGroupMenbers 群工作人员
     * @return 结果
     */
    public int updateChatGroupMenbers(ChatGroupMenbers chatGroupMenbers);

    /**
     * 批量删除群工作人员
     *
     * @param mbrIds 需要删除的群工作人员主键集合
     * @return 结果
     */
    public int deleteChatGroupMenbersByMbrIds(Long[] mbrIds);

    /**
     * 删除群工作人员信息
     *
     * @param mbrId 群工作人员主键
     * @return 结果
     */
    public int deleteChatGroupMenbersByMbrId(Long mbrId);

    /**
     * 修改状态
     *
     * @param chatGroupMenbers 工作
     * @return 结果
     */
    public int updateStatus(ChatGroupMenbers chatGroupMenbers);

    /**
     * 批量审批
     *
     * @param apporParams 批量审批参数
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);
    /**
     * 反审批
     *
     * @param Id 工作主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 工作主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);

}
