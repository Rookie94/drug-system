package com.ruoyi.cms.online.service.impl;

import java.util.List;

import com.ruoyi.cms.online.domain.ChatGroup;
import com.ruoyi.cms.online.domain.vo.ChatGroupMenbersVo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ResApporParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.online.mapper.ChatGroupMenbersMapper;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.service.IChatGroupMenbersService;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 群工作人员Service业务层处理
 *
 * @author admin
 * @date 2025-04-30
 */
@Service
public class ChatGroupMenbersServiceImpl implements IChatGroupMenbersService
{
    @Autowired
    private ChatGroupMenbersMapper chatGroupMenbersMapper;

    /**
     * 查询群工作人员
     *
     * @param mbrId 群工作人员主键
     * @return 群工作人员
     */
    @Override
    public ChatGroupMenbersVo selectChatGroupMenbersByMbrId(Long mbrId)
    {
        return chatGroupMenbersMapper.selectChatGroupMenbersByMbrId(mbrId);
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
        return chatGroupMenbersMapper.selectChatGroupList(chatGroup);
    }

    /**
     * 查询群工作人员列表
     *
     * @param chatGroupMenbers 群工作人员
     * @return 群工作人员
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ChatGroupMenbersVo> selectChatGroupMenbersList(ChatGroupMenbersVo chatGroupMenbers)
    {
        return chatGroupMenbersMapper.selectChatGroupMenbersList(chatGroupMenbers);
    }

    /**
     * 校验群组成员是否唯一
     *
     * @param chatGroupMenbers 角色信息
     * @return 结果
     */
    @Override
    public boolean checkGroupUserUnique(ChatGroupMenbers chatGroupMenbers)
    {
        Long mbrId = StringUtils.isNull(chatGroupMenbers.getMbrId()) ? -1L : chatGroupMenbers.getMbrId();
        ChatGroupMenbersVo groupMenber = chatGroupMenbersMapper.checkGroupUserUnique(chatGroupMenbers);
        if (StringUtils.isNotNull(groupMenber) && groupMenber.getMbrId().longValue()!=mbrId.longValue())
        {
            return false;
        }
        return true;
    }

    /**
     * 新增群工作人员
     *
     * @param chatGroupMenbers 群工作人员
     * @return 结果
     */
    @Override
    public int insertChatGroupMenbers(ChatGroupMenbers chatGroupMenbers)
    {
        chatGroupMenbers.setCreateBy(getUsername());
        chatGroupMenbers.setCreateTime(DateUtils.getNowDate());
        return chatGroupMenbersMapper.insertChatGroupMenbers(chatGroupMenbers);
    }

    /**
     * 修改群工作人员
     *
     * @param chatGroupMenbers 群工作人员
     * @return 结果
     */
    @Override
    public int updateChatGroupMenbers(ChatGroupMenbers chatGroupMenbers)
    {
        chatGroupMenbers.setUpdateBy(getUsername());
        chatGroupMenbers.setUpdateTime(DateUtils.getNowDate());
        return chatGroupMenbersMapper.updateChatGroupMenbers(chatGroupMenbers);
    }

    /**
     * 批量删除群工作人员
     *
     * @param mbrIds 需要删除的群工作人员主键
     * @return 结果
     */
    @Override
    public int deleteChatGroupMenbersByMbrIds(Long[] mbrIds)
    {
        return chatGroupMenbersMapper.deleteChatGroupMenbersByMbrIds(mbrIds);
    }

    /**
     * 删除群工作人员信息
     *
     * @param mbrId 群工作人员主键
     * @return 结果
     */
    @Override
    public int deleteChatGroupMenbersByMbrId(Long mbrId)
    {
        return chatGroupMenbersMapper.deleteChatGroupMenbersByMbrId(mbrId);
    }

    /**
     * 修改工作状态
     *
     * @param chatGroupMenbers 工作
     * @return 结果
     */
    public int updateStatus(ChatGroupMenbers chatGroupMenbers)
    {
        chatGroupMenbers.setUpdateBy(getUsername());
        chatGroupMenbers.setUpdateTime(DateUtils.getNowDate());
        return chatGroupMenbersMapper.updateStatus(chatGroupMenbers);
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
        return chatGroupMenbersMapper.apporByIds(apporParams);
    }

    /**
     * 反审批工作
     *
     * @param id 工作主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return chatGroupMenbersMapper.unApporById(id);
    }

    /**
     * 批量反审批工作
     *
     * @param ids 需要删除的工作主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return chatGroupMenbersMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 工作主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return chatGroupMenbersMapper.selectApporedByIds(ids);
    }

}
