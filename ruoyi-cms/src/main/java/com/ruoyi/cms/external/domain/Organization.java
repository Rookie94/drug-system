package com.ruoyi.cms.external.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import java.util.List;

/**
 * 组织机构实体
 */
public class Organization extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orgName;
    private String orgCode;
    private String parentId;
    private Integer levels;
    private List<Organization> children;

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
}