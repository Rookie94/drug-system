package com.ruoyi.cms.job.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.job.mapper.ResSkillMapper;
import com.ruoyi.cms.job.domain.ResSkill;
import com.ruoyi.cms.job.service.IResSkillService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 技能信息Service业务层处理
 *
 * @author admin
 * @date 2025-04-10
 */
@Service
public class ResSkillServiceImpl implements IResSkillService
{
    @Autowired
    private ResSkillMapper resSkillMapper;

    /**
     * 查询技能信息
     *
     * @param skillid 技能信息主键
     * @return 技能信息
     */
    @Override
    public ResSkill selectResSkillBySkillid(Long skillid)
    {
        return resSkillMapper.selectResSkillBySkillid(skillid);
    }

    /**
     * 查询技能信息列表
     *
     * @param resSkill 技能信息
     * @return 技能信息
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
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
        resSkill.setUserId(getUserId());
        resSkill.setDeptId(getDeptId());
        resSkill.setCreateBy(getUsername());
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
        resSkill.setUpdateBy(getUsername());
        resSkill.setUpdateTime(DateUtils.getNowDate());
        return resSkillMapper.updateResSkill(resSkill);
    }

    /**
     * 批量删除技能信息
     *
     * @param skillids 需要删除的技能信息主键
     * @return 结果
     */
    @Override
    public int deleteResSkillBySkillids(Long[] skillids)
    {
        return resSkillMapper.deleteResSkillBySkillids(skillids);
    }

    /**
     * 删除技能信息信息
     *
     * @param skillid 技能信息主键
     * @return 结果
     */
    @Override
    public int deleteResSkillBySkillid(Long skillid)
    {
        return resSkillMapper.deleteResSkillBySkillid(skillid);
    }

    /**
     * 修改技能状态
     *
     * @param resSkill 技能
     * @return 结果
     */
    public int updateStatus(ResSkill resSkill)
    {
        resSkill.setUpdateBy(getUsername());
        resSkill.setUpdateTime(DateUtils.getNowDate());
        return resSkillMapper.updateStatus(resSkill);
    }

    /**
     * 批量审批
     *
     * @param apporParams 审批参数
     * @return 结果
     */
    @Override
    public int apporByIds(ResApporParam apporParams)
    {
        apporParams.setApporBy(getUsername());
        apporParams.setApporTime(DateUtils.getNowDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(apporParams.flag==2){
            apporParams.setPublishTime(dateFormat.format(DateUtils.getNowDate()));
        }
        return resSkillMapper.apporByIds(apporParams);
    }

    /**
     * 反审批技能
     *
     * @param id 技能主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return resSkillMapper.unApporById(id);
    }

    /**
     * 批量反审批技能
     *
     * @param ids 需要删除的技能主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return resSkillMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 技能主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resSkillMapper.selectApporedByIds(ids);
    }



}
