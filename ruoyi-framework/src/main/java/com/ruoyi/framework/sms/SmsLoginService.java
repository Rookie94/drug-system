package com.ruoyi.framework.sms;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserPhoneNotExistsException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.framework.security.provider.SmsCodeAuthenticationToken;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SmsLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    /**
     * 手机号登录验证
     *
     * @param phoneNumber
     * @param code
     * @return
     */
    public String login(String phoneNumber, String code) {
        this.checkMessageCaptcha(phoneNumber, code);

        // 2.用户存在, 用户验证
        Authentication authentication = null;
        try {
            // 使用自定义的toke鉴权器构造一个没有鉴权的token
            SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(phoneNumber, code);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL, e.getMessage()));
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_SUCCESS,
                MessageUtils.message("user.login.success")));
        if (authentication == null) {
            throw new UserPhoneNotExistsException();
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());

        // 生成token
        return tokenService.createToken(loginUser);

    }

    /**
     * 校验验证码
     *
     * @param phoneNumber
     * @param code
     */
    public void checkMessageCaptcha(String phoneNumber, String code) {
        String verifyKey = CacheConstants.CAPTCHA_PHONE_CODE_KEY + phoneNumber;
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        redisCache.deleteObject(verifyKey);
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(phoneNumber, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}