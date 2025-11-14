package com.ruoyi.cms.task.service;

import java.util.List;


/**
 * 技能信息Service接口
 *
 * @author admin
 * @date 2025-04-10
 */
public interface IMyTaskService
{
    /**
     * 自动发布资源
     *
     */
    public int publishResources();

    /**
     * 更新资源概览
     *
     */
    public int updateEtlReport1();

    /**
     * 同步荣飞接口组织架构
     *
     */
    public void syncOrg()  throws Exception ;

    /**
     * 同步荣飞接口police
     *
     */
    public void syncPolice() throws Exception ;

    /**
     * 同步荣飞接口后续照管对象
     *
     */
    public void syncCarePerson() throws Exception ;

}
