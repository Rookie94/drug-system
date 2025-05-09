package com.ruoyi.cms.offline.service;

import java.util.List;
import com.ruoyi.cms.offline.domain.ActivitiesLive;
import com.ruoyi.cms.offline.domain.vo.ActivitiesLiveVo;

/**
 * VIEWService接口
 * 
 * @author admin
 * @date 2025-05-09
 */
public interface IActivitiesLiveService 
{
    /**
     * 查询VIEW
     * 
     * @param liveId VIEW主键
     * @return VIEW
     */
    public ActivitiesLiveVo selectActivitiesLiveByLiveId(Long liveId);

    /**
     * 查询VIEW列表
     * 
     * @param activitiesLive VIEW
     * @return VIEW集合
     */
    public List<ActivitiesLiveVo> selectActivitiesLiveList(ActivitiesLiveVo activitiesLive);

    /**
     * 新增VIEW
     * 
     * @param activitiesLive VIEW
     * @return 结果
     */
    public int insertActivitiesLive(ActivitiesLive activitiesLive);

    /**
     * 修改VIEW
     * 
     * @param activitiesLive VIEW
     * @return 结果
     */
    public int updateActivitiesLive(ActivitiesLive activitiesLive);

    /**
     * 批量删除VIEW
     * 
     * @param liveIds 需要删除的VIEW主键集合
     * @return 结果
     */
    public int deleteActivitiesLiveByLiveIds(Long[] liveIds);

    /**
     * 删除VIEW信息
     * 
     * @param liveId VIEW主键
     * @return 结果
     */
    public int deleteActivitiesLiveByLiveId(Long liveId);
}
