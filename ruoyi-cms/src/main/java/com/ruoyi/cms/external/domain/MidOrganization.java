package com.ruoyi.cms.external.domain;

public class MidOrganization {

    private String id;
    private String orgName;
    private String orgCode;
    private String parentId;
    private Integer levels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }

    @Override
    public String toString() {
        return "MidOrganization{" +
                "id='" + id + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", parentId='" + parentId + '\'' +
                ", levels=" + levels +
                '}';
    }

}