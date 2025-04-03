package com.ruoyi.cms.job.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.job.mapper.ResJobMapper;
import com.ruoyi.cms.job.domain.ResJob;
import com.ruoyi.cms.job.service.IResJobService;

/**
 * 招聘信息Service业务层处理
 * 
 * @author admin
 * @date 2025-03-28
 */
@Service
public class ResJobServiceImpl implements IResJobService 
{
    @Autowired
    private ResJobMapper resJobMapper;

    /**
     * 查询招聘信息
     * 
     * @param jobId 招聘信息主键
     * @return 招聘信息
     */
    @Override
    public ResJob selectResJobByJobId(Integer jobId)
    {
        return resJobMapper.selectResJobByJobId(jobId);
    }

    /**
     * 查询招聘信息列表
     * 
     * @param resJob 招聘信息
     * @return 招聘信息
     */
    @Override
    public List<ResJob> selectResJobList(ResJob resJob)
    {
        return resJobMapper.selectResJobList(resJob);
    }

    /**
     * 新增招聘信息
     * 
     * @param resJob 招聘信息
     * @return 结果
     */
    @Override
    public int insertResJob(ResJob resJob)
    {
        resJob.setCreateTime(DateUtils.getNowDate());
        return resJobMapper.insertResJob(resJob);
    }

    /**
     * 修改招聘信息
     * 
     * @param resJob 招聘信息
     * @return 结果
     */
    @Override
    public int updateResJob(ResJob resJob)
    {
        resJob.setUpdateTime(DateUtils.getNowDate());
        return resJobMapper.updateResJob(resJob);
    }

    /**
     * 批量删除招聘信息
     * 
     * @param jobIds 需要删除的招聘信息主键
     * @return 结果
     */
    @Override
    public int deleteResJobByJobIds(Integer[] jobIds)
    {
        return resJobMapper.deleteResJobByJobIds(jobIds);
    }

    /**
     * 删除招聘信息信息
     * 
     * @param jobId 招聘信息主键
     * @return 结果
     */
    @Override
    public int deleteResJobByJobId(Integer jobId)
    {
        return resJobMapper.deleteResJobByJobId(jobId);
    }
}
