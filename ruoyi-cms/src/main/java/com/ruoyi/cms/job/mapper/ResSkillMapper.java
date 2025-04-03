package com.ruoyi.cms.job.mapper;

import java.util.List;
import com.ruoyi.cms.job.domain.ResSkill;

/**
 * 技能信息Mapper接口
 * 
 * @author admin
 * @date 2025-04-03
 */
public interface ResSkillMapper 
{
    /**
     * 查询技能信息
     * 
     * @param skillId 技能信息主键
     * @return 技能信息
     */
    public ResSkill selectResSkillBySkillId(Integer skillId);

    /**
     * 查询技能信息列表
     * 
     * @param resSkill 技能信息
     * @return 技能信息集合
     */
    public List<ResSkill> selectResSkillList(ResSkill resSkill);

    /**
     * 新增技能信息
     * 
     * @param resSkill 技能信息
     * @return 结果
     */
    public int insertResSkill(ResSkill resSkill);

    /**
     * 修改技能信息
     * 
     * @param resSkill 技能信息
     * @return 结果
     */
    public int updateResSkill(ResSkill resSkill);

    /**
     * 删除技能信息
     * 
     * @param skillId 技能信息主键
     * @return 结果
     */
    public int deleteResSkillBySkillId(Integer skillId);

    /**
     * 批量删除技能信息
     * 
     * @param skillIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResSkillBySkillIds(Integer[] skillIds);
}
