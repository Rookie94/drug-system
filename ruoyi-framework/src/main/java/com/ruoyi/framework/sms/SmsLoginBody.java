package com.ruoyi.framework.sms;

import com.ruoyi.common.xss.Xss;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @className: SmsLoginBody
 * @author: liuyh
 * @date: 2025/5/22 10:05
 * @Version: 1.0
 */
@Data
public class SmsLoginBody implements Serializable {
    private static final long serialVersionUID = 1L;

    @Xss(message = "手机号不能出现任何脚本")
    @NotBlank(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号码长度不能超过11个字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phoneNumber;

    @Xss(message = "验证码不能出现任何脚本")
    @Size(min = 4, max = 4, message = "验证码长度为4个字符")
    @Pattern(regexp = "^[0-9]+$", message = "验证码应为纯数字，格式不正确")
    private String code;
}