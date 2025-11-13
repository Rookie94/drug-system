package com.ruoyi.cms.external.service.impl;

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
        try {
            log.info("开始全量同步组织机构信息");

            // 清空组织机构表
            int deletedCount = organizationMapper.deleteAllOrganizations();
            log.info("清空组织机构表，删除 {} 条记录", deletedCount);

            ExternalApiResponse<Organization> response = apiHttpClient.doGet("get-org-data", null, Organization.class);

            if (!response.isSuccess()) {
                log.error("获取组织机构失败: {}", response.getMsg());
                return AjaxResult.error("获取组织机构失败: " + response.getMsg());
            }

            Organization org = response.getDecodedData();
            if (org != null) {
                int savedCount = saveOrganizationRecursive(org, 0);
                log.info("组织机构全量同步完成，共处理 {} 个组织", savedCount);
                return AjaxResult.success("组织机构全量同步成功，共处理 " + savedCount + " 个组织");
            } else {
                log.warn("未获取到组织机构数据");
                return AjaxResult.error("未获取到组织机构数据");
            }
        } catch (Exception e) {
            log.error("获取组织机构信息异常", e);
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
            ExternalApiResponse<List<Police>> response = apiHttpClient.doPostForList("police-verify", request, Police.class);

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
            ExternalApiResponse<List<CarePerson>> response = apiHttpClient.doPostForList("archives-verify", request, CarePerson.class);

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
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult syncPolice() {
        try {
            log.info("开始全量同步在册警员列表");

            // 清空警员表
            int deletedCount = policeMapper.deleteAllPolice();
            log.info("清空警员表，删除 {} 条记录", deletedCount);

            // 使用类型安全的方法处理列表返回
            ExternalApiResponse<List<Police>> response = apiHttpClient.doGetForList("all-police-info", null, Police.class);

            if (!response.isSuccess()) {
                log.error("获取警员列表失败: {}", response.getMsg());
                return AjaxResult.error("获取警员列表失败: " + response.getMsg());
            }

            List<Police> policeList = response.getDecodedData();
            if (policeList != null && !policeList.isEmpty()) {
                int successCount = processPoliceBatch(policeList);

                String message = String.format("警员信息全量同步完成，总数: %d，成功: %d，失败: %d",
                        policeList.size(), successCount, policeList.size() - successCount);
                log.info(message);

                if (successCount < policeList.size()) {
                    return AjaxResult.warn(message);
                } else {
                    return AjaxResult.success(message);
                }
            } else {
                log.info("未获取到警员数据");
                return AjaxResult.success("未获取到警员数据");
            }
        } catch (Exception e) {
            log.error("获取警员列表异常", e);
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
        int pageSize = 1000;
        int totalSaved = 0;
        int totalPages = 0;
        int totalRecords = 0;

        try {
            log.info("开始全量同步在册照管人员列表");

            // 清空照管人员表
            int deletedCount = carePersonMapper.deleteAllCarePersons();
            log.info("清空照管人员表，删除 {} 条记录", deletedCount);

            // 先获取第一页数据，了解总记录数
            Map<String, Object> firstPageParams = new HashMap<>();
            firstPageParams.put("pageIndex", pageIndex);
            firstPageParams.put("pageSize", pageSize);

            ExternalApiResponse<ExternalPageResponse> firstResponse = apiHttpClient.doGet("all-archives-info", firstPageParams, ExternalPageResponse.class);

            if (!firstResponse.isSuccess()) {
                log.error("获取照管人员列表失败: {}", firstResponse.getMsg());
                return AjaxResult.error("获取照管人员列表失败: " + firstResponse.getMsg());
            }

            ExternalPageResponse firstPage = firstResponse.getDecodedData();
            if (firstPage == null) {
                return AjaxResult.error("获取照管人员列表数据为空");
            }

            totalRecords = firstPage.getTotal();
            totalPages = (int) Math.ceil((double) totalRecords / pageSize);

            log.info("照管人员总数: {}，总页数: {}", totalRecords, totalPages);

            if (totalRecords == 0) {
                return AjaxResult.success("没有照管人员数据需要同步");
            }

            // 处理第一页数据
            if (firstPage.getDecodedList() != null && !firstPage.getDecodedList().isEmpty()) {
                int pageSaved = processCarePersonPage(firstPage.getDecodedList());
                totalSaved += pageSaved;
                log.info("第1页处理完成，本页数量: {}，成功保存: {}，累计数量: {}",
                        firstPage.getDecodedList().size(), pageSaved, totalSaved);
            }

            // 从第二页开始循环获取所有数据
            for (pageIndex = 2; pageIndex <= totalPages; pageIndex++) {
                log.info("正在获取第{}/{}页数据", pageIndex, totalPages);

                Map<String, Object> pageParams = new HashMap<>();
                pageParams.put("pageIndex", pageIndex);
                pageParams.put("pageSize", pageSize);

                ExternalApiResponse<ExternalPageResponse> response = apiHttpClient.doGet("all-archives-info", pageParams, ExternalPageResponse.class);

                if (!response.isSuccess()) {
                    log.error("获取照管人员列表第{}页失败: {}", pageIndex, response.getMsg());
                    // 记录错误但继续处理下一页
                    continue;
                }

                ExternalPageResponse pageResponse = response.getDecodedData();
                if (pageResponse != null && pageResponse.getDecodedList() != null
                        && !pageResponse.getDecodedList().isEmpty()) {

                    int pageSaved = processCarePersonPage(pageResponse.getDecodedList());
                    totalSaved += pageSaved;
                    log.info("第{}页处理完成，本页数量: {}，成功保存: {}，累计数量: {}",
                            pageIndex, pageResponse.getDecodedList().size(), pageSaved, totalSaved);

                    // 添加延迟，避免请求过快
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    log.warn("第{}页数据为空", pageIndex);
                }
            }

            String message = String.format("照管人员信息全量同步完成，应同步: %d 条，实际成功: %d 条", totalRecords, totalSaved);
            log.info(message);

            if (totalSaved < totalRecords) {
                return AjaxResult.warn(message);
            } else {
                return AjaxResult.success(message);
            }

        } catch (Exception e) {
            log.error("获取照管人员列表异常", e);
            String message = String.format("照管人员信息同步异常，已成功: %d 条，错误: %s", totalSaved, e.getMessage());
            return AjaxResult.error(message);
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

                ExternalApiResponse<ExternalPageResponse> response = apiHttpClient.doGet("all-archives-info", params, ExternalPageResponse.class);

                if (!response.isSuccess()) {
                    log.error("获取照管人员列表第{}页失败: {}", currentPage, response.getMsg());
                    break;
                }

                ExternalPageResponse pageResponse = response.getDecodedData();
                if (pageResponse != null && pageResponse.getDecodedList() != null
                        && !pageResponse.getDecodedList().isEmpty()) {

                    int batchSaved = processCarePersonPage(pageResponse.getDecodedList());
                    totalSaved += batchSaved;

                    log.info("第{}批处理完成，本批数量: {}，成功保存: {}，累计数量: {}",
                            currentPage, pageResponse.getDecodedList().size(), batchSaved, totalSaved);

                    // 检查是否还有更多数据
                    int currentTotal = currentPage * pageSize;
                    if (pageResponse.getTotal() != null && currentTotal >= pageResponse.getTotal()) {
                        hasMore = false;
                        log.info("已到达最后一页，停止同步");
                    }

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