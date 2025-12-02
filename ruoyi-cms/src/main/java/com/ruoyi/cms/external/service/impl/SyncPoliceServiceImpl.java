package com.ruoyi.cms.external.service.impl;

import com.ruoyi.cms.external.domain.MidPolice;
import com.ruoyi.cms.external.domain.SyncUserInfo;
import com.ruoyi.cms.external.mapper.SyncPoliceMapper;
import com.ruoyi.cms.external.service.ISyncPoliceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SyncPoliceServiceImpl implements ISyncPoliceService {

    private static final Logger logger = LoggerFactory.getLogger(SyncPoliceServiceImpl.class);

    @Autowired
    private SyncPoliceMapper syncPoliceMapper;

    @Autowired
    private UserTableCleanupService userTableCleanupService;

    /**
     * 备份逻辑
     */
    private void backupSysUserinfo() {
        // 1. 生成备份表名，例如: sys_userinfo_bak_20251124_140522
        String timeSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupTableName = "sys_userinfo_bak_" + timeSuffix;

        try {
            // 2. 执行备份
            syncPoliceMapper.createBackupTable(backupTableName);
            logger.info("警察数据目标表已备份至: {}", backupTableName);
        } catch (Exception e) {
            // 3. 决策：如果备份失败，是否允许继续同步？
            // 通常建议抛出异常，停止同步，防止数据丢失风险
            throw new RuntimeException("备份警察数据目标表失败，同步终止: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncPoliceData() {
        logger.info("开始同步警察数据...");

        // 1. 备份当前表
        backupSysUserinfo();

        // 2. 触发清理旧表
        userTableCleanupService.cleanOldBackupsAsync();

        // 3. 获取所有中间表数据
        List<MidPolice> midPoliceList = syncPoliceMapper.selectAllMidPolice();
        logger.info("从中间表获取到 {} 条警察数据", midPoliceList.size());

        // 4. 获取部门映射关系 (org_id -> dept_id) - 修复：使用selectList而不是selectOne
        List<Map<String, Object>> deptMappingList = syncPoliceMapper.selectDeptMapping();
        Map<String, Long> orgIdToDeptIdMap = new HashMap<>();
        for (Map<String, Object> map : deptMappingList) {
            String orgId = (String) map.get("org_id");
            Long deptId = ((Number) map.get("dept_id")).longValue();
            orgIdToDeptIdMap.put(orgId, deptId);
        }
        logger.info("获取到 {} 个部门映射关系", orgIdToDeptIdMap.size());

        int insertCount = 0;
        int updateCount = 0;
        int deptUpdateCount = 0;

        // 5. 遍历中间表数据，同步到目标表
        for (MidPolice police : midPoliceList) {
            try {
                // 转换性别
                String sexCode = convertSex(police.getSex());

                // 检查是否已存在
                SyncUserInfo existingUser = syncPoliceMapper.selectUserByUserGuid(police.getId());

                // 获取对应的部门ID
                Long deptId = orgIdToDeptIdMap.get(police.getOrgId());
                if (deptId == null) {
                    logger.warn("警察 {} 的 org_id {} 在部门表中找不到对应部门，使用默认部门ID 100",
                            police.getName(), police.getOrgId());
                    deptId = 100L; // 默认部门ID
                }

                if (existingUser == null) {
                    // 插入新记录
                    SyncUserInfo newUser = new SyncUserInfo();
                    newUser.setUserGuid(police.getId());
                    newUser.setUserName(police.getMobileNumber());
                    newUser.setNickName(police.getName());
                    newUser.setSex(sexCode);
                    newUser.setBirthday(police.getBirthday());
                    newUser.setPhoneNumber(police.getMobileNumber());
                    newUser.setOrgId(police.getOrgId());
                    newUser.setAddress(police.getAddress());
                    newUser.setDeptId(deptId);

                    // 检查手机号是否重复
                    int phoneCount = syncPoliceMapper.countUserByPhoneNumber(police.getMobileNumber());
                    if (phoneCount > 0) {
                        logger.warn("手机号 {} 已存在，跳过插入用户: {}", police.getMobileNumber(), police.getName());
                        continue;
                    }

                    syncPoliceMapper.insertUser(newUser);
                    insertCount++;
                    logger.info("插入新用户: {} - {}, 部门ID: {}", police.getId(), police.getName(), deptId);
                } else {
                    // 更新现有记录
                    existingUser.setNickName(police.getName());
                    existingUser.setSex(sexCode);
                    existingUser.setBirthday(police.getBirthday());
                    existingUser.setPhoneNumber(police.getMobileNumber());
                    existingUser.setOrgId(police.getOrgId());
                    existingUser.setAddress(police.getAddress());

                    // 检查部门ID是否需要更新
                    boolean deptChanged = !deptId.equals(existingUser.getDeptId());
                    if (deptChanged) {
                        existingUser.setDeptId(deptId);
                        deptUpdateCount++;
                    }

                    syncPoliceMapper.updateUser(existingUser);
                    updateCount++;
                    logger.info("更新用户: {} - {}, 部门ID: {}{}",
                            police.getId(), police.getName(), deptId,
                            deptChanged ? " (部门已更新)" : "");
                }
            } catch (Exception e) {
                logger.error("处理警察数据失败: {} - {}", police.getId(), police.getName(), e);
            }
        }

        // 6. 软删除不在中间表中的用户
        int deleteCount = syncPoliceMapper.softDeleteMissingUsers();

        // 7. 更新缺失部门信息的现有用户
        int missingDeptUpdateCount = syncPoliceMapper.updateMissingDeptInfo(orgIdToDeptIdMap);

        logger.info("警察数据同步完成: 新增 {} 条, 更新 {} 条, 软删除 {} 条, 部门更新 {} 条, 缺失部门修复 {} 条",
                insertCount, updateCount, deleteCount, deptUpdateCount, missingDeptUpdateCount);
    }

    /**
     * 转换性别
     */
    private String convertSex(String sex) {
        if ("男".equals(sex)) {
            return "0";
        } else if ("女".equals(sex)) {
            return "1";
        } else {
            return "2";
        }
    }
}