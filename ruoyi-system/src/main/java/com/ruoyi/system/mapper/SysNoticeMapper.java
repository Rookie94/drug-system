package com.ruoyi.system.mapper;

import java.util.Date;
import java.util.List;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.domain.ResApporParam;
import org.apache.ibatis.annotations.Param;

/**
 * 通知公告Mapper接口
 *
 * @author admin
 * @date 2025-04-10
 */
public interface SysNoticeMapper
{
    /**
     * 查询通知公告
     *
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    public SysNotice selectSysNoticeByNoticeId(Long noticeId);

    /**
     * 查询通知公告列表
     *
     * @param sysNotice 通知公告
     * @return 通知公告集合
     */
    public List<SysNotice> selectSysNoticeList(SysNotice sysNotice);

    /**
     * 新增通知公告
     *
     * @param sysNotice 通知公告
     * @return 结果
     */
    public int insertSysNotice(SysNotice sysNotice);

    /**
     * 修改通知公告
     *
     * @param sysNotice 通知公告
     * @return 结果
     */
    public int updateSysNotice(SysNotice sysNotice);

    /**
     * 删除通知公告
     *
     * @param noticeId 通知公告主键
     * @return 结果
     */
    public int deleteSysNoticeByNoticeId(Long noticeId);

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysNoticeByNoticeIds(Long[] noticeIds);

    /**
     * 修改状态
     *
     * @param sysNotice 通知公告
     * @return 结果
     */
    public int updateStatus(SysNotice sysNotice);

    /**
     * 批量审批
     *
     * @param apporParams 批量审批参数
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);
    /**
     * 反审批通知公告
     *
     * @param Id 通知公告主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批通知公告
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 通知公告主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);

}
