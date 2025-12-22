package com.ruoyi.common.utils.file;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.http.HttpMethodName;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

public class ZosV4Signer {

    /**
     * 对 HttpPut 进行 AWS SigV4 签名，直接修改请求头
     *
     * @param request   已组装好的 PUT 请求（含 resource path）
     * @param endPoint  内网 endpoint，例如 http://100.86.2.1:80
     * @param region    固定 cn（ZOS 要求）
     * @param service   固定 s3
     * @param ak        access-key
     * @param sk        明文 secret-key
     */
    public static void sign(HttpRequestBase request,
                            String endPoint,
                            String region,
                            String service,
                            String ak,
                            String sk) {
        AWSCredentials cred = new BasicAWSCredentials(ak, sk);
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(service);
        signer.setRegionName(region);

        // 构造 SDK 用的 SignableRequest 适配器
        ZosSignableRequestAdapter signable = new ZosSignableRequestAdapter(request, endPoint);
        signer.sign(signable, cred);
    }
}