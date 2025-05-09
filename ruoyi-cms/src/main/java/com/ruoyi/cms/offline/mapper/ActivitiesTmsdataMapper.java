package com.ruoyi.cms.offline.mapper;

import java.util.List;

import com.ruoyi.cms.offline.domain.ActivitiesTmsdata;
import com.ruoyi.cms.offline.domain.vo.ActivitiesTmsdataVo;

/**
 * 现场经颅磁Mapper接口
 * 
 * @author admin
 * @date 2025-05-09
 */
public interface ActivitiesTmsdataMapper 
{
    /**
     * 查询现场经颅磁
     * 
     * @param analyzeId 现场经颅磁主键
     * @return 现场经颅磁
     */
    public ActivitiesTmsdataVo selectActivitiesTmsdataByAnalyzeId(Long analyzeId);

    /**
     * 查询现场经颅磁列表
     * 
     * @param activitiesTmsdata 现场经颅磁
     * @return 现场经颅磁集合
     */
    public List<ActivitiesTmsdataVo> selectActivitiesTmsdataList(ActivitiesTmsdataVo activitiesTmsdata);

    /**
     * 新增现场经颅磁
     * 
     * @param activitiesTmsdata 现场经颅磁
     * @return 结果
     */
    public int insertActivitiesTmsdata(ActivitiesTmsdata activitiesTmsdata);

    /**
     * 修改现场经颅磁
     * 
     * @param activitiesTmsdata 现场经颅磁
     * @return 结果
     */
    public int updateActivitiesTmsdata(ActivitiesTmsdata activitiesTmsdata);

    /**
     * 删除现场经颅磁
     * 
     * @param analyzeId 现场经颅磁主键
     * @return 结果
     */
    public int deleteActivitiesTmsdataByAnalyzeId(Long analyzeId);

    /**
     * 批量删除现场经颅磁
     * 
     * @param analyzeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteActivitiesTmsdataByAnalyzeIds(Long[] analyzeIds);
}
