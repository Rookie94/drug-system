package com.ruoyi.cms.external.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 照管人员实体
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarePerson extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Sex")
    private String sex;

    @JsonProperty("Birthday")
    private String birthday;

    @JsonProperty("IntoTime")
    private String intoTime;

    @JsonProperty("OutTime")
    private String outTime;

    @JsonProperty("MobileNumber")
    private String mobileNumber;

    @JsonProperty("IDNumber")
    private String idNumber;

    @JsonProperty("OrgName")
    private String orgName;

    @JsonProperty("OrgCode")
    private String orgCode;

    @JsonProperty("OrgId")
    private String orgId;

    @JsonProperty("Address")
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIntoTime() {
        return intoTime;
    }

    public void setIntoTime(String intoTime) {
        this.intoTime = intoTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CarePerson{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", intoTime='" + intoTime + '\'' +
                ", outTime='" + outTime + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgId='" + orgId + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}