package com.ruoyi.cms.job.mapper;

import java.util.List;
import com.ruoyi.cms.job.domain.ResJobinfo;

/**
 * 招聘信息Mapper接口
 * 
 * @author admin
 * @date 2025-04-03
 */
public interface ResJobinfoMapper 
{
    /**
     * 查询招聘信息
     * 
     * @param jobId 招聘信息主键
     * @return 招聘信息
     */
    public ResJobinfo selectResJobinfoByJobId(Integer jobId);

    /**
     * 查询招聘信息列表
     * 
     * @param resJobinfo 招聘信息
     * @return 招聘信息集合
     */
    public List<ResJobinfo> selectResJobinfoList(ResJobinfo resJobinfo);

    /**
     * 新增招聘信息
     * 
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    public int insertResJobinfo(ResJobinfo resJobinfo);

    /**
     * 修改招聘信息
     * 
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    public int updateResJobinfo(ResJobinfo resJobinfo);

    /**
     * 删除招聘信息
     * 
     * @param jobId 招聘信息主键
     * @return 结果
     */
    public int deleteResJobinfoByJobId(Integer jobId);

    /**
     * 批量删除招聘信息
     * 
     * @param jobIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResJobinfoByJobIds(Integer[] jobIds);
}
