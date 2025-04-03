package com.ruoyi.framework.security.wxsys;

import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.wxsys.service.IMiniAppUserService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.OSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service("userDetailsMiniAppByOpenIdServiceImpl")
public class UserDetailsMiniAppByOpenIdServiceImpl implements UserDetailsService
{
    @Autowired
    private IMiniAppUserService miniAppUserService;

    @Autowired
    private OSSUtils ossUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        String[] split = username.split(",");
        String openId = split[0];
        Long miniAppId = Long.valueOf(split[1]);
        MiniAppUser miniAppUser = miniAppUserService.selectMiniAppUserByOpenIdAndMiniAppId(openId,miniAppId);
        if(ObjectUtils.isEmpty(miniAppUser))
        {
            log.info("小程序用户不存在,openId:{},miniAppId:{}",openId,miniAppId);
            throw new ServiceException("miniAppUser is not found.");
        }
        return createWxLoginUser(miniAppUser);
    }

    public UserDetails createWxLoginUser(MiniAppUser miniAppUser)
    {
        String avatar = miniAppUser.getAvatar();
        miniAppUser.setAvatar(ObjectUtils.isEmpty(avatar) ? avatar : ossUtils.getUrl(avatar,false));
        return new LoginUser(miniAppUser);
    }
}
