package com.ruoyi.cms.external.mapper;

import com.ruoyi.cms.external.domain.MidCarePerson;
import com.ruoyi.cms.external.domain.SyncUserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 照管对象同步Mapper接口
 */
public interface SyncCarePersonMapper {

    /**
     * 照管对象相关方法
     */
    List<MidCarePerson> selectAllCarePersons();
    SyncUserInfo selectUserByOrgIdAndIdNumber(@Param("orgId") String orgId, @Param("idNumber") String idNumber);
    void insertUserInfo(SyncUserInfo userInfo);
    void updateUserInfo(SyncUserInfo userInfo);
    int softDeleteMissingCarePersons();
    List<SyncUserInfo> selectAllDeptsWithOrgId();
    void createUserInfoBackupTable(@Param("backupTableName") String backupTableName);
}