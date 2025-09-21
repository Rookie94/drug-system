package com.ruoyi.cms.etl.mapper;

import java.util.List;
import com.ruoyi.cms.etl.domain.EtlDatasource;

/**
 * 数据源管理Mapper接口
 * 
 * @author admin
 * @date 2025-09-21
 */
public interface EtlDatasourceMapper 
{
    /**
     * 查询数据源管理
     * 
     * @param configId 数据源管理主键
     * @return 数据源管理
     */
    public EtlDatasource selectEtlDatasourceByConfigId(Long configId);

    /**
     * 查询数据源管理列表
     * 
     * @param etlDatasource 数据源管理
     * @return 数据源管理集合
     */
    public List<EtlDatasource> selectEtlDatasourceList(EtlDatasource etlDatasource);

    /**
     * 新增数据源管理
     * 
     * @param etlDatasource 数据源管理
     * @return 结果
     */
    public int insertEtlDatasource(EtlDatasource etlDatasource);

    /**
     * 修改数据源管理
     * 
     * @param etlDatasource 数据源管理
     * @return 结果
     */
    public int updateEtlDatasource(EtlDatasource etlDatasource);

    /**
     * 删除数据源管理
     * 
     * @param configId 数据源管理主键
     * @return 结果
     */
    public int deleteEtlDatasourceByConfigId(Long configId);

    /**
     * 批量删除数据源管理
     * 
     * @param configIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEtlDatasourceByConfigIds(Long[] configIds);
}
