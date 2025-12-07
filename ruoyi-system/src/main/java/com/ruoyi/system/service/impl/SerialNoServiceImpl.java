package com.ruoyi.system.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SerialNoMapper;
import com.ruoyi.system.domain.SerialNo;
import com.ruoyi.system.service.ISerialNoService;

/**
 * 流水号管理Service业务层处理
 *
 * @author admin
 * @date 2025-05-16
 */
@Service
public class SerialNoServiceImpl implements ISerialNoService
{
    @Autowired
    private SerialNoMapper serialNoMapper;

    /**
     * 查询流水号管理
     *
     * @param id 流水号管理主键
     * @return 流水号管理
     */
    @Override
    public SerialNo selectSerialNoById(String id)
    {
        return serialNoMapper.selectSerialNoById(id);
    }

    /**
     * 查询流水号管理列表
     *
     * @param serialNo 流水号管理
     * @return 流水号管理
     */
    @Override
    public List<SerialNo> selectSerialNoList(SerialNo serialNo)
    {
        return serialNoMapper.selectSerialNoList(serialNo);
    }

    /**
     * 新增流水号管理
     *
     * @param serialNo 流水号管理
     * @return 结果
     */
    @Override
    public int insertSerialNo(SerialNo serialNo)
    {
        serialNo.setCreateTime(DateUtils.getNowDate());

        // 设置 lastResetDate 的默认值
        if (serialNo.getLastResetDate() == null || serialNo.getLastResetDate().trim().isEmpty()) {
            // 如果日期格式不为空，使用当前日期；否则为空字符串
            if (serialNo.getDateFormat() != null && !serialNo.getDateFormat().trim().isEmpty()) {
                serialNo.setLastResetDate(getCurrentDateString(serialNo.getDateFormat()));
            } else {
                serialNo.setLastResetDate("");
            }
        }
        // 确保 currentSeq 有默认值（从0开始）
        if (serialNo.getCurrentSeq() == null) {
            serialNo.setCurrentSeq(0L);
        }

        return serialNoMapper.insertSerialNo(serialNo);
    }

    /**
     * 修改流水号管理
     *
     * @param serialNo 流水号管理
     * @return 结果
     */
    @Override
    public int updateSerialNo(SerialNo serialNo)
    {
        serialNo.setUpdateTime(DateUtils.getNowDate());
        return serialNoMapper.updateSerialNo(serialNo);
    }

    /**
     * 批量删除流水号管理
     *
     * @param ids 需要删除的流水号管理主键
     * @return 结果
     */
    @Override
    public int deleteSerialNoByIds(String[] ids)
    {
        return serialNoMapper.deleteSerialNoByIds(ids);
    }

    /**
     * 删除流水号管理信息
     *
     * @param id 流水号管理主键
     * @return 结果
     */
    @Override
    public int deleteSerialNoById(String id)
    {
        return serialNoMapper.deleteSerialNoById(id);
    }

    /**
     * 检查是否需要重置序列号
     */
    private boolean needReset(String strategy, String lastDate, String dateFormat) {
        if (lastDate == null || lastDate.trim().isEmpty()) {
            return true;
        }

        try {
            switch (strategy) {
                case "DAILY":
                    // 对于每日重置，需要完整的日期
                    LocalDate lastDay = parseToLocalDate(lastDate, dateFormat);
                    return lastDay.isBefore(LocalDate.now());

                case "MONTHLY":
                    // 对于每月重置，只需要年月
                    YearMonth lastMonth = parseToYearMonth(lastDate, dateFormat);
                    return lastMonth.isBefore(YearMonth.now());

                case "YEARLY":
                    // 对于每年重置，只需要年份
                    int lastYear = parseToYear(lastDate, dateFormat);
                    return lastYear < LocalDate.now().getYear();

                case "NEVER":
                    return false;

                default:
                    return false;
            }
        } catch (DateTimeParseException e) {
            // 如果解析失败，也认为需要重置
            return true;
        }
    }

    /**
     * 根据日期格式将字符串解析为LocalDate
     */
    private LocalDate parseToLocalDate(String dateStr, String format) {
        String pattern = format.replace("YYYY", "yyyy").replace("YY", "yy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 如果格式只有年月（如yyMM），需要添加默认的日
        if (pattern.equals("yyMM") || pattern.equals("yyyyMM")) {
            YearMonth yearMonth = YearMonth.parse(dateStr, formatter);
            return yearMonth.atDay(1);
        }
        // 如果格式只有年（如yy），需要添加默认的月日
        else if (pattern.equals("yy") || pattern.equals("yyyy")) {
            int year = Integer.parseInt(dateStr);
            return LocalDate.of(year, 1, 1);
        }
        else {
            return LocalDate.parse(dateStr, formatter);
        }
    }

    /**
     * 根据日期格式将字符串解析为YearMonth
     */
    private YearMonth parseToYearMonth(String dateStr, String format) {
        String pattern = format.replace("YYYY", "yyyy").replace("YY", "yy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 如果格式只有年月，直接解析
        if (pattern.equals("yyMM") || pattern.equals("yyyyMM")) {
            return YearMonth.parse(dateStr, formatter);
        }
        // 如果格式是完整日期，提取年月
        else if (pattern.contains("dd") || pattern.contains("DD")) {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return YearMonth.from(date);
        }
        // 如果格式只有年
        else if (pattern.equals("yy") || pattern.equals("yyyy")) {
            int year = Integer.parseInt(dateStr);
            return YearMonth.of(year, 1);
        }
        else {
            // 尝试作为完整日期解析
            try {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                return YearMonth.from(date);
            } catch (DateTimeParseException e) {
                // 如果失败，尝试作为年月解析
                return YearMonth.parse(dateStr, formatter);
            }
        }
    }

    /**
     * 根据日期格式将字符串解析为年份
     */
    private int parseToYear(String dateStr, String format) {
        String pattern = format.replace("YYYY", "yyyy").replace("YY", "yy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 如果格式只有年，直接解析
        if (pattern.equals("yy") || pattern.equals("yyyy")) {
            return Integer.parseInt(dateStr);
        }
        // 如果格式包含年月或完整日期
        else {
            try {
                // 先尝试作为完整日期解析
                LocalDate date = LocalDate.parse(dateStr, formatter);
                return date.getYear();
            } catch (DateTimeParseException e1) {
                try {
                    // 再尝试作为年月解析
                    YearMonth yearMonth = YearMonth.parse(dateStr, formatter);
                    return yearMonth.getYear();
                } catch (DateTimeParseException e2) {
                    // 如果都失败，尝试直接提取年份部分
                    return Integer.parseInt(dateStr.substring(0, Math.min(dateStr.length(), 4)));
                }
            }
        }
    }

    /**
     * 获取当前时间的格式化字符串
     */
    private String getCurrentDateString(String dateFormat) {
        String pattern = dateFormat.replace("YYYY", "yyyy").replace("YY", "yy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 根据格式决定使用哪种时间类型
        if (pattern.contains("dd") || pattern.contains("DD")) {
            return LocalDate.now().format(formatter);
        } else if (pattern.contains("MM")) {
            return YearMonth.now().format(formatter);
        } else if (pattern.equals("yy") || pattern.equals("yyyy")) {
            return String.valueOf(LocalDate.now().getYear());
        } else {
            // 默认使用LocalDate
            return LocalDate.now().format(formatter);
        }
    }

    /**
     * 检查和重置序列号
     */
    private void checkAndResetSequence(SerialNo rule) {
        String currentDate = getCurrentDateString(rule.getDateFormat());

        // 如果最后重置日期为空或者需要重置
        if (rule.getLastResetDate() == null || rule.getLastResetDate().trim().isEmpty() ||
                needReset(rule.getResetStrategy(), rule.getLastResetDate(), rule.getDateFormat())) {
            // 重置时设置为0，这样第一次使用时自增为1
            rule.setCurrentSeq(0L);
            rule.setLastResetDate(currentDate);
        }
    }

    /**
     * 构建流水号
     */
    private String buildSerialNumber(SerialNo rule) {
        StringBuilder sb = new StringBuilder();
        if (rule.getPrefix() != null) sb.append(rule.getPrefix());
        sb.append(getCurrentDateString(rule.getDateFormat()));
        // 使用自增后的序列号生成流水号
        sb.append(String.format("%0" + rule.getSeqLength() + "d", rule.getCurrentSeq()));
        if (rule.getSuffix() != null) sb.append(rule.getSuffix());
        return sb.toString();
    }

    /**
     * 获取流水号（带乐观锁重试机制）
     *
     * @param ruleId 流水号规则主键
     * @return 流水号
     */
    public String getSerialNumber(String ruleId) {
        int retryCount = 3; // 最大重试次数

        while (retryCount-- > 0) {
            SerialNo rule = serialNoMapper.selectSerialNoById(ruleId);
            if (rule == null) {
                throw new RuntimeException("流水号规则不存在: " + ruleId);
            }

            // 保存当前版本号
            Long originalVersion = rule.getVersion();

            // 1. 检查是否需要重置序列
            checkAndResetSequence(rule);

            // 2. 序列号自增（先自增再使用，这样第一个号就是00001）
            rule.setCurrentSeq(rule.getCurrentSeq() + 1);

            // 3. 生成完整流水号
            String number = buildSerialNumber(rule);

            // 4. 设置更新时间
            rule.setUpdateTime(DateUtils.getNowDate());

            // 5. 使用乐观锁更新
            int result = serialNoMapper.updateSerialNoWithVersion(rule);
            if (result > 0) {
                return number;
            }
            // 如果更新失败（版本冲突），继续重试
        }

        throw new RuntimeException("获取流水号失败，请重试");
    }
}