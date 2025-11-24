package com.ruoyi.cms.external.service.impl;

import com.ruoyi.cms.external.domain.MidOrganization;
import com.ruoyi.cms.external.domain.SyncDeptInfo;
import com.ruoyi.cms.external.mapper.SyncOrgMapper;
import com.ruoyi.cms.external.service.ISyncOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SyncOrgServiceImpl implements ISyncOrgService { // 实现接口

    @Autowired
    private SyncOrgMapper syncOrgMapper;

    @Autowired
    private DeptTableCleanupService deptTableCleanupService;

    private static final String SOURCE_ROOT_PARENT_ID = "00000000-0000-0000-0000-000000000000";

    /**
     * 备份逻辑
     */
    private void backupSysDept() {
        // 1. 生成备份表名，例如: sys_dept_bak_20251124_140522
        String timeSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String backupTableName = "sys_dept_bak_" + timeSuffix;

        try {
            // 2. 执行备份
            syncOrgMapper.createBackupTable(backupTableName);
            System.out.println("目标表已备份至: " + backupTableName);
        } catch (Exception e) {
            // 3. 决策：如果备份失败，是否允许继续同步？
            // 通常建议抛出异常，停止同步，防止数据丢失风险
            throw new RuntimeException("备份目标表失败，同步终止: " + e.getMessage());
        }
    }

    @Override // 添加@Override注解
    @Transactional(rollbackFor = Exception.class)
    public void syncOrganizationData() {
        // 1. 备份当前表
        backupSysDept();

        // 2. 触发清理旧表
        deptTableCleanupService.cleanOldBackupsAsync();

        // 3. 获取源数据
        List<MidOrganization> midList = syncOrgMapper.selectAllMidOrgs();
        if (midList.isEmpty()) return;

        // 4. 第一轮：基础数据落库
        for (MidOrganization mid : midList) {
            SyncDeptInfo existingDept = syncOrgMapper.selectDeptByOrgId(mid.getId());

            SyncDeptInfo dept = new SyncDeptInfo();
            dept.setDeptName(mid.getOrgName());
            dept.setOrgId(mid.getId());
            dept.setOrgCode(mid.getOrgCode());
            dept.setParentOrgId(mid.getParentId()); // 保持原始的 parent_id
            dept.setLevels(mid.getLevels());

            if (existingDept != null) {
                dept.setDeptId(existingDept.getDeptId());
                syncOrgMapper.updateDeptBasic(dept);
            } else {
                syncOrgMapper.insertDept(dept);
            }
        }

        // 5. 软删除缺失的部门
        int deletedCount = syncOrgMapper.softDeleteMissingDepts();

        // 6. 获取所有同步的部门并构建Map
        List<SyncDeptInfo> allSyncedDepts = syncOrgMapper.selectAllSyncedDepts();
        Map<String, SyncDeptInfo> orgIdMap = allSyncedDepts.stream()
                .collect(Collectors.toMap(SyncDeptInfo::getOrgId, Function.identity()));

        // 7. 根据规则更新树形关系
        for (SyncDeptInfo current : allSyncedDepts) {
            // 根据规则设置 parentId 和 ancestors
            Long parentId = determineParentId(current, orgIdMap);
            String ancestors = determineAncestors(current, orgIdMap);

            syncOrgMapper.updateDeptTreeRelations(current.getDeptId(), parentId, ancestors);
        }

        System.out.println("同步完成，共处理 " + midList.size() + " 个部门，删除 " + deletedCount + " 个缺失部门");
    }

    /**
     * 根据规则确定父部门ID
     */
    private Long determineParentId(SyncDeptInfo current, Map<String, SyncDeptInfo> orgIdMap) {
        String parentOrgId = current.getParentOrgId();

        // 规则1: 如果当前节点是顶级节点 (ID和ParentId都是0000...)，则parentId为0
        if (isRootNode(current)) {
            return 0L;
        }

        // 规则2: 如果父节点ID是顶级节点ID，则挂到顶级节点下
        if (SOURCE_ROOT_PARENT_ID.equals(parentOrgId)) {
            // 找到顶级节点对应的部门ID
            SyncDeptInfo rootDept = findRootDept(orgIdMap);
            return rootDept != null ? rootDept.getDeptId() : 0L;
        }

        // 其他情况：根据parentOrgId找到对应的部门ID
        SyncDeptInfo parentDept = orgIdMap.get(parentOrgId);
        return parentDept != null ? parentDept.getDeptId() : 0L;
    }

    /**
     * 根据规则确定祖先链
     */
    private String determineAncestors(SyncDeptInfo current, Map<String, SyncDeptInfo> orgIdMap) {
        // 规则1: 如果是顶级节点，ancestors为"0"
        if (isRootNode(current)) {
            return "0";
        }

        // 规则2: 如果父节点是顶级节点，ancestors为"0,顶级部门ID"
        String parentOrgId = current.getParentOrgId();
        if (SOURCE_ROOT_PARENT_ID.equals(parentOrgId)) {
            SyncDeptInfo rootDept = findRootDept(orgIdMap);
            return rootDept != null ? "0," + rootDept.getDeptId() : "0";
        }

        // 其他情况：递归构建ancestors
        return calculateAncestors(current, orgIdMap);
    }

    /**
     * 判断是否为根节点
     */
    private boolean isRootNode(SyncDeptInfo dept) {
        return SOURCE_ROOT_PARENT_ID.equals(dept.getOrgId()) &&
                SOURCE_ROOT_PARENT_ID.equals(dept.getParentOrgId());
    }

    /**
     * 查找根节点对应的部门
     */
    private SyncDeptInfo findRootDept(Map<String, SyncDeptInfo> orgIdMap) {
        return orgIdMap.get(SOURCE_ROOT_PARENT_ID);
    }

    /**
     * 递归计算祖先链
     */
    private String calculateAncestors(SyncDeptInfo current, Map<String, SyncDeptInfo> orgIdMap) {
        if (current.getParentOrgId() == null || SOURCE_ROOT_PARENT_ID.equals(current.getParentOrgId())) {
            SyncDeptInfo rootDept = findRootDept(orgIdMap);
            return rootDept != null ? "0," + rootDept.getDeptId() : "0";
        }

        SyncDeptInfo parent = orgIdMap.get(current.getParentOrgId());
        if (parent == null) {
            SyncDeptInfo rootDept = findRootDept(orgIdMap);
            return rootDept != null ? "0," + rootDept.getDeptId() : "0";
        }

        String parentAncestors = calculateAncestors(parent, orgIdMap);
        return parentAncestors + "," + parent.getDeptId();
    }
}