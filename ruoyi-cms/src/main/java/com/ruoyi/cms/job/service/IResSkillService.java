package com.ruoyi.cms.job.service;

import java.util.List;
import com.ruoyi.cms.job.domain.ResSkill;

/**
 * 技能信息Service接口
 *
 * @author admin
 * @date 2025-04-10
 */
public interface IResSkillService
{
    /**
     * 查询技能信息
     *
     * @param skillid 技能信息主键
     * @return 技能信息
     */
    public ResSkill selectResSkillBySkillid(Long skillid);

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
     * 批量删除技能信息
     *
     * @param skillids 需要删除的技能信息主键集合
     * @return 结果
     */
    public int deleteResSkillBySkillids(Long[] skillids);

    /**
     * 删除技能信息信息
     *
     * @param skillid 技能信息主键
     * @return 结果
     */
    public int deleteResSkillBySkillid(Long skillid);

    /**
     * 修改技能状态
     *
     * @param resSkill 技能
     * @return 结果
     */
    public int updateStatus(ResSkill resSkill);

    /**
     * 批量审批技能
     *
     * @param ids 需要删除的技能主键集合
     * @return 结果
     */
    public int apporByIds(Long[] ids);

    /**
     * 审批技能信息
     *
     * @param id 技能主键
     * @return 结果
     */
    public int apporById(Long id);

    /**
     * 批量反审批技能
     *
     * @param ids 需要删除的技能主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] ids);

    /**
     * 反审批技能信息
     *
     * @param ids 技能主键
     * @return 结果
     */
    public int unApporById(Long ids);

    /**
     * 查询已审核的单据清单
     *
     * @param ids 技能主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids);

}
