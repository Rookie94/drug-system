package com.ruoyi.cms.external.service;

/**
 * 组织机构同步服务接口
 */
public interface ISyncOrgService {

    /**
     * 同步组织机构数据
     * 将中间表(mid_organization)的数据同步到目标表(sys_dept)
     *
     * 同步逻辑包括：
     * 1. 备份当前目标表
     * 2. 清理旧的备份表
     * 3. 基础数据落库（新增或更新部门基本信息）
     * 4. 软删除在中间表中不存在的部门
     * 5. 构建部门树形关系（parent_id和ancestors）
     *
     * @throws RuntimeException 当备份失败或其他同步过程中出现异常时抛出
     */
    void syncOrganizationData();
}