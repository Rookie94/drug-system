package com.ruoyi.cms.external.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.cms.external.utils.ExternalApiHttpClient;
import com.ruoyi.cms.external.domain.CarePerson;
import com.ruoyi.cms.external.domain.Organization;
import com.ruoyi.cms.external.domain.Police;
import com.ruoyi.cms.external.domain.request.ArchivesVerifyRequest;
import com.ruoyi.cms.external.domain.request.PoliceVerifyRequest;
import com.ruoyi.cms.external.domain.response.ExternalApiResponse;
import com.ruoyi.cms.external.domain.response.ExternalPageResponse;
import com.ruoyi.cms.external.mapper.CarePersonMapper;
import com.ruoyi.cms.external.mapper.OrganizationMapper;
import com.ruoyi.cms.external.mapper.PoliceMapper;
import com.ruoyi.cms.external.service.IExternalApiDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 后续照管平台数据服务实现
 */
@Service
public class ExternalApiDataServiceImpl implements IExternalApiDataService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiDataServiceImpl.class);

    @Autowired
    private ExternalApiHttpClient apiHttpClient;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private PoliceMapper policeMapper;

    @Autowired
    private CarePersonMapper carePersonMapper;

    /**
     * 1. 获取组织机构信息接口 - 全量同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult syncOrganizations() {
        List<Organization> orgList = null;
        try {
            log.info("开始全量同步组织机构信息");

            // 第一步：先获取数据，不进行任何数据操作
            ExternalApiResponse<List<Organization>> response = apiHttpClient.doGetForList("GetOrgData", null, Organization.class);

            if (!response.isSuccess()) {
                log.error("获取组织机构失败: {}", response.getMsg());
                return AjaxResult.error("获取组织机构失败: " + response.getMsg());
            }

            orgList = response.getDecodedData();
            if (orgList == null || orgList.isEmpty()) {
                log.info("未获取到组织机构数据，保持现有数据不变");
                return AjaxResult.error("未获取到组织机构数据，保持现有数据不变");
            }

            log.info("成功获取到 {} 个组织机构数据，开始同步", orgList.size());

            // 第二步：只有在成功获取数据后才进行数据操作
            // 清空组织机构表
            int deletedCount = organizationMapper.deleteAllOrganizations();
            log.info("清空组织机构表，删除 {} 条记录", deletedCount);

            // 处理组织机构数据
            int savedCount = 0;
            for (Organization org : orgList) {
                savedCount = saveOrganizationRecursive(org, savedCount);
            }

            log.info("组织机构全量同步完成，共处理 {} 个组织", savedCount);
            return AjaxResult.success("组织机构全量同步成功，共处理 " + savedCount + " 个组织");

        } catch (Exception e) {
            log.error("获取组织机构信息异常", e);

            // 如果已经清空了数据但后续处理失败，记录告警
            if (orgList != null) {
                log.error("组织机构同步过程中发生异常，可能导致数据不一致。已获取数据量: {}", orgList.size());
                // 这里可以发送告警通知管理员
            }

            return AjaxResult.error("组织机构同步异常: " + e.getMessage());
        }
    }

    /**
     * 2. 警察注册校验接口 - 只做校验，不写入数据库
     */
    @Override
    public AjaxResult verifyPolice(String mobileNumber) {
        try {
            log.info("开始警察注册校验，手机号: {}", mobileNumber);

            PoliceVerifyRequest request = new PoliceVerifyRequest();
            request.setMobileNumber(mobileNumber);

            // 直接返回验证结果，不保存到数据库
            ExternalApiResponse<List<Police>> response = apiHttpClient.doPostForList("PoliceVerify", request, Police.class);

            if (response.isSuccess()) {
                List<Police> policeList = response.getDecodedData();
                if (policeList != null && !policeList.isEmpty()) {
                    log.info("警察注册校验成功，验证通过 {} 人", policeList.size());
                    // 只返回验证结果，不保存数据
                    return AjaxResult.success("验证成功", policeList);
                } else {
                    log.warn("警察注册校验成功但未返回数据");
                    return AjaxResult.error("未找到对应的警员信息");
                }
            } else {
                log.error("警察注册校验失败: {}", response.getMsg());
                return AjaxResult.error("验证失败: " + response.getMsg());
            }
        } catch (Exception e) {
            log.error("警察注册校验异常", e);
            return AjaxResult.error("验证异常: " + e.getMessage());
        }
    }

    /**
     * 3. 出所人员注册校验接口 - 只做校验，不写入数据库
     */
    @Override
    public AjaxResult verifyCarePerson(String idNumber) {
        try {
            log.info("开始照管人员注册校验，身份证号: {}", idNumber);

            ArchivesVerifyRequest request = new ArchivesVerifyRequest();
            request.setIDNumber(idNumber);

            // 直接返回验证结果，不保存到数据库
            ExternalApiResponse<List<CarePerson>> response = apiHttpClient.doPostForList("ArchivesVerify", request, CarePerson.class);

            if (response.isSuccess()) {
                List<CarePerson> carePersonList = response.getDecodedData();
                if (carePersonList != null && !carePersonList.isEmpty()) {
                    log.info("照管人员注册校验成功，验证通过 {} 人", carePersonList.size());
                    // 只返回验证结果，不保存数据
                    return AjaxResult.success("验证成功", carePersonList);
                } else {
                    log.warn("照管人员注册校验成功但未返回数据");
                    return AjaxResult.error("未找到对应的照管人员信息");
                }
            } else {
                log.error("照管人员注册校验失败: {}", response.getMsg());
                return AjaxResult.error("验证失败: " + response.getMsg());
            }
        } catch (Exception e) {
            log.error("照管人员注册校验异常", e);
            return AjaxResult.error("验证异常: " + e.getMessage());
        }
    }

    /**
     * 4. 获取在册警员列表接口 - 全量同步
     */
    /**
     * 4. 获取在册警员列表接口 - 全量同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult syncPolice() {
        List<Police> policeList = null;
        try {
            log.info("开始全量同步在册警员列表");

            // 第一步：先获取数据，不进行任何数据操作
            ExternalApiResponse<List<Police>> response = apiHttpClient.doGetForList("AllPoliceInfo", null, Police.class);

            if (!response.isSuccess()) {
                log.error("获取警员列表失败: {}", response.getMsg());
                return AjaxResult.error("获取警员列表失败: " + response.getMsg());
            }

            policeList = response.getDecodedData();
            if (policeList == null || policeList.isEmpty()) {
                log.info("未获取到警员数据，保持现有数据不变");
                return AjaxResult.error("未获取到警员数据，保持现有数据不变");
            }

            log.info("成功获取到 {} 条警员数据，开始同步", policeList.size());

            // 第二步：只有在成功获取数据后才进行数据操作
            // 清空警员表
            int deletedCount = policeMapper.deleteAllPolice();
            log.info("清空警员表，删除 {} 条记录", deletedCount);

            // 处理警员数据
            int successCount = processPoliceBatch(policeList);

            String message = String.format("警员信息全量同步完成，总数: %d，成功: %d，失败: %d",
                    policeList.size(), successCount, policeList.size() - successCount);
            log.info(message);

            if (successCount < policeList.size()) {
                return AjaxResult.warn(message);
            } else {
                return AjaxResult.success(message);
            }
        } catch (Exception e) {
            log.error("警员信息同步异常", e);

            // 如果已经清空了数据但后续处理失败，这里可以记录告警或采取其他恢复措施
            if (policeList != null) {
                log.error("数据同步过程中发生异常，可能导致数据不一致。已获取数据量: {}", policeList.size());
                // 这里可以发送告警通知管理员
            }

            return AjaxResult.error("警员信息同步异常: " + e.getMessage());
        }
    }

    /**
     * 5. 获取在册照管人员列表接口 - 全量同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult syncCarePersons() {
        int pageIndex = 1;
        int pageSize = 500;
        int totalSaved = 0;
        boolean hasClearedData = false;

        try {
            log.info("开始全量同步在册照管人员列表");

            // 第一步：先获取第一页数据，确认接口可用
            Map<String, Object> firstPageParams = new HashMap<>();
            firstPageParams.put("pageIndex", pageIndex);
            firstPageParams.put("pageSize", pageSize);

            ExternalApiResponse<List<CarePerson>> firstResponse = apiHttpClient.doGetForList("AllArchivesInfo", firstPageParams, CarePerson.class);

            if (!firstResponse.isSuccess()) {
                // 修改：如果是"未查询到数据"，视为正常情况
                if ("未查询到数据".equals(firstResponse.getMsg())) {
                    log.info("第一页未查询到数据，保持现有数据不变");
                    return AjaxResult.success("未查询到照管人员数据，保持现有数据不变");
                }
                log.error("获取照管人员列表失败: {}", firstResponse.getMsg());
                return AjaxResult.error("获取照管人员列表失败: " + firstResponse.getMsg());
            }

            List<CarePerson> firstPageList = firstResponse.getDecodedData();
            if (firstPageList == null || firstPageList.isEmpty()) {
                log.info("第一页照管人员数据为空，保持现有数据不变");
                return AjaxResult.success("未获取到照管人员数据，保持现有数据不变");
            }

            log.info("成功获取到第一页数据，共 {} 条记录，开始同步", firstPageList.size());

            // 第二步：只有在确认接口可用后才清空数据
            int deletedCount = carePersonMapper.deleteAllCarePersons();
            hasClearedData = true;
            log.info("清空照管人员表，删除 {} 条记录", deletedCount);

            // 处理第一页数据
            totalSaved += processCarePersonPage(firstPageList);
            log.info("第1页处理完成，本页数量: {}，累计数量: {}", firstPageList.size(), totalSaved);

            // 第三步：继续获取后续页面数据
            pageIndex = 2;
            boolean hasMoreData = true;

            while (hasMoreData) {
                log.info("正在获取第{}页数据", pageIndex);

                Map<String, Object> pageParams = new HashMap<>();
                pageParams.put("pageIndex", pageIndex);
                pageParams.put("pageSize", pageSize);

                ExternalApiResponse<List<CarePerson>> response = apiHttpClient.doGetForList("AllArchivesInfo", pageParams, CarePerson.class);

                if (!response.isSuccess()) {
                    // 修改：如果是"未查询到数据"，视为正常结束
                    if ("未查询到数据".equals(response.getMsg())) {
                        log.info("第{}页未查询到数据，同步完成", pageIndex);
                        hasMoreData = false;
                        continue;
                    }
                    log.error("获取照管人员列表第{}页失败: {}", pageIndex, response.getMsg());
                    // 不再继续处理，抛出异常让事务回滚
                    throw new RuntimeException("获取第" + pageIndex + "页数据失败: " + response.getMsg());
                }

                List<CarePerson> pageList = response.getDecodedData();
                if (pageList != null && !pageList.isEmpty()) {
                    int pageSaved = processCarePersonPage(pageList);
                    totalSaved += pageSaved;
                    log.info("第{}页处理完成，本页数量: {}，成功保存: {}，累计数量: {}",
                            pageIndex, pageList.size(), pageSaved, totalSaved);

                    pageIndex++;

                    // 添加延迟，避免请求过快
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("同步过程被中断", e);
                    }
                } else {
                    log.info("第{}页数据为空，停止获取", pageIndex);
                    hasMoreData = false;
                }
            }

            String message = String.format("照管人员信息全量同步完成，共处理 %d 条记录", totalSaved);
            log.info(message);
            return AjaxResult.success(message);

        } catch (Exception e) {
            log.error("照管人员同步异常", e);

            // 如果已经清空了数据但后续处理失败，记录告警
            if (hasClearedData) {
                log.error("照管人员同步过程中发生异常，已清空数据但未完成同步，事务将回滚");
            }

            return AjaxResult.error("照管人员信息同步异常: " + e.getMessage());
        }
    }

    /**
     * 分批同步照管人员信息（可选方案）- 全量同步
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult syncCarePersonsBatch() {
        int pageSize = 500;
        int currentPage = 1;
        int totalSaved = 0;
        boolean hasMore = true;

        try {
            log.info("开始全量分批同步照管人员信息，页大小: {}", pageSize);

            // 清空照管人员表
            int deletedCount = carePersonMapper.deleteAllCarePersons();
            log.info("清空照管人员表，删除 {} 条记录", deletedCount);

            while (hasMore) {
                log.info("正在获取第{}批数据", currentPage);

                Map<String, Object> params = new HashMap<>();
                params.put("pageIndex", currentPage);
                params.put("pageSize", pageSize);

                ExternalApiResponse<List<CarePerson>> response = apiHttpClient.doGetForList("AllArchivesInfo", params, CarePerson.class);

                if (!response.isSuccess()) {
                    // 修改：如果是"未查询到数据"，视为正常结束
                    if ("未查询到数据".equals(response.getMsg())) {
                        log.info("第{}页未查询到数据，同步完成", currentPage);
                        break;
                    }
                    log.error("获取照管人员列表第{}页失败: {}", currentPage, response.getMsg());
                    break;
                }

                List<CarePerson> pageList = response.getDecodedData();
                if (pageList != null && !pageList.isEmpty()) {
                    int batchSaved = processCarePersonPage(pageList);
                    totalSaved += batchSaved;

                    log.info("第{}批处理完成，本批数量: {}，成功保存: {}，累计数量: {}",
                            currentPage, pageList.size(), batchSaved, totalSaved);

                    currentPage++;

                    // 添加延迟
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                } else {
                    log.info("第{}页无数据，停止同步", currentPage);
                    hasMore = false;
                }
            }

            return AjaxResult.success("照管人员信息全量分批同步完成，共同步: " + totalSaved + " 条");

        } catch (Exception e) {
            log.error("分批同步照管人员信息异常", e);
            return AjaxResult.error("分批同步异常，已成功: " + totalSaved + " 条，错误: " + e.getMessage());
        }
    }

    /**
     * 递归保存组织机构信息 - 直接插入，不检查existing
     */
    private int saveOrganizationRecursive(Organization org, int count) {
        Date now = new Date();
        org.setCreateTime(now);
        org.setUpdateTime(now);

        // 直接插入，不检查existing
        organizationMapper.insertOrganization(org);
        log.debug("新增组织机构: {} (Level: {})", org.getOrgName(), org.getLevels());
        count++;

        // 递归保存子组织
        if (org.getChildren() != null && !org.getChildren().isEmpty()) {
            for (Organization child : org.getChildren()) {
                count = saveOrganizationRecursive(child, count);
            }
        }

        return count;
    }

    /**
     * 批量处理警员数据 - 直接插入，不检查existing
     */
    private int processPoliceBatch(List<Police> policeList) {
        int successCount = 0;
        List<Police> toInsert = new ArrayList<>();

        // 直接准备插入，不检查existing
        for (Police police : policeList) {
            try {
                Date now = new Date();
                police.setCreateTime(now);
                police.setUpdateTime(now);
                toInsert.add(police);
            } catch (Exception e) {
                log.error("处理警员信息失败: {}", police.getName(), e);
            }
        }

        // 批量插入
        if (!toInsert.isEmpty()) {
            try {
                policeMapper.batchInsertPolice(toInsert);
                successCount += toInsert.size();
            } catch (Exception e) {
                log.error("批量插入警员失败，降级为单条插入", e);
                successCount += insertPoliceIndividually(toInsert);
            }
        }

        return successCount;
    }

    /**
     * 处理单页照管人员数据 - 直接插入，不检查existing
     */
    private int processCarePersonPage(List<CarePerson> carePersonList) {
        int savedCount = 0;
        List<CarePerson> toInsert = new ArrayList<>();

        // 直接准备插入，不检查existing
        for (CarePerson person : carePersonList) {
            try {
                Date now = new Date();
                person.setCreateTime(now);
                person.setUpdateTime(now);
                toInsert.add(person);
            } catch (Exception e) {
                log.error("处理照管人员信息失败: {}", person.getName(), e);
            }
        }
        /*
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String jsonOutput = mapper.writeValueAsString(carePersonList);
            System.out.println("照管人员列表:");
            System.out.println(jsonOutput);
        }
        catch(Exception ex){
            //
        }
        */

        // 批量插入
        if (!toInsert.isEmpty()) {
            try {
                carePersonMapper.batchInsertCarePerson(toInsert);
                savedCount += toInsert.size();
            } catch (Exception e) {
                log.error("批量插入照管人员失败，降级为单条插入", e);
                savedCount += insertCarePersonIndividually(toInsert);
            }
        }

        return savedCount;
    }

    // 数据查询方法
    @Override
    public List<Organization> getOrganizationList(Organization organization) {
        return organizationMapper.selectOrganizationList(organization);
    }

    @Override
    public List<Police> getPoliceList(Police police) {
        return policeMapper.selectPoliceList(police);
    }

    @Override
    public List<CarePerson> getCarePersonList(CarePerson carePerson) {
        return carePersonMapper.selectCarePersonList(carePerson);
    }

    /**
     * 单条插入警员 - 避免泛型警告
     */
    private int insertPoliceIndividually(List<Police> policeList) {
        int count = 0;
        for (Police police : policeList) {
            try {
                policeMapper.insertPolice(police);
                count++;
            } catch (Exception e) {
                log.error("单条插入警员失败: {}", police.getName(), e);
            }
        }
        return count;
    }

    /**
     * 单条插入照管人员 - 避免泛型警告
     */
    private int insertCarePersonIndividually(List<CarePerson> carePersonList) {
        int count = 0;
        for (CarePerson person : carePersonList) {
            try {
                carePersonMapper.insertCarePerson(person);
                count++;
            } catch (Exception e) {
                log.error("单条插入照管人员失败: {}", person.getName(), e);
            }
        }
        return count;
    }
}