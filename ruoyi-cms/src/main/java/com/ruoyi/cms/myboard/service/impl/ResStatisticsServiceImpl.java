package com.ruoyi.cms.myboard.service.impl;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.cms.myboard.domain.vo.*;
import com.ruoyi.cms.myboard.mapper.ResStatisticsMapper;
import com.ruoyi.cms.myboard.service.IResStatisticsService;
import com.ruoyi.common.utils.poi.ExcelUtil;

@Service
public class ResStatisticsServiceImpl implements IResStatisticsService {

    private static final Logger log = LoggerFactory.getLogger(ResStatisticsServiceImpl.class);

    @Autowired
    private ResStatisticsMapper resStatisticsMapper;

    @Autowired
    private ISysUserService userService;

    // 标准日期格式化器
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // 日期时间格式化器（用于数据库查询）
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResStatisticsDataVO getStatisticsData(ResStatisticsQueryVO queryVO) {
        // 处理查询参数 - 修正日期解析逻辑
        processQueryDates(queryVO);

        // 获取原始访问日志数据
        List<AccessLogVO> accessLogs = resStatisticsMapper.selectAccessLogs(queryVO);

        // 获取用户信息映射
        Map<String, String> userTypeMap = getUserTypeMap(accessLogs);

        // 构建统计数据
        return buildStatisticsData(accessLogs, userTypeMap, queryVO);
    }

    /**
     * 处理查询日期参数
     * 修正：String类型日期解析为LocalDate，处理前端传递的字符串格式
     */
    private void processQueryDates(ResStatisticsQueryVO queryVO) {
        try {
            LocalDate startDate = null;
            LocalDate endDate = null;

            // 解析开始日期
            if (StringUtils.isNotEmpty(queryVO.getStartDate())) {
                try {
                    // 解析前端传递的字符串日期
                    startDate = LocalDate.parse(queryVO.getStartDate(), DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    log.warn("开始日期格式错误({})，使用默认值(30天前)", queryVO.getStartDate());
                    startDate = LocalDate.now().minusDays(30);
                }
            } else {
                // 默认30天前
                startDate = LocalDate.now().minusDays(30);
            }

            // 解析结束日期
            if (StringUtils.isNotEmpty(queryVO.getEndDate())) {
                try {
                    endDate = LocalDate.parse(queryVO.getEndDate(), DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    log.warn("结束日期格式错误({})，使用默认值(今天)", queryVO.getEndDate());
                    endDate = LocalDate.now();
                }
            } else {
                // 默认今天
                endDate = LocalDate.now();
            }

            // 确保结束日期不早于开始日期
            if (endDate.isBefore(startDate)) {
                endDate = startDate;
                log.warn("结束日期早于开始日期，自动调整为同一起始日期:{}", startDate.format(DATE_FORMATTER));
            }

            // 重新设置格式化后的日期字符串（供MyBatis查询使用）
            queryVO.setStartDate(startDate.format(DATE_FORMATTER));
            queryVO.setEndDate(endDate.format(DATE_FORMATTER));

            log.debug("查询日期范围: {} 至 {}", queryVO.getStartDate(), queryVO.getEndDate());

        } catch (Exception e) {
            log.error("处理查询日期参数异常", e);
            // 设置默认值
            LocalDate defaultStart = LocalDate.now().minusDays(30);
            LocalDate defaultEnd = LocalDate.now();
            queryVO.setStartDate(defaultStart.format(DATE_FORMATTER));
            queryVO.setEndDate(defaultEnd.format(DATE_FORMATTER));
        }
    }

    /**
     * 获取用户类型映射
     */
    private Map<String, String> getUserTypeMap(List<AccessLogVO> accessLogs) {
        // 提取所有用户名
        Set<String> usernames = accessLogs.stream()
                .map(AccessLogVO::getUserName)
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.toSet());

        Map<String, String> userTypeMap = new HashMap<>();

        // 批量查询用户信息
        for (String username : usernames) {
            SysUser user = userService.selectUserByUserName(username);
            if (user != null && StringUtils.isNotEmpty(user.getUserType())) {
                userTypeMap.put(username, user.getUserType());
            } else {
                userTypeMap.put(username, "33"); // 游客及其他
            }
        }

        // 处理匿名用户
        userTypeMap.put("", "33");
        userTypeMap.put(null, "33");

        return userTypeMap;
    }

    /**
     * 构建统计数据
     */
    private ResStatisticsDataVO buildStatisticsData(List<AccessLogVO> accessLogs,
                                                    Map<String, String> userTypeMap,
                                                    ResStatisticsQueryVO queryVO) {
        ResStatisticsDataVO data = new ResStatisticsDataVO();

        // 计算基础统计
        int totalAccess = accessLogs.size();
        int uniqueVisitors = (int) accessLogs.stream()
                .map(AccessLogVO::getUserName)
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .count();

        // 计算平均访问时长
        double avgVisitDuration = calculateAvgVisitDuration(accessLogs);

        // 计算平均访问深度
        double avgVisitDepth = calculateAvgVisitDepth(accessLogs);

        data.setTotalAccess(totalAccess);
        data.setUniqueVisitors(uniqueVisitors);
        data.setAvgVisitDuration(avgVisitDuration);
        data.setAvgVisitDepth(avgVisitDepth);

        // 按用户类型统计
        List<ModuleAccessVO> userAccessData = calculateUserAccessData(accessLogs, userTypeMap);
        data.setUserAccessData(userAccessData);

        // 按资源分类统计
        List<ModuleAccessVO> moduleAccessData = calculateModuleAccessData(accessLogs);
        data.setModuleAccessData(moduleAccessData);

        // 获取热门模块
        String topModule = moduleAccessData.stream()
                .max(Comparator.comparingInt(ModuleAccessVO::getValue))
                .map(ModuleAccessVO::getName)
                .orElse("无数据");
        data.setTopModule(topModule);

        // 按日期统计访问量
        DailyAccessDataVO dailyAccessData = calculateDailyAccessData(accessLogs, queryVO);
        data.setDailyAccessData(dailyAccessData);

        // 模块访问趋势
        ModuleTrendDataVO moduleTrendData = calculateModuleTrendData(accessLogs, queryVO);
        data.setModuleTrendData(moduleTrendData);

        // 热门文章（按访问次数排序）
        List<TopArticleVO> topArticles = calculateTopArticles(accessLogs);
        data.setTopArticles(topArticles);

        // 按小时统计访问分布
        TimeDistributionDataVO timeDistributionData = calculateTimeDistributionData(accessLogs);
        data.setTimeDistributionData(timeDistributionData);

        // 按日期统计活跃用户
        DailyLoginDataVO dailyLoginData = calculateDailyLoginData(accessLogs, queryVO);
        data.setDailyLoginData(dailyLoginData);

        return data;
    }

    /**
     * 计算平均访问时长（模拟实现）
     */
    private double calculateAvgVisitDuration(List<AccessLogVO> accessLogs) {
        double totalDuration = 0;
        int count = 0;

        for (AccessLogVO log : accessLogs) {
            String username = log.getUserName();
            double duration = 3.0 + Math.random() * 10; // 3-13分钟

            if (StringUtils.isNotEmpty(username)) {
                if (username.contains("admin") || username.contains("警官")) {
                    duration = 8.0 + Math.random() * 10; // 8-18分钟
                } else if (username.contains("ceshi") || username.contains("出所")) {
                    duration = 5.0 + Math.random() * 8; // 5-13分钟
                }
            }

            totalDuration += duration;
            count++;
        }

        return count > 0 ? BigDecimal.valueOf(totalDuration / count)
                .setScale(1, RoundingMode.HALF_UP).doubleValue() : 0.0;
    }

    /**
     * 计算平均访问深度（模拟实现）
     */
    private double calculateAvgVisitDepth(List<AccessLogVO> accessLogs) {
        Map<String, Set<String>> userDailyArticles = new HashMap<>();

        for (AccessLogVO log : accessLogs) {
            String username = StringUtils.isNotEmpty(log.getUserName()) ? log.getUserName() : "anonymous";

            LocalDateTime accessTime = log.getAccessTime();
            String date = accessTime != null ?
                    accessTime.toLocalDate().format(DATE_FORMATTER) : "unknown";

            String key = username + "_" + date;
            String articleKey = log.getResId() + "_" + log.getResTitle();

            userDailyArticles.computeIfAbsent(key, k -> new HashSet<>()).add(articleKey);
        }

        double totalDepth = userDailyArticles.values().stream()
                .mapToInt(Set::size)
                .sum();

        int userCount = userDailyArticles.size();

        return userCount > 0 ? BigDecimal.valueOf((double) totalDepth / userCount)
                .setScale(1, RoundingMode.HALF_UP).doubleValue() : 0.0;
    }

    /**
     * 计算用户类型访问数据
     */
    private List<ModuleAccessVO> calculateUserAccessData(List<AccessLogVO> accessLogs,
                                                         Map<String, String> userTypeMap) {
        Map<String, Integer> userTypeCount = new HashMap<>();
        Map<String, Double> userTypeDurationSum = new HashMap<>();

        // 初始化
        userTypeCount.put("00", 0);
        userTypeCount.put("11", 0);
        userTypeCount.put("22", 0);
        userTypeCount.put("33", 0);

        userTypeDurationSum.put("00", 0.0);
        userTypeDurationSum.put("11", 0.0);
        userTypeDurationSum.put("22", 0.0);
        userTypeDurationSum.put("33", 0.0);

        // 统计
        for (AccessLogVO log : accessLogs) {
            String userType = userTypeMap.get(log.getUserName());
            if (userType == null) {
                userType = "33";
            }

            // 统计访问次数
            userTypeCount.put(userType, userTypeCount.get(userType) + 1);

            // 模拟访问时长
            double duration = 5.0 + Math.random() * 5;
            userTypeDurationSum.put(userType, userTypeDurationSum.get(userType) + duration);
        }

        // 转换为VO
        List<ModuleAccessVO> result = new ArrayList<>();
        Map<String, String> typeNameMap = new HashMap<>();
        typeNameMap.put("00", "警官");
        typeNameMap.put("11", "出所人员");
        typeNameMap.put("22", "社康社戒人员");
        typeNameMap.put("33", "游客及其他");

        for (String userType : userTypeCount.keySet()) {
            int count = userTypeCount.get(userType);
            if (count > 0) {
                ModuleAccessVO vo = new ModuleAccessVO();
                vo.setName(typeNameMap.get(userType));
                vo.setValue(count);

                // 计算平均时长
                double avgDuration = count > 0 ?
                        BigDecimal.valueOf(userTypeDurationSum.get(userType) / count)
                                .setScale(1, RoundingMode.HALF_UP).doubleValue() : 0.0;
                vo.setAvgDuration(avgDuration);

                result.add(vo);
            }
        }

        return result.stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .collect(Collectors.toList());
    }

    /**
     * 计算模块访问数据
     */
    private List<ModuleAccessVO> calculateModuleAccessData(List<AccessLogVO> accessLogs) {
        // 按资源名称分组统计
        Map<String, Integer> moduleCount = accessLogs.stream()
                .collect(Collectors.groupingBy(
                        log -> StringUtils.isNotEmpty(log.getResName()) ? log.getResName() : "其它",
                        Collectors.summingInt(e -> 1)
                ));

        return moduleCount.entrySet().stream()
                .map(entry -> {
                    ModuleAccessVO vo = new ModuleAccessVO();
                    vo.setName(entry.getKey());
                    vo.setValue(entry.getValue());
                    return vo;
                })
                .sorted((a, b) -> b.getValue() - a.getValue())
                .collect(Collectors.toList());
    }

    /**
     * 计算每日访问数据
     */
    private DailyAccessDataVO calculateDailyAccessData(List<AccessLogVO> accessLogs,
                                                       ResStatisticsQueryVO queryVO) {
        // 按访问时间分组统计
        Map<String, Integer> dailyCount = accessLogs.stream()
                .filter(log -> log.getAccessTime() != null)
                .collect(Collectors.groupingBy(
                        log -> log.getAccessTime().toLocalDate().format(DATE_FORMATTER),
                        Collectors.summingInt(e -> 1)
                ));

        // 填充缺失日期
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        try {
            LocalDate startDate = LocalDate.parse(queryVO.getStartDate(), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(queryVO.getEndDate(), DATE_FORMATTER);

            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                String dateStr = currentDate.format(DATE_FORMATTER);
                dates.add(dateStr);
                counts.add(dailyCount.getOrDefault(dateStr, 0));
                currentDate = currentDate.plusDays(1);
            }
        } catch (Exception e) {
            log.error("计算每日访问数据异常", e);
        }

        DailyAccessDataVO vo = new DailyAccessDataVO();
        vo.setDates(dates);
        vo.setCounts(counts);
        return vo;
    }

    /**
     * 计算模块访问趋势数据
     */
    private ModuleTrendDataVO calculateModuleTrendData(List<AccessLogVO> accessLogs,
                                                       ResStatisticsQueryVO queryVO) {
        ModuleTrendDataVO result = new ModuleTrendDataVO();

        try {
            // 获取所有日期
            LocalDate startDate = LocalDate.parse(queryVO.getStartDate(), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(queryVO.getEndDate(), DATE_FORMATTER);

            List<String> dates = new ArrayList<>();
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                dates.add(currentDate.format(DATE_FORMATTER));
                currentDate = currentDate.plusDays(1);
            }
            result.setDates(dates);

            // 获取热门模块（前5个）
            List<ModuleAccessVO> topModules = calculateModuleAccessData(accessLogs);
            if (topModules.size() > 5) {
                topModules = topModules.subList(0, 5);
            }

            // 按模块和日期分组统计
            Map<String, Map<String, Integer>> moduleDailyCount = new HashMap<>();

            for (AccessLogVO log : accessLogs) {
                if (log.getAccessTime() != null) {
                    String dateStr = log.getAccessTime().toLocalDate().format(DATE_FORMATTER);
                    String module = StringUtils.isNotEmpty(log.getResName()) ? log.getResName() : "其它";

                    moduleDailyCount.computeIfAbsent(module, k -> new HashMap<>());
                    Map<String, Integer> dailyMap = moduleDailyCount.get(module);
                    dailyMap.put(dateStr, dailyMap.getOrDefault(dateStr, 0) + 1);
                }
            }

            // 构建趋势数据
            List<ModuleTrendSeriesVO> seriesList = new ArrayList<>();
            for (ModuleAccessVO module : topModules) {
                String moduleName = module.getName();
                ModuleTrendSeriesVO series = new ModuleTrendSeriesVO();
                series.setName(moduleName);

                List<Integer> data = new ArrayList<>();
                for (String date : dates) {
                    int count = moduleDailyCount.containsKey(moduleName) && moduleDailyCount.get(moduleName).containsKey(date)
                            ? moduleDailyCount.get(moduleName).get(date) : 0;
                    data.add(count);
                }
                series.setData(data);
                seriesList.add(series);
            }

            result.setSeries(seriesList);

        } catch (Exception e) {
            log.error("计算模块访问趋势数据异常", e);
            result.setDates(new ArrayList<>());
            result.setSeries(new ArrayList<>());
        }

        return result;
    }

    /**
     * 计算热门文章
     */
    private List<TopArticleVO> calculateTopArticles(List<AccessLogVO> accessLogs) {
        Map<String, TopArticleVO> articleMap = new HashMap<>();

        for (AccessLogVO log : accessLogs) {
            if (log.getResId() != null && StringUtils.isNotEmpty(log.getResTitle())) {
                String key = log.getResId() + "_" + log.getResTitle();
                TopArticleVO article = articleMap.get(key);

                if (article == null) {
                    article = new TopArticleVO();
                    article.setId(log.getResId());
                    article.setTitle(log.getResTitle());
                    article.setModule(log.getResName());
                    article.setViews(0);

                    if (log.getAccessTime() != null) {
                        article.setDate(log.getAccessTime().toLocalDate().format(DATE_FORMATTER));
                    } else {
                        article.setDate("");
                    }

                    articleMap.put(key, article);
                }
                article.setViews(article.getViews() + 1);
            }
        }

        // 按访问次数排序，取前10名
        return articleMap.values().stream()
                .sorted((a, b) -> b.getViews() - a.getViews())
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * 计算时间分布数据
     */
    private TimeDistributionDataVO calculateTimeDistributionData(List<AccessLogVO> accessLogs) {
        TimeDistributionDataVO result = new TimeDistributionDataVO();

        // 定义时间段
        String[] hourRanges = {"00-03", "03-06", "06-09", "09-12", "12-15", "15-18", "18-21", "21-24"};
        int[] hourCounts = new int[8];

        // 统计每个时间段的访问量
        for (AccessLogVO log : accessLogs) {
            if (log.getAccessTime() != null) {
                int hour = log.getAccessTime().getHour();

                // 分配到对应的时间段
                if (hour >= 0 && hour < 3) hourCounts[0]++;
                else if (hour >= 3 && hour < 6) hourCounts[1]++;
                else if (hour >= 6 && hour < 9) hourCounts[2]++;
                else if (hour >= 9 && hour < 12) hourCounts[3]++;
                else if (hour >= 12 && hour < 15) hourCounts[4]++;
                else if (hour >= 15 && hour < 18) hourCounts[5]++;
                else if (hour >= 12 && hour < 21) hourCounts[6]++;
                else if (hour >= 21 && hour < 24) hourCounts[7]++;
            }
        }

        result.setHours(Arrays.asList(hourRanges));
        result.setCounts(Arrays.stream(hourCounts).boxed().collect(Collectors.toList()));

        return result;
    }

    /**
     * 计算每日活跃用户数据
     */
    private DailyLoginDataVO calculateDailyLoginData(List<AccessLogVO> accessLogs,
                                                     ResStatisticsQueryVO queryVO) {
        DailyLoginDataVO result = new DailyLoginDataVO();

        try {
            // 获取所有日期
            LocalDate startDate = LocalDate.parse(queryVO.getStartDate(), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(queryVO.getEndDate(), DATE_FORMATTER);

            List<String> dates = new ArrayList<>();
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                dates.add(currentDate.format(DATE_FORMATTER));
                currentDate = currentDate.plusDays(1);
            }
            result.setDates(dates);

            // 按日期统计活跃用户数（去重）
            Map<String, Set<String>> dailyActiveUsers = new HashMap<>();

            for (AccessLogVO log : accessLogs) {
                if (log.getAccessTime() != null && StringUtils.isNotEmpty(log.getUserName())) {
                    String dateStr = log.getAccessTime().toLocalDate().format(DATE_FORMATTER);
                    dailyActiveUsers.computeIfAbsent(dateStr, k -> new HashSet<>())
                            .add(log.getUserName());
                }
            }

            // 构建活跃用户数列表
            List<Integer> counts = new ArrayList<>();
            for (String date : dates) {
                int count = dailyActiveUsers.containsKey(date) ? dailyActiveUsers.get(date).size() : 0;
                counts.add(count);
            }

            result.setCounts(counts);

        } catch (Exception e) {
            log.error("计算每日活跃用户数据异常", e);
            result.setDates(new ArrayList<>());
            result.setCounts(new ArrayList<>());
        }

        return result;
    }

    @Override
    public void exportStatisticsData(ResStatisticsQueryVO queryVO, HttpServletResponse response) {
        // 处理查询日期参数
        processQueryDates(queryVO);

        // 获取统计数据
        ResStatisticsDataVO data = getStatisticsData(queryVO);

        // 准备导出数据
        List<Map<String, Object>> exportData = new ArrayList<>();

        // 1. 资源访问统计概览
        Map<String, Object> summaryData = new LinkedHashMap<>();
        summaryData.put("统计项", "资源访问统计概览");
        summaryData.put("查询条件", String.format("时间范围: %s 至 %s",
                queryVO.getStartDate(), queryVO.getEndDate()));
        exportData.add(summaryData);

        // 2. 详细统计项
        for (ModuleAccessVO item : data.getUserAccessData()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("用户类型", item.getName());
            row.put("访问次数", item.getValue());
            row.put("平均访问时长(分钟)", item.getAvgDuration());
            exportData.add(row);
        }

        // 3. 模块访问统计
        Map<String, Object> moduleHeader = new LinkedHashMap<>();
        moduleHeader.put("模块名称", "模块访问统计");
        exportData.add(moduleHeader);

        for (ModuleAccessVO item : data.getModuleAccessData()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("模块名称", item.getName());
            row.put("访问次数", item.getValue());
            exportData.add(row);
        }

        // 4. 热门文章
        Map<String, Object> articleHeader = new LinkedHashMap<>();
        articleHeader.put("文章标题", "热门文章排行榜");
        exportData.add(articleHeader);

        for (TopArticleVO item : data.getTopArticles()) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("文章标题", item.getTitle());
            row.put("所属模块", item.getModule());
            row.put("访问次数", item.getViews());
            row.put("发布日期", item.getDate());
            exportData.add(row);
        }

        // 使用ExcelUtil导出
        try {
            //ExcelUtil<Map<String, Object>> util = new ExcelUtil<>(Map.class);
            //util.exportExcel(response, exportData, "资源访问统计分析数据");
        } catch (Exception e) {
            log.error("导出统计数据异常", e);
        }
    }
}