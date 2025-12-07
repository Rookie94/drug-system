package com.ruoyi.cms.offline.mapper;

import java.util.List;
import com.ruoyi.cms.offline.domain.ActivitiesLive;
import com.ruoyi.cms.offline.domain.vo.ActivitiesLiveVo;
import com.ruoyi.cms.res.domain.ResSlider;
import com.ruoyi.system.domain.ResApporParam;

/**
 * ActivitiesLiveMapper接口
 * 
 * @author admin
 * @date 2025-05-09
 */
public interface ActivitiesLiveMapper 
{
    /**
     * 查询现场资讯
     * 
     * @param liveId 现场资讯主键
     * @return 现场资讯
     */
    public ActivitiesLiveVo selectActivitiesLiveByLiveId(Long liveId);

    /**
     * 查询现场资讯列表
     * 
     * @param activitiesLive 现场资讯
     * @return 现场资讯集合
     */
    public List<ActivitiesLiveVo> selectActivitiesLiveList(ActivitiesLiveVo activitiesLive);

    /**
     * 新增现场资讯
     * 
     * @param activitiesLive 现场资讯
     * @return 结果
     */
    public int insertActivitiesLive(ActivitiesLive activitiesLive);

    /**
     * 修改现场资讯
     * 
     * @param activitiesLive 现场资讯
     * @return 结果
     */
    public int updateActivitiesLive(ActivitiesLive activitiesLive);

    /**
     * 删除现场资讯
     * 
     * @param liveId 现场资讯主键
     * @return 结果
     */
    public int deleteActivitiesLiveByLiveId(Long liveId);

    /**
     * 批量删除现场资讯
     * 
     * @param liveIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteActivitiesLiveByLiveIds(Long[] liveIds);

    /**
     * 修改状态
     *
     * @param activitiesLive 处方
     * @return 结果
     */
    public int updateStatus(ActivitiesLive activitiesLive);

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
