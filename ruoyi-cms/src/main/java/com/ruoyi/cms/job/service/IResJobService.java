package com.ruoyi.cms.job.service;

import java.util.List;
import com.ruoyi.cms.job.domain.ResJob;

/**
 * 招聘信息Service接口
 * 
 * @author admin
 * @date 2025-03-28
 */
public interface IResJobService 
{
    /**
     * 查询招聘信息
     * 
     * @param jobId 招聘信息主键
     * @return 招聘信息
     */
    public ResJob selectResJobByJobId(Integer jobId);

    /**
     * 查询招聘信息列表
     * 
     * @param resJob 招聘信息
     * @return 招聘信息集合
     */
    public List<ResJob> selectResJobList(ResJob resJob);

    /**
     * 新增招聘信息
     * 
     * @param resJob 招聘信息
     * @return 结果
     */
    public int insertResJob(ResJob resJob);

    /**
     * 修改招聘信息
     * 
     * @param resJob 招聘信息
     * @return 结果
     */
    public int updateResJob(ResJob resJob);

    /**
     * 批量删除招聘信息
     * 
     * @param jobIds 需要删除的招聘信息主键集合
     * @return 结果
     */
    public int deleteResJobByJobIds(Integer[] jobIds);

    /**
     * 删除招聘信息信息
     * 
     * @param jobId 招聘信息主键
     * @return 结果
     */
    public int deleteResJobByJobId(Integer jobId);
}
