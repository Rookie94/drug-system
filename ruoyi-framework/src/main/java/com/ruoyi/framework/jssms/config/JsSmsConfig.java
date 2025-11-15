package com.ruoyi.framework.jssms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信平台 jssms 配置映射
 */
@Data
@Component
@ConfigurationProperties(prefix = "jssms")
public class JsSmsConfig {

    /** 请求地址 */
    private Api api = new Api();

    /** 第三方标识 */
    private String third;

    /** 第三方密钥 */
    private String thirdkey;

    /** RSA 公钥 */
    private String publicKey;

    /** 模板 ID */
    private String templateId;

    @Data
    public static class Api {
        private String url;
    }
}
