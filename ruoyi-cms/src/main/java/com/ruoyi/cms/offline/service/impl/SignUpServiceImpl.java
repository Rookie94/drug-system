package com.ruoyi.cms.offline.service.impl;

import java.util.List;

import com.ruoyi.cms.offline.domain.vo.SignUpVo;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.SignUpMapper;
import com.ruoyi.cms.offline.domain.SignUp;
import com.ruoyi.cms.offline.service.ISignUpService;

/**
 * 预约详情Service业务层处理
 * 
 * @author admin
 * @date 2025-04-28
 */
@Service
public class SignUpServiceImpl implements ISignUpService 
{
    @Autowired
    private SignUpMapper signUpMapper;

    /**
     * 查询预约详情
     * 
     * @param signId 预约详情主键
     * @return 预约详情
     */
    @Override
    public SignUpVo selectSignUpBySignId(Long signId)
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
    public List<SignUpVo> selectSignUpList(SignUpVo signUp)
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
    public int insertSignUp(SignUp signUp)
    {
        signUp.setCreateTime(DateUtils.getNowDate());
        return signUpMapper.insertSignUp(signUp);
    }

    /**
     * 修改预约详情
     * 
     * @param signUp 预约详情
     * @return 结果
     */
    @Override
    public int updateSignUp(SignUp signUp)
    {
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
