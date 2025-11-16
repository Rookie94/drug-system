package com.ruoyi.cms.common.service.impl;

import com.ruoyi.cms.common.service.ICommonSelectService;
import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.cms.survey.mapper.SurveyMapper;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通用选择服务实现
 */
@Service
public class CommonSelectServiceImpl implements ICommonSelectService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SurveyMapper surveyMapper;

    /**
     * 查询用户列表
     */
    @Override
    public List<SysUser> selectUserList(Map<String, Object> params) {
        // 构建查询参数
        SysUser user = new SysUser();
        user.setParams(params);
        // 可根据需要设置其他查询条件
        if (params.containsKey("userName")) {
            user.setUserName((String) params.get("userName"));
        }
        if (params.containsKey("phonenumber")) {
            user.setPhoneNumber((String) params.get("phonenumber"));
        }
        if (params.containsKey("status")) {
            user.setStatus((String) params.get("status"));
        }
        if (params.containsKey("deptId")) {
            user.setDeptId(Long.valueOf(params.get("deptId").toString()));
        }
        return sysUserMapper.selectUserList(user);
    }

    /**
     * 查询部门列表
     */
    @Override
    public List<SysDept> selectDeptList(Map<String, Object> params) {
        SysDept dept = new SysDept();
        dept.setParams(params);
        // 设置查询条件
        if (params.containsKey("deptName")) {
            dept.setDeptName((String) params.get("deptName"));
        }
        if (params.containsKey("status")) {
            dept.setStatus((String) params.get("status"));
        }
        return sysDeptMapper.selectDeptList(dept);
    }

    /**
     * 查询角色列表
     */
    @Override
    public List<SysRole> selectRoleList(Map<String, Object> params) {
        SysRole role = new SysRole();
        role.setParams(params);
        // 设置查询条件
        if (params.containsKey("roleName")) {
            role.setRoleName((String) params.get("roleName"));
        }
        if (params.containsKey("roleKey")) {
            role.setRoleKey((String) params.get("roleKey"));
        }
        if (params.containsKey("status")) {
            role.setStatus((String) params.get("status"));
        }
        return sysRoleMapper.selectRoleList(role);
    }

    /**
     * 查询用户列表
     */
    @Override
    public List<Survey> selectSurveyList(Map<String, Object> params) {
        // 构建查询参数
        Survey survey = new Survey();
        survey.setParams(params);
        // 可根据需要设置其他查询条件
        if (params.containsKey("surveyName")) {
            survey.setSurveyName((String) params.get("surveyName"));
        }
        return surveyMapper.selectSurveyList(survey);
    }

    /**
     * 根据数据类型查询对应列表
     */
    @Override
    public List<?> selectDataListByType(String dataType, Map<String, Object> params) {
        switch (dataType) {
            case "user":
                return selectUserList(params);
            case "department":
                return selectDeptList(params);
            case "role":
                return selectRoleList(params);
            case "survey":
                return selectSurveyList(params);
            //可以继续扩展其他数据类型
            default:
                throw new RuntimeException("不支持的数据类型: " + dataType);
        }
    }
}
