package com.ruoyi.ruoyiapi.Controller.wxapi;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.common.core.domain.entity.MiniAppUserDTO;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.MiniAppException;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.wxsys.entity.R;
import com.ruoyi.wxsys.service.IMiniAppUserService;

@RestController
@RequestMapping("/miniapp")
public class WxMiniAppUserController extends BaseController
{
    @Autowired
    private IMiniAppUserService miniAppUserService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取微信小程序用户信息
     * @return
     */
    @GetMapping("/getMiniAppUserInfo")
    public R getMiniAppUserInfo()
    {

        return R.success(getLoginUser().getMiniAppUser());
    }

    /**
     * 更新微信小程序用户信息
     * @param miniAppUserDTO
     * @return
     */
    @PostMapping("/updateMiniAppUser")
    public R updateMiniAppUser(@RequestBody MiniAppUserDTO miniAppUserDTO)
    {
        LoginUser loginUser = getLoginUser();
        //这里防止被人恶意发包修改 通过Security获取当前的id
        miniAppUserService.updateMiniAppUser(new MiniAppUser(miniAppUserDTO,loginUser.getMiniAppUser().getId()));
        //更新缓存
        loginUser.getMiniAppUser().setNickName(miniAppUserDTO.getNickName());
        loginUser.getMiniAppUser().setSex(miniAppUserDTO.getSex());
        tokenService.setLoginUser(loginUser);
        return R.success();
    }

    @PostMapping("/uploadAvatar")
    public R uploadAvatar(@RequestParam("avatarfile") MultipartFile file) throws MiniAppException
    {
        LoginUser loginUser = getLoginUser();
        JSONObject data = miniAppUserService.uploadAvatar(loginUser.getMiniAppUser(), file);
        return R.success(data);
    }
}
