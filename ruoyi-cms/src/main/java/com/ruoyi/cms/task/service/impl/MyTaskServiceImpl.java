package com.ruoyi.cms.task.service.impl;

import com.ruoyi.cms.job.mapper.ResSkillMapper;
import com.ruoyi.cms.task.mapper.MyTaskMapper;
import com.ruoyi.cms.task.service.IMyTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

}
