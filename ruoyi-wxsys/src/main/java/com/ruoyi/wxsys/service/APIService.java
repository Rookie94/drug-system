package com.ruoyi.wxsys.service;

import com.ruoyi.wxsys.manager.WxMaServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class APIService
{

    @Autowired
    private IMiniAppService miniAppService;

    @Autowired
    private IMiniAppUserService miniAppUserService;

    @Autowired
    private WxMaServiceManager wxMaServiceManager;

}
