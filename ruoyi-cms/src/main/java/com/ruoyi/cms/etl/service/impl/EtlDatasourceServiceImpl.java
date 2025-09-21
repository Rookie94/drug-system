package com.ruoyi.cms.etl.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.etl.mapper.EtlDatasourceMapper;
import com.ruoyi.cms.etl.domain.EtlDatasource;
import com.ruoyi.cms.etl.service.IEtlDatasourceService;

/**
 * 数据源管理Service业务层处理
 * 
 * @author admin
 * @date 2025-09-21
 */
@Service
public class EtlDatasourceServiceImpl implements IEtlDatasourceService 
{
    @Autowired
    private EtlDatasourceMapper etlDatasourceMapper;

    /**
     * 查询数据源管理
     * 
     * @param configId 数据源管理主键
     * @return 数据源管理
     */
    @Override
    public EtlDatasource selectEtlDatasourceByConfigId(Long configId)
    {
        return etlDatasourceMapper.selectEtlDatasourceByConfigId(configId);
    }

    /**
     * 查询数据源管理列表
     * 
     * @param etlDatasource 数据源管理
     * @return 数据源管理
     */
    @Override
    public List<EtlDatasource> selectEtlDatasourceList(EtlDatasource etlDatasource)
    {
        return etlDatasourceMapper.selectEtlDatasourceList(etlDatasource);
    }

    /**
     * 新增数据源管理
     * 
     * @param etlDatasource 数据源管理
     * @return 结果
     */
    @Override
    public int insertEtlDatasource(EtlDatasource etlDatasource)
    {
        etlDatasource.setCreateTime(DateUtils.getNowDate());
        return etlDatasourceMapper.insertEtlDatasource(etlDatasource);
    }

    /**
     * 修改数据源管理
     * 
     * @param etlDatasource 数据源管理
     * @return 结果
     */
    @Override
    public int updateEtlDatasource(EtlDatasource etlDatasource)
    {
        etlDatasource.setUpdateTime(DateUtils.getNowDate());
        return etlDatasourceMapper.updateEtlDatasource(etlDatasource);
    }

    /**
     * 批量删除数据源管理
     * 
     * @param configIds 需要删除的数据源管理主键
     * @return 结果
     */
    @Override
    public int deleteEtlDatasourceByConfigIds(Long[] configIds)
    {
        return etlDatasourceMapper.deleteEtlDatasourceByConfigIds(configIds);
    }

    /**
     * 删除数据源管理信息
     * 
     * @param configId 数据源管理主键
     * @return 结果
     */
    @Override
    public int deleteEtlDatasourceByConfigId(Long configId)
    {
        return etlDatasourceMapper.deleteEtlDatasourceByConfigId(configId);
    }
}
