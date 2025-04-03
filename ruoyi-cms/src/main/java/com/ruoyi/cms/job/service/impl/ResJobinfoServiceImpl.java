package com.ruoyi.cms.job.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.job.mapper.ResJobinfoMapper;
import com.ruoyi.cms.job.domain.ResJobinfo;
import com.ruoyi.cms.job.service.IResJobinfoService;

/**
 * 招聘信息Service业务层处理
 * 
 * @author admin
 * @date 2025-04-03
 */
@Service
public class ResJobinfoServiceImpl implements IResJobinfoService 
{
    @Autowired
    private ResJobinfoMapper resJobinfoMapper;

    /**
     * 查询招聘信息
     * 
     * @param jobId 招聘信息主键
     * @return 招聘信息
     */
    @Override
    public ResJobinfo selectResJobinfoByJobId(Integer jobId)
    {
        return resJobinfoMapper.selectResJobinfoByJobId(jobId);
    }

    /**
     * 查询招聘信息列表
     * 
     * @param resJobinfo 招聘信息
     * @return 招聘信息
     */
    @Override
    public List<ResJobinfo> selectResJobinfoList(ResJobinfo resJobinfo)
    {
        return resJobinfoMapper.selectResJobinfoList(resJobinfo);
    }

    /**
     * 新增招聘信息
     * 
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    @Override
    public int insertResJobinfo(ResJobinfo resJobinfo)
    {
        resJobinfo.setCreateTime(DateUtils.getNowDate());
        return resJobinfoMapper.insertResJobinfo(resJobinfo);
    }

    /**
     * 修改招聘信息
     * 
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    @Override
    public int updateResJobinfo(ResJobinfo resJobinfo)
    {
        resJobinfo.setUpdateTime(DateUtils.getNowDate());
        return resJobinfoMapper.updateResJobinfo(resJobinfo);
    }

    /**
     * 批量删除招聘信息
     * 
     * @param jobIds 需要删除的招聘信息主键
     * @return 结果
     */
    @Override
    public int deleteResJobinfoByJobIds(Integer[] jobIds)
    {
        return resJobinfoMapper.deleteResJobinfoByJobIds(jobIds);
    }

    /**
     * 删除招聘信息信息
     * 
     * @param jobId 招聘信息主键
     * @return 结果
     */
    @Override
    public int deleteResJobinfoByJobId(Integer jobId)
    {
        return resJobinfoMapper.deleteResJobinfoByJobId(jobId);
    }
}
