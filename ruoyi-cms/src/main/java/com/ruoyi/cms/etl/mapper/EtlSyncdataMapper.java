package com.ruoyi.cms.etl.mapper;

import java.util.List;
import com.ruoyi.cms.etl.domain.EtlSyncdata;

/**
 * 数据清洗Mapper接口
 * 
 * @author admin
 * @date 2025-09-21
 */
public interface EtlSyncdataMapper 
{
    /**
     * 查询数据清洗
     * 
     * @param syncId 数据清洗主键
     * @return 数据清洗
     */
    public EtlSyncdata selectEtlSyncdataBySyncId(Long syncId);

    /**
     * 查询数据清洗列表
     * 
     * @param etlSyncdata 数据清洗
     * @return 数据清洗集合
     */
    public List<EtlSyncdata> selectEtlSyncdataList(EtlSyncdata etlSyncdata);

    /**
     * 新增数据清洗
     * 
     * @param etlSyncdata 数据清洗
     * @return 结果
     */
    public int insertEtlSyncdata(EtlSyncdata etlSyncdata);

    /**
     * 修改数据清洗
     * 
     * @param etlSyncdata 数据清洗
     * @return 结果
     */
    public int updateEtlSyncdata(EtlSyncdata etlSyncdata);

    /**
     * 删除数据清洗
     * 
     * @param syncId 数据清洗主键
     * @return 结果
     */
    public int deleteEtlSyncdataBySyncId(Long syncId);

    /**
     * 批量删除数据清洗
     * 
     * @param syncIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEtlSyncdataBySyncIds(Long[] syncIds);
}
