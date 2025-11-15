package com.ruoyi.framework.sms;

import lombok.Data;

@Data
public class SmsSendDTO {
    // AJ-Captcha 自带的字段
    private String captchaVerification;
    // 我们自己需要的数据
    private String phoneNumber;
}