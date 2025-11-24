package com.ruoyi.cms.external.service;

/**
 * 警察数据同步服务接口
 */
public interface ISyncPoliceService {

    /**
     * 同步警察数据
     * 将中间表(mid_police)的数据同步到目标表(sys_userinfo)
     *
     * 同步逻辑包括：
     * 1. 备份当前目标表
     * 2. 清理旧的备份表
     * 3. 基础数据落库（新增或更新警察基本信息）
     * 4. 软删除在中间表中不存在的警察
     *
     * @throws RuntimeException 当备份失败或其他同步过程中出现异常时抛出
     */
    void syncPoliceData();
}