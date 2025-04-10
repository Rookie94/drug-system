package com.ruoyi.cms.res.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResCaseMapper;
import com.ruoyi.cms.res.domain.ResCase;
import com.ruoyi.cms.res.service.IResCaseService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 戒治案例Service业务层处理
 * 
 * @author admin
 * @date 2025-04-10
 */
@Service
public class ResCaseServiceImpl implements IResCaseService 
{
    @Autowired
    private ResCaseMapper resCaseMapper;

    /**
     * 查询戒治案例
     * 
     * @param caseid 戒治案例主键
     * @return 戒治案例
     */
    @Override
    public ResCase selectResCaseByCaseid(Long caseid)
    {
        return resCaseMapper.selectResCaseByCaseid(caseid);
    }

    /**
     * 查询戒治案例列表
     * 
     * @param resCase 戒治案例
     * @return 戒治案例
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ResCase> selectResCaseList(ResCase resCase)
    {
        return resCaseMapper.selectResCaseList(resCase);
    }

    /**
     * 新增戒治案例
     * 
     * @param resCase 戒治案例
     * @return 结果
     */
    @Override
    public int insertResCase(ResCase resCase)
    {
        resCase.setUserId(getUserId());
        resCase.setDeptId(getDeptId());
        resCase.setCreateBy(getUsername());
        resCase.setCreateTime(DateUtils.getNowDate());
        return resCaseMapper.insertResCase(resCase);
    }

    /**
     * 修改戒治案例
     * 
     * @param resCase 戒治案例
     * @return 结果
     */
    @Override
    public int updateResCase(ResCase resCase)
    {
        resCase.setUpdateBy(getUsername());
        resCase.setUpdateTime(DateUtils.getNowDate());
        return resCaseMapper.updateResCase(resCase);
    }

    /**
     * 批量删除戒治案例
     * 
     * @param caseids 需要删除的戒治案例主键
     * @return 结果
     */
    @Override
    public int deleteResCaseByCaseids(Long[] caseids)
    {
        return resCaseMapper.deleteResCaseByCaseids(caseids);
    }

    /**
     * 删除戒治案例信息
     * 
     * @param caseid 戒治案例主键
     * @return 结果
     */
    @Override
    public int deleteResCaseByCaseid(Long caseid)
    {
        return resCaseMapper.deleteResCaseByCaseid(caseid);
    }

    /**
     * 修改戒治案例状态
     *
     * @param resCase 戒治机构
     * @return 结果
     */
    public int updateStatus(ResCase resCase)
    {
        if(resCase.getStatus()=="0"){
            resCase.setStatus("1");
        }
        else{
            resCase.setStatus("0");
        }
        resCase.setUpdateBy(getUsername());
        resCase.setUpdateTime(DateUtils.getNowDate());
        return resCaseMapper.updateStatus(resCase);
    }

    /**
     * 审批戒治案例信息
     *
     * @param id 戒治案例主键
     * @return 结果
     */
    @Override
    public int apporById(Long id)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return resCaseMapper.apporById(id,userName,apporDate);
    }

    /**
     * 批量审批戒治机构
     *
     * @param ids 需要删除的戒治案例主键
     * @return 结果
     */
    @Override
    public int apporByIds(Long[] ids)
    {
        String userName=getUsername();
        Date apporDate=DateUtils.getNowDate();
        return resCaseMapper.apporByIds(ids,userName,apporDate);
    }

    /**
     * 反审批戒治戒治案例
     *
     * @param id 戒治案例主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return resCaseMapper.unApporById(id);
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
        return resCaseMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resCaseMapper.selectApporedByIds(ids);
    }


}
