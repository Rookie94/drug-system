package com.ruoyi.cms.res.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.cms.res.domain.ResOrginfo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResExpertMapper;
import com.ruoyi.cms.res.domain.ResExpert;
import com.ruoyi.cms.res.service.IResExpertService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 戒治专家Service业务层处理
 * 
 * @author admin
 * @date 2025-04-09
 */
@Service
public class ResExpertServiceImpl implements IResExpertService 
{
    @Autowired
    private ResExpertMapper resExpertMapper;

    /**
     * 查询戒治专家
     * 
     * @param expertid 戒治专家主键
     * @return 戒治专家
     */
    @Override
    public ResExpert selectResExpertByExpertid(Long expertid)
    {
        return resExpertMapper.selectResExpertByExpertid(expertid);
    }

    /**
     * 查询戒治专家列表
     * 
     * @param resExpert 戒治专家
     * @return 戒治专家
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ResExpert> selectResExpertList(ResExpert resExpert)
    {
        return resExpertMapper.selectResExpertList(resExpert);
    }

    /**
     * 新增戒治专家
     * 
     * @param resExpert 戒治专家
     * @return 结果
     */
    @Override
    public int insertResExpert(ResExpert resExpert)
    {
        resExpert.setUserId(getUserId());
        resExpert.setDeptId(getDeptId());
        resExpert.setCreateBy(getUsername());
        resExpert.setCreateTime(DateUtils.getNowDate());
        return resExpertMapper.insertResExpert(resExpert);
    }

    /**
     * 修改戒治专家
     * 
     * @param resExpert 戒治专家
     * @return 结果
     */
    @Override
    public int updateResExpert(ResExpert resExpert)
    {
        resExpert.setUpdateBy(getUsername());
        resExpert.setUpdateTime(DateUtils.getNowDate());
        return resExpertMapper.updateResExpert(resExpert);
    }

    /**
     * 批量删除戒治专家
     * 
     * @param expertids 需要删除的戒治专家主键
     * @return 结果
     */
    @Override
    public int deleteResExpertByExpertids(Long[] expertids)
    {
        return resExpertMapper.deleteResExpertByExpertids(expertids);
    }

    /**
     * 删除戒治专家信息
     * 
     * @param expertid 戒治专家主键
     * @return 结果
     */
    @Override
    public int deleteResExpertByExpertid(Long expertid)
    {
        return resExpertMapper.deleteResExpertByExpertid(expertid);
    }


    /**
     * 修改专家状态
     *
     * @param resExpert 戒治机构
     * @return 结果
     */
    public int updateStatus(ResExpert resExpert)
    {
        if(resExpert.getStatus()=="0"){
            resExpert.setStatus("1");
        }
        else{
            resExpert.setStatus("0");
        }
        resExpert.setUpdateBy(getUsername());
        resExpert.setUpdateTime(DateUtils.getNowDate());
        return resExpertMapper.updateStatus(resExpert);
    }

    /**
     * 审批专家信息
     *
     * @param id 专家主键
     * @return 结果
     */
    @Override
    public int apporById(Long id)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return resExpertMapper.apporById(id,userName,apporDate);
    }

    /**
     * 批量审批戒治机构
     *
     * @param ids 需要删除的专家主键
     * @return 结果
     */
    @Override
    public int apporByIds(Long[] ids)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return resExpertMapper.apporByIds(ids,userName,apporDate);
    }

    /**
     * 反审批戒治专家
     *
     * @param id 专家主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return resExpertMapper.unApporById(id);
    }

    /**
     * 批量反审批专家
     *
     * @param ids 需要删除的专家主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return resExpertMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 专家主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resExpertMapper.selectApporedByIds(ids);
    }


}
