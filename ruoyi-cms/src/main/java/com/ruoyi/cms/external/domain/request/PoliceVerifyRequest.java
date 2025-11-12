package com.ruoyi.cms.external.domain.request;

import javax.validation.constraints.NotBlank;

/**
 * 警察注册校验请求参数
 */
public class PoliceVerifyRequest {
    @NotBlank(message = "手机号码不能为空")
    private String MobileNumber;

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }
}