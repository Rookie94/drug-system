package com.ruoyi.framework.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * redis配置
 *
 * @author ruoyi
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport
{
    @Autowired
    private RedisParam redisParam;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .commandTimeout(parseDuration(redisParam.getTimeout()))
                .poolConfig(getLettucePoolConfig())
                .build();

        if ("cluster".equalsIgnoreCase(redisParam.getMode())) {
            // 集群模式
            RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();

            // 解析集群节点
            if (redisParam.getCluster() != null && redisParam.getCluster().getNodes() != null) {
                for (String node : redisParam.getCluster().getNodes()) {
                    String[] parts = node.split(":");
                    if (parts.length == 2) {
                        clusterConfig.addClusterNode(new RedisNode(parts[0], Integer.parseInt(parts[1])));
                    }
                }
            }

            // 设置密码
            if (redisParam.getPassword() != null && !redisParam.getPassword().isEmpty()) {
                clusterConfig.setPassword(redisParam.getPassword());
            }

            return new LettuceConnectionFactory(clusterConfig, clientConfig);
        } else {
            // 单机模式
            RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
            standaloneConfig.setHostName(redisParam.getHost());
            standaloneConfig.setPort(redisParam.getPort());
            standaloneConfig.setDatabase(redisParam.getDatabase());

            if (redisParam.getPassword() != null && !redisParam.getPassword().isEmpty()) {
                standaloneConfig.setPassword(redisParam.getPassword());
            }

            return new LettuceConnectionFactory(standaloneConfig, clientConfig);
        }
    }

    private org.apache.commons.pool2.impl.GenericObjectPoolConfig<Object> getLettucePoolConfig() {
        org.apache.commons.pool2.impl.GenericObjectPoolConfig<Object> poolConfig =
                new org.apache.commons.pool2.impl.GenericObjectPoolConfig<>();

        RedisParam.Pool pool = redisParam.getLettuce();
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMaxTotal(pool.getMaxActive());

        // 解析最大等待时间
        Duration maxWaitDuration = parseDuration(pool.getMaxWait());
        poolConfig.setMaxWaitMillis(maxWaitDuration.toMillis());

        return poolConfig;
    }

    /**
     * 解析Spring Boot格式的时间字符串为Duration
     * 支持格式: "10s", "1m", "2h", "500ms", "-1ms"等
     */
    private Duration parseDuration(String durationStr) {
        if (durationStr == null || durationStr.trim().isEmpty()) {
            return Duration.ZERO;
        }

        String str = durationStr.trim().toLowerCase();

        try {
            // 处理负值（如"-1ms"表示无限等待）
            if (str.startsWith("-")) {
                String positiveStr = str.substring(1);
                Duration positiveDuration = parsePositiveDuration(positiveStr);
                return Duration.ofMillis(-positiveDuration.toMillis());
            }

            return parsePositiveDuration(str);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid duration format: " + durationStr, e);
        }
    }

    private Duration parsePositiveDuration(String str) {
        if (str.endsWith("ms")) {
            long millis = Long.parseLong(str.substring(0, str.length() - 2));
            return Duration.ofMillis(millis);
        } else if (str.endsWith("s")) {
            long seconds = Long.parseLong(str.substring(0, str.length() - 1));
            return Duration.ofSeconds(seconds);
        } else if (str.endsWith("m")) {
            long minutes = Long.parseLong(str.substring(0, str.length() - 1));
            return Duration.ofMinutes(minutes);
        } else if (str.endsWith("h")) {
            long hours = Long.parseLong(str.substring(0, str.length() - 1));
            return Duration.ofHours(hours);
        } else if (str.endsWith("d")) {
            long days = Long.parseLong(str.substring(0, str.length() - 1));
            return Duration.ofDays(days);
        } else {
            // 默认按毫秒处理
            return Duration.ofMillis(Long.parseLong(str));
        }
    }

    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public DefaultRedisScript<Long> limitScript()
    {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 限流脚本
     */
    private String limitScriptText()
    {
        return "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local time = tonumber(ARGV[2])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', key, time)\n" +
                "end\n" +
                "return tonumber(current);";
    }
}