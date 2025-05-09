package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysArea;

/**
 * 行政区域Mapper接口
 * 
 * @author admin
 * @date 2025-05-08
 */
public interface SysAreaMapper 
{
    /**
     * 查询行政区域
     * 
     * @param id 行政区域主键
     * @return 行政区域
     */
    public SysArea selectSysAreaById(Long id);

    /**
     * 查询行政区域列表
     * 
     * @param sysArea 行政区域
     * @return 行政区域集合
     */
    public List<SysArea> selectSysAreaList(SysArea sysArea);

    /**
     * 查询行政区域列表
     *
     * @param sysArea 行政区域
     * @return 行政区域集合
     */
    public List<SysArea> selectSysAreaWithChild(SysArea sysArea);

    /**
     * 新增行政区域
     * 
     * @param sysArea 行政区域
     * @return 结果
     */
    public int insertSysArea(SysArea sysArea);

    /**
     * 修改行政区域
     * 
     * @param sysArea 行政区域
     * @return 结果
     */
    public int updateSysArea(SysArea sysArea);

    /**
     * 修改状态
     *
     * @param sysArea 戒治机构
     * @return 结果
     */
    public int updateStatus(SysArea sysArea);

    /**
     * 删除行政区域
     * 
     * @param id 行政区域主键
     * @return 结果
     */
    public int deleteSysAreaById(Long id);

    /**
     * 批量删除行政区域
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAreaByIds(Long[] ids);
}
