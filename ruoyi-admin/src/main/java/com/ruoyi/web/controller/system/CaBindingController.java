package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.web.service.CaBindingService;
import com.ruoyi.web.service.CaLoginService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/user")
public class CaBindingController {
    @Autowired private CaLoginService caLoginService;
    @Autowired private CaBindingService caBindingService;
    @Autowired private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @GetMapping("/{userId}/ca-binding")
    public AjaxResult getBinding(@PathVariable Long userId) {
        userService.checkUserAllowed(new SysUser(userId));
        userService.checkUserDataScope(userId);
        return AjaxResult.success(caBindingService.getByUserId(userId));
    }

    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "UKey绑定", businessType = BusinessType.UPDATE, isSaveRequestData = false)
    @PostMapping("/{userId}/ca-binding")
    public AjaxResult bind(@PathVariable Long userId, @RequestBody CaLoginController.CaLoginBody body) {
        userService.checkUserAllowed(new SysUser(userId));
        userService.checkUserDataScope(userId);
        SysUser user = userService.selectUserById(userId);
        if (user == null || UserStatus.DELETED.getCode().equals(user.getDelFlag())
                || UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new ServiceException("只能给有效用户绑定 UKey");
        }
        CaLoginService.CaIdentity identity = caLoginService.verifyIdentity(body.getChallengeId(), body.getIdentityTicket());
        caBindingService.bind(userId, identity, SecurityUtils.getUsername());
        return AjaxResult.success("UKey 绑定成功");
    }

    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "UKey解绑", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userId}/ca-binding")
    public AjaxResult unbind(@PathVariable Long userId) {
        userService.checkUserAllowed(new SysUser(userId));
        userService.checkUserDataScope(userId);
        caBindingService.unbind(userId);
        return AjaxResult.success("UKey 已解绑");
    }
}
