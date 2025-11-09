package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysResLog;

/**
 * 资源日志Mapper接口
 * 
 * @author admin
 * @date 2025-11-09
 */
public interface SysResLogMapper 
{
    /**
     * 查询资源日志
     * 
     * @param logId 资源日志主键
     * @return 资源日志
     */
    public SysResLog selectSysResLogByLogId(Long logId);

    /**
     * 查询资源日志列表
     * 
     * @param sysResLog 资源日志
     * @return 资源日志集合
     */
    public List<SysResLog> selectSysResLogList(SysResLog sysResLog);

    /**
     * 新增资源日志
     * 
     * @param sysResLog 资源日志
     * @return 结果
     */
    public int insertSysResLog(SysResLog sysResLog);

    /**
     * 修改资源日志
     * 
     * @param sysResLog 资源日志
     * @return 结果
     */
    public int updateSysResLog(SysResLog sysResLog);

    /**
     * 删除资源日志
     * 
     * @param logId 资源日志主键
     * @return 结果
     */
    public int deleteSysResLogByLogId(Long logId);

    /**
     * 批量删除资源日志
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysResLogByLogIds(Long[] logIds);
}
