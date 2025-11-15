package com.ruoyi.framework.jssms.domain;

import lombok.Data;

/**
 * 短信发送响应
 */
@Data
public class JsSmsSendResponse {

    /**
     * 状态码
     */
    private String status;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 短信ID
     */
    private String smsid;

    /**
     * 短信拆分条数
     */
    private String numberOfSplits;

    /**
     * 短信字节长度
     */
    private String smsLength;

    /**
     * 模板余量
     */
    private String templateBalanceNum;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 短信内容
     */
    private String messageInfo;
}