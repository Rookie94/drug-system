package com.ruoyi.cms.external.service.impl;

import com.ruoyi.cms.external.domain.MidCarePerson;
import com.ruoyi.cms.external.domain.SyncUserInfo;
import com.ruoyi.cms.external.mapper.SyncCarePersonMapper;
import com.ruoyi.cms.external.service.ISyncCarePersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SyncCarePersonServiceImpl implements ISyncCarePersonService {

    @Autowired
    private UserTableCleanupService userTableCleanupService;

    @Autowired
    private SyncCarePersonMapper syncCarePersonMapper;

    private static final String DEFAULT_PASSWORD = "$2a$10$HEx28z0Yg.zAC18qSGyPWegZynrTgO0hOaZgGBQuwSb.93I2Gvq8G";
    private static final String USER_TYPE_CARE_PERSON = "11"; // 照管对象类型

    /**
     * 备份逻辑
     */
    private void backupSysUserInfo() {
        String timeSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupTableName = "sys_userinfo_bak_" + timeSuffix;

        try {
            syncCarePersonMapper.createUserInfoBackupTable(backupTableName);
            System.out.println("目标表已备份至: " + backupTableName);
        } catch (Exception e) {
            throw new RuntimeException("备份目标表失败，同步终止: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncCarePersonData() {
        // 1. 备份当前表
        backupSysUserInfo();

        // 2. 触发清理旧表
        userTableCleanupService.cleanOldBackupsAsync();

        // 3. 获取源数据
        List<MidCarePerson> carePersonList = syncCarePersonMapper.selectAllCarePersons();
        if (carePersonList.isEmpty()) {
            System.out.println("未找到照管对象数据");
            return;
        }

        // 4. 构建机构ID到部门ID的映射
        Map<String, Long> orgIdToDeptIdMap = buildOrgIdToDeptIdMap();

        // 5. 第一轮：基础数据落库
        int insertCount = 0;
        int updateCount = 0;

        for (MidCarePerson carePerson : carePersonList) {
            // 根据机构ID获取部门ID
            Long deptId = orgIdToDeptIdMap.get(carePerson.getOrgId());
            if (deptId == null) {
                System.out.println("警告: 未找到机构ID " + carePerson.getOrgId() + " 对应的部门ID，跳过用户: " + carePerson.getName());
                continue;
            }

            SyncUserInfo existingUser = syncCarePersonMapper.selectUserByOrgIdAndIdNumber(carePerson.getOrgId(), carePerson.getIdNumber());

            SyncUserInfo userInfo = new SyncUserInfo();
            userInfo.setUserName(carePerson.getMobileNumber()); // 手机号作为用户名
            userInfo.setNickName(carePerson.getName());
            userInfo.setUserType(USER_TYPE_CARE_PERSON);
            userInfo.setBirthday(carePerson.getBirthday());
            userInfo.setEntryDate(carePerson.getIntoTime()); // into_time -> entry_date
            userInfo.setOutTime(carePerson.getOutTime());
            userInfo.setPhoneNumber(carePerson.getMobileNumber());
            userInfo.setSex(convertSex(carePerson.getSex()));
            userInfo.setIdNumber(carePerson.getIdNumber());
            userInfo.setAddress(carePerson.getAddress());
            userInfo.setDeptId(deptId);
            userInfo.setOrgId(carePerson.getOrgId());
            userInfo.setPassword(DEFAULT_PASSWORD);
            userInfo.setStatus("0"); // 正常状态
            userInfo.setDelFlag("0"); // 未删除
            userInfo.setUserGuid(carePerson.getId());

            if (existingUser != null) {
                // 更新
                userInfo.setUserId(existingUser.getUserId());
                userInfo.setUserGuid(existingUser.getUserGuid());
                syncCarePersonMapper.updateUserInfo(userInfo);
                updateCount++;
            } else {
                // 插入
                syncCarePersonMapper.insertUserInfo(userInfo);
                insertCount++;
            }
        }

        // 6. 软删除缺失的照管对象
        int deletedCount = syncCarePersonMapper.softDeleteMissingCarePersons();

        System.out.println("照管对象同步完成: 新增 " + insertCount + " 个，更新 " + updateCount + " 个，删除 " + deletedCount + " 个");
    }

    /**
     * 构建机构ID到部门ID的映射
     */
    private Map<String, Long> buildOrgIdToDeptIdMap() {
        List<SyncUserInfo> deptList = syncCarePersonMapper.selectAllDeptsWithOrgId();
        return deptList.stream()
                .collect(Collectors.toMap(SyncUserInfo::getOrgId, SyncUserInfo::getDeptId));
    }

    /**
     * 转换性别：男->0, 女->1, 其他->2
     */
    private String convertSex(String sex) {
        if ("男".equals(sex)) {
            return "0";
        } else if ("女".equals(sex)) {
            return "1";
        } else {
            return "2"; // 未知
        }
    }

}