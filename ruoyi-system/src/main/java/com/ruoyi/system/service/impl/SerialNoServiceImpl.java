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


    private boolean needReset(String strategy, String lastDate) {
        LocalDate last = LocalDate.parse(lastDate, DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate now = LocalDate.now();

        switch (strategy) {
            case "DAILY": return last.isBefore(now);
            case "MONTHLY": return last.getMonthValue() != now.getMonthValue();
            case "YEARLY": return last.getYear() != now.getYear();
            default: return false;
        }
    }

    private void checkAndResetSequence(SerialNo rule) {
        String currentDate = LocalDate.now().format(getDateFormatter(rule.getDateFormat()));

        if (!currentDate.equals(rule.getLastResetDate())) {
            if (needReset(rule.getResetStrategy(), rule.getLastResetDate())) {
                rule.setCurrentSeq(0L);
                rule.setLastResetDate(currentDate);
            }
        }
    }

    private String buildSerialNumber(SerialNo rule) {
        StringBuilder sb = new StringBuilder();
        if (rule.getPrefix() != null) sb.append(rule.getPrefix());
        sb.append(LocalDate.now().format(getDateFormatter(rule.getDateFormat())));
        sb.append(String.format("%0" + rule.getSeqLength() + "d", rule.getCurrentSeq()));
        if (rule.getSuffix() != null) sb.append(rule.getSuffix());
        return sb.toString();
    }

    private DateTimeFormatter getDateFormatter(String format) {
        return DateTimeFormatter.ofPattern(format.replace("YYYY", "yyyy").replace("YY", "yy"));
    }

    /**
     * 获取流水号
     *
     * @param ruleId 流水号规则主键
     * @return 流水号
     */
    public String getSerialNumber(String ruleId) {
        SerialNo rule = serialNoMapper.selectSerialNoById(ruleId);
        // 1. 检查是否需要重置序列
        checkAndResetSequence(rule);
        // 2. 生成完整流水号
        String number = buildSerialNumber(rule);
        // 3. 更新序列号（带乐观锁）
        rule.setCurrentSeq(rule.getCurrentSeq() + 1);
        serialNoMapper.updateSerialNo(rule);
        return number;
    }

}
