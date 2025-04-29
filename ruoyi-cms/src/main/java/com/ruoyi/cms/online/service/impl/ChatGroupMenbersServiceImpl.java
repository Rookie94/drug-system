package com.ruoyi.cms.online.service.impl;

import java.util.List;

import com.ruoyi.cms.online.domain.vo.ChatGroupMenbersVo;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.online.mapper.ChatGroupMenbersMapper;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.service.IChatGroupMenbersService;

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
     * 查询群工作人员列表
     *
     * @param chatGroupMenbers 群工作人员
     * @return 群工作人员
     */
    @Override
    public List<ChatGroupMenbersVo> selectChatGroupMenbersList(ChatGroupMenbersVo chatGroupMenbers)
    {
        return chatGroupMenbersMapper.selectChatGroupMenbersList(chatGroupMenbers);
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
}
