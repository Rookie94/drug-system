package com.ruoyi.cms.job.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.job.mapper.ResSkillMapper;
import com.ruoyi.cms.job.domain.ResSkill;
import com.ruoyi.cms.job.service.IResSkillService;

/**
 * 技能信息Service业务层处理
 * 
 * @author admin
 * @date 2025-04-03
 */
@Service
public class ResSkillServiceImpl implements IResSkillService 
{
    @Autowired
    private ResSkillMapper resSkillMapper;

    /**
     * 查询技能信息
     * 
     * @param skillId 技能信息主键
     * @return 技能信息
     */
    @Override
    public ResSkill selectResSkillBySkillId(Integer skillId)
    {
        return resSkillMapper.selectResSkillBySkillId(skillId);
    }

    /**
     * 查询技能信息列表
     * 
     * @param resSkill 技能信息
     * @return 技能信息
     */
    @Override
    public List<ResSkill> selectResSkillList(ResSkill resSkill)
    {
        return resSkillMapper.selectResSkillList(resSkill);
    }

    /**
     * 新增技能信息
     * 
     * @param resSkill 技能信息
     * @return 结果
     */
    @Override
    public int insertResSkill(ResSkill resSkill)
    {
        resSkill.setCreateTime(DateUtils.getNowDate());
        return resSkillMapper.insertResSkill(resSkill);
    }

    /**
     * 修改技能信息
     * 
     * @param resSkill 技能信息
     * @return 结果
     */
    @Override
    public int updateResSkill(ResSkill resSkill)
    {
        resSkill.setUpdateTime(DateUtils.getNowDate());
        return resSkillMapper.updateResSkill(resSkill);
    }

    /**
     * 批量删除技能信息
     * 
     * @param skillIds 需要删除的技能信息主键
     * @return 结果
     */
    @Override
    public int deleteResSkillBySkillIds(Integer[] skillIds)
    {
        return resSkillMapper.deleteResSkillBySkillIds(skillIds);
    }

    /**
     * 删除技能信息信息
     * 
     * @param skillId 技能信息主键
     * @return 结果
     */
    @Override
    public int deleteResSkillBySkillId(Integer skillId)
    {
        return resSkillMapper.deleteResSkillBySkillId(skillId);
    }
}
