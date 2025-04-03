package com.ruoyi.cms.res.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.BaseOrginfoMapper;
import com.ruoyi.cms.res.domain.BaseOrginfo;
import com.ruoyi.cms.res.service.IBaseOrginfoService;

/**
 * 戒治机构Service业务层处理
 * 
 * @author admin
 * @date 2025-03-28
 */
@Service
public class BaseOrginfoServiceImpl implements IBaseOrginfoService 
{
    @Autowired
    private BaseOrginfoMapper baseOrginfoMapper;

    /**
     * 查询戒治机构
     * 
     * @param orgid 戒治机构主键
     * @return 戒治机构
     */
    @Override
    public BaseOrginfo selectBaseOrginfoByOrgid(Long orgid)
    {
        return baseOrginfoMapper.selectBaseOrginfoByOrgid(orgid);
    }

    /**
     * 查询戒治机构列表
     * 
     * @param baseOrginfo 戒治机构
     * @return 戒治机构
     */
    @Override
    public List<BaseOrginfo> selectBaseOrginfoList(BaseOrginfo baseOrginfo)
    {
        return baseOrginfoMapper.selectBaseOrginfoList(baseOrginfo);
    }

    /**
     * 新增戒治机构
     * 
     * @param baseOrginfo 戒治机构
     * @return 结果
     */
    @Override
    public int insertBaseOrginfo(BaseOrginfo baseOrginfo)
    {
        baseOrginfo.setCreateTime(DateUtils.getNowDate());
        return baseOrginfoMapper.insertBaseOrginfo(baseOrginfo);
    }

    /**
     * 修改戒治机构
     * 
     * @param baseOrginfo 戒治机构
     * @return 结果
     */
    @Override
    public int updateBaseOrginfo(BaseOrginfo baseOrginfo)
    {
        baseOrginfo.setUpdateTime(DateUtils.getNowDate());
        return baseOrginfoMapper.updateBaseOrginfo(baseOrginfo);
    }

    /**
     * 批量删除戒治机构
     * 
     * @param orgids 需要删除的戒治机构主键
     * @return 结果
     */
    @Override
    public int deleteBaseOrginfoByOrgids(Long[] orgids)
    {
        return baseOrginfoMapper.deleteBaseOrginfoByOrgids(orgids);
    }

    /**
     * 删除戒治机构信息
     * 
     * @param orgid 戒治机构主键
     * @return 结果
     */
    @Override
    public int deleteBaseOrginfoByOrgid(Long orgid)
    {
        return baseOrginfoMapper.deleteBaseOrginfoByOrgid(orgid);
    }
}
