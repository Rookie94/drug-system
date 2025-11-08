package com.ruoyi.cms.common.service;

import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.List;
import java.util.Map;

/**
 * 通用选择服务接口
 */
public interface ICommonSelectService {

    /**
     * 查询用户列表
     *
     * @param params 查询参数
     * @return 用户列表
     */
    List<SysUser> selectUserList(Map<String, Object> params);

    /**
     * 查询部门列表
     *
     * @param params 查询参数
     * @return 部门列表
     */
    List<SysDept> selectDeptList(Map<String, Object> params);

    /**
     * 查询角色列表
     *
     * @param params 查询参数
     * @return 角色列表
     */
    List<SysRole> selectRoleList(Map<String, Object> params);

    /**
     * 查询问卷列表
     *
     * @param params 查询参数
     * @return 问卷列表
     */
    List<Survey> selectSurveyList(Map<String, Object> params);

    /**
     * 根据数据类型查询对应列表
     *
     * @param dataType 数据类型
     * @param params 查询参数
     * @return 数据列表
     */
    List<?> selectDataListByType(String dataType, Map<String, Object> params);
}
