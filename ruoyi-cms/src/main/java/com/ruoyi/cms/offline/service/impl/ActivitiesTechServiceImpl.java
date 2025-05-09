package com.ruoyi.cms.offline.service.impl;

import java.util.List;

import com.ruoyi.cms.offline.domain.ActivitiesTech;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesTechMapper;
import com.ruoyi.cms.offline.domain.vo.ActivitiesTechVo;
import com.ruoyi.cms.offline.service.IActivitiesTechService;

/**
 * 活动戒治技术资料Service业务层处理
 * 
 * @author admin
 * @date 2025-05-10
 */
@Service
public class ActivitiesTechServiceImpl implements IActivitiesTechService 
{
    @Autowired
    private ActivitiesTechMapper activitiesTechMapper;

    /**
     * 查询活动戒治技术资料
     * 
     * @param techId 活动戒治技术资料主键
     * @return 活动戒治技术资料
     */
    @Override
    public ActivitiesTechVo selectActivitiesTechByTechId(Long techId)
    {
        return activitiesTechMapper.selectActivitiesTechByTechId(techId);
    }

    /**
     * 查询活动戒治技术资料列表
     * 
     * @param activitiesTech 活动戒治技术资料
     * @return 活动戒治技术资料
     */
    @Override
    public List<ActivitiesTechVo> selectActivitiesTechList(ActivitiesTechVo activitiesTech)
    {
        return activitiesTechMapper.selectActivitiesTechList(activitiesTech);
    }

    /**
     * 新增活动戒治技术资料
     * 
     * @param activitiesTech 活动戒治技术资料
     * @return 结果
     */
    @Override
    public int insertActivitiesTech(ActivitiesTech activitiesTech)
    {
        activitiesTech.setCreateTime(DateUtils.getNowDate());
        return activitiesTechMapper.insertActivitiesTech(activitiesTech);
    }

    /**
     * 修改活动戒治技术资料
     * 
     * @param activitiesTech 活动戒治技术资料
     * @return 结果
     */
    @Override
    public int updateActivitiesTech(ActivitiesTech activitiesTech)
    {
        activitiesTech.setUpdateTime(DateUtils.getNowDate());
        return activitiesTechMapper.updateActivitiesTech(activitiesTech);
    }

    /**
     * 批量删除活动戒治技术资料
     * 
     * @param techIds 需要删除的活动戒治技术资料主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesTechByTechIds(Long[] techIds)
    {
        return activitiesTechMapper.deleteActivitiesTechByTechIds(techIds);
    }

    /**
     * 删除活动戒治技术资料信息
     * 
     * @param techId 活动戒治技术资料主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesTechByTechId(Long techId)
    {
        return activitiesTechMapper.deleteActivitiesTechByTechId(techId);
    }
}
