package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCustomLog;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 自定义日志服务接口
 */
public interface ISysCustomLogService {
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
     * 异步插入单条日志
     *
     * @param sysLog 日志对象
     * @return CompletableFuture
     */
    CompletableFuture<Void> asyncInsertLog(SysCustomLog sysLog);

    /**
     * 异步批量插入日志
     *
     * @param logList 日志列表
     * @return CompletableFuture
     */
    CompletableFuture<Void> asyncBatchInsertLog(List<SysCustomLog> logList);

    /**
     * 同步插入单条日志
     *
     * @param sysLog 日志对象
     */
    void insertLog(SysCustomLog sysLog);

    /**
     * 同步批量插入日志
     *
     * @param logList 日志列表
     */
    void batchInsertLog(List<SysCustomLog> logList);

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