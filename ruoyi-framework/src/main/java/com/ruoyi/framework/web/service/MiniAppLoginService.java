package com.ruoyi.framework.web.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.wxsys.manager.WxMaServiceManager;
import com.ruoyi.wxsys.service.IMiniAppService;
import com.ruoyi.wxsys.service.IMiniAppUserService;
import com.ruoyi.common.core.domain.entity.MiniApp;
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.model.WxLoginBody;
import com.ruoyi.common.core.domain.model.WxLoginUser;
import com.ruoyi.common.exception.MiniAppException;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.framework.security.wxsys.MiniAppByOpenIdAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;

@Slf4j
@Service
public class MiniAppLoginService
{
    @Autowired
    private IMiniAppService miniAppService;

    @Autowired
    private IMiniAppUserService miniAppUserService;

    @Autowired
    private WxMaServiceManager wxMaServiceManager;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public JSONObject wxMiniLogin(WxLoginBody wxLoginBody) throws MiniAppException
    {
        JSONObject data = new JSONObject();
        MiniApp miniApp = miniAppService.selectMiniAppByAppid(wxLoginBody.getAppid());
        if(ObjectUtils.isEmpty(miniApp))
        {
            throw new MiniAppException("appid error");
        }
        WxMaService wxMaService = wxMaServiceManager.getWxMaService(wxLoginBody.getAppid());
        // 小程序用户验证
        Authentication authentication = null;
        try
        {
            WxMaJscode2SessionResult sessionResult = wxMaService.jsCode2SessionInfo(wxLoginBody.getCode());
            insertMiniAppUser(sessionResult.getOpenid(),miniApp.getId());
            MiniAppByOpenIdAuthenticationToken miniAppByOpenIdAuthenticationToken = new MiniAppByOpenIdAuthenticationToken(sessionResult.getOpenid() + "," +  miniApp.getId());
            AuthenticationContextHolder.setContext(miniAppByOpenIdAuthenticationToken);
            authentication = authenticationManager.authenticate(miniAppByOpenIdAuthenticationToken);

        }
        catch (Exception e)
        {
            log.error("wxMiniLogin fail",e);
        }
        finally
        {
            AuthenticationContextHolder.clearContext();
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = tokenService.createToken(loginUser);
        data.put("token", token);
        return data;
    }

    private void insertMiniAppUser(String openId,Long miniAppId)
    {
        try
        {
            MiniAppUser miniAppUser = new MiniAppUser();
            miniAppUser.setMiniAppId(miniAppId);
            miniAppUser.setOpenId(openId);
            miniAppUser.setNickName("微信用户" + openId.substring(openId.length()-3));
            miniAppUserService.insertMiniAppUser(miniAppUser);
        }
        catch (DuplicateKeyException e)
        {
            log.error("用户已经存在,无需重复添加,openId:{},miniAppId:{}",openId,miniAppId);
        }
    }
}
