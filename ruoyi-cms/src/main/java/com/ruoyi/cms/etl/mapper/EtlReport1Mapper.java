package com.ruoyi.cms.etl.mapper;

import java.util.List;
import java.util.Map;

import com.ruoyi.cms.etl.domain.EtlReport1;

/**
 * 数据总览Mapper接口
 * 
 * @author admin
 * @date 2025-10-22
 */
public interface EtlReport1Mapper 
{
    /**
     * 查询数据总览
     * 
     * @param id 数据总览主键
     * @return 数据总览
     */
    public EtlReport1 selectEtlReport1ById(Long id);

    /**
     * 查询数据总览列表
     * 
     * @param etlReport1 数据总览
     * @return 数据总览集合
     */
    public List<EtlReport1> selectEtlReport1List(EtlReport1 etlReport1);


    /**
     * 近30天 每日成功登录用户数（去重）
     */
    public List<Map<String, Object>> selectLoginTrend();

    /**
     * 新增数据总览
     * 
     * @param etlReport1 数据总览
     * @return 结果
     */
    public int insertEtlReport1(EtlReport1 etlReport1);

    /**
     * 修改数据总览
     * 
     * @param etlReport1 数据总览
     * @return 结果
     */
    public int updateEtlReport1(EtlReport1 etlReport1);

    /**
     * 删除数据总览
     * 
     * @param id 数据总览主键
     * @return 结果
     */
    public int deleteEtlReport1ById(Long id);

    /**
     * 批量删除数据总览
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEtlReport1ByIds(Long[] ids);
}
