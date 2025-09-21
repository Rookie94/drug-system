package com.ruoyi.cms.etl.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.etl.mapper.EtlSyncdataMapper;
import com.ruoyi.cms.etl.domain.EtlSyncdata;
import com.ruoyi.cms.etl.service.IEtlSyncdataService;

/**
 * 数据清洗Service业务层处理
 * 
 * @author admin
 * @date 2025-09-21
 */
@Service
public class EtlSyncdataServiceImpl implements IEtlSyncdataService 
{
    @Autowired
    private EtlSyncdataMapper etlSyncdataMapper;

    /**
     * 查询数据清洗
     * 
     * @param syncId 数据清洗主键
     * @return 数据清洗
     */
    @Override
    public EtlSyncdata selectEtlSyncdataBySyncId(Long syncId)
    {
        return etlSyncdataMapper.selectEtlSyncdataBySyncId(syncId);
    }

    /**
     * 查询数据清洗列表
     * 
     * @param etlSyncdata 数据清洗
     * @return 数据清洗
     */
    @Override
    public List<EtlSyncdata> selectEtlSyncdataList(EtlSyncdata etlSyncdata)
    {
        return etlSyncdataMapper.selectEtlSyncdataList(etlSyncdata);
    }

    /**
     * 新增数据清洗
     * 
     * @param etlSyncdata 数据清洗
     * @return 结果
     */
    @Override
    public int insertEtlSyncdata(EtlSyncdata etlSyncdata)
    {
        etlSyncdata.setCreateTime(DateUtils.getNowDate());
        return etlSyncdataMapper.insertEtlSyncdata(etlSyncdata);
    }

    /**
     * 修改数据清洗
     * 
     * @param etlSyncdata 数据清洗
     * @return 结果
     */
    @Override
    public int updateEtlSyncdata(EtlSyncdata etlSyncdata)
    {
        etlSyncdata.setUpdateTime(DateUtils.getNowDate());
        return etlSyncdataMapper.updateEtlSyncdata(etlSyncdata);
    }

    /**
     * 批量删除数据清洗
     * 
     * @param syncIds 需要删除的数据清洗主键
     * @return 结果
     */
    @Override
    public int deleteEtlSyncdataBySyncIds(Long[] syncIds)
    {
        return etlSyncdataMapper.deleteEtlSyncdataBySyncIds(syncIds);
    }

    /**
     * 删除数据清洗信息
     * 
     * @param syncId 数据清洗主键
     * @return 结果
     */
    @Override
    public int deleteEtlSyncdataBySyncId(Long syncId)
    {
        return etlSyncdataMapper.deleteEtlSyncdataBySyncId(syncId);
    }
}
