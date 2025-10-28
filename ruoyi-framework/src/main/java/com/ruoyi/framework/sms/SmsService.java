package com.ruoyi.framework.sms;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @className: SmsService
 * @author: admin
 * @date: 2025/5/21 17:57
 * @Version: 1.0
 */

@Slf4j
@Service
public class SmsService {
    /**
     * 短信服务提供商
     * {@value CONFIG_ID}
     */
    private static final String CONFIG_ID = "alibaba";

    @Autowired
    private RedisCache redisCache;

    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param message
     * @return
     */
    public boolean sendSms(String phoneNumber, String message) {
        SmsResponse smsResponse = SmsFactory.getSmsBlend(CONFIG_ID).sendMessage(phoneNumber, message);
        boolean beSent = smsResponse.isSuccess();
        if (!beSent) {
            log.info("短信服务商错误响应原始消息体: {}", smsResponse.getData());
        }
        return beSent;
    }

    /**
     * 发送短信
     *
     * @param phoneNumber
     * @param messages
     * @return
     */
    public boolean sendSms(String phoneNumber, LinkedHashMap<String, String> messages) {
        SmsResponse smsResponse = SmsFactory.getSmsBlend(CONFIG_ID).sendMessage(phoneNumber, messages);
        boolean beSent = smsResponse.isSuccess();
        if (!beSent) {
            log.info("短信服务商错误响应原始消息体: {}", smsResponse.getData());
        }
        return smsResponse.isSuccess();
    }

    /**
     * 发送手机验证方法
     *
     * @param phoneNumber
     * @return
     */
    public boolean sendVerificationCode(String phoneNumber) {
        String code = this.generateAndStoreCode(phoneNumber);
        LinkedHashMap<String, String> messages = new LinkedHashMap<>();
        messages.put("code", code);
        return this.sendSms(phoneNumber, messages);
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
        int code = ThreadLocalRandom.current().nextInt(100000, 999999);
        String codeStr = String.valueOf(code);
        String key = CacheConstants.CAPTCHA_PHONE_CODE_KEY + phoneNumber;
        redisCache.setCacheObject(key, codeStr, 5, TimeUnit.MINUTES);
        return codeStr;
    }


}