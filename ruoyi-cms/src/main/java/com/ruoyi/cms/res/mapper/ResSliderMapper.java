package com.ruoyi.cms.res.mapper;

import java.util.List;
import com.ruoyi.cms.res.domain.ResSlider;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 轮播图Mapper接口
 * 
 * @author admin
 * @date 2025-10-23
 */
public interface ResSliderMapper 
{
    /**
     * 查询轮播图
     * 
     * @param sliderId 轮播图主键
     * @return 轮播图
     */
    public ResSlider selectResSliderBySliderId(Long sliderId);

    /**
     * 查询轮播图列表
     * 
     * @param resSlider 轮播图
     * @return 轮播图集合
     */
    public List<ResSlider> selectResSliderList(ResSlider resSlider);

    /**
     * 新增轮播图
     * 
     * @param resSlider 轮播图
     * @return 结果
     */
    public int insertResSlider(ResSlider resSlider);

    /**
     * 修改轮播图
     * 
     * @param resSlider 轮播图
     * @return 结果
     */
    public int updateResSlider(ResSlider resSlider);

    /**
     * 删除轮播图
     * 
     * @param sliderId 轮播图主键
     * @return 结果
     */
    public int deleteResSliderBySliderId(Long sliderId);

    /**
     * 批量删除轮播图
     * 
     * @param sliderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResSliderBySliderIds(Long[] sliderIds);

    /**
     * 修改状态
     *
     * @param resSlider 处方
     * @return 结果
     */
    public int updateStatus(ResSlider resSlider);

    /**
     * 批量审批
     *
     * @param apporParams 批量审批参数
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);
    /**
     * 反审批
     *
     * @param Id 主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);

}
