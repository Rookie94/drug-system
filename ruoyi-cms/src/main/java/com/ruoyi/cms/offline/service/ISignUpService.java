package com.ruoyi.cms.offline.service;

import java.util.List;
import com.ruoyi.cms.offline.domain.SignUp;
import com.ruoyi.cms.offline.domain.vo.SignUpVo;

/**
 * 预约详情Service接口
 * 
 * @author admin
 * @date 2025-04-28
 */
public interface ISignUpService 
{
    /**
     * 查询预约详情
     * 
     * @param signId 预约详情主键
     * @return 预约详情
     */
    public SignUpVo selectSignUpBySignId(Long signId);

    /**
     * 查询预约详情列表
     * 
     * @param signUp 预约详情
     * @return 预约详情集合
     */
    public List<SignUpVo> selectSignUpList(SignUpVo signUp);

    /**
     * 新增预约详情
     * 
     * @param signUp 预约详情
     * @return 结果
     */
    public int insertSignUp(SignUp signUp);

    /**
     * 修改预约详情
     * 
     * @param signUp 预约详情
     * @return 结果
     */
    public int updateSignUp(SignUp signUp);

    /**
     * 批量删除预约详情
     * 
     * @param signIds 需要删除的预约详情主键集合
     * @return 结果
     */
    public int deleteSignUpBySignIds(Long[] signIds);

    /**
     * 删除预约详情信息
     * 
     * @param signId 预约详情主键
     * @return 结果
     */
    public int deleteSignUpBySignId(Long signId);
}
