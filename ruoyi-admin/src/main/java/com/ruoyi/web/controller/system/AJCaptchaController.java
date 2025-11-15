package com.ruoyi.web.controller.system;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 验证码操作处理
 *
 * @author cxstar
 */
@RestController
public class AJCaptchaController {

    @Resource
    private CaptchaService captchaService;

    /**
     * 获取验证码接口
     *
     * @param captchaVO 验证码参数
     *                  "captchaType": "blockPuzzle",
     *                  "clientUid": "唯一标识"
     */
    @PostMapping("/captcha/get")
    public ResponseModel get(@RequestBody CaptchaVO captchaVO) {
        return captchaService.get(captchaVO);
    }

    /**
     * 校验滑动验证
     *
     * @param captchaVO 验证码参数
     *                  "captchaType": "blockPuzzle",
     *                  "pointJson": "QxIVdlJoWUi04iM+65hTow==",  //aes加密坐标信息
     *                  "token": "71dd26999e314f9abb0c635336976635"  //get请求返回的token
     */
    @PostMapping("/captcha/check")
    public ResponseModel check(@RequestBody CaptchaVO captchaVO) {
        return captchaService.check(captchaVO);
    }

}

