package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysAreaMapper;
import com.ruoyi.system.domain.SysArea;
import com.ruoyi.system.service.ISysAreaService;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 行政区域Service业务层处理
 * 
 * @author admin
 * @date 2025-05-08
 */
@Service
public class SysAreaServiceImpl implements ISysAreaService 
{
    @Autowired
    private SysAreaMapper sysAreaMapper;

    /**
     * 查询行政区域
     * 
     * @param id 行政区域主键
     * @return 行政区域
     */
    @Override
    public SysArea selectSysAreaById(Long id)
    {
        return sysAreaMapper.selectSysAreaById(id);
    }

    /**
     * 查询行政区域列表
     * 
     * @param sysArea 行政区域
     * @return 行政区域
     */
    @Override
    public List<SysArea> selectSysAreaList(SysArea sysArea)
    {
        return sysAreaMapper.selectSysAreaList(sysArea);
    }

    /**
     * 查询行政区域列表
     *
     * @param sysArea 行政区域
     * @return 行政区域集合
     */
    public List<SysArea> selectSysAreaWithChild(SysArea sysArea){
        return sysAreaMapper.selectSysAreaWithChild(sysArea);
    }

    /**
     * 新增行政区域
     * 
     * @param sysArea 行政区域
     * @return 结果
     */
    @Override
    public int insertSysArea(SysArea sysArea)
    {
        sysArea.setCreateBy(getUsername());
        sysArea.setCreateTime(DateUtils.getNowDate());
        return sysAreaMapper.insertSysArea(sysArea);
    }

    /**
     * 修改行政区域
     * 
     * @param sysArea 行政区域
     * @return 结果
     */
    @Override
    public int updateSysArea(SysArea sysArea)
    {
        sysArea.setUpdateBy(getUsername());
        sysArea.setUpdateTime(DateUtils.getNowDate());
        return sysAreaMapper.updateSysArea(sysArea);
    }


    /**
     * 修改状态
     *
     * @param sysArea 戒治机构
     * @return 结果
     */
    @Override
    public int updateStatus(SysArea sysArea){
        sysArea.setUpdateBy(getUsername());
        sysArea.setUpdateTime(DateUtils.getNowDate());
        return sysAreaMapper.updateStatus(sysArea);
    }

    /**
     * 批量删除行政区域
     * 
     * @param ids 需要删除的行政区域主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaByIds(Long[] ids)
    {
        return sysAreaMapper.deleteSysAreaByIds(ids);
    }

    /**
     * 删除行政区域信息
     * 
     * @param id 行政区域主键
     * @return 结果
     */
    @Override
    public int deleteSysAreaById(Long id)
    {
        return sysAreaMapper.deleteSysAreaById(id);
    }
}
