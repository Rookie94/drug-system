package com.ruoyi.cms.offline.service.impl;

import java.util.List;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesTmsdataMapper;
import com.ruoyi.cms.offline.domain.vo.ActivitiesTmsdataVo;
import com.ruoyi.cms.offline.domain.ActivitiesTmsdata;
import com.ruoyi.cms.offline.service.IActivitiesTmsdataService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 现场经颅磁Service业务层处理
 * 
 * @author admin
 * @date 2025-05-09
 */
@Service
public class ActivitiesTmsdataServiceImpl implements IActivitiesTmsdataService 
{
    @Autowired
    private ActivitiesTmsdataMapper activitiesTmsdataMapper;

    /**
     * 查询现场经颅磁
     * 
     * @param analyzeId 现场经颅磁主键
     * @return 现场经颅磁
     */
    @Override
    public ActivitiesTmsdataVo selectActivitiesTmsdataByAnalyzeId(Long analyzeId)
    {
        return activitiesTmsdataMapper.selectActivitiesTmsdataByAnalyzeId(analyzeId);
    }

    /**
     * 查询现场经颅磁列表
     * 
     * @param activitiesTmsdata 现场经颅磁
     * @return 现场经颅磁
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ActivitiesTmsdataVo> selectActivitiesTmsdataList(ActivitiesTmsdataVo activitiesTmsdata)
    {
        return activitiesTmsdataMapper.selectActivitiesTmsdataList(activitiesTmsdata);
    }

    /**
     * 新增现场经颅磁
     * 
     * @param activitiesTmsdata 现场经颅磁
     * @return 结果
     */
    @Override
    public int insertActivitiesTmsdata(ActivitiesTmsdata activitiesTmsdata)
    {
        //activitiesTmsdata.setUserId(getUserId());
        //activitiesTmsdata.setDeptId(getDeptId());
        activitiesTmsdata.setCreateBy(getUsername());
        activitiesTmsdata.setCreateTime(DateUtils.getNowDate());
        return activitiesTmsdataMapper.insertActivitiesTmsdata(activitiesTmsdata);
    }

    /**
     * 修改现场经颅磁
     * 
     * @param activitiesTmsdata 现场经颅磁
     * @return 结果
     */
    @Override
    public int updateActivitiesTmsdata(ActivitiesTmsdata activitiesTmsdata)
    {
        activitiesTmsdata.setUpdateBy(getUsername());
        activitiesTmsdata.setUpdateTime(DateUtils.getNowDate());
        return activitiesTmsdataMapper.updateActivitiesTmsdata(activitiesTmsdata);
    }

    /**
     * 批量删除现场经颅磁
     * 
     * @param analyzeIds 需要删除的现场经颅磁主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesTmsdataByAnalyzeIds(Long[] analyzeIds)
    {
        return activitiesTmsdataMapper.deleteActivitiesTmsdataByAnalyzeIds(analyzeIds);
    }

    /**
     * 删除现场经颅磁信息
     * 
     * @param analyzeId 现场经颅磁主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesTmsdataByAnalyzeId(Long analyzeId)
    {
        return activitiesTmsdataMapper.deleteActivitiesTmsdataByAnalyzeId(analyzeId);
    }
}
