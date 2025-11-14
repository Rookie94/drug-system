package com.ruoyi.cms.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruoyi.cms.task.service.IMyTaskService;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("myTask")
public class MyTask
{
    @Autowired
    private IMyTaskService myTaskService;

    public int publishResources()
    {
        return myTaskService.publishResources();
    }

    public int updateEtlReport1()
    {
        return myTaskService.updateEtlReport1();
    }

    public void syncOrg() throws Exception
    {
        myTaskService.syncOrg();
    }

    public void syncPolice() throws Exception
    {
        myTaskService.syncPolice();
    }

    public void syncCarePerson() throws Exception
    {
        myTaskService.syncCarePerson();
    }

}
