package com.ruoyi.wxapp.Controller.offline;

import com.ruoyi.cms.offline.domain.*;
import com.ruoyi.cms.offline.service.*;
import com.ruoyi.cms.res.domain.ResOrginfo;
import com.ruoyi.cms.res.service.IResOrginfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.getUserId;

@RestController
@RequestMapping("/api/offline/")
public class ActivitiesController extends BaseController
{

    @Autowired
    private IActivitiesService activitiesService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IActivitiesSignUpService signUpService;

    @Autowired
    private IActivitiesCheckinService checkinService;

    @Autowired
    private IActivitiesTmsdataService activitiesTmsdataService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private IActivitiesTechService activitiesTechService;

    @Autowired
    private IActivitiesLiveService activitiesLiveService;

    @Autowired
    private IActivitiesReviewService activitiesReviewService;

    /**
     * 查询报名中的活动
     */
    @GetMapping("/getPrimaryActivities")
    public TableDataInfo listPrimaryActivities(Activities activities)
    {
        startPage();
        activities.setUseDataScope(false);
        activities.setParentActivityId(0L);
        activities.setAppored("2");
        activities.setStatus("0");
        List<Activities> list = activitiesService.selectActivitiesList(activities);
        return getDataTable(list);
    }

    /**
     * 查询活动详情
     */
    @GetMapping("/getActivity/{activityId}")
    public AjaxResult getActivityInfo(@PathVariable("activityId") Long activityId)
    {
        return success(activitiesService.selectActivitiesByActivityId(activityId));
    }

    /**
     * 查询活动详情
     */
    @GetMapping("/getChildActivities/{parentActivityId}")
    public TableDataInfo getChildActivities(@PathVariable("parentActivityId") Long parentActivityId)
    {
        startPage();
        Activities activities=new Activities();
        activities.setUseDataScope(false);
        activities.setParentActivityId(parentActivityId);
        activities.setStatus("0");
        List<Activities> list = activitiesService.selectActivitiesList(activities);
        return getDataTable(list);
    }


    /**
     * 新增预约详情
     */
    @Log(title = "活动报名", businessType = BusinessType.INSERT)
    @PostMapping("/signUp/{activityId}")
    public AjaxResult signUp(@PathVariable("activityId") Long activityId)
    {
        if(activityId==null){
            return error("活动id不能为空");
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activityId);
        if(activity==null){
            return error("活动不存在");
        }
        if(!activity.getStatus().equals("0")){
            return error("活动已停用,不能报名");
        }
        if(!activity.getAppored().equals("2")==false){
            return error("活动处于不可报名状态");
        }
        if(!activity.getParentActivityId().equals("0")==false){
            return error("主活动才能报名");
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("无法获取登录用户信息");
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            return error("游客不能报名参加活动");
        }
        if(sysUser.getUserType().equals("11")==false){
            return error("学员才能报名!");
        }
        ActivitiesSignUp signUp=new ActivitiesSignUp();
        signUp.setActivityId(activityId);
        return toAjax(signUpService.insertSignUp(signUp));
    }

    /**
     * 新增预约详情
     */
    @Log(title = "活动报名", businessType = BusinessType.INSERT)
    @PostMapping("/checkIn/{activityId}")
    public AjaxResult checkIn(@PathVariable("activityId") Long activityId)
    {
        if(activityId==null){
            return error("活动id不能为空");
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activityId);
        if(activity==null){
            return error("活动不存在");
        }
        if(!activity.getStatus().equals("0")){
            return error("活动已停用,不能签到");
        }
        if(!activity.getAppored().equals("3")==false){
            return error("活动处于不可签到状态");
        }
        if(!activity.getParentActivityId().equals("0")==false){
            return error("主活动才能签到");
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("无法获取登录用户信息");
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            return error("游客不能参加活动");
        }
        if(sysUser.getUserType().equals("11")==false){
            return error("学员才能签到!");
        }
        ActivitiesCheckin checkIn=new ActivitiesCheckin();
        checkIn.setActivityId(activityId);
        return toAjax(checkinService.insertActivitiesCheckin(checkIn));
    }

    /**
     * 发布子活动
     */
    @Log(title = "发布子活动", businessType = BusinessType.INSERT)
    @PostMapping("/publishChildActivities")
    public AjaxResult publishChildActivities(@RequestBody Activities activities)
    {
        return toAjax(activitiesService.insertActivities(activities));
    }

    /**
     * 停用子活动
     */
    @Log(title = "活动发布", businessType = BusinessType.UPDATE)
    @PostMapping("/disableChildActivities")
    public AjaxResult disableChildActivities(@RequestBody Activities activities)
    {
        return toAjax(activitiesService.updateStatus(activities));
    }

    /**
     * 现场经颅磁数据上传
     */
    @Log(title = "现场经颅磁", businessType = BusinessType.INSERT)
    @PostMapping("/tmsCommit")
    public AjaxResult tmsCommit(@RequestBody ActivitiesTmsdata activitiesTmsdata)
    {
        return toAjax(activitiesTmsdataService.insertActivitiesTmsdata(activitiesTmsdata));
    }

    /**
     * 获取技术分类
     */
    @GetMapping("/getTechType")
    public TableDataInfo listTechType()
    {
        startPage();
        SysDictData dict=new SysDictData();
        dict.setDictType("sys_activities_type");
        dict.setStatus("0");
        List<SysDictData> list = dictDataService.selectDictDataList(dict);
        return getDataTable(list);
    }


    /**
     * 活动戒治技术资料上传
     */
    @Log(title = "活动戒治技术资料", businessType = BusinessType.INSERT)
    @PostMapping("/techCommit")
    public AjaxResult techCommit(@RequestBody ActivitiesTech activitiesTech)
    {
        return toAjax(activitiesTechService.insertActivitiesTech(activitiesTech));
    }

    /**
     * 活动评价
     */
    @Log(title = "活动评价", businessType = BusinessType.INSERT)
    @PostMapping("/reviewCommit")
    public AjaxResult add(@RequestBody ActivitiesReview activitiesReview)
    {
        return toAjax(activitiesReviewService.insertActivitiesReview(activitiesReview));
    }

    /**
     * 新增现场资讯
     */
    @Log(title = "现场资讯", businessType = BusinessType.INSERT)
    @PostMapping("/liveCommit")
    public AjaxResult add(@RequestBody ActivitiesLive activitiesLive)
    {
        return toAjax(activitiesLiveService.insertActivitiesLive(activitiesLive));
    }

}
