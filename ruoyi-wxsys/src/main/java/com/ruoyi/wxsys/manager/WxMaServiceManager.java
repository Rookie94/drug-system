package com.ruoyi.wxsys.manager;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisConfigImpl;
import com.ruoyi.common.core.domain.entity.MiniApp;
import com.ruoyi.wxsys.properties.WxProperties;
import com.ruoyi.wxsys.service.IMiniAppService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxProperties.class)
public class WxMaServiceManager
{
    @Autowired
    private WxProperties wxProperties;

    @Autowired
    private IMiniAppService miniAppService;

    private final Map<String, WxMaService> wxMaServiceMap = new HashMap<>();

    @PostConstruct
    public void init()
    {
        List<MiniApp> miniAppList = miniAppService.selectMiniAppList(new MiniApp());
        for (MiniApp miniApp : miniAppList)
        {
            try
            {
                WxMaDefaultConfigImpl configStorage;
                if(wxProperties.getUseRedis())
                {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    JedisPool jedisPool = new JedisPool(poolConfig, wxProperties.getJedisConfig().getHost(),
                            wxProperties.getJedisConfig().getPort(),wxProperties.getJedisConfig().getTimeout(),
                            ObjectUtils.isEmpty(wxProperties.getJedisConfig().getPassword()) ? null : wxProperties.getJedisConfig().getPassword(),
                            wxProperties.getJedisConfig().getDatabase());
                    configStorage = new WxMaRedisConfigImpl(jedisPool);
                }
                else
                {
                    configStorage = new WxMaDefaultConfigImpl();
                }
                configStorage.setAppid(miniApp.getAppid());
                configStorage.setSecret(miniApp.getSecret());
                configStorage.setToken(miniApp.getToken());
                configStorage.setAesKey(miniApp.getAesKey());
                WxMaService wxMaService = new WxMaServiceImpl();
                wxMaService.setWxMaConfig(configStorage);
                wxMaServiceMap.put(miniApp.getAppid(), wxMaService);
                log.info("小程序【{}】初始化成功({}),appid:{}", miniApp.getName(), wxProperties.getUseRedis() ? "redis" : "内存", miniApp.getAppid());
            }
            catch (Exception e)
            {
                log.info("小程序【{}】初始化发生了未知异常,appid:{}:{},异常信息：", miniApp.getName(), miniApp.getAppid(), e);
            }
        }
    }

    public WxMaService getWxMaService(String appId)
    {
        return wxMaServiceMap.get(appId);
    }

}
