package com.ruoyi.cms.external.service.impl;

import com.ruoyi.cms.external.mapper.SyncOrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DeptTableCleanupService {

    @Autowired
    private SyncOrgMapper syncOrgMapper;

    // 定义时间格式，必须与你备份时的格式一致
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private static final String PREFIX = "sys_dept_bak_";

    /**
     * 异步执行：清理7天前的备份表
     */
    @Async // <--- 关键：另起线程执行，不阻塞主流程
    public void cleanOldBackupsAsync() {
        System.out.println("=== 开始异步清理过期备份表 ===");

        try {
            // 1. 获取所有备份表
            List<String> tables = syncOrgMapper.selectBackupTableNames();
            if (tables == null || tables.isEmpty()) {
                return;
            }

            // 2. 计算过期时间阈值 (当前时间 - 7天)
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

            for (String tableName : tables) {
                try {
                    // 3. 解析表名中的时间
                    // 表名格式假设: sys_dept_bak_20251124_103000
                    String timePart = tableName.replace(PREFIX, "");

                    // 简单的防误删校验：如果后缀长度不对，跳过
                    if (timePart.length() != 15) {
                        continue;
                    }

                    LocalDateTime tableTime = LocalDateTime.parse(timePart, DATE_FMT);

                    // 4. 比较时间：如果表的时间 早于 7天前
                    if (tableTime.isBefore(sevenDaysAgo)) {
                        System.out.println("发现过期备份表，准备删除: " + tableName);
                        syncOrgMapper.dropTable(tableName);
                    }
                } catch (Exception e) {
                    // 捕获单个表解析/删除异常，防止中断整个循环
                    System.err.println("处理表 " + tableName + " 时出错: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("清理备份表任务异常: " + e.getMessage());
        }

        System.out.println("=== 异步清理任务结束 ===");
    }
}