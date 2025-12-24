package com.ruoyi.cms.offline.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import com.ruoyi.cms.offline.domain.vo.*;
import com.ruoyi.cms.offline.mapper.*;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.system.service.ISerialNoService;
import com.ruoyi.cms.offline.domain.Activities;
import com.ruoyi.cms.offline.service.IActivitiesService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 活动发布Service业务层处理
 *
 * @author admin
 * @date 2025-04-25
 */
@Service
public class ActivitiesServiceImpl implements IActivitiesService
{
    @Autowired
    private ActivitiesMapper activitiesMapper;

    @Autowired
    private ISerialNoService serialNoService;

    @Autowired
    private ActivitiesSignUpMapper activitiesSignUpMapper;

    @Autowired
    private ActivitiesCheckinMapper activitiesCheckinMapper;

    @Autowired
    private ActivitiesReviewMapper activitiesReviewMapper;

    @Autowired
    private ActivitiesLiveMapper activitiesLiveMapper;


    @Autowired
    private ActivitiesTmsdataMapper activitiesTmsdataMapper;

    @Autowired
    private ActivitiesTechMapper activitiesTechMapper;

    /**
     * 查询活动发布
     *
     * @param activityId 活动发布主键
     * @return 活动发布
     */
    @Override
    public Activities selectActivitiesByActivityId(Long activityId)
    {
        return activitiesMapper.selectActivitiesByActivityId(activityId);
    }

    /**
     * 查询活动发布列表
     *
     * @param activities 活动发布
     * @return 活动发布
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<Activities> selectActivitiesList(Activities activities)
    {
        return activitiesMapper.selectActivitiesList(activities);
    }

    /**
     * 查询活动发布列表
     *
     * @param activities 活动发布
     * @return 活动发布
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<Activities> selectPrimaryActivitiesList(Activities activities)
    {
        return activitiesMapper.selectPrimaryActivitiesList(activities);
    }

    /**
     * 查询带状态的活动发布列表
     *
     * @param activities 活动发布
     * @return 活动发布集合
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ActivitiesStateVo> selectActivitiesStateByStudent(ActivitiesStateVo activities){
        return activitiesMapper.selectActivitiesStateByStudent(activities);
    }

    /**
     * 查询活动发布列表
     *
     * @param activities 活动发布
     * @return 活动发布
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<Activities> selectActivitiesWithChild(Activities activities)
    {
        return activitiesMapper.selectActivitiesWithChild(activities);
    }

    /**
     * 查询活动发布（用于选择框）
     *
     * @param activitiesQueryVo 活动发布
     * @return 活动发布集合
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<Activities> selectActivitiesSelectList(ActivitiesQueryVo activitiesQueryVo)
    {
        return activitiesMapper.selectActivitiesSelectList(activitiesQueryVo);
    }

    /**
     * 查询主活动下的子活动数量
     *
     * @param activityId 主活动ID
     * @return 子活动数量
     */
    @Override
    public int selectChildActivitiesCount(Long activityId) {
        return activitiesMapper.selectChildActivitiesCount(activityId);
    }

    /**
     * 新增活动发布
     *
     * @param activities 活动发布
     * @return 结果
     */
    @Override
    public int insertActivities(Activities activities)
    {
        if(activities.getParentActivityId()==0){
            if(StringUtils.isEmpty(activities.getActivityCode())){
                String activityCode=serialNoService.getSerialNumber("ActivityCode");
                if(activityCode.equals("")){
                    activityCode=serialNoService.getSerialNumber("ActivityCode");
                }
                activities.setActivityCode(activityCode);
            }
        }
        else{
            Activities parentActivity = activitiesMapper.selectActivitiesByActivityId(activities.getParentActivityId());
            if (parentActivity != null) {
                if(StringUtils.isEmpty(activities.getActivityCode())) {
                    Activities query = new Activities();
                    query.setParentActivityId(activities.getParentActivityId());
                    List<Activities> subActivities = activitiesMapper.selectActivitiesList(query);
                    int subCount = subActivities.size();
                    String sequence = String.format("%03d", subCount + 1);
                    String subActivityCode = parentActivity.getActivityCode() + "-" + sequence;
                    activities.setActivityCode(subActivityCode);
                }
            } else {
                if(StringUtils.isEmpty(activities.getActivityCode())){
                    String activityCode=serialNoService.getSerialNumber("ActivityCode");
                    if(activityCode.equals("")){
                        activityCode=serialNoService.getSerialNumber("ActivityCode");
                    }
                    activities.setActivityCode(activityCode);
                }
            }
        }
        activities.setUserId(getUserId());
        activities.setDeptId(getDeptId());
        activities.setCreateBy(getUsername());
        activities.setCreateTime(DateUtils.getNowDate());
        return activitiesMapper.insertActivities(activities);
    }

    /**
     * 修改活动发布
     *
     * @param activities 活动发布
     * @return 结果
     */
    @Override
    public int updateActivities(Activities activities)
    {
        activities.setUpdateBy(getUsername());
        activities.setUpdateTime(DateUtils.getNowDate());
        return activitiesMapper.updateActivities(activities);
    }

    /**
     * 批量删除活动发布
     *
     * @param activityIds 需要删除的活动发布主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesByActivityIds(Long[] activityIds)
    {
        // 检查每个要删除的活动
        for (Long activityId : activityIds) {
            Activities activity = activitiesMapper.selectActivitiesByActivityId(activityId);
            if (activity != null && activity.getParentActivityId() == 0) {
                // 如果是主活动，检查是否有子活动
                int childCount = activitiesMapper.selectChildActivitiesCount(activityId);
                if (childCount > 0) {
                    throw new ServiceException("主活动【" + activity.getActivityName() + "】存在子活动，请先删除子活动");
                }
            }
            //检查报名
            ActivitiesSignUpVo signUp=new ActivitiesSignUpVo();
            signUp.setActivityId(activityId);
            List<ActivitiesSignUpVo> listSignUps=activitiesSignUpMapper.selectSignUpList(signUp);
            if(listSignUps!=null && listSignUps.size()>0){
                throw new ServiceException("活动【" + activity.getActivityName() + "】已存在报名数据，请先删除报名数据");
            }
            //检查签到
            ActivitiesCheckinVo checkinVo=new ActivitiesCheckinVo();
            checkinVo.setActivityId(activityId);
            List<ActivitiesCheckinVo> listCheckIns=activitiesCheckinMapper.selectActivitiesCheckinList(checkinVo);
            if(listCheckIns!=null && listCheckIns.size()>0){
                throw new ServiceException("活动【" + activity.getActivityName() + "】已存在签到数据，请先删除签到数据");
            }
            //评价
            ActivitiesReviewVo reviewVo=new ActivitiesReviewVo();
            reviewVo.setActivityId(activityId);
            List<ActivitiesReviewVo> listReviewVos=activitiesReviewMapper.selectActivitiesReviewList(reviewVo);
            if(listReviewVos!=null && listReviewVos.size()>0){
                throw new ServiceException("活动【" + activity.getActivityName() + "】已存在评价数据，请先删除评价数据");
            }
            //现场
            ActivitiesLiveVo liveVo=new ActivitiesLiveVo();
            liveVo.setActivityId(activityId);
            List<ActivitiesLiveVo> listLiveVos=activitiesLiveMapper.selectActivitiesLiveList(liveVo);
            if(listLiveVos!=null && listLiveVos.size()>0){
                throw new ServiceException("活动【" + activity.getActivityName() + "】已存在现场资讯数据，请先删除现场资讯数据");
            }
            //经颅磁
            ActivitiesTmsdataVo tmsVo=new ActivitiesTmsdataVo();
            tmsVo.setActivityId(activityId);
            List<ActivitiesTmsdataVo> listTmsVos=activitiesTmsdataMapper.selectActivitiesTmsdataList(tmsVo);
            if(listTmsVos!=null && listTmsVos.size()>0){
                throw new ServiceException("活动【" + activity.getActivityName() + "】已存在经颅磁数据，请先删除经颅磁数据");
            }
            //其它
            ActivitiesTechVo techVo=new ActivitiesTechVo();
            techVo.setActivityId(activityId);
            List<ActivitiesTechVo> listTechVos=activitiesTechMapper.selectActivitiesTechList(techVo);
            if(listTechVos!=null && listTechVos.size()>0){
                throw new ServiceException("活动【" + activity.getActivityName() + "】已存在戒治技术数据，请先删除戒治技术数据");
            }
        }

        return activitiesMapper.deleteActivitiesByActivityIds(activityIds);
    }

    /**
     * 删除活动发布信息
     *
     * @param activityId 活动发布主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesByActivityId(Long activityId)
    {
        Activities activity = activitiesMapper.selectActivitiesByActivityId(activityId);
        if (activity != null && activity.getParentActivityId() == 0) {
            // 如果是主活动，检查是否有子活动
            int childCount = activitiesMapper.selectChildActivitiesCount(activityId);
            if (childCount > 0) {
                throw new ServiceException("主活动【" + activity.getActivityName() + "】存在子活动，请先删除子活动");
            }
        }
        return activitiesMapper.deleteActivitiesByActivityId(activityId);
    }

    /**
     * 修改戒治案例状态
     *
     * @param activities 戒治机构
     * @return 结果
     */
    public int updateStatus(Activities activities)
    {
        activities.setUpdateBy(getUsername());
        activities.setUpdateTime(DateUtils.getNowDate());
        return activitiesMapper.updateStatus(activities);
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
        return activitiesMapper.apporByIds(apporParams);
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
        return activitiesMapper.unApporById(id);
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
        return activitiesMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return activitiesMapper.selectApporedByIds(ids);
    }

}