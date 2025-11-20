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

    /** 角色id */
    private String roleId;

    //用户填写的手机号
    private String phoneNumber;
    //身份证号
    private String idCardNumber;

    private String nickName;

    private String Sex;

    private String birthDay;

    private String provinceId;

    private String cityId;

    private String areaId;

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

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Override
    public String toString() {
        return "WxLoginBody{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                ", encryptedIv='" + encryptedIv + '\'' +
                ", encryptedData='" + encryptedData + '\'' +
                ", roleId='" + roleId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", nickName='" + nickName + '\'' +
                ", Sex='" + Sex + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", provinceId='" + provinceId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", areaId='" + areaId + '\'' +
                '}';
    }

}