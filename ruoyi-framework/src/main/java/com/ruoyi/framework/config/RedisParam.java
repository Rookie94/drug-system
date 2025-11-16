package com.ruoyi.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisParam {

    /** single 或 cluster */
    private String mode = "single";

    /** 单节点主机 */
    private String host = "127.0.0.1";

    /** 单节点端口 */
    private int port = 6379;

    /** 数据库索引 */
    private int database = 0;

    /** 密码 */
    private String password = "";

    /** 连接超时 */
    private String timeout = "10s";

    /** 集群节点列表 */
    private Cluster cluster;

    /** 连接池参数 */
    private Pool lettuce = new Pool();

    @Data
    public static class Cluster {
        private List<String> nodes;
    }

    @Data
    public static class Pool {
        private int minIdle = 0;
        private int maxIdle = 8;
        private int maxActive = 8;
        private String maxWait = "-1ms";
    }
}