package com.ruoyi.cms.external.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * API配置类
 */
@Component
@ConfigurationProperties(prefix = "external.config")
public class ExternalApiConfig {
    private String baseUrl;
    private String secretKey;
    private Map<String, String> interfaces;

    // 静态常量定义
    private static final Set<String> PARAM_SIGN_INTERFACES;
    private static final Set<String> NO_PARAM_SIGN_INTERFACES;

    static {
        // 初始化需要参数值签名的接口
        Set<String> paramSet = new HashSet<>();
        paramSet.add("policeverify");
        paramSet.add("archivesverify");
        PARAM_SIGN_INTERFACES = Collections.unmodifiableSet(paramSet);

        // 初始化只需要接口名称签名的接口
        Set<String> noParamSet = new HashSet<>();
        noParamSet.add("getorgdata");
        noParamSet.add("allpoliceinfo");
        noParamSet.add("allarchivesinfo");
        NO_PARAM_SIGN_INTERFACES = Collections.unmodifiableSet(noParamSet);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Map<String, String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Map<String, String> interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * 需要参数值签名的接口列表
     */
    public Set<String> getParamSignInterfaces() {
        return PARAM_SIGN_INTERFACES;
    }

    /**
     * 只需要接口名称签名的接口列表
     */
    public Set<String> getNoParamSignInterfaces() {
        return NO_PARAM_SIGN_INTERFACES;
    }

    /**
     * 验证接口名称是否有效
     */
    public boolean isValidInterface(String interfaceName) {
        String interfaceNameLower = interfaceName.toLowerCase();
        return PARAM_SIGN_INTERFACES.contains(interfaceNameLower) ||
                NO_PARAM_SIGN_INTERFACES.contains(interfaceNameLower);
    }
}