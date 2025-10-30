package com.ruoyi.cms.res.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.cms.res.domain.ResRxdata;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ResApporParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResTechMapper;
import com.ruoyi.cms.res.domain.ResTech;
import com.ruoyi.cms.res.service.IResTechService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 戒治技术宣传Service业务层处理
 * 
 * @author admin
 * @date 2025-10-29
 */
@Service
public class ResTechServiceImpl implements IResTechService 
{
    @Autowired
    private ResTechMapper resTechMapper;

    /**
     * 查询戒治技术宣传
     * 
     * @param techId 戒治技术宣传主键
     * @return 戒治技术宣传
     */
    @Override
    public ResTech selectResTechByTechId(Long techId)
    {
        return resTechMapper.selectResTechByTechId(techId);
    }

    /**
     * 查询戒治技术宣传列表
     * 
     * @param resTech 戒治技术宣传
     * @return 戒治技术宣传
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ResTech> selectResTechList(ResTech resTech)
    {
        return resTechMapper.selectResTechList(resTech);
    }

    /**
     * 新增戒治技术宣传
     * 
     * @param resTech 戒治技术宣传
     * @return 结果
     */
    @Override
    public int insertResTech(ResTech resTech)
    {
        resTech.setUserId(getUserId());
        resTech.setDeptId(getDeptId());
        resTech.setCreateBy(getUsername());
        resTech.setCreateTime(DateUtils.getNowDate());
        return resTechMapper.insertResTech(resTech);
    }

    /**
     * 修改戒治技术宣传
     * 
     * @param resTech 戒治技术宣传
     * @return 结果
     */
    @Override
    public int updateResTech(ResTech resTech)
    {
        resTech.setUpdateBy(getUsername());
        resTech.setUpdateTime(DateUtils.getNowDate());
        return resTechMapper.updateResTech(resTech);
    }

    /**
     * 批量删除戒治技术宣传
     * 
     * @param techIds 需要删除的戒治技术宣传主键
     * @return 结果
     */
    @Override
    public int deleteResTechByTechIds(Long[] techIds)
    {
        return resTechMapper.deleteResTechByTechIds(techIds);
    }

    /**
     * 删除戒治技术宣传信息
     * 
     * @param techId 戒治技术宣传主键
     * @return 结果
     */
    @Override
    public int deleteResTechByTechId(Long techId)
    {
        return resTechMapper.deleteResTechByTechId(techId);
    }

    /**
     * 修改状态
     *
     * @param resTech 处方
     * @return 结果
     */
    public int updateStatus(ResTech resTech)
    {
        resTech.setUpdateBy(getUsername());
        resTech.setUpdateTime(DateUtils.getNowDate());
        return resTechMapper.updateStatus(resTech);
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
        return resTechMapper.apporByIds(apporParams);
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
        return resTechMapper.unApporById(id);
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
        return resTechMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resTechMapper.selectApporedByIds(ids);
    }

}
