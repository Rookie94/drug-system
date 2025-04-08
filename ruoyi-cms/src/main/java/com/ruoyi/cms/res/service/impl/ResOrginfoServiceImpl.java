package com.ruoyi.cms.res.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResOrginfoMapper;
import com.ruoyi.cms.res.domain.ResOrginfo;
import com.ruoyi.cms.res.service.IResOrginfoService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 戒治机构Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-08
 */
@Service
public class ResOrginfoServiceImpl implements IResOrginfoService 
{
    @Autowired
    private ResOrginfoMapper resOrginfoMapper;

    /**
     * 查询戒治机构
     * 
     * @param orgid 戒治机构主键
     * @return 戒治机构
     */
    @Override
    public ResOrginfo selectResOrginfoByOrgid(Long orgid)
    {
        return resOrginfoMapper.selectResOrginfoByOrgid(orgid);
    }

    /**
     * 查询戒治机构列表
     * 
     * @param resOrginfo 戒治机构
     * @return 戒治机构
     */
    @Override
    public List<ResOrginfo> selectResOrginfoList(ResOrginfo resOrginfo)
    {
        return resOrginfoMapper.selectResOrginfoList(resOrginfo);
    }

    /**
     * 新增戒治机构
     * 
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    @Override
    public int insertResOrginfo(ResOrginfo resOrginfo)
    {
        resOrginfo.setUserId(getUserId());
        resOrginfo.setDeptId(getDeptId());
        resOrginfo.setCreateBy(getUsername());
        resOrginfo.setCreateTime(DateUtils.getNowDate());
        return resOrginfoMapper.insertResOrginfo(resOrginfo);
    }

    /**
     * 修改戒治机构
     * 
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    @Override
    public int updateResOrginfo(ResOrginfo resOrginfo)
    {
        resOrginfo.setUpdateBy(getUsername());
        resOrginfo.setUpdateTime(DateUtils.getNowDate());
        return resOrginfoMapper.updateResOrginfo(resOrginfo);
    }

    /**
     * 批量删除戒治机构
     * 
     * @param orgids 需要删除的戒治机构主键
     * @return 结果
     */
    @Override
    public int deleteResOrginfoByOrgids(Long[] orgids)
    {
        return resOrginfoMapper.deleteResOrginfoByOrgids(orgids);
    }

    /**
     * 删除戒治机构信息
     * 
     * @param orgid 戒治机构主键
     * @return 结果
     */
    @Override
    public int deleteResOrginfoByOrgid(Long orgid)
    {
        return resOrginfoMapper.deleteResOrginfoByOrgid(orgid);
    }

    /**
     * 审批戒治机构信息
     *
     * @param orgid 戒治机构主键
     * @return 结果
     */
    @Override
    public int apporResOrginfoByOrgid(Long orgid)
    {
        ResOrginfo resOrginfo=resOrginfoMapper.selectResOrginfoByOrgid(orgid);
        resOrginfo.setAppored("1");
        resOrginfo.setApporBy(getUsername());
        resOrginfo.setApporTime(DateUtils.getNowDate());
        return resOrginfoMapper.apporResOrginfoByOrgid(orgid);
    }

    /**
     * 反审批戒治机构信息
     *
     * @param orgid 戒治机构主键
     * @return 结果
     */
    @Override
    public int unApporResOrginfoByOrgid(Long orgid)
    {
        ResOrginfo resOrginfo=resOrginfoMapper.selectResOrginfoByOrgid(orgid);
        resOrginfo.setAppored("0");
        return resOrginfoMapper.apporResOrginfoByOrgid(orgid);
    }

    /**
     * 批量审批戒治机构
     *
     * @param orgids 需要删除的戒治机构主键
     * @return 结果
     */
    @Override
    public int apporResOrginfoByOrgids(Long[] orgids)
    {
        return resOrginfoMapper.apporResOrginfoByOrgids(orgids);
    }

    /**
     * 批量反审批戒治机构
     *
     * @param orgids 需要删除的戒治机构主键
     * @return 结果
     */
    @Override
    public int unApporResOrginfoByOrgids(Long[] orgids)
    {
        return resOrginfoMapper.apporResOrginfoByOrgids(orgids);
    }



}
