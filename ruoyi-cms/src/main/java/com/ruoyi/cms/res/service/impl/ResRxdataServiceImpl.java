package com.ruoyi.cms.res.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.cms.res.domain.ResCase;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ResApporParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResRxdataMapper;
import com.ruoyi.cms.res.domain.ResRxdata;
import com.ruoyi.cms.res.service.IResRxdataService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 戒治处方Service业务层处理
 * 
 * @author admin
 * @date 2025-05-08
 */
@Service
public class ResRxdataServiceImpl implements IResRxdataService 
{
    @Autowired
    private ResRxdataMapper resRxdataMapper;

    /**
     * 查询戒治处方
     * 
     * @param rxId 戒治处方主键
     * @return 戒治处方
     */
    @Override
    public ResRxdata selectResRxdataByRxId(Long rxId)
    {
        return resRxdataMapper.selectResRxdataByRxId(rxId);
    }

    /**
     * 查询戒治处方列表
     * 
     * @param resRxdata 戒治处方
     * @return 戒治处方
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ResRxdata> selectResRxdataList(ResRxdata resRxdata)
    {
        return resRxdataMapper.selectResRxdataList(resRxdata);
    }

    /**
     * 新增戒治处方
     * 
     * @param resRxdata 戒治处方
     * @return 结果
     */
    @Override
    public int insertResRxdata(ResRxdata resRxdata)
    {
        resRxdata.setUserId(getUserId());
        resRxdata.setDeptId(getDeptId());
        resRxdata.setCreateBy(getUsername());
        resRxdata.setCreateTime(DateUtils.getNowDate());
        return resRxdataMapper.insertResRxdata(resRxdata);
    }

    /**
     * 修改戒治处方
     * 
     * @param resRxdata 戒治处方
     * @return 结果
     */
    @Override
    public int updateResRxdata(ResRxdata resRxdata)
    {
        resRxdata.setUpdateBy(getUsername());
        resRxdata.setUpdateTime(DateUtils.getNowDate());
        return resRxdataMapper.updateResRxdata(resRxdata);
    }

    /**
     * 批量删除戒治处方
     * 
     * @param rxIds 需要删除的戒治处方主键
     * @return 结果
     */
    @Override
    public int deleteResRxdataByRxIds(Long[] rxIds)
    {
        return resRxdataMapper.deleteResRxdataByRxIds(rxIds);
    }

    /**
     * 删除戒治处方信息
     * 
     * @param rxId 戒治处方主键
     * @return 结果
     */
    @Override
    public int deleteResRxdataByRxId(Long rxId)
    {
        return resRxdataMapper.deleteResRxdataByRxId(rxId);
    }

    /**
     * 修改状态
     *
     * @param resRxdata 处方
     * @return 结果
     */
    public int updateStatus(ResRxdata resRxdata)
    {
        resRxdata.setUpdateBy(getUsername());
        resRxdata.setUpdateTime(DateUtils.getNowDate());
        return resRxdataMapper.updateStatus(resRxdata);
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
        return resRxdataMapper.apporByIds(apporParams);
    }

    /**
     * 反审批戒治案例
     *
     * @param id 戒治案例主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return resRxdataMapper.unApporById(id);
    }

    /**
     * 批量反审批戒治案例
     *
     * @param ids 需要删除的戒治案例主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return resRxdataMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resRxdataMapper.selectApporedByIds(ids);
    }


}
