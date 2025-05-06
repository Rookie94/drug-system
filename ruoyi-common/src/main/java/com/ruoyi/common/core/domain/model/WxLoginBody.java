package com.ruoyi.common.core.domain.model;

public class WxLoginBody {

    /** 小程序Id*/
    private Long id;

    /** 临时登录凭证 code 只能使用一次 */
    private String code;

    //后端获取手机号的code
    private String phoneCode;

    /** 偏移量 */
    private String encryptedIv;

    /** 加密数据 */
    private String encryptedData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getEncryptedIv() {
        return encryptedIv;
    }

    public void setEncryptedIv(String encryptedIv) {
        this.encryptedIv = encryptedIv;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    @Override
    public String toString() {
        return "WxLoginBody{" +
                "id='" + id.toString() + '\'' +
                "code='" + code + '\'' +
                "pCode='" + phoneCode + '\'' +
                ", encryptedIv='" + encryptedIv + '\'' +
                ", encryptedData='" + encryptedData + '\'' +
                '}';
    }
}