package com.ruoyi.framework.jssms.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.jssms.config.JsSmsConfig;
import com.ruoyi.framework.jssms.domain.JsSmsSendRequest;
import com.ruoyi.framework.jssms.domain.JsSmsSendResponse;
import com.ruoyi.framework.jssms.service.IJsSmsService;
import com.ruoyi.framework.jssms.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 短信服务实现 - 修复异常处理
 */
@Slf4j
@Service
public class JsSmsServiceImpl implements IJsSmsService {

//    @Value("${jssms.api.url}")
//    private String smsApiUrl;
//
//    @Value("${jssms.third}")
//    private String third;
//
//    @Value("${jssms.thirdkey}")
//    private String thirdkey;
//
//    @Value("${jssms.publicKey}")
//    private String publicKey;
//
//    @Value("${jssms.templateId}")
//    private String templateId;

    @Autowired
    private JsSmsConfig jsSmsConfig;

    // 缓存公钥对象，避免重复解析
    private RSAPublicKey cachedPublicKey;
    private final Object lock = new Object();

    private final RestTemplate restTemplate;

    @Autowired
    private RedisCache redisCache;

    public JsSmsServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 获取公钥（带缓存和异常处理）
     */
    private RSAPublicKey getPublicKey() {
        if (cachedPublicKey == null) {
            synchronized (lock) {
                if (cachedPublicKey == null) {
                    try {
                        log.info("初始化RSA公钥...");
                        cachedPublicKey = RSAUtils.getPublicKey(jsSmsConfig.getPublicKey());
                        log.info("RSA公钥初始化完成");
                    } catch (Exception e) {
                        log.error("RSA公钥初始化失败，请检查公钥配置是否正确", e);
                        throw new RuntimeException("RSA公钥初始化失败: " + e.getMessage(), e);
                    }
                }
            }
        }
        return cachedPublicKey;
    }

    @Override
    public JsSmsSendResponse sendTemplateSms(JsSmsSendRequest request) {
        try {
            // 预先验证公钥
            RSAPublicKey publicKeyObj = getPublicKey();

            // 1. 构建请求参数
            Map<String, Object> params = buildSmsParams(request);

            // 2. 生成签名和请求头
            HttpHeaders headers = generateHeaders(publicKeyObj);

            // 3. 构建请求体
            Map<String, Object> requestBody = buildRequestBody(params, publicKeyObj);

            // 4. 发送请求
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    jsSmsConfig.getApi().getUrl(), HttpMethod.POST, entity, String.class);

            // 5. 解析响应
            if (response.getStatusCode() == HttpStatus.OK) {
                JsSmsSendResponse smsResponse = JSON.parseObject(response.getBody(), JsSmsSendResponse.class);
                log.info("短信发送成功：{}", smsResponse);
                return smsResponse;
            } else {
                log.error("短信接口响应异常，状态码：{}", response.getStatusCode());
                throw new RuntimeException("短信接口响应异常：" + response.getStatusCode());
            }

        } catch (Exception e) {
            log.error("短信发送失败", e);
            throw new RuntimeException("短信发送失败：" + e.getMessage());
        }
    }

    /**
     * 发送手机验证方法
     *
     * @param phoneNumber
     * @return
     */
    public JsSmsSendResponse sendVerificationCode(String phoneNumber) {
        JsSmsSendRequest request=new JsSmsSendRequest();
        //验证码
        String code = this.generateAndStoreCode(phoneNumber);
        //手机号
        List<String> mobileList=new ArrayList<>();
        mobileList.add(phoneNumber);
        request.setMobileList(mobileList);
        //模板ID
        request.setTemplateId(jsSmsConfig.getTemplateId());
        //参数
        List<String> paramList=new ArrayList<>();
        paramList.add(code);
        paramList.add("1");
        request.setParamList(paramList);
        return this.sendTemplateSms(request);
    }

    /**
     * 生成6位随机验证码并存入Redis
     * <br>
     * <b>默认5分钟过期</b>
     *
     * @param phoneNumber 手机号
     * @return 生成的验证码
     */
    private String generateAndStoreCode(String phoneNumber) {
        //由于短信验证码不通,默认888888测试
        int code = ThreadLocalRandom.current().nextInt(1000, 9999);
        //int code=8888;
        String codeStr = String.valueOf(code);
        String key = CacheConstants.CAPTCHA_PHONE_CODE_KEY + phoneNumber;
        redisCache.setCacheObject(key, codeStr, 1, TimeUnit.MINUTES);
        return codeStr;
    }

    /**
     * 构建短信参数
     */
    private Map<String, Object> buildSmsParams(JsSmsSendRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("third", jsSmsConfig.getThird());
        params.put("thirdkey", jsSmsConfig.getThirdkey());
        params.put("mobileList", request.getMobileList());
        params.put("templateId", request.getTemplateId());
        params.put("paramList", request.getParamList());

        if (StringUtils.isNotEmpty(request.getExicode())) {
            params.put("exicode", request.getExicode());
        }

        return params;
    }

    /**
     * 生成请求头（包含签名）
     */
    private HttpHeaders generateHeaders(RSAPublicKey publicKeyObj) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = generateNonce();
        String signature = generateSignature(timestamp, nonce, publicKeyObj);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Third", jsSmsConfig.getThird());
        headers.set("X-Timestamp", timestamp);
        headers.set("X-Nonce", nonce);
        headers.set("X-Signature", signature);

        return headers;
    }

    /**
     * 生成Nonce（13位毫秒级时间戳+UUID前8位）
     */
    private String generateNonce() {
        String millis = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return millis + uuid;
    }

    /**
     * 生成签名
     */
    private String generateSignature(String timestamp, String nonce, RSAPublicKey publicKeyObj) {
        try {
            // 按照ASCII码排序的参数
            Map<String, String> signParams = new TreeMap<>();
            signParams.put("nonce", nonce);
            signParams.put("third", jsSmsConfig.getThird());
            signParams.put("thirdkey", jsSmsConfig.getThirdkey());
            signParams.put("timestamp", timestamp);

            // 构建签名字符串
            StringBuilder signBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : signParams.entrySet()) {
                if (signBuilder.length() > 0) {
                    signBuilder.append("&");
                }
                signBuilder.append(entry.getKey()).append("=").append(entry.getValue());
            }

            String signString = signBuilder.toString();
            log.debug("签名字符串：{}", signString);

            // 使用RSA加密
            return RSAUtils.publicEncrypt(signString, publicKeyObj);
        } catch (Exception e) {
            log.error("生成签名失败", e);
            throw new RuntimeException("生成签名失败: " + e.getMessage());
        }
    }

    /**
     * 构建请求体
     */
    private Map<String, Object> buildRequestBody(Map<String, Object> params, RSAPublicKey publicKeyObj) {
        try {
            // 加密参数
            String jsonParams = JSON.toJSONString(params);
            String encryptedData = RSAUtils.publicEncrypt(jsonParams, publicKeyObj);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("data", encryptedData);
            requestBody.put("third", jsSmsConfig.getThird());

            return requestBody;
        } catch (Exception e) {
            log.error("构建请求体失败", e);
            throw new RuntimeException("构建请求体失败: " + e.getMessage());
        }
    }
}