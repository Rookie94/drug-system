package com.ruoyi.common.config;

import com.obs.services.ObsClient;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Minio 配置信息
 *
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "huawei")
public class HwObsConfig {

    /**
     * 代理地址
     */
    private static String proxyUrl;

    /**
     * 服务地址
     */
    private static String url;

    /**
     * 用户名
     */
    private static String accessKey;

    /**
     * 密码
     */
    private static String secretKey;

    /**
     * 存储桶名称
     */
    private static String bucketName;

    /**
     * 是否启用
     */
    private static boolean useEnable;

    public static String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        HwObsConfig.proxyUrl = proxyUrl;
    }

    public static String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        HwObsConfig.url = url;
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        HwObsConfig.accessKey = accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        HwObsConfig.secretKey = secretKey;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        HwObsConfig.bucketName = bucketName;
    }

    public static boolean isUseEnable() {
        return useEnable;
    }

    public void setUseEnable(boolean useEnable) {
        HwObsConfig.useEnable = useEnable;
    }

    public static ObsClient getObsClient() {
        try {
            if (useEnable) {
                ObsClient obsClient = new ObsClient(accessKey, secretKey,url);
                if(!obsClient.headBucket(bucketName)){
                    //
                }
                return obsClient;
            }
            else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

}
