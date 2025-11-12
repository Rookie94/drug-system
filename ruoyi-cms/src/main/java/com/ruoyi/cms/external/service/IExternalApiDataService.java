package com.ruoyi.cms.external.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.cms.external.domain.CarePerson;
import com.ruoyi.cms.external.domain.Organization;
import com.ruoyi.cms.external.domain.Police;
import java.util.List;

/**
 * 后续照管平台数据服务接口
 */
public interface IExternalApiDataService {

    // 组织机构相关
    AjaxResult syncOrganizations();

    AjaxResult syncPolice();
    AjaxResult syncCarePersons();
    AjaxResult syncCarePersonsBatch();

    // 警员相关
    AjaxResult verifyPolice(String mobileNumber);
    // 照管人员相关
    AjaxResult verifyCarePerson(String idNumber);


    // 数据查询
    List<Organization> getOrganizationList(Organization organization);
    List<Police> getPoliceList(Police police);
    List<CarePerson> getCarePersonList(CarePerson carePerson);

}