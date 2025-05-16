package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SerialNo;

/**
 * 流水号管理Mapper接口
 * 
 * @author admin
 * @date 2025-05-16
 */
public interface SerialNoMapper 
{
    /**
     * 查询流水号管理
     * 
     * @param id 流水号管理主键
     * @return 流水号管理
     */
    public SerialNo selectSerialNoById(String id);

    /**
     * 查询流水号管理列表
     * 
     * @param serialNo 流水号管理
     * @return 流水号管理集合
     */
    public List<SerialNo> selectSerialNoList(SerialNo serialNo);

    /**
     * 新增流水号管理
     * 
     * @param serialNo 流水号管理
     * @return 结果
     */
    public int insertSerialNo(SerialNo serialNo);

    /**
     * 修改流水号管理
     * 
     * @param serialNo 流水号管理
     * @return 结果
     */
    public int updateSerialNo(SerialNo serialNo);

    /**
     * 删除流水号管理
     * 
     * @param id 流水号管理主键
     * @return 结果
     */
    public int deleteSerialNoById(String id);

    /**
     * 批量删除流水号管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSerialNoByIds(String[] ids);
}
