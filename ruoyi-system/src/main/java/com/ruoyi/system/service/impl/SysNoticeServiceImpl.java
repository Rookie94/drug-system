package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysNoticeMapper;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 通知公告Service业务层处理
 *
 * @author admin
 * @date 2025-04-10
 */
@Service
public class SysNoticeServiceImpl implements ISysNoticeService
{
    @Autowired
    private SysNoticeMapper sysNoticeMapper;

    /**
     * 查询通知公告
     *
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    @Override
    public SysNotice selectSysNoticeByNoticeId(Long noticeId)
    {
        return sysNoticeMapper.selectSysNoticeByNoticeId(noticeId);
    }

    /**
     * 查询通知公告列表
     *
     * @param sysNotice 通知公告
     * @return 通知公告
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<SysNotice> selectSysNoticeList(SysNotice sysNotice)
    {
        return sysNoticeMapper.selectSysNoticeList(sysNotice);
    }

    /**
     * 新增通知公告
     *
     * @param sysNotice 通知公告
     * @return 结果
     */
    @Override
    public int insertSysNotice(SysNotice sysNotice)
    {
        sysNotice.setUserId(getUserId());
        sysNotice.setDeptId(getDeptId());
        sysNotice.setCreateBy(getUsername());
        sysNotice.setCreateTime(DateUtils.getNowDate());
        return sysNoticeMapper.insertSysNotice(sysNotice);
    }

    /**
     * 修改通知公告
     *
     * @param sysNotice 通知公告
     * @return 结果
     */
    @Override
    public int updateSysNotice(SysNotice sysNotice)
    {
        sysNotice.setUpdateBy(getUsername());
        sysNotice.setUpdateTime(DateUtils.getNowDate());
        return sysNoticeMapper.updateSysNotice(sysNotice);
    }

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的通知公告主键
     * @return 结果
     */
    @Override
    public int deleteSysNoticeByNoticeIds(Long[] noticeIds)
    {
        return sysNoticeMapper.deleteSysNoticeByNoticeIds(noticeIds);
    }

    /**
     * 删除通知公告信息
     *
     * @param noticeId 通知公告主键
     * @return 结果
     */
    @Override
    public int deleteSysNoticeByNoticeId(Long noticeId)
    {
        return sysNoticeMapper.deleteSysNoticeByNoticeId(noticeId);
    }

    /**
     * 修改状态
     *
     * @param sysNotice 戒治机构
     * @return 结果
     */
    public int updateStatus(SysNotice sysNotice)
    {
        if(sysNotice.getStatus()=="0"){
            sysNotice.setStatus("1");
        }
        else{
            sysNotice.setStatus("0");
        }
        sysNotice.setUpdateBy(getUsername());
        sysNotice.setUpdateTime(DateUtils.getNowDate());
        return sysNoticeMapper.updateStatus(sysNotice);
    }

    /**
     * 审批信息
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public int apporById(Long id)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return sysNoticeMapper.apporById(id,userName,apporDate);
    }

    /**
     * 批量审批戒治机构
     *
     * @param ids 需要删除的主键
     * @return 结果
     */
    @Override
    public int apporByIds(Long[] ids)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return sysNoticeMapper.apporByIds(ids,userName,apporDate);
    }

    /**
     * 反审批
     *
     * @param id 主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return sysNoticeMapper.unApporById(id);
    }

    /**
     * 批量反审批
     *
     * @param ids 需要删除的主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return sysNoticeMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return sysNoticeMapper.selectApporedByIds(ids);
    }

}
