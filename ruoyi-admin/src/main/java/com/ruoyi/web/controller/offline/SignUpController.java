package com.ruoyi.web.controller.offline;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.cms.offline.domain.vo.SignUpVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.cms.offline.domain.SignUp;
import com.ruoyi.cms.offline.service.ISignUpService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 预约详情Controller
 * 
 * @author admin
 * @date 2025-04-28
 */
@RestController
@RequestMapping("/offline/signup")
public class SignUpController extends BaseController
{
    @Autowired
    private ISignUpService signUpService;

    /**
     * 查询预约详情列表
     */
    @PreAuthorize("@ss.hasPermi('offline:signup:list')")
    @GetMapping("/list")
    public TableDataInfo list(SignUpVo signUp)
    {
        startPage();
        List<SignUpVo> list = signUpService.selectSignUpList(signUp);
        return getDataTable(list);
    }

    /**
     * 导出预约详情列表
     */
    @PreAuthorize("@ss.hasPermi('offline:signup:export')")
    @Log(title = "预约详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SignUpVo signUp)
    {
        List<SignUpVo> list = signUpService.selectSignUpList(signUp);
        ExcelUtil<SignUpVo> util = new ExcelUtil<SignUpVo>(SignUpVo.class);
        util.exportExcel(response, list, "预约详情数据");
    }

    /**
     * 获取预约详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('offline:signup:query')")
    @GetMapping(value = "/{signId}")
    public AjaxResult getInfo(@PathVariable("signId") Long signId)
    {
        return success(signUpService.selectSignUpBySignId(signId));
    }

    /**
     * 新增预约详情
     */
    @PreAuthorize("@ss.hasPermi('offline:signup:add')")
    @Log(title = "预约详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SignUp signUp)
    {
        return toAjax(signUpService.insertSignUp(signUp));
    }

    /**
     * 修改预约详情
     */
    @PreAuthorize("@ss.hasPermi('offline:signup:edit')")
    @Log(title = "预约详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SignUp signUp)
    {
        return toAjax(signUpService.updateSignUp(signUp));
    }

    /**
     * 删除预约详情
     */
    @PreAuthorize("@ss.hasPermi('offline:signup:remove')")
    @Log(title = "预约详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{signIds}")
    public AjaxResult remove(@PathVariable Long[] signIds)
    {
        return toAjax(signUpService.deleteSignUpBySignIds(signIds));
    }
}
