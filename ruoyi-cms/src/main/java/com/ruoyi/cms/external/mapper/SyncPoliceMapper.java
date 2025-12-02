package com.ruoyi.cms.external.mapper;

import com.ruoyi.cms.external.domain.MidPolice;
import com.ruoyi.cms.external.domain.SyncUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SyncPoliceMapper {

    /**
     * 查询所有中间表警察数据
     */
    List<MidPolice> selectAllMidPolice();

    /**
     * 根据user_guid查询用户信息
     */
    SyncUserInfo selectUserByUserGuid(@Param("userGuid") String userGuid);

    /**
     * 插入用户信息
     */
    int insertUser(SyncUserInfo userInfo);

    /**
     * 更新用户信息
     */
    int updateUser(SyncUserInfo userInfo);

    /**
     * 软删除不在中间表中的用户
     */
    int softDeleteMissingUsers();

    /**
     * 根据手机号查询用户数量（用于检查重复）
     */
    int countUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    // 新增备份相关方法
    /**
     * 创建备份表
     */
    void createBackupTable(@Param("backupTableName") String backupTableName);

    /**
     * 查询所有备份表名
     */
    List<String> selectBackupTableNames();

    /**
     * 删除表
     */
    void dropTableIfExists(@Param("tableName") String tableName);

    // 修复部门相关方法
    /**
     * 获取部门映射关系 (org_id -> dept_id) - 返回List
     */
    List<Map<String, Object>> selectDeptMapping();

    /**
     * 更新缺失部门信息的用户
     */
    int updateMissingDeptInfo(@Param("orgIdToDeptIdMap") Map<String, Long> orgIdToDeptIdMap);
}