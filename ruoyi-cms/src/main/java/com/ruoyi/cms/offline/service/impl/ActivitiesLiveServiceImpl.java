package com.ruoyi.cms.offline.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.cms.res.domain.ResSlider;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ResApporParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesLiveMapper;
import com.ruoyi.cms.offline.domain.ActivitiesLive;
import com.ruoyi.cms.offline.domain.vo.ActivitiesLiveVo;
import com.ruoyi.cms.offline.service.IActivitiesLiveService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * VIEWService业务层处理
 * 
 * @author admin
 * @date 2025-05-09
 */
@Service
public class ActivitiesLiveServiceImpl implements IActivitiesLiveService 
{
    @Autowired
    private ActivitiesLiveMapper activitiesLiveMapper;

    /**
     * 查询VIEW
     * 
     * @param liveId VIEW主键
     * @return VIEW
     */
    @Override
    public ActivitiesLiveVo selectActivitiesLiveByLiveId(Long liveId)
    {
        return activitiesLiveMapper.selectActivitiesLiveByLiveId(liveId);
    }

    /**
     * 查询VIEW列表
     * 
     * @param activitiesLive VIEW
     * @return VIEW
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ActivitiesLiveVo> selectActivitiesLiveList(ActivitiesLiveVo activitiesLive)
    {
        return activitiesLiveMapper.selectActivitiesLiveList(activitiesLive);
    }

    /**
     * 新增VIEW
     * 
     * @param activitiesLive VIEW
     * @return 结果
     */
    @Override
    public int insertActivitiesLive(ActivitiesLive activitiesLive)
    {
        activitiesLive.setUserId(getUserId());
        activitiesLive.setDeptId(getDeptId());
        activitiesLive.setCreateBy(getUsername());
        activitiesLive.setCreateTime(DateUtils.getNowDate());
        return activitiesLiveMapper.insertActivitiesLive(activitiesLive);
    }

    /**
     * 修改VIEW
     * 
     * @param activitiesLive VIEW
     * @return 结果
     */
    @Override
    public int updateActivitiesLive(ActivitiesLive activitiesLive)
    {
        activitiesLive.setUpdateBy(getUsername());
        activitiesLive.setUpdateTime(DateUtils.getNowDate());
        return activitiesLiveMapper.updateActivitiesLive(activitiesLive);
    }

    /**
     * 批量删除VIEW
     * 
     * @param liveIds 需要删除的VIEW主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesLiveByLiveIds(Long[] liveIds)
    {
        return activitiesLiveMapper.deleteActivitiesLiveByLiveIds(liveIds);
    }

    /**
     * 删除VIEW信息
     * 
     * @param liveId VIEW主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesLiveByLiveId(Long liveId)
    {
        return activitiesLiveMapper.deleteActivitiesLiveByLiveId(liveId);
    }

    /**
     * 修改状态
     *
     * @param activitiesLive VIEW
     * @return 结果
     */
    public int updateStatus(ActivitiesLive activitiesLive)
    {
        activitiesLive.setUpdateBy(getUsername());
        activitiesLive.setUpdateTime(DateUtils.getNowDate());
        return activitiesLiveMapper.updateStatus(activitiesLive);
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
        return activitiesLiveMapper.apporByIds(apporParams);
    }

    /**
     * 反审批VIEW
     *
     * @param id VIEW主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return activitiesLiveMapper.unApporById(id);
    }

    /**
     * 批量反审批VIEW
     *
     * @param ids 需要删除的VIEW主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return activitiesLiveMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids VIEW主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return activitiesLiveMapper.selectApporedByIds(ids);
    }

}
