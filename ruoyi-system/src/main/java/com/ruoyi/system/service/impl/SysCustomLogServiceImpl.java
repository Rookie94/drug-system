package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysCustomLog;
import com.ruoyi.system.mapper.SysCustomLogMapper;
import com.ruoyi.system.service.ISysCustomLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义日志服务实现 - 高性能版本
 */
@Service
@Slf4j
public class SysCustomLogServiceImpl implements ISysCustomLogService {

    @Autowired
    private SysCustomLogMapper customLogMapper;

    // 高性能缓冲队列配置
    private static final int BUFFER_CAPACITY = 1000; // 缓冲队列容量
    private static final int BATCH_SIZE = 100; // 批量插入大小
    private static final long FLUSH_INTERVAL = 5000; // 刷新间隔(毫秒)

    // 线程安全的缓冲队列
    private final BlockingQueue<SysCustomLog> logBuffer = new LinkedBlockingQueue<>(BUFFER_CAPACITY);

    // 批量处理执行器
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "log-batch-processor");
        t.setDaemon(true);
        return t;
    });

    // 异步处理执行器
    private final ExecutorService asyncExecutor = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            r -> {
                Thread t = new Thread(r, "log-async-processor");
                t.setDaemon(true);
                return t;
            },
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private volatile boolean running = true;
    private final AtomicInteger bufferSize = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        // 启动定时批量处理任务
        scheduler.scheduleAtFixedRate(this::flushBuffer, FLUSH_INTERVAL, FLUSH_INTERVAL, TimeUnit.MILLISECONDS);
        log.info("自定义日志服务初始化完成，批量处理已启动");
    }

    @PreDestroy
    public void destroy() {
        running = false;
        // 关闭前刷新缓冲区的所有日志
        flushBuffer();
        scheduler.shutdown();
        asyncExecutor.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            if (!asyncExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                asyncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            asyncExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        log.info("自定义日志服务已关闭");
    }

    /**
     * 异步插入单条日志
     */
    @Async("logExecutor")
    @Override
    public CompletableFuture<Void> asyncInsertLog(SysCustomLog sysLog) {
        return CompletableFuture.runAsync(() -> insertLogInternal(sysLog), asyncExecutor);
    }

    /**
     * 异步批量插入日志
     */
    @Async("logExecutor")
    @Override
    public CompletableFuture<Void> asyncBatchInsertLog(List<SysCustomLog> logList) {
        return CompletableFuture.runAsync(() -> batchInsertLogInternal(logList), asyncExecutor);
    }

    /**
     * 同步插入单条日志 - 使用缓冲队列提高性能
     */
    @Override
    public void insertLog(SysCustomLog sysLog) {
        if (sysLog == null) {
            return;
        }

        try {
            // 设置操作时间
            if (sysLog.getOperTime() == null) {
                sysLog.setOperTime(new Date());
            }

            // 尝试放入缓冲队列
            if (logBuffer.offer(sysLog, 100, TimeUnit.MILLISECONDS)) {
                int currentSize = bufferSize.incrementAndGet();

                // 如果缓冲队列达到批量大小，立即触发批量插入
                if (currentSize >= BATCH_SIZE) {
                    flushBuffer();
                }
            } else {
                // 队列已满，直接插入单条记录
                log.warn("日志缓冲队列已满，直接插入单条日志");
                insertLogInternal(sysLog);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // 队列操作被中断，直接插入
            insertLogInternal(sysLog);
        } catch (Exception e) {
            log.error("插入日志到缓冲队列失败", e);
            // 降级处理：直接插入
            insertLogInternal(sysLog);
        }
    }

    /**
     * 同步批量插入日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsertLog(List<SysCustomLog> logList) {
        batchInsertLogInternal(logList);
    }

    /**
     * 刷新缓冲区，批量插入日志
     */
    private void flushBuffer() {
        if (bufferSize.get() == 0) {
            return;
        }

        List<SysCustomLog> batchList = new ArrayList<>(BATCH_SIZE);
        int drained = logBuffer.drainTo(batchList, BATCH_SIZE);

        if (drained > 0) {
            bufferSize.addAndGet(-drained);
            batchInsertLogInternal(batchList);
        }
    }

    /**
     * 内部批量插入方法
     */
    private void batchInsertLogInternal(List<SysCustomLog> logList) {
        if (CollectionUtils.isEmpty(logList)) {
            return;
        }

        try {
            // 设置创建时间和更新时间
            Date now = new Date();
            for (SysCustomLog logItem : logList) {
                if (logItem.getCreateTime() == null) {
                    logItem.setCreateTime(now);
                }
                if (logItem.getUpdateTime() == null) {
                    logItem.setUpdateTime(now);
                }
            }

            // 批量插入
            customLogMapper.batchInsertCustomLog(logList);
            log.debug("批量插入 {} 条日志成功", logList.size());
        } catch (Exception e) {
            log.error("批量插入日志失败，数量: {}", logList.size(), e);

            // 降级处理：尝试单条插入
            for (SysCustomLog logItem : logList) {
                try {
                    insertLogInternal(logItem);
                } catch (Exception ex) {
                    log.error("单条插入日志也失败，日志标题: {}", logItem.getLogTitle(), ex);
                }
            }
        }
    }

    /**
     * 内部单条插入方法
     */
    private void insertLogInternal(SysCustomLog sysLog) {
        if (sysLog == null) {
            return;
        }

        try {
            // 设置时间
            Date now = new Date();
            if (sysLog.getCreateTime() == null) {
                sysLog.setCreateTime(now);
            }
            if (sysLog.getUpdateTime() == null) {
                sysLog.setUpdateTime(now);
            }
            if (sysLog.getOperTime() == null) {
                sysLog.setOperTime(now);
            }

            customLogMapper.insertCustomLog(sysLog);
        } catch (Exception e) {
            log.error("插入单条日志失败，标题: {}", sysLog.getLogTitle(), e);
            // 这里可以选择将失败日志写入文件或其他存储
        }
    }

    // 其他查询和删除方法
    @Override
    public List<SysCustomLog> selectCustomLogList(SysCustomLog queryLog) {
        return customLogMapper.selectCustomLogList(queryLog);
    }

    @Override
    public int selectCustomLogCount(SysCustomLog sysCustomLog) {
        return customLogMapper.selectCustomLogCount(sysCustomLog);
    }

    @Override
    public SysCustomLog selectCustomLogById(Long logId) {
        return customLogMapper.selectCustomLogById(logId);
    }

    @Override
    public int deleteCustomLogById(Long logId) {
        return customLogMapper.deleteCustomLogById(logId);
    }

    @Override
    public int deleteCustomLogByIds(Long[] logIds) {
        return customLogMapper.deleteCustomLogByIds(logIds);
    }

    @Override
    public int cleanCustomLog() {
        return customLogMapper.cleanCustomLog();
    }

}