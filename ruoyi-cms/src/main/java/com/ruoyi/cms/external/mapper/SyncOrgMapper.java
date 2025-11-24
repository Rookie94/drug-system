package com.ruoyi.cms.external.mapper;

import com.ruoyi.cms.external.domain.MidOrganization;
import com.ruoyi.cms.external.domain.SyncDeptInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SyncOrgMapper {

    /**
     * 查询当前数据库中所有符合备份命名的表
     * @return 表名列表
     */
    List<String> selectBackupTableNames();

    /**
     * 删除指定表
     * @param tableName 表名
     */
    void dropTable(@Param("tableName") String tableName);

    /**
     * 备份目标表
     * @param backupTableName 备份后的表名
     */
    void createBackupTable(@Param("backupTableName") String backupTableName);

    /**
     * (可选) 检查表是否存在，防止同名报错
     */
    int checkTableExists(@Param("tableName") String tableName);

    // 1. 获取所有中间表数据
    List<MidOrganization> selectAllMidOrgs();

    // 2. 根据 org_id 查询目标表是否存在
    SyncDeptInfo selectDeptByOrgId(@Param("orgId") String orgId);

    // 3. 插入新部门
    int insertDept(SyncDeptInfo dept);

    // 4. 更新部门基础信息
    int updateDeptBasic(SyncDeptInfo dept);

    // 5. 更新部门层级关系 (parent_id, ancestors)
    int updateDeptTreeRelations(@Param("deptId") Long deptId,
                                @Param("parentId") Long parentId,
                                @Param("ancestors") String ancestors);

    // 6. 获取所有已同步的部门（用于计算树结构）
    List<SyncDeptInfo> selectAllSyncedDepts();

    /**
     * 停用不在指定ID列表中的部门
     * @return 影响行数
     */
    int softDeleteMissingDepts();

}
