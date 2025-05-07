package com.ruoyi.wxapp.Controller.wx;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.WxParam;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMiniAppUserService;
import com.ruoyi.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.MiniApp;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.WxLoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.service.ISysMiniAppService;
import com.ruoyi.common.utils.WeChatUtils;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class WxLoginController  {

    private static final Logger logger = LoggerFactory.getLogger(WxLoginController.class);

    @Autowired
    private ISysMiniAppService appService;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMiniAppUserService miniAppUserService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    /**
     * 检查手机号绑定
     * @param wxLoginBody
     * @return
     */
    @PostMapping("/check-binding")
    public AjaxResult checkBinding(@RequestBody WxLoginBody wxLoginBody)
    {
        logger.info("登录参数：" + JSON.toJSONString(wxLoginBody));

        //id
        Long id= wxLoginBody.getId();

        //AppId,AppSecret
        MiniApp wxApp=appService.selectMiniAppById(id);
        if(wxApp==null){
            return AjaxResult.error("微信登录失败,配置不存在！");
        }

        String appId=wxApp.getAppid();
        String appSecret=wxApp.getSecret();

        //获取登录凭证 只能用一次
        String code = wxLoginBody.getCode();

        //向微信服务器发送请求获取用户信息
        WxParam wxParam=WeChatUtils.getOpenIdAndSessionKey(appId,appSecret,code);

        if(wxParam==null){
            return AjaxResult.error("检查手机号绑定失败,获取参数失败！");
        }

        //获取session_key和openid
        String sessionKey = wxParam.getSessionKey();
        String openId = wxParam.getOpenId();

        MiniAppUser miniAppUser=new MiniAppUser();
        miniAppUser.setMiniAppId(id);
        miniAppUser.setOpenId(openId);

        List<MiniAppUser> listTemp= miniAppUserService.selectMiniAppUserList(miniAppUser);
        if((listTemp==null || listTemp.size()==0) ){
            return new AjaxResult(401,"手机号未绑定");
        }
        else{
            if(StringUtils.isEmpty(listTemp.get(0).getPhoneNumber())){
                return new AjaxResult(401,"手机号未绑定");
            }
            else{
                return new AjaxResult(200,"手机号已绑定");
            }
        }
    }

    /**
     * 微信登录
     * @param wxLoginBody
     * @return
     */
    @PostMapping("/binding-phone")
    public AjaxResult bindingPhoneNumber(@RequestBody WxLoginBody wxLoginBody)
    {
        logger.info("登录参数：" + JSON.toJSONString(wxLoginBody));

        //id
        Long id= wxLoginBody.getId();

        //AppId,AppSecret
        MiniApp wxApp=appService.selectMiniAppById(id);
        if(wxApp==null){
            return AjaxResult.error("手机号绑定失败,配置不存在！");
        }

        String appId=wxApp.getAppid();
        String appSecret=wxApp.getSecret();

        //获取登录凭证 只能用一次
        String code = wxLoginBody.getCode();

        //向微信服务器发送请求获取用户信息
        WxParam wxParam=WeChatUtils.getOpenIdAndSessionKey(appId,appSecret,code);

        if(wxParam==null){
            return AjaxResult.error("手机号绑定失败,获取参数失败！");
        }

        //获取session_key和openid
        String sessionKey = wxParam.getSessionKey();
        String openId = wxParam.getOpenId();

        //phoneCode
        String phoneCode = wxLoginBody.getPhoneCode();
        //秘钥
        String encryptedIv = wxLoginBody.getEncryptedIv();
        //加密数据
        String encryptedData = wxLoginBody.getEncryptedData();

        MiniAppUser miniAppUser=new MiniAppUser();
        miniAppUser.setMiniAppId(id);
        miniAppUser.setOpenId(openId);

        List<MiniAppUser> listTemp= miniAppUserService.selectMiniAppUserList(miniAppUser);

        if((listTemp==null || listTemp.size()==0) ){
            //新用户
            if(StringUtils.isEmpty(encryptedIv) || StringUtils.isEmpty(encryptedData)){
                return AjaxResult.error("手机号绑定失败,获取IV和加密参数失败！");
            }
            else{

                wxParam = WeChatUtils.parseWxParam(wxParam, sessionKey, phoneCode, encryptedIv, encryptedData);
                if (wxParam == null) {
                    return AjaxResult.error("手机号绑定失败,参数解析失败！");
                }

                if (wxParam.getPurePhoneNumber()==""){
                    return AjaxResult.error("手机号绑定失败,获取手机号失败！");
                }

                //记录微信登录信息
                miniAppUser.setNickName(wxParam.getNickName());
                miniAppUser.setAvatar(wxParam.getAvatarUrl());
                miniAppUser.setPhoneNumber(wxParam.getPurePhoneNumber());
                miniAppUserService.insertMiniAppUser(miniAppUser);

                List<MiniAppUser> list1 = miniAppUserService.selectMiniAppUserList(miniAppUser);
                if (list1 == null && list1.size() == 0) {
                    return AjaxResult.error("手机号绑定失败,数据写入失败！");
                }

                String phoneNumber=list1.get(0).getPhoneNumber();

                //判断手机号匹配情况
                SysUser wUser = userService.selectWxUserByPhoneNumber(phoneNumber);
                if(wUser==null) {
                    return AjaxResult.error("手机号绑定失败,用户未注册！");
                }
                else{
                    //绑定
                    wUser.setOpenId(openId);
                    wUser.setUpdateBy("sys");
                    wUser.setUpdateTime(DateUtils.getNowDate());
                    userService.updateUser(wUser);
                }

                SysUser sysUser=new SysUser();
                sysUser.setOpenId(openId);
                sysUser.setPhonenumber(phoneNumber);
                List<SysUser> list2=userService.selectUserList(sysUser);
                if(list2!=null && list2.size()>0){
                    return AjaxResult.success("手机号绑定成功！");
                }
                else{
                    return AjaxResult.error("手机号绑定失败,写入失败！");
                }
            }
        }
        else{
            //二次绑定
            return AjaxResult.error("手机号已绑定,请勿再次绑定！");
        }
    }


    /**
     * 微信登录
     * @param wxLoginBody
     * @return
     */
    @PostMapping("/wxLogin")
    public AjaxResult wxLogin(@RequestBody WxLoginBody wxLoginBody)
    {
        logger.info("登录参数：" + JSON.toJSONString(wxLoginBody));

        //id
        Long id= wxLoginBody.getId();

        //AppId,AppSecret
        MiniApp wxApp=appService.selectMiniAppById(id);
        if(wxApp==null){
            return AjaxResult.error("微信登录失败,配置不存在！");
        }

        String appId=wxApp.getAppid();
        String appSecret=wxApp.getSecret();

        //获取登录凭证 只能用一次
        String code = wxLoginBody.getCode();

        //向微信服务器发送请求获取用户信息
        WxParam wxParam=WeChatUtils.getOpenIdAndSessionKey(appId,appSecret,code);

        if(wxParam==null){
            return AjaxResult.error("微信登录失败,获取参数失败！");
        }

        //获取session_key和openid
        String sessionKey = wxParam.getSessionKey();
        String openId = wxParam.getOpenId();

        SysUser wxUser = userService.selectWxUserByOpenId(openId);
        if (wxUser == null) {
            return AjaxResult.error("微信登录失败,用户信息不存在！");
        }

        if(!wxUser.getStatus().equals("0")){
            return AjaxResult.error("微信登录失败,用户已禁止登录！");
        }

        //获取token
        String token = loginService.wxLogin(openId,wxUser);
        AjaxResult ajax = AjaxResult.success();
        ajax.put(Constants.TOKEN, token);
        return ajax;

    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        //过滤敏感信息
        user.setOpenId("");
        user.setPassword("");
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

}
