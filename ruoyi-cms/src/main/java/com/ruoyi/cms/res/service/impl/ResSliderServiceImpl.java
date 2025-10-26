package com.ruoyi.cms.res.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.cms.res.domain.ResRxdata;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ResApporParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResSliderMapper;
import com.ruoyi.cms.res.domain.ResSlider;
import com.ruoyi.cms.res.service.IResSliderService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 轮播图Service业务层处理
 * 
 * @author admin
 * @date 2025-10-23
 */
@Service
public class ResSliderServiceImpl implements IResSliderService 
{
    @Autowired
    private ResSliderMapper resSliderMapper;

    /**
     * 查询轮播图
     * 
     * @param sliderId 轮播图主键
     * @return 轮播图
     */
    @Override
    public ResSlider selectResSliderBySliderId(Long sliderId)
    {
        return resSliderMapper.selectResSliderBySliderId(sliderId);
    }

    /**
     * 查询轮播图列表
     * 
     * @param resSlider 轮播图
     * @return 轮播图
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ResSlider> selectResSliderList(ResSlider resSlider)
    {
        return resSliderMapper.selectResSliderList(resSlider);
    }

    /**
     * 新增轮播图
     * 
     * @param resSlider 轮播图
     * @return 结果
     */
    @Override
    public int insertResSlider(ResSlider resSlider)
    {
        resSlider.setUserId(getUserId());
        resSlider.setDeptId(getDeptId());
        resSlider.setCreateBy(getUsername());
        resSlider.setCreateTime(DateUtils.getNowDate());
        return resSliderMapper.insertResSlider(resSlider);
    }

    /**
     * 修改轮播图
     * 
     * @param resSlider 轮播图
     * @return 结果
     */
    @Override
    public int updateResSlider(ResSlider resSlider)
    {
        resSlider.setUpdateBy(getUsername());
        resSlider.setUpdateTime(DateUtils.getNowDate());
        return resSliderMapper.updateResSlider(resSlider);
    }

    /**
     * 批量删除轮播图
     * 
     * @param sliderIds 需要删除的轮播图主键
     * @return 结果
     */
    @Override
    public int deleteResSliderBySliderIds(Long[] sliderIds)
    {
        return resSliderMapper.deleteResSliderBySliderIds(sliderIds);
    }

    /**
     * 删除轮播图信息
     * 
     * @param sliderId 轮播图主键
     * @return 结果
     */
    @Override
    public int deleteResSliderBySliderId(Long sliderId)
    {
        return resSliderMapper.deleteResSliderBySliderId(sliderId);
    }

    /**
     * 修改状态
     *
     * @param resSlider 处方
     * @return 结果
     */
    public int updateStatus(ResSlider resSlider)
    {
        resSlider.setUpdateBy(getUsername());
        resSlider.setUpdateTime(DateUtils.getNowDate());
        return resSliderMapper.updateStatus(resSlider);
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
        return resSliderMapper.apporByIds(apporParams);
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
        return resSliderMapper.unApporById(id);
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
        return resSliderMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resSliderMapper.selectApporedByIds(ids);
    }


}
