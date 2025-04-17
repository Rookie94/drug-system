package com.ruoyi.cms.res.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.common.annotation.DataScope;
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
    @DataScope(deptAlias = "t", userAlias = "t")
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
     * 修改戒治机构状态
     *
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    public int updateStatus(ResOrginfo resOrginfo)
    {
        if(resOrginfo.getStatus()=="0"){
            resOrginfo.setStatus("1");
        }
        else{
            resOrginfo.setStatus("0");
        }
        resOrginfo.setUpdateBy(getUsername());
        resOrginfo.setUpdateTime(DateUtils.getNowDate());
        return resOrginfoMapper.updateStatus(resOrginfo);
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
     * 批量审批
     *
     * @param apporParams 审批参数
     * @return 结果
     */
    @Override
    public int apporByIds(ResApporParam apporParams)
    {
        apporParams.setApporBy(getUsername());
        apporParams.setApporTime(DateUtils.getNowDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(apporParams.flag==2){
            apporParams.setPublishTime(dateFormat.format(DateUtils.getNowDate()));
        }
        return resOrginfoMapper.apporByIds(apporParams);
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
        return resOrginfoMapper.unApporResOrginfoByOrgid(orgid);
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
        return resOrginfoMapper.unApporResOrginfoByOrgids(orgids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param orgids 戒治机构主键
     * @return 结果
     */
    public List<Integer> selectApporedOrgByOrgids(Long[] orgids){
        return resOrginfoMapper.selectApporedOrgByOrgids(orgids);
    }

}
