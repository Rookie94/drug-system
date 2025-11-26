package com.ruoyi.cms.external.service.impl;

import com.ruoyi.cms.external.mapper.SyncPoliceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTableCleanupService {

    @Autowired
    private SyncPoliceMapper syncPoliceMapper;

    /**
     * 异步清理旧的备份表
     */
    @Async
    public void cleanOldBackupsAsync() {
        try {
            // 获取所有备份表名
            List<String> backupTables = syncPoliceMapper.selectBackupTableNames();

            if (backupTables.isEmpty()) {
                System.out.println("未找到用户数据备份表");
                return;
            }

            System.out.println("找到 " + backupTables.size() + " 个用户数据备份表");

            // 保留最近5个备份，删除其他旧备份
            if (backupTables.size() > 8) {
                List<String> tablesToDelete = backupTables.subList(0, backupTables.size() - 8);

                for (String tableName : tablesToDelete) {
                    try {
                        syncPoliceMapper.dropTableIfExists(tableName);
                        System.out.println("已删除旧备份表: " + tableName);
                    } catch (Exception e) {
                        System.err.println("删除备份表失败: " + tableName + ", 错误: " + e.getMessage());
                    }
                }
                System.out.println("用户数据备份表清理完成，保留最近5个备份");
            } else {
                System.out.println("用户数据备份表数量未超过限制，无需清理");
            }
        } catch (Exception e) {
            System.err.println("清理用户数据备份表时发生错误: " + e.getMessage());
        }
    }
}