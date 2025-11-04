package com.ruoyi.cms.etl.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.etl.mapper.EtlReport1Mapper;
import com.ruoyi.cms.etl.domain.EtlReport1;
import com.ruoyi.cms.etl.service.IEtlReport1Service;

/**
 * 数据总览Service业务层处理
 * 
 * @author admin
 * @date 2025-10-22
 */
@Service
public class EtlReport1ServiceImpl implements IEtlReport1Service 
{
    @Autowired
    private EtlReport1Mapper etlReport1Mapper;

    /**
     * 查询数据总览
     * 
     * @param id 数据总览主键
     * @return 数据总览
     */
    @Override
    public EtlReport1 selectEtlReport1ById(Long id)
    {
        return etlReport1Mapper.selectEtlReport1ById(id);
    }

    /**
     * 查询数据总览列表
     * 
     * @param etlReport1 数据总览
     * @return 数据总览
     */
    @Override
    public List<EtlReport1> selectEtlReport1List(EtlReport1 etlReport1)
    {
        return etlReport1Mapper.selectEtlReport1List(etlReport1);
    }

    @Override
    public Map<String, Object> selectLoginTrend() {
        List<Map<String, Object>> list = etlReport1Mapper.selectLoginTrend();
        List<String> days = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        // 使用 SimpleDateFormat 确保日期格式一致
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

        // 构建数据映射
        Map<String, Long> map = new HashMap<>();
        for (Map<String, Object> m : list) {
            Object dayObj = m.get("day");
            if (dayObj != null) {
                try {
                    Date date;
                    if (dayObj instanceof java.sql.Date) {
                        date = new Date(((java.sql.Date) dayObj).getTime());
                    } else if (dayObj instanceof String) {
                        // 如果是字符串，尝试解析
                        String dayStr = (String) dayObj;
                        if (dayStr.contains("-")) {
                            // 处理 "yyyy-MM-dd" 格式
                            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                            date = dbFormat.parse(dayStr);
                        } else {
                            // 已经是 "MM/dd" 格式
                            date = sdf.parse(dayStr);
                        }
                    } else {
                        continue;
                    }
                    String key = sdf.format(date);
                    map.put(key, ((Number) m.get("cnt")).longValue());
                } catch (Exception e) {
                    // 日期解析失败，跳过该记录
                    continue;
                }
            }
        }

        //补全近30天
        Calendar c = Calendar.getInstance();
        for (int i = 29; i >= 0; i--) {
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, -i);
            String monthDay = sdf.format(c.getTime());
            days.add(monthDay);
            data.add(map.getOrDefault(monthDay, 0L));
        }

        Map<String, Object> result = new HashMap<>(2);
        result.put("days", days);
        result.put("data", data);
        return result;
    }

    /**
     * 新增数据总览
     * 
     * @param etlReport1 数据总览
     * @return 结果
     */
    @Override
    public int insertEtlReport1(EtlReport1 etlReport1)
    {
        return etlReport1Mapper.insertEtlReport1(etlReport1);
    }

    /**
     * 修改数据总览
     * 
     * @param etlReport1 数据总览
     * @return 结果
     */
    @Override
    public int updateEtlReport1(EtlReport1 etlReport1)
    {
        etlReport1.setUpdateTime(DateUtils.getNowDate());
        return etlReport1Mapper.updateEtlReport1(etlReport1);
    }

    /**
     * 批量删除数据总览
     * 
     * @param ids 需要删除的数据总览主键
     * @return 结果
     */
    @Override
    public int deleteEtlReport1ByIds(Long[] ids)
    {
        return etlReport1Mapper.deleteEtlReport1ByIds(ids);
    }

    /**
     * 删除数据总览信息
     * 
     * @param id 数据总览主键
     * @return 结果
     */
    @Override
    public int deleteEtlReport1ById(Long id)
    {
        return etlReport1Mapper.deleteEtlReport1ById(id);
    }
}
