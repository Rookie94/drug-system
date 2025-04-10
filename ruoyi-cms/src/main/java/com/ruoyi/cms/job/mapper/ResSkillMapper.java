package com.ruoyi.cms.job.mapper;

import java.util.Date;
import java.util.List;

import java.util.List;
import com.ruoyi.cms.job.domain.ResSkill;
import org.apache.ibatis.annotations.Param;

/**
 * 技能信息Mapper接口
 *
 * @author admin
 * @date 2025-04-10
 */
public interface ResSkillMapper
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
     * 删除技能信息
     *
     * @param skillid 技能信息主键
     * @return 结果
     */
    public int deleteResSkillBySkillid(Long skillid);

    /**
     * 批量删除技能信息
     *
     * @param skillids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResSkillBySkillids(Long[] skillids);

    /**
     * 修改技能状态
     *
     * @param resSkill 技能
     * @return 结果
     */
    public int updateStatus(ResSkill resSkill);

    /**
     * 审批技能
     *
     * @param Id 技能主键
     * @return 结果
     */
    public int apporById(@Param("Id") Long Id, @Param("apporBy") String apporBy, @Param("apporTime") Date apporTime);

    /**
     * 批量审批技能
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int apporByIds(@Param("Ids") Long[] Ids,@Param("apporBy") String apporBy,@Param("apporTime") Date apporTime);

    /**
     * 反审批技能
     *
     * @param Id 技能主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批技能
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 技能主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);

}
