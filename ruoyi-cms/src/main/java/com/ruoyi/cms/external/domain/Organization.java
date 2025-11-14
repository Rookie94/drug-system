package com.ruoyi.cms.external.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.List;

/**
 * 组织机构实体
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Id")
    private String id;

    @JsonProperty("OrgName")
    private String orgName;

    @JsonProperty("OrgCode")
    private String orgCode;

    @JsonProperty("ParentId")
    private String parentId;

    @JsonProperty("Level")
    private Integer levels;

    @JsonProperty("Children")
    private List<Organization> children;

    // 必须有无参构造函数
    public Organization() {
    }

    // 添加 getter 和 setter
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

    public List<Organization> getChildren() {
        return children;
    }

    public void setChildren(List<Organization> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id='" + id + '\'' +
                ", orgName='" + orgName + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", parentId='" + parentId + '\'' +
                ", levels=" + levels +
                ", children=" + (children != null ? children.size() : 0) +
                '}';
    }
}