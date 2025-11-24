package com.ruoyi.cms.external.domain;

import java.util.Date;

public class MidCarePerson {
    private String id;
    private String name;
    private String sex;
    private Date birthday;
    private Date intoTime;
    private Date outTime;
    private String mobileNumber;
    private String idNumber;
    private String orgName;
    private String orgCode;
    private String orgId;
    private String address;
    private Date createTime;
    private Date updateTime;
    private String remark;

    // getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }

    public Date getIntoTime() { return intoTime; }
    public void setIntoTime(Date intoTime) { this.intoTime = intoTime; }

    public Date getOutTime() { return outTime; }
    public void setOutTime(Date outTime) { this.outTime = outTime; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }

    public String getOrgCode() { return orgCode; }
    public void setOrgCode(String orgCode) { this.orgCode = orgCode; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}