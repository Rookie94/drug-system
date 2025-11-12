package com.ruoyi.cms.external.mapper;

import com.ruoyi.cms.external.domain.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 组织机构Mapper接口
 */
@Mapper
public interface OrganizationMapper {
    /**
     * 查询组织机构
     */
    Organization selectOrganizationById(String id);

    /**
     * 查询组织机构列表
     */
    List<Organization> selectOrganizationList(Organization organization);

    /**
     * 新增组织机构
     */
    int insertOrganization(Organization organization);

    /**
     * 修改组织机构
     */
    int updateOrganization(Organization organization);

    /**
     * 删除组织机构
     */
    int deleteOrganizationById(String id);

    /**
     * 批量删除组织机构
     */
    int deleteOrganizationByIds(String[] ids);

    /**
     * 根据机构代码查询
     */
    Organization selectOrganizationByCode(String orgCode);

    /**
     * 查询子机构列表
     */
    List<Organization> selectChildrenOrganization(String parentId);
}