package com.ruoyi.cms.task.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.cms.external.service.IExternalApiDataService;
import com.ruoyi.cms.task.mapper.MyTaskMapper;
import com.ruoyi.cms.task.service.IMyTaskService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.framework.utils.MyLog;

/**
 * 技能信息Service业务层处理
 *
 * @author admin
 * @date 2025-04-10
 */
@Service
public class MyTaskServiceImpl implements IMyTaskService
{

    @Autowired
    private MyTaskMapper myTaskMapper;

    @Autowired
    private IExternalApiDataService apiDataService;


    /**
     * 新增技能信息
     */
    @Override
    public int publishResources()
    {
       return myTaskMapper.publishResources();
    }

    /**
     * 更新资源概览
     */
    @Override
    public int updateEtlReport1()
    {
        return myTaskMapper.updateEtlReport1();
    }

    @Override
    public int syncOrg() {
        AjaxResult ajaxResult=apiDataService.syncOrganizations();
        if(ajaxResult.isSuccess()){
            MyLog.success("荣飞syncOrg接口","定时同步任务","成功");
            return 1;
        }
        else {
            MyLog.error("荣飞syncOrg接口","定时同步任务","失败",ajaxResult.get("msg").toString());
            return 0;
        }
    }

    @Override
    public int syncPolice() {
        AjaxResult ajaxResult=apiDataService.syncPolice();
        if(ajaxResult.isSuccess()){
            MyLog.success("荣飞syncPolice接口","定时同步任务","成功");
            return 1;
        }
        else {
            MyLog.error("荣飞syncPolice接口","定时同步任务","失败",ajaxResult.get("msg").toString());
            return 0;
        }
    }

    @Override
    public int syncCarePerson() {
        AjaxResult ajaxResult=apiDataService.syncCarePersons();
        if(ajaxResult.isSuccess()){
            MyLog.success("syncCarePerson","定时同步任务","成功");
            return 1;
        }
        else {
            MyLog.error("syncCarePerson","定时同步任务","失败",ajaxResult.get("msg").toString());
            return 0;
        }
    }

}
