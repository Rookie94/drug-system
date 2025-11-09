package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.vo.ResVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysResLogMapper;
import com.ruoyi.system.domain.SysResLog;
import com.ruoyi.system.service.ISysResLogService;

/**
 * 资源日志Service业务层处理
 * 
 * @author admin
 * @date 2025-11-09
 */
@Service
public class SysResLogServiceImpl implements ISysResLogService 
{
    @Autowired
    private SysResLogMapper sysResLogMapper;

    /**
     * 查询资源日志
     * 
     * @param logId 资源日志主键
     * @return 资源日志
     */
    @Override
    public SysResLog selectSysResLogByLogId(Long logId)
    {
        return sysResLogMapper.selectSysResLogByLogId(logId);
    }

    /**
     * 查询资源日志列表
     * 
     * @param sysResLog 资源日志
     * @return 资源日志
     */
    @Override
    public List<SysResLog> selectSysResLogList(SysResLog sysResLog)
    {
        return sysResLogMapper.selectSysResLogList(sysResLog);
    }

    /**
     * 新增资源日志
     * 
     * @param sysResLog 资源日志
     * @return 结果
     */
    @Override
    public int insertSysResLog(SysResLog sysResLog)
    {
        sysResLog.setCreateTime(DateUtils.getNowDate());
        return sysResLogMapper.insertSysResLog(sysResLog);
    }

    /**
     * 修改资源日志
     * 
     * @param sysResLog 资源日志
     * @return 结果
     */
    @Override
    public int updateSysResLog(SysResLog sysResLog)
    {
        sysResLog.setUpdateTime(DateUtils.getNowDate());
        return sysResLogMapper.updateSysResLog(sysResLog);
    }

    /**
     * 批量删除资源日志
     * 
     * @param logIds 需要删除的资源日志主键
     * @return 结果
     */
    @Override
    public int deleteSysResLogByLogIds(Long[] logIds)
    {
        return sysResLogMapper.deleteSysResLogByLogIds(logIds);
    }

    /**
     * 删除资源日志信息
     * 
     * @param logId 资源日志主键
     * @return 结果
     */
    @Override
    public int deleteSysResLogByLogId(Long logId)
    {
        return sysResLogMapper.deleteSysResLogByLogId(logId);
    }
}
