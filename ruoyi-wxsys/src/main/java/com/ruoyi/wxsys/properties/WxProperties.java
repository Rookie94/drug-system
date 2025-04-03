package com.ruoyi.wxsys.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxProperties
{
    private Boolean useRedis = false;

    private JedisConfig jedisConfig;

    @Data
    public static class JedisConfig
    {
        /**
         * redis服务器 主机地址
         */
        private String host;

        /**
         * redis服务器 端口号
         */
        private Integer port;

        /**
         * redis服务器 密码
         */
        private String password;

        /**
         * redis 服务连接超时时间
         */
        private Integer timeout;

        /**
         * redis使用的库
         */
        private Integer database;


    }
}
