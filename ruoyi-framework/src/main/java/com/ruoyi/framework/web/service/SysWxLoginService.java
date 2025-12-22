package com.ruoyi.framework.web.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.common.core.domain.entity.MiniAppUserVo;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.WxLoginBody;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysMiniAppUserService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysWxLoginService {

    @Autowired
    private ISysMiniAppUserService miniAppUserService;

    @Autowired
    private ISysUserService userService;

    public AjaxResult registerUser(Long appId, String openId, String wxNickName,String wxAvatarUrl, WxLoginBody wxLoginBody) {
        try {
            registerUserTransactional(appId, openId, wxNickName, wxAvatarUrl, wxLoginBody);
            return AjaxResult.success("用户注册成功！");
        } catch (Exception ex) {
            return AjaxResult.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public AjaxResult registerUserTransactional(Long appId,String openId,String wxNickName,String wxAvatarUrl,WxLoginBody wxLoginBody){
        try{
            MiniAppUser miniAppUser = new MiniAppUser();
            miniAppUser.setMiniAppId(appId);
            miniAppUser.setOpenId(openId);

            String roleId=wxLoginBody.getRoleId();
            if(roleId.equals("99")){
                roleId="33";
            }

            String phoneNumber = wxLoginBody.getPhoneNumber();

            //身份证号
            String idCardNumber=wxLoginBody.getIdCardNumber();
            //姓名
            String nickName=wxLoginBody.getNickName();
            //性别
            String sex=wxLoginBody.getSex();
            //birthDay
            String birthDay=wxLoginBody.getBirthDay();
            //NationId
            String nationId=wxLoginBody.getNationId();
            //ProvinceId
            String provinceId=wxLoginBody.getProvinceId();
            //CityId
            String cityId=wxLoginBody.getCityId();
            //AreaId
            String areaId=wxLoginBody.getAreaId();

            //记录微信登录信息
            miniAppUser.setNickName(wxNickName);
            miniAppUser.setAvatar(wxAvatarUrl);
            miniAppUser.setPhoneNumber(phoneNumber);
            miniAppUserService.insertMiniAppUser(miniAppUser);

            List<MiniAppUser> list1 = miniAppUserService.selectMiniAppUserList(miniAppUser);
            if (list1 == null || list1.size() == 0) {
                throw new Exception("用户信息登记失败！");
            }

            SysUser wUser;

            //判断手机号匹配情况
            if(roleId.equals("00")){
                wUser = userService.selectWxUserByPhoneNumber(phoneNumber);
                if (wUser == null) {
                    throw new Exception("当前信息与系统不匹配,注册失败！");
                }
                else {
                    //绑定
                    wUser.setOpenId(openId);
                    wUser.setUpdateBy("sys");
                    wUser.setUpdateTime(DateUtils.getNowDate());
                    userService.updateUser(wUser);
                    return AjaxResult.success("用户注册成功！");
                }
            }
            else if (roleId.equals("11")){
                wUser = userService.selectWxUserByIdCardNumber(idCardNumber);
                if (wUser == null) {
                    throw new Exception("当前信息与系统不匹配,注册失败！");
                }
                else {
                    //绑定
                    wUser.setOpenId(openId);
                    wUser.setUpdateBy("sys");
                    wUser.setUpdateTime(DateUtils.getNowDate());
                    userService.updateUser(wUser);
                    return AjaxResult.success("用户注册成功！");
                }
            }
            else{
                wUser = userService.selectWxUserByPhoneNumber(phoneNumber);
                if (wUser == null) {
                    //创建游客用户
                    MiniAppUserVo miniAppUserVo=new MiniAppUserVo();
                    miniAppUserVo.setOpenId(openId);
                    miniAppUserVo.setPhoneNumber(phoneNumber);
                    miniAppUserVo.setIdNumber(idCardNumber);
                    miniAppUserVo.setNickName(nickName);
                    miniAppUserVo.setSex(sex);
                    if(StringUtils.isNotEmpty(provinceId)){
                        miniAppUserVo.setProvinceId(provinceId);
                    }
                    if(StringUtils.isNotEmpty(cityId)){
                        miniAppUserVo.setCityId(cityId);
                    }
                    if(StringUtils.isNotEmpty(areaId)){
                        miniAppUserVo.setAreaId(areaId);
                    }
                    miniAppUserService.insertMiniAppGuestUser(miniAppUserVo);
                }
                else{
                    if(wUser.getUserType().equals("00") || wUser.getUserType().equals("11")){
                        throw new Exception("用户注册失败,请勿选择错误的身份！");
                    }
                }
                wUser = userService.selectWxUserByPhoneNumber(phoneNumber);
                if (wUser == null) {
                    throw new Exception("用户注册失败！");
                }
                else{
                    return AjaxResult.success("用户注册成功！");
                }
            }
        }
        catch(Exception ex){
            //return AjaxResult.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}
