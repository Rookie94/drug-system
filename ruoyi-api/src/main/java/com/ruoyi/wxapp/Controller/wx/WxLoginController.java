package com.ruoyi.wxapp.Controller.wx;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.framework.utils.MyLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.MiniApp;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.WxLoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.WeChatUtils;
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.common.core.domain.entity.MiniAppUserVo;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.WxParam;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.jssms.domain.JsSmsSendResponse;
import com.ruoyi.framework.jssms.service.IJsSmsService;
import com.ruoyi.framework.sms.SmsLoginService;
import com.ruoyi.framework.web.service.SysWxLoginService;
import com.ruoyi.framework.sms.SmsSendDTO;
import com.ruoyi.framework.web.service.TokenService;

import com.ruoyi.system.service.ISysMiniAppService;
import com.ruoyi.system.domain.SysArea;
import com.ruoyi.system.service.ISysAreaService;
import com.ruoyi.system.service.ISysMiniAppUserService;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
public class WxLoginController  {

    private static final Logger logger = LoggerFactory.getLogger(WxLoginController.class);

    @Autowired
    private WeChatUtils weChatUtils;

    @Resource
    private CaptchaService captchaService;

    @Autowired
    private IJsSmsService jsSmsService;

    @Autowired
    private ISysMiniAppService appService;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMiniAppUserService miniAppUserService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysWxLoginService sysWxLoginService;

    @Autowired
    private ISysAreaService sysAreaService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SmsLoginService smsLoginService;

    /**
     * 获取验证码接口
     *
     * @param captchaVO 验证码参数
     *                  "captchaType": "blockPuzzle",
     *                  "clientUid": "唯一标识"
     */
    @PostMapping("/captcha/get")
    public ResponseModel get(@RequestBody CaptchaVO captchaVO) {
        return captchaService.get(captchaVO);
    }

    /**
     * 校验滑动验证
     *
     * @param captchaVO 验证码参数
     *                  "captchaType": "blockPuzzle",
     *                  "pointJson": "QxIVdlJoWUi04iM+65hTow==",  //aes加密坐标信息
     *                  "token": "71dd26999e314f9abb0c635336976635"  //get请求返回的token
     */
    @PostMapping("/captcha/check")
    public ResponseModel check(@RequestBody CaptchaVO captchaVO) {
        return captchaService.check(captchaVO);
    }

    @GetMapping("/getarea")
    public AjaxResult list(SysArea sysArea)
    {
        sysArea.setUseDataScope(false);
        sysArea.setStatus("0");
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        return AjaxResult.success(list);
    }

    /**
     * 发送验证码接口，需要二次验证
     *
     * @param smsSendDTO
     * @return
     */
    @PostMapping("/sendSms")
    public AjaxResult sendSms(@RequestBody SmsSendDTO smsSendDTO) {
        // 1. 先校验滑块
        CaptchaVO captchaVO = new CaptchaVO();
        captchaVO.setCaptchaVerification(smsSendDTO.getCaptchaVerification());
        ResponseModel response = captchaService.verification(captchaVO);
        if (!response.isSuccess()) {
            return AjaxResult.error("滑块验证失败!");
        }
        String phoneNumber = smsSendDTO.getPhoneNumber();
        if (StringUtils.isBlank(phoneNumber) || phoneNumber.length() != 11 || !phoneNumber.matches("^1[3-9]\\d{9}$")) {
            return AjaxResult.error("请输入有效的长度为11位的手机号");
        }
        JsSmsSendResponse jsSmsSendResponse=jsSmsService.sendVerificationCode(phoneNumber);
        if (!jsSmsSendResponse.getStatus().equals("0")) {
            return AjaxResult.error("验证码发送失败，请稍后重试");
        }
        return AjaxResult.success("验证码发送成功");
    }

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
        WxParam wxParam=weChatUtils.getOpenIdAndSessionKey(appId,appSecret,code);

        if(wxParam==null){
            return AjaxResult.error("获取当前微信参数失败！");
        }

        //获取session_key和openid
        String sessionKey = wxParam.getSessionKey();
        String openId = wxParam.getOpenId();

        MiniAppUser miniAppUser=new MiniAppUser();
        miniAppUser.setMiniAppId(id);
        miniAppUser.setOpenId(openId);

        List<MiniAppUser> listTemp= miniAppUserService.selectMiniAppUserList(miniAppUser);
        if((listTemp==null || listTemp.size()==0) ){
            return new AjaxResult(401,"当前微信未绑定");
        }
        else{
            return new AjaxResult(200,"当前微信已绑定");
        }
    }

    /**
     * 微信登录
     * @param wxLoginBody
     * @return
     */
    @PostMapping("/binding-phone")
    public AjaxResult bindingPhoneNumber(@RequestBody WxLoginBody wxLoginBody) {
        logger.info("登录参数：" + JSON.toJSONString(wxLoginBody));

        //id
        Long id = wxLoginBody.getId();

        //AppId,AppSecret
        MiniApp wxApp = appService.selectMiniAppById(id);
        if (wxApp == null) {
            return AjaxResult.error("用户注册失败,小程序配置不存在！");
        }

        String appId = wxApp.getAppid();
        String appSecret = wxApp.getSecret();

        //获取登录凭证 只能用一次
        String code = wxLoginBody.getCode();

        //向微信服务器发送请求获取用户信息
        WxParam wxParam = weChatUtils.getOpenIdAndSessionKey(appId, appSecret, code);

        if (wxParam == null) {
            return AjaxResult.error("绑定失败,获取当前微信参数失败！");
        }

        //获取session_key和openid
        //String sessionKey = wxParam.getSessionKey();
        String openId = wxParam.getOpenId();

        //phoneCode
        //String phoneCode = wxLoginBody.getPhoneCode();
        //verifyCode

        //验证码
        String verifyCode= wxLoginBody.getVerifyCode();
        //角色Id
        String roleId=wxLoginBody.getRoleId();
        if(roleId.equals("99")){
            roleId="33";
        }
        //身份证号
        String idCardNumber=wxLoginBody.getIdCardNumber();
        //姓名
        String nickName=wxLoginBody.getNickName();
        //性别
        String sex=wxLoginBody.getSex();

        //秘钥
        //String encryptedIv = wxLoginBody.getEncryptedIv();
        //加密数据
        //String encryptedData = wxLoginBody.getEncryptedData();

        MiniAppUser miniAppUser = new MiniAppUser();
        miniAppUser.setMiniAppId(id);
        miniAppUser.setOpenId(openId);
        List<MiniAppUser> listTemp = miniAppUserService.selectMiniAppUserList(miniAppUser);

        if ((listTemp == null || listTemp.size() == 0)) {
            //String phoneNumber = "";
            //新用户
            /*
            if (!StringUtils.isEmpty(encryptedIv) && !StringUtils.isEmpty(encryptedData)) {
                wxParam = WeChatUtils.parseWxParam(wxParam, sessionKey, phoneCode, encryptedIv, encryptedData);
                if (wxParam == null) {
                    return AjaxResult.error("手机号绑定失败,参数解析失败！");
                }
                phoneNumber = wxParam.getPurePhoneNumber();

            } else {
                phoneNumber = wxLoginBody.getPhoneNumber();
            }
            */
            String phoneNumber = wxLoginBody.getPhoneNumber();
            if (phoneNumber == null || phoneNumber == "") {
                return AjaxResult.error("手机号不能为空！");
            }
            if (verifyCode == null || verifyCode == "") {
                return AjaxResult.error("验证码不能为空！");
            }
            if(roleId.equals("11")){
                if (idCardNumber == null || idCardNumber == "") {
                    return AjaxResult.error("身份证号不能为空！");
                }
            }
            if(roleId.equals("33")){
                if (nickName == null || nickName == "") {
                    return AjaxResult.error("姓名不能为空！");
                }
                if (sex == null || sex == "") {
                    return AjaxResult.error("性别不能为空！");
                }
            }
            try {
                smsLoginService.checkMessageCaptcha(phoneNumber,verifyCode);
            }
            catch(Exception ex)
            {
                return AjaxResult.error("手机验证码不正确！");
            }
            AjaxResult ajaxResult=sysWxLoginService.registerUser(id,openId,wxParam.getNickName(),wxParam.getAvatarUrl(),wxLoginBody);
            if(!ajaxResult.isSuccess()){
                StringBuilder sb = new StringBuilder();
                sb.append("AppId:").append(id);
                sb.append("OpenId:").append(openId);
                sb.append("手机号:").append(phoneNumber);
                sb.append("身份证号:").append(idCardNumber);
                sb.append("姓名:").append(nickName);
                String result = sb.toString();
                MyLog.error("用户注册","wxLogin","失败",result);
            }
            return ajaxResult;

        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("错误信息:").append("用户在MiniAppUser中已存在,已经注册过,二次注册!");
            sb.append("AppId:").append(id);
            sb.append("OpenId:").append(openId);
            sb.append("手机号:").append(wxLoginBody.getPhoneNumber());
            sb.append("身份证号:").append(idCardNumber);
            sb.append("姓名:").append(nickName);
            String result = sb.toString();
            MyLog.error("用户注册","wxLogin","失败",result);
            return AjaxResult.error("用户已注册.如有问题请与系统管理员联系！");
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
        WxParam wxParam=weChatUtils.getOpenIdAndSessionKey(appId,appSecret,code);

        if(wxParam==null){
            return AjaxResult.error("微信登录失败,获取参数失败！");
        }

        //获取session_key和openid
        String sessionKey = wxParam.getSessionKey();
        String openId = wxParam.getOpenId();

        SysUser wxUser = userService.selectWxUserByOpenId(openId);
        if (wxUser == null) {
            //记录日志
            StringBuilder sb = new StringBuilder();
            sb.append("错误信息:").append("微信登录失败,用户信息不存在!");
            sb.append("AppId:").append(id);
            sb.append("OpenId:").append(openId);
            sb.append("手机号:").append(wxLoginBody.getPhoneNumber());
            String result = sb.toString();
            MyLog.error("用户注册","wxLogin","失败",result);

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
    @GetMapping("/getInfo")
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
