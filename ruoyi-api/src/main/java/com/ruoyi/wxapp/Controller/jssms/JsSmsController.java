package com.ruoyi.wxapp.Controller.jssms;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

import com.ruoyi.framework.jssms.domain.JsSmsSendRequest;
import com.ruoyi.framework.jssms.domain.JsSmsSendResponse;
import com.ruoyi.framework.jssms.service.IJsSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信发送控制器
 */
@RestController
@RequestMapping("/api/sms")
public class JsSmsController extends BaseController {

    @Autowired
    private IJsSmsService jsSmsService;



    /**
     * 发送模板短信
     */
    @PostMapping("/sendTemplateSms")
    public AjaxResult sendTemplateSms(@RequestBody JsSmsSendRequest request) {
        try {
            JsSmsSendResponse response = jsSmsService.sendTemplateSms(request);
            return AjaxResult.success(response);
        } catch (Exception e) {
            return AjaxResult.error("短信发送失败：" + e.getMessage());
        }
    }
}