package com.ruoyi.common.utils;

//import com.aliyun.oss.HttpMethod;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;

@Component
public class OSSUtils
{
    /*

    @Value("${oss.endpoint}")
    private String endpoint;

    @Value("${oss.accessKeyId}")
    private String accessKeyId;

    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${oss.bucket}")
    public String bucket;
    
    private OSS client;


    public String getEndpoint()
    {
        return endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId()
    {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId)
    {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret()
    {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret)
    {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucket()
    {
        return bucket;
    }

    public void setBucket(String bucket)
    {
        this.bucket = bucket;
    }

    public OSS getClient()
    {
        return client;
    }

    public void setClient(OSS client)
    {
        this.client = client;
    }

    static
    {
    }

    @PostConstruct
    public void init()
    {
        client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
    
    public Boolean upload(String objPath, byte[] data)
    {
        PutObjectRequest request = new PutObjectRequest(bucket, objPath, new ByteArrayInputStream(data));
        request.setProcess("true");
        PutObjectResult result=client.putObject(request);
        return result.getResponse().getStatusCode() == 200;
    }
    */

    public String getUrl(String filePath,Boolean isPublic)
    {
        String url = "";
        /*
        if(isPublic)
        {
            url = "https://" + bucket + "." + endpoint + "/" + filePath;
        }
        else
        {

            // 指定生成的签名URL过期时间，单位为毫秒。本示例以设置过期时间为1小时为例。
            Date expiration = new Date(new Date().getTime() + 3600 * 1000L * 24 * 30);

            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, filePath, HttpMethod.GET);
            // 设置过期时间。
            request.setExpiration(expiration);
            url = client.generatePresignedUrl(request).toString();
        }
         */
        return url;
    }



}
