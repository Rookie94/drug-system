package com.ruoyi.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxLoginBody
{
    /**
     * 小程序的appid
     */
    @NotEmpty(message = "appid cannot be empty")
    private String appid;

    /**
     * 前端调用wx.login产生的小程序code  用于js登录
     */
    @NotEmpty(message = "code cannot be empty")
    private String code;
}
