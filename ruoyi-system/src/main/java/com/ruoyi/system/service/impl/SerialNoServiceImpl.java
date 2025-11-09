package com.ruoyi.system.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        // 使用规则中的日期格式设置最后重置日期
        String currentDate = LocalDate.now().format(getDateFormatter(serialNo.getDateFormat()));
        serialNo.setLastResetDate(currentDate);
        // 设置初始序列号为0，这样第一次使用会自增为1
        serialNo.setCurrentSeq(0L);
        serialNo.setCreateTime(DateUtils.getNowDate());
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

    private boolean needReset(String strategy, String lastDate, String dateFormat) {
        if (lastDate == null) return true;

        LocalDate last = LocalDate.parse(lastDate, getDateFormatter(dateFormat));
        LocalDate now = LocalDate.now();

        switch (strategy) {
            case "DAILY": return last.isBefore(now);
            case "MONTHLY": return last.getMonthValue() != now.getMonthValue() || last.getYear() != now.getYear();
            case "YEARLY": return last.getYear() != now.getYear();
            case "NEVER": return false;
            default: return false;
        }
    }

    private void checkAndResetSequence(SerialNo rule) {
        String currentDate = LocalDate.now().format(getDateFormatter(rule.getDateFormat()));

        // 如果最后重置日期为空或者需要重置
        if (rule.getLastResetDate() == null || needReset(rule.getResetStrategy(), rule.getLastResetDate(), rule.getDateFormat())) {
            // 重置时设置为0，这样第一次使用时自增为1
            rule.setCurrentSeq(0L);
            rule.setLastResetDate(currentDate);
        }
    }

    private String buildSerialNumber(SerialNo rule) {
        StringBuilder sb = new StringBuilder();
        if (rule.getPrefix() != null) sb.append(rule.getPrefix());
        sb.append(LocalDate.now().format(getDateFormatter(rule.getDateFormat())));
        // 使用自增后的序列号生成流水号
        sb.append(String.format("%0" + rule.getSeqLength() + "d", rule.getCurrentSeq()));
        if (rule.getSuffix() != null) sb.append(rule.getSuffix());
        return sb.toString();
    }

    private DateTimeFormatter getDateFormatter(String format) {
        // 统一日期格式处理
        String pattern = format.replace("YYYY", "yyyy").replace("YY", "yy");
        return DateTimeFormatter.ofPattern(pattern);
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