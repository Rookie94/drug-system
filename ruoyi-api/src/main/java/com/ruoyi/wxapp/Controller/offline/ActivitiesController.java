package com.ruoyi.wxapp.Controller.offline;

import com.ruoyi.cms.offline.domain.*;
import com.ruoyi.cms.offline.domain.vo.*;
import com.ruoyi.cms.offline.service.*;
import com.ruoyi.cms.res.domain.ResOrginfo;
import com.ruoyi.cms.res.service.IResOrginfoService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
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

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.getUserId;

@RestController
@RequestMapping("/api/offline")
@ResponseBody
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
        activities.setStatus("0");
        List<Activities> list = activitiesService.selectPrimaryActivitiesList(activities);
        return getDataTable(list);
    }

    /**
     * 查询学员活动状态
     */
    @GetMapping("/getActivitiesStateByStudentId")
    public TableDataInfo getActivitiesStateByStudentId(ActivitiesStateVo activities)
    {
        startPage();
        activities.setUseDataScope(false);
        activities.setUserId(getUserId());
        if(activities.getParentActivityId()==null){
            activities.setParentActivityId(0L);
        }
        List<ActivitiesStateVo> list = activitiesService.selectActivitiesStateByStudent(activities);
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
        if(!activity.getAppored().equals("2")&& !activity.getAppored().equals("3")){
            return error("活动没有处于报名中或活动进行中状态,不可报名");
        }
        if(activity.getParentActivityId()!=0){
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
        if(sysUser.getUserType().equals("00")){
            return error("学员才能报名!");
        }
        ActivitiesSignUpVo signUpCheck=new ActivitiesSignUpVo();
        signUpCheck.setUserId(sysUser.getUserId());
        signUpCheck.setActivityId(activityId);
        signUpCheck.setUseDataScope(false);
        List<ActivitiesSignUpVo> list= signUpService.selectSignUpList(signUpCheck);
        if(list!=null && list.size()>0){
            return error("学员已报名,请勿重复报名!");
        }
        ActivitiesSignUp signUp=new ActivitiesSignUp();
        signUp.setActivityId(activityId);
        return toAjax(signUpService.insertSignUp(signUp));
    }

    /**
     * 查询活动详情
     */
    @GetMapping("/getSignUpState/{activityId}")
    public AjaxResult getSignUpState(@PathVariable("activityId") Long activityId)
    {
        ActivitiesSignUpVo signUp=new ActivitiesSignUpVo();
        signUp.setUseDataScope(false);
        signUp.setActivityId(activityId);
        signUp.setUserId(getUserId());
        List<ActivitiesSignUpVo> list=signUpService.selectSignUpList(signUp);
        if(list==null || list.size()==0){
            return error("当前学员未报名!");
        }
        else{
            return success("当前学员已报名!");
        }
    }

    /**
     * 获取活动报名人员清单
     */
    @GetMapping("/getSignUpList")
    public TableDataInfo getSignUpList(ActivitiesSignUpVo signUp)
    {
        TableDataInfo tableDataInfo=new TableDataInfo();
        tableDataInfo.setCode(500);
        tableDataInfo.setTotal(0L);
        if(signUp.getActivityId()==null){
            tableDataInfo.setMsg("活动id不能为空");
            return tableDataInfo;
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(signUp.getActivityId());
        if(activity==null){
            tableDataInfo.setMsg("活动不存在");
            return tableDataInfo;
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            tableDataInfo.setMsg("登录信息不存在");
            return tableDataInfo;
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            tableDataInfo.setMsg("用户信息不存在");
            return tableDataInfo;
        }
        if(sysUser.getUserType().equals("00")==false){
            tableDataInfo.setMsg("学员没有权限获取报名人员清单");
            return tableDataInfo;
        }
        startPage();
        signUp.setUseDataScope(false);
        List<ActivitiesSignUpVo> list=signUpService.selectSignUpList(signUp);
        return getDataTable(list);
    }

    private int calculateAge(Date birthDate) {
        LocalDate defaultDate = LocalDate.of(1990, 1, 1);
        LocalDate birthLocalDate = (birthDate != null) ?
                birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() :
                defaultDate;
        return Period.between(birthLocalDate, LocalDate.now()).getYears();
    }

    /**
     * 新增预约详情
     */
    @Log(title = "活动签到", businessType = BusinessType.INSERT)
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

        if(activity.getAppored().equals("3")==false){
            return error("活动进行中才可以签到");
        }

        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("无法获取登录用户信息");
        }

        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            return error("游客不能参加活动");
        }
        if(sysUser.getUserType().equals("00")){
            return error("学员才能签到");
        }

        //检查报名情况
        ActivitiesSignUpVo signUp=new ActivitiesSignUpVo();
        if(activity.getParentActivityId()==0){
            signUp.setUseDataScope(false);
            signUp.setActivityId(activityId);
            signUp.setUserId(getUserId());
            List<ActivitiesSignUpVo> list=signUpService.selectSignUpList(signUp);
            if(list==null || list.size()==0){
                return new AjaxResult(HttpStatus.SEE_OTHER,"学员报名活动以后才能签到!",activityId);
            }
        }
        else{
            signUp.setUseDataScope(false);
            signUp.setActivityId(activity.getParentActivityId());
            signUp.setUserId(getUserId());
            List<ActivitiesSignUpVo> list=signUpService.selectSignUpList(signUp);
            if(list==null || list.size()==0){
                return new AjaxResult(HttpStatus.SEE_OTHER,"学员主活动报名以后才能签到子活动!",activity.getParentActivityId());
            }
        }

        //检查签到情况
        ActivitiesCheckinVo checkinCheck=new ActivitiesCheckinVo();
        checkinCheck.setUserId(sysUser.getUserId());
        checkinCheck.setActivityId(activityId);
        checkinCheck.setUseDataScope(false);
        List<ActivitiesCheckinVo> list= checkinService.selectActivitiesCheckinList(checkinCheck);
        if(list!=null && list.size()>0){
            return error("学员已签到,请勿重复签到!");
        }

        //开始签到
        ActivitiesCheckin checkIn=new ActivitiesCheckin();
        checkIn.setActivityId(activityId);
        if(activity.getParentActivityId()==0){
            return toAjax(checkinService.insertActivitiesCheckin(checkIn));
        }
        else{
            if(activity.getActivityType().equals("0")){
                //经颅磁
                ActivitiesTmsdataVo tsmDataCheck=new ActivitiesTmsdataVo();
                tsmDataCheck.setUseDataScope(false);
                tsmDataCheck.setActivityId(activityId);
                tsmDataCheck.setUserId(sysUser.getUserId());
                List<ActivitiesTmsdataVo> list1=activitiesTmsdataService.selectActivitiesTmsdataList(tsmDataCheck);
                if(list1==null || list1.size()==0){
                    ActivitiesTmsdata tsmData=new ActivitiesTmsdata();
                    tsmData.setActivityId(activityId);
                    tsmData.setUserId(sysUser.getUserId());
                    tsmData.setDeptId(sysUser.getDeptId());
                    tsmData.setStatus("0");
                    tsmData.setName(sysUser.getNickName());
                    tsmData.setSex(sysUser.getSex().equals(0) ? "男":"女");
                    tsmData.setAge(String.valueOf(calculateAge(sysUser.getBirthday())));
                    return toAjax(activitiesTmsdataService.insertActivitiesTmsdata(tsmData));
                }
                else{
                    return error("学员经颅磁数据已生成,请勿重复创建!");
                }
            }
            else{
                //其它图文
                ActivitiesTechVo techCheck=new ActivitiesTechVo();
                techCheck.setUseDataScope(false);
                techCheck.setActivityId(activityId);
                techCheck.setUserId(sysUser.getUserId());
                List<ActivitiesTechVo> list2=activitiesTechService.selectActivitiesTechList(techCheck);
                if(list2==null || list2.size()==0){
                    ActivitiesTech techData=new ActivitiesTech();
                    techData.setActivityId(activityId);
                    techData.setUserId(sysUser.getUserId());
                    techData.setDeptId(sysUser.getDeptId());
                    techData.setStatus("0");
                    techData.setName(sysUser.getNickName());
                    techData.setSex(sysUser.getSex().equals(0) ? "男":"女");
                    techData.setAge(String.valueOf(calculateAge(sysUser.getBirthday())));
                    techData.setTechType(activity.getActivityType());
                    return toAjax(activitiesTechService.insertActivitiesTech(techData));
                }
                else{
                    return error("学员子活动数据已生成,请勿重复生成!");
                }
            }
        }
    }

    /**
     * 获取活动签到人员清单
     */
    @GetMapping("/getCheckInList")
    public TableDataInfo getCheckInList(ActivitiesCheckinVo checkIn)
    {
        TableDataInfo tableDataInfo=new TableDataInfo();
        tableDataInfo.setCode(500);
        tableDataInfo.setTotal(0L);
        if(checkIn.getActivityId()==null){
            tableDataInfo.setMsg("活动id不能为空");
            return tableDataInfo;
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(checkIn.getActivityId());
        if(activity==null){
            tableDataInfo.setMsg("活动不存在");
            return tableDataInfo;
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            tableDataInfo.setMsg("登录信息不存在");
            return tableDataInfo;
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            tableDataInfo.setMsg("用户信息不存在");
            return tableDataInfo;
        }
        if(sysUser.getUserType().equals("00")==false){
            tableDataInfo.setMsg("学员没有权限获取签到人员清单");
            return tableDataInfo;
        }
        startPage();
        checkIn.setUseDataScope(false);
        List<ActivitiesCheckinVo> list=checkinService.selectActivitiesCheckinList(checkIn);
        return getDataTable(list);
    }


    /**
     * 查询活动详情
     */
    @GetMapping("/getCheckInState/{activityId}")
    public AjaxResult getCheckInState(@PathVariable("activityId") Long activityId)
    {
        ActivitiesCheckinVo checkIn=new ActivitiesCheckinVo();
        checkIn.setUseDataScope(false);
        checkIn.setActivityId(activityId);
        checkIn.setUserId(getUserId());
        List<ActivitiesCheckinVo> list=checkinService.selectActivitiesCheckinList(checkIn);
        if(list==null || list.size()==0){
            return error("当前学员未签到");
        }
        else{
            return success("当前学员已签到");
        }
    }

    /**
     * 发布子活动
     */
    @Log(title = "发布子活动", businessType = BusinessType.INSERT)
    @PostMapping("/publishChildActivities")
    public AjaxResult publishChildActivities(@RequestBody Activities activities)
    {
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("登录用户才能发布子活动");
        }
        if(loginUser.getUser()==null){
            return error("游客不能发布子活动");
        }
        if(loginUser.getUser().getUserType().equals("00")==false){
            return error("学员不能发布子活动");
        }
        if(activities.getActivityType()==null)
        {
            return error("子活动分类不能为空");
        }
        activities.setAppored("3");
        return toAjax(activitiesService.insertActivities(activities));
    }

    /**
     * 发布子活动
     */
    @Log(title = "更新子活动", businessType = BusinessType.UPDATE)
    @PostMapping("/updateChildActivities")
    public AjaxResult updateChildActivities(@RequestBody Activities activities)
    {
        if(activities.getActivityId()==null)
        {
            return error("子活动id不能为空");
        }
        if(activities.getActivityType()==null)
        {
            return error("子活动分类不能为空");
        }
        return toAjax(activitiesService.updateActivities(activities));
    }

    /**
     * 停用子活动
     */
    @Log(title = "停用活动", businessType = BusinessType.UPDATE)
    @PostMapping("/disableChildActivities")
    public AjaxResult disableChildActivities(@RequestBody Activities activities)
    {
        return toAjax(activitiesService.updateStatus(activities));
    }

    /**
     * 现场经颅磁数据上传
     */
    @Log(title = "现场经颅磁", businessType = BusinessType.UPDATE)
    @PostMapping("/tmsCommit")
    public AjaxResult tmsCommit(@RequestBody ActivitiesTmsdata activitiesTmsdata)
    {
        if(activitiesTmsdata.getAnalyzeId()==null){
            return error("经颅磁技术id不能为空");
        }
        if(activitiesTmsdata.getActivityId()==null){
            return error("活动id不能为空");
        }
        if(activitiesTmsdata.getUserId()==null){
            return error("学员id不能为空");
        }
        if(activitiesTmsdata.getDeptId()==null){
            return error("学员部门id不能为空");
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activitiesTmsdata.getActivityId());
        if(activity==null){
            return error("活动不存在");
        }
        if(!activity.getStatus().equals("0")){
            return error("活动已停用,不能上传现场资讯");
        }
        if(!activity.getAppored().equals("5")==false){
            return error("活动已归档,不能上传经颅磁资讯");
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("无法获取登录用户信息");
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            return error("游客不能上传经颅磁资讯");
        }
        if(sysUser.getUserType().equals("00")==false){
            return error("警官才能上传经颅磁资讯");
        }
        return toAjax(activitiesTmsdataService.updateActivitiesTmsdata(activitiesTmsdata));
    }

    /**
     * 现场经颅磁数据上传
     **/
    @GetMapping("/getTmsData")
    public TableDataInfo getTmsData(ActivitiesTmsdataVo activitiesTmsdata)
    {
        TableDataInfo tableDataInfo=new TableDataInfo();
        tableDataInfo.setCode(500);
        tableDataInfo.setTotal(0L);
        if(activitiesTmsdata.getActivityId()==null){
            tableDataInfo.setMsg("活动id不能为空");
            return tableDataInfo;
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activitiesTmsdata.getActivityId());
        if(activity==null){
            tableDataInfo.setMsg("活动不存在");
            return tableDataInfo;
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            tableDataInfo.setMsg("登录信息不存在");
            return tableDataInfo;
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            tableDataInfo.setMsg("用户信息不存在");
            return tableDataInfo;
        }
        if(sysUser.getUserType().equals("00")==false){
            tableDataInfo.setMsg("学员没有权限获取经颅磁填报数据");
            return tableDataInfo;
        }
        startPage();
        activitiesTmsdata.setUseDataScope(false);
        List<ActivitiesTmsdataVo> list=activitiesTmsdataService.selectActivitiesTmsdataList(activitiesTmsdata);
        return getDataTable(list);
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
    @Log(title = "活动戒治技术资料", businessType = BusinessType.UPDATE)
    @PostMapping("/techCommit")
    public AjaxResult techCommit(@RequestBody ActivitiesTech activitiesTech)
    {
        if(activitiesTech.getTechId()==null){
            return error("戒治技术id不能为空");
        }
        if(activitiesTech.getActivityId()==null){
            return error("活动id不能为空");
        }
        if(activitiesTech.getUserId()==null){
            return error("学员id不能为空");
        }
        if(activitiesTech.getDeptId()==null){
            return error("学员部门id不能为空");
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activitiesTech.getActivityId());
        if(activity==null){
            return error("活动不存在");
        }
        if(!activity.getStatus().equals("0")){
            return error("活动已停用,不能上传戒治技术资料");
        }
        if(!activity.getAppored().equals("5")==false){
            return error("活动已归档,不能上传戒治技术资料");
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("无法获取登录用户信息");
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            return error("游客不能上传戒治技术资料");
        }
        if(sysUser.getUserType().equals("00")==false){
            return error("警官才能上传戒治技术资料");
        }
        return toAjax(activitiesTechService.updateActivitiesTech(activitiesTech));
    }

    /**
     * 现场活动戒治技术资料
     **/
    @GetMapping("/getTechData")
    public TableDataInfo getTmsData(ActivitiesTechVo activitiesTechVo)
    {
        TableDataInfo tableDataInfo=new TableDataInfo();
        tableDataInfo.setCode(500);
        tableDataInfo.setTotal(0L);
        if(activitiesTechVo.getActivityId()==null){
            tableDataInfo.setMsg("活动id不能为空");
            return tableDataInfo;
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activitiesTechVo.getActivityId());
        if(activity==null){
            tableDataInfo.setMsg("活动不存在");
            return tableDataInfo;
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            tableDataInfo.setMsg("登录信息不存在");
            return tableDataInfo;
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            tableDataInfo.setMsg("用户信息不存在");
            return tableDataInfo;
        }
        if(sysUser.getUserType().equals("00")==false){
            tableDataInfo.setMsg("学员没有权限获取戒治技术填报数据");
            return tableDataInfo;
        }
        startPage();
        activitiesTechVo.setUseDataScope(false);
        List<ActivitiesTechVo> list=activitiesTechService.selectActivitiesTechList(activitiesTechVo);
        return getDataTable(list);
    }


    /**
     * 活动评价
     */
    @Log(title = "活动评价", businessType = BusinessType.INSERT)
    @PostMapping("/reviewCommit")
    public AjaxResult reviewCommit(@RequestBody ActivitiesReview activitiesReview)
    {
        if(activitiesReview.getActivityId()==null){
            return error("活动id不能为空");
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activitiesReview.getActivityId());
        if(activity==null){
            return error("活动不存在");
        }
        if(!activity.getStatus().equals("0")){
            return error("活动已停用,不能评价");
        }
        if(!activity.getAppored().equals("5")==false){
            return error("活动已归档,处于不可评价状态");
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("无法获取登录用户信息");
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            return error("游客不能参加活动评价");
        }
        if(sysUser.getUserType().equals("00")){
            return error("学员才能参加活动评价");
        }
        return toAjax(activitiesReviewService.insertActivitiesReview(activitiesReview));
    }

    /**
     * 新增现场资讯
     */
    @Log(title = "现场资讯", businessType = BusinessType.INSERT)
    @PostMapping("/liveCommit")
    public AjaxResult liveCommit(@RequestBody ActivitiesLive activitiesLive)
    {
        if(activitiesLive.getActivityId()==null){
            return error("活动id不能为空");
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activitiesLive.getActivityId());
        if(activity==null){
            return error("活动不存在");
        }
        if(!activity.getStatus().equals("0")){
            return error("活动已停用,不能上传现场资讯");
        }
        if(!activity.getAppored().equals("5")==false){
            return error("活动已归档,不能上传现场资讯");
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            return error("无法获取登录用户信息");
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            return error("游客不能上传现场资讯");
        }
        if(sysUser.getUserType().equals("00")==false){
            return error("警官才能上传现场资讯");
        }
        return toAjax(activitiesLiveService.insertActivitiesLive(activitiesLive));
    }

    /**
     * 现场资讯列表
     **/
    @GetMapping("/getLiveData")
    public TableDataInfo getLiveData(ActivitiesLiveVo activitiesLive)
    {
        TableDataInfo tableDataInfo=new TableDataInfo();
        tableDataInfo.setCode(500);
        tableDataInfo.setTotal(0L);
        if(activitiesLive.getActivityId()==null){
            tableDataInfo.setMsg("活动id不能为空");
            return tableDataInfo;
        }
        Activities activity= activitiesService.selectActivitiesByActivityId(activitiesLive.getActivityId());
        if(activity==null){
            tableDataInfo.setMsg("活动不存在");
            return tableDataInfo;
        }
        LoginUser loginUser=getLoginUser();
        if(loginUser==null){
            tableDataInfo.setMsg("登录信息不存在");
            return tableDataInfo;
        }
        SysUser sysUser=loginUser.getUser();
        if(sysUser==null){
            tableDataInfo.setMsg("用户信息不存在");
            return tableDataInfo;
        }
        if(!sysUser.getUserType().equals("00")){
            tableDataInfo.setMsg("学员没有权限获取现场资讯列表数据");
            return tableDataInfo;
        }
        startPage();
        activitiesLive.setUseDataScope(false);
        List<ActivitiesLiveVo> list=activitiesLiveService.selectActivitiesLiveList(activitiesLive);
        return getDataTable(list);
    }

}
