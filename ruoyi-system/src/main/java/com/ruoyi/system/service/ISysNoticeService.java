package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysNotice;

/**
 * 通知公告Service接口
 *
 * @author admin
 * @date 2025-04-10
 */
public interface ISysNoticeService
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
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的通知公告主键集合
     * @return 结果
     */
    public int deleteSysNoticeByNoticeIds(Long[] noticeIds);

    /**
     * 删除通知公告信息
     *
     * @param noticeId 通知公告主键
     * @return 结果
     */
    public int deleteSysNoticeByNoticeId(Long noticeId);

    /**
     * 修改状态
     *
     * @param sysNotice 戒治机构
     * @return 结果
     */
    public int updateStatus(SysNotice sysNotice);

    /**
     * 批量审批
     *
     * @param ids 需要删除的主键集合
     * @return 结果
     */
    public int apporByIds(Long[] ids);

    /**
     * 审批信息
     *
     * @param id 主键
     * @return 结果
     */
    public int apporById(Long id);

    /**
     * 批量反审批
     *
     * @param ids 需要删除的主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] ids);

    /**
     * 反审批信息
     *
     * @param ids 主键
     * @return 结果
     */
    public int unApporById(Long ids);

    /**
     * 查询已审核的单据清单
     *
     * @param ids 主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids);

}
