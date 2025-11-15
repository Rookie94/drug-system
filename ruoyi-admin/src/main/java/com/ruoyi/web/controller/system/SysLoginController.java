package com.ruoyi.web.controller.system;

import java.util.List;
import java.util.Set;


import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.ruoyi.framework.jssms.domain.JsSmsSendResponse;
import com.ruoyi.framework.sms.SmsSendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jssms.service.IJsSmsService;
import com.ruoyi.framework.sms.SmsLoginBody;
import com.ruoyi.framework.sms.SmsLoginService;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private IJsSmsService jsSmsService;

    @Autowired
    private SmsLoginService smsLoginService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
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
     * 手机号登录
     *
     * @param smsLoginBody 登录信息
     * @return 结果
     */
    @PostMapping("/smsLogin")
    public AjaxResult loginBySms(@Validated @RequestBody SmsLoginBody smsLoginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = smsLoginService.login(smsLoginBody.getPhonenumber(), smsLoginBody.getCode());
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
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions))
        {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
