package com.ruoyi.cms.job.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.job.mapper.ResJobinfoMapper;
import com.ruoyi.cms.job.domain.ResJobinfo;
import com.ruoyi.cms.job.service.IResJobinfoService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 招聘信息Service业务层处理
 *
 * @author admin
 * @date 2025-04-10
 */
@Service
public class ResJobinfoServiceImpl implements IResJobinfoService
{
    @Autowired
    private ResJobinfoMapper resJobinfoMapper;

    /**
     * 查询招聘信息
     *
     * @param jobid 招聘信息主键
     * @return 招聘信息
     */
    @Override
    public ResJobinfo selectResJobinfoByJobid(Long jobid)
    {
        return resJobinfoMapper.selectResJobinfoByJobid(jobid);
    }

    /**
     * 查询招聘信息列表
     *
     * @param resJobinfo 招聘信息
     * @return 招聘信息
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
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
        resJobinfo.setUserId(getUserId());
        resJobinfo.setDeptId(getDeptId());
        resJobinfo.setCreateBy(getUsername());
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
        resJobinfo.setUpdateBy(getUsername());
        resJobinfo.setUpdateTime(DateUtils.getNowDate());
        return resJobinfoMapper.updateResJobinfo(resJobinfo);
    }

    /**
     * 批量删除招聘信息
     *
     * @param jobids 需要删除的招聘信息主键
     * @return 结果
     */
    @Override
    public int deleteResJobinfoByJobids(Long[] jobids)
    {
        return resJobinfoMapper.deleteResJobinfoByJobids(jobids);
    }

    /**
     * 删除招聘信息信息
     *
     * @param jobid 招聘信息主键
     * @return 结果
     */
    @Override
    public int deleteResJobinfoByJobid(Long jobid)
    {
        return resJobinfoMapper.deleteResJobinfoByJobid(jobid);
    }

    /**
     * 修改工作状态
     *
     * @param resJobInfo 工作
     * @return 结果
     */
    public int updateStatus(ResJobinfo resJobInfo)
    {
        if(resJobInfo.getStatus()=="0"){
            resJobInfo.setStatus("1");
        }
        else{
            resJobInfo.setStatus("0");
        }
        resJobInfo.setUpdateBy(getUsername());
        resJobInfo.setUpdateTime(DateUtils.getNowDate());
        return resJobinfoMapper.updateStatus(resJobInfo);
    }

    /**
     * 审批工作信息
     *
     * @param id 工作主键
     * @return 结果
     */
    @Override
    public int apporById(Long id)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return resJobinfoMapper.apporById(id,userName,apporDate);
    }

    /**
     * 批量审批戒治机构
     *
     * @param ids 需要删除的工作主键
     * @return 结果
     */
    @Override
    public int apporByIds(Long[] ids)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return resJobinfoMapper.apporByIds(ids,userName,apporDate);
    }

    /**
     * 反审批工作
     *
     * @param id 工作主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return resJobinfoMapper.unApporById(id);
    }

    /**
     * 批量反审批工作
     *
     * @param ids 需要删除的工作主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return resJobinfoMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 工作主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resJobinfoMapper.selectApporedByIds(ids);
    }


}
