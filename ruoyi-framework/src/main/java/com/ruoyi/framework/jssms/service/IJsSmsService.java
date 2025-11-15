package com.ruoyi.framework.jssms.service;

import com.ruoyi.framework.jssms.domain.JsSmsSendRequest;
import com.ruoyi.framework.jssms.domain.JsSmsSendResponse;

/**
 * 短信服务接口
 */
public interface IJsSmsService {

    /**
     * 发送模板短信
     */
    JsSmsSendResponse sendTemplateSms(JsSmsSendRequest request);

    /**
     * 发送手机验证方法
     *
     * @param phoneNumber
     * @return
     */
    JsSmsSendResponse sendVerificationCode(String phoneNumber);

}