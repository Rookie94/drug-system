package com.ruoyi.cms.offline.service.impl;

import java.util.List;

import com.ruoyi.cms.offline.domain.vo.ActivitiesSignUpVo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesSignUpMapper;
import com.ruoyi.cms.offline.domain.ActivitiesSignUp;
import com.ruoyi.cms.offline.service.IActivitiesSignUpService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 预约详情Service业务层处理
 * 
 * @author admin
 * @date 2025-04-28
 */
@Service
public class ActivitiesSignUpServiceImpl implements IActivitiesSignUpService
{
    @Autowired
    private ActivitiesSignUpMapper signUpMapper;

    /**
     * 查询预约详情
     * 
     * @param signId 预约详情主键
     * @return 预约详情
     */
    @Override
    public ActivitiesSignUpVo selectSignUpBySignId(Long signId)
    {
        return signUpMapper.selectSignUpBySignId(signId);
    }

    /**
     * 查询预约详情列表
     * 
     * @param signUp 预约详情
     * @return 预约详情
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ActivitiesSignUpVo> selectSignUpList(ActivitiesSignUpVo signUp)
    {
        return signUpMapper.selectSignUpList(signUp);
    }

    /**
     * 新增预约详情
     * 
     * @param signUp 预约详情
     * @return 结果
     */
    @Override
    public int insertSignUp(ActivitiesSignUp signUp)
    {
        signUp.setUserId(getUserId());
        signUp.setDeptId(getDeptId());
        signUp.setCreateBy(getUsername());
        signUp.setCreateTime(DateUtils.getNowDate());
        signUp.setSignTime(DateUtils.getNowDate());
        return signUpMapper.insertSignUp(signUp);
    }

    /**
     * 修改预约详情
     * 
     * @param signUp 预约详情
     * @return 结果
     */
    @Override
    public int updateSignUp(ActivitiesSignUp signUp)
    {
        signUp.setUpdateBy(getUsername());
        signUp.setUpdateTime(DateUtils.getNowDate());
        return signUpMapper.updateSignUp(signUp);
    }

    /**
     * 批量删除预约详情
     * 
     * @param signIds 需要删除的预约详情主键
     * @return 结果
     */
    @Override
    public int deleteSignUpBySignIds(Long[] signIds)
    {
        return signUpMapper.deleteSignUpBySignIds(signIds);
    }

    /**
     * 删除预约详情信息
     * 
     * @param signId 预约详情主键
     * @return 结果
     */
    @Override
    public int deleteSignUpBySignId(Long signId)
    {
        return signUpMapper.deleteSignUpBySignId(signId);
    }
}
