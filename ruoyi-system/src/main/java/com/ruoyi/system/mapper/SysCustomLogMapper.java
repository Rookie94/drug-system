package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysCustomLog;
import java.util.List;

/**
 * 自定义日志Mapper接口
 */
public interface SysCustomLogMapper {
    /**
     * 查询自定义日志
     *
     * @param logId 自定义日志主键
     * @return 自定义日志
     */
    public SysCustomLog selectCustomLogById(Long logId);

    /**
     * 查询自定义日志列表
     *
     * @param sysCustomLog 自定义日志
     * @return 自定义日志集合
     */
    public List<SysCustomLog> selectCustomLogList(SysCustomLog sysCustomLog);

    /**
     * 查询自定义日志数量
     *
     * @param sysCustomLog 自定义日志
     * @return 数量
     */
    public int selectCustomLogCount(SysCustomLog sysCustomLog);

    /**
     * 新增自定义日志
     *
     * @param sysCustomLog 自定义日志
     * @return 结果
     */
    public int insertCustomLog(SysCustomLog sysCustomLog);

    /**
     * 批量插入自定义日志
     *
     * @param logList 日志列表
     * @return 结果
     */
    public int batchInsertCustomLog(List<SysCustomLog> logList);

    /**
     * 修改自定义日志
     *
     * @param sysCustomLog 自定义日志
     * @return 结果
     */
    public int updateCustomLog(SysCustomLog sysCustomLog);

    /**
     * 删除自定义日志
     *
     * @param logId 自定义日志主键
     * @return 结果
     */
    public int deleteCustomLogById(Long logId);

    /**
     * 批量删除自定义日志
     *
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCustomLogByIds(Long[] logIds);

    /**
     * 清空自定义日志
     *
     * @return 结果
     */
    public int cleanCustomLog();
}