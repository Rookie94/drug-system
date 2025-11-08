package com.ruoyi.web.controller.common;

import com.ruoyi.cms.common.service.ICommonSelectService;
import com.ruoyi.cms.survey.domain.Survey;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pub/common")
public class CommonSelectController extends BaseController {

    @Autowired
    private ICommonSelectService commonSelectService;

    /**
     * 通用数据选择接口
     */
    /**
     * 通用数据选择接口
     */
    @GetMapping("/getData")
    public TableDataInfo getSelectData(@RequestParam Map<String, Object> params) {
        String dataType = (String) params.get("dataType");

        if (StringUtils.isEmpty(dataType)) {
            throw new RuntimeException("数据类型不能为空");
        }

        // 根据数据类型调用不同的服务
        switch (dataType) {
            case "user":
                return getUsers(params);
            case "department":
                return getDepartments(params);
            case "role":
                return getRoles(params);
            case "survey":
                return getSurvey(params);
            default:
                throw new RuntimeException("不支持的数据类型: " + dataType);
        }
    }

    /**
     * 获取用户列表
     */
    private TableDataInfo getUsers(Map<String, Object> params) {
        startPage();
        List<SysUser> list = commonSelectService.selectUserList(params);
        return getDataTable(list);
    }

    /**
     * 获取部门列表
     */
    private TableDataInfo getDepartments(Map<String, Object> params) {
        startPage();
        List<SysDept> list = commonSelectService.selectDeptList(params);
        return getDataTable(list);
    }

    /**
     * 获取角色列表
     */
    private TableDataInfo getRoles(Map<String, Object> params) {
        startPage();
        List<SysRole> list = commonSelectService.selectRoleList(params);
        return getDataTable(list);
    }

    /**
     * 获取问卷列表
     */
    private TableDataInfo getSurvey(Map<String, Object> params) {
        startPage();
        List<Survey> list = commonSelectService.selectSurveyList(params);
        return getDataTable(list);
    }

}