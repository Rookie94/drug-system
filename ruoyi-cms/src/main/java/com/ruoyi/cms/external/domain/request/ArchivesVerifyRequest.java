package com.ruoyi.cms.external.domain.request;

import javax.validation.constraints.NotBlank;

/**
 * 出所人员注册校验请求参数
 */
public class ArchivesVerifyRequest {
    @NotBlank(message = "身份证号不能为空")
    private String IDNumber;

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }
}