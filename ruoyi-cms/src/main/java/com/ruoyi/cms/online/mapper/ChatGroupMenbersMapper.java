package com.ruoyi.cms.online.mapper;

import java.util.List;
import com.ruoyi.cms.online.domain.ChatGroupMenbers;
import com.ruoyi.cms.online.domain.vo.ChatGroupMenbersVo;

/**
 * 群工作人员Mapper接口
 *
 * @author admin
 * @date 2025-04-30
 */
public interface ChatGroupMenbersMapper
{
    /**
     * 查询群工作人员
     *
     * @param mbrId 群工作人员主键
     * @return 群工作人员
     */
    public ChatGroupMenbersVo selectChatGroupMenbersByMbrId(Long mbrId);

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
     * 删除群工作人员
     *
     * @param mbrId 群工作人员主键
     * @return 结果
     */
    public int deleteChatGroupMenbersByMbrId(Long mbrId);

    /**
     * 批量删除群工作人员
     *
     * @param mbrIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatGroupMenbersByMbrIds(Long[] mbrIds);
}
