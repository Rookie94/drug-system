package com.ruoyi.framework.security.wxsys;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class MiniAppByOpenIdAuthenticationProvider implements AuthenticationProvider
{
    private UserDetailsService userDetailsService;

    public MiniAppByOpenIdAuthenticationProvider(UserDetailsService userDetailsService)
    {
        setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        MiniAppByOpenIdAuthenticationToken miniAppAuthenticationToken = (MiniAppByOpenIdAuthenticationToken) authentication;
        //小程序openId
        String openId = (String) authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(openId);
        MiniAppByOpenIdAuthenticationToken authenticationResult = new MiniAppByOpenIdAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationResult.setDetails(authentication.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return MiniAppByOpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService()
    {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService)
    {
        this.userDetailsService = userDetailsService;
    }
}
