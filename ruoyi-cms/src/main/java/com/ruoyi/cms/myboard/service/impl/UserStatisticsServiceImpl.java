package com.ruoyi.cms.myboard.service.impl;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import com.ruoyi.cms.myboard.mapper.UserStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.cms.myboard.service.IUserStatisticsService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.myboard.domain.vo.*;

/**
 * 人员统计分析Service业务层处理
 *
 * @author ruoyi
 * @date 2025-12-09
 */
@Service
public class UserStatisticsServiceImpl implements IUserStatisticsService
{
    @Autowired
    private UserStatisticsMapper userStatisticsMapper;

    /**
     * 查询人员统计数据
     *
     * @param userStatisticsVo 查询条件
     * @return 统计数据
     */
    @Override
    public UserStatisticsVo selectUserStatistics(UserStatisticsVo userStatisticsVo)
    {
        UserStatisticsVo result = new UserStatisticsVo();

        // 设置默认日期范围（最近30天）
        if (userStatisticsVo.getStartDate() == null || userStatisticsVo.getEndDate() == null) {
            Date endDate = new Date();
            Date startDate = DateUtils.addDays(endDate, -30);
            userStatisticsVo.setStartDate(DateUtils.parseDateToStr("yyyy-MM-dd", startDate));
            userStatisticsVo.setEndDate(DateUtils.parseDateToStr("yyyy-MM-dd", endDate));
        }

        // 1. 获取用户总数和各类型人数
        Map<String, Object> userCounts = userStatisticsMapper.selectUserCounts(userStatisticsVo);

        Long totalUsersLong = (Long) userCounts.getOrDefault("totalUsers", 0L);
        result.setTotalUsers(totalUsersLong != null ? totalUsersLong.intValue() : 0);

        Long policeCountLong = (Long) userCounts.getOrDefault("policeCount", 0L);
        result.setPoliceCount(policeCountLong != null ? policeCountLong.intValue() : 0);

        Long releasedCountLong = (Long) userCounts.getOrDefault("releasedCount", 0L);
        result.setReleasedCount(releasedCountLong != null ? releasedCountLong.intValue() : 0);

        Long rehabCountLong = (Long) userCounts.getOrDefault("rehabCount", 0L);
        result.setRehabCount(rehabCountLong != null ? rehabCountLong.intValue() : 0);

        Long otherCountLong = (Long) userCounts.getOrDefault("otherCount", 0L);
        result.setOtherCount(otherCountLong != null ? otherCountLong.intValue() : 0);

        // 2. 用户类型占比数据
        List<Map<String, Object>> userTypeList = userStatisticsMapper.selectUserTypeStatistics(userStatisticsVo);
        List<ChartDataVo> userTypeData = new ArrayList<>();
        for (Map<String, Object> map : userTypeList) {
            ChartDataVo Vo = new ChartDataVo();
            Vo.setName(map.get("name").toString());
            Vo.setValue(Integer.parseInt(map.get("value").toString()));
            userTypeData.add(Vo);
        }
        result.setUserTypeData(userTypeData);

        // 3. 每日注册数据
        List<Map<String, Object>> dailyRegisterList = userStatisticsMapper.selectDailyRegisterStatistics(userStatisticsVo);
        ChartDataVo dailyRegisterData = new ChartDataVo();
        List<String> dates = new ArrayList<>();
        List<Integer> dailyCounts = new ArrayList<>();
        for (Map<String, Object> map : dailyRegisterList) {
            dates.add(map.get("date").toString());
            dailyCounts.add(Integer.parseInt(map.get("count").toString()));
        }
        dailyRegisterData.setDates(dates);
        dailyRegisterData.setCounts(dailyCounts);
        result.setDailyRegisterData(dailyRegisterData);

        // 4. 每周注册数据
        List<Map<String, Object>> weeklyRegisterList = userStatisticsMapper.selectWeeklyRegisterStatistics(userStatisticsVo);
        ChartDataVo weeklyRegisterData = new ChartDataVo();
        List<String> weeks = new ArrayList<>();
        List<Integer> weeklyCounts = new ArrayList<>();
        for (Map<String, Object> map : weeklyRegisterList) {
            weeks.add(map.get("week").toString());
            weeklyCounts.add(Integer.parseInt(map.get("count").toString()));
        }
        weeklyRegisterData.setWeeks(weeks);
        weeklyRegisterData.setCounts(weeklyCounts);
        result.setWeeklyRegisterData(weeklyRegisterData);

        // 5. 每月注册数据
        List<Map<String, Object>> monthlyRegisterList = userStatisticsMapper.selectMonthlyRegisterStatistics(userStatisticsVo);
        ChartDataVo monthlyRegisterData = new ChartDataVo();
        List<String> months = new ArrayList<>();
        List<Integer> monthlyCounts = new ArrayList<>();
        for (Map<String, Object> map : monthlyRegisterList) {
            months.add(map.get("month").toString());
            monthlyCounts.add(Integer.parseInt(map.get("count").toString()));
        }
        monthlyRegisterData.setMonths(months);
        monthlyRegisterData.setCounts(monthlyCounts);
        result.setMonthlyRegisterData(monthlyRegisterData);

        // 6. 每日登录数据
        List<Map<String, Object>> dailyLoginList = userStatisticsMapper.selectDailyLoginStatistics(userStatisticsVo);
        ChartDataVo dailyLoginData = new ChartDataVo();
        List<String> loginDates = new ArrayList<>();
        List<Integer> loginCounts = new ArrayList<>();
        for (Map<String, Object> map : dailyLoginList) {
            loginDates.add(map.get("date").toString());
            loginCounts.add(Integer.parseInt(map.get("count").toString()));
        }
        dailyLoginData.setDates(loginDates);
        dailyLoginData.setCounts(loginCounts);
        result.setDailyLoginData(dailyLoginData);

        return result;
    }

    /**
     * 导出人员统计数据
     *
     * @param response 响应对象
     * @param userStatisticsVo 查询条件
     */
    @Override
    public void exportUserStatistics(HttpServletResponse response, UserStatisticsVo userStatisticsVo)
    {
        // 获取统计数据
        UserStatisticsVo result = selectUserStatistics(userStatisticsVo);

        // 转换导出数据格式
        List<Map<String, Object>> exportList = new ArrayList<>();

        // 汇总数据
        Map<String, Object> summaryMap = new HashMap<>();
        summaryMap.put("统计项", "人员统计汇总");
        summaryMap.put("用户总数", result.getTotalUsers());
        summaryMap.put("警官人数", result.getPoliceCount());
        summaryMap.put("出所人员人数", result.getReleasedCount());
        summaryMap.put("社康社戒人员总数", result.getRehabCount());
        summaryMap.put("游客及其它人员总数", result.getOtherCount());
        exportList.add(summaryMap);

        // 导出Excel
        //ExcelUtil<Map<String, Object>> util = new ExcelUtil<>(Map.class);
        //util.exportExcel(response, exportList, "人员统计数据");
    }
}