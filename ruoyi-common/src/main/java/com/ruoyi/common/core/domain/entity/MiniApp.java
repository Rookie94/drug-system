package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小程序信息对象 t_mini_app
 * 
 * @author 盖子
 * @date 2024-11-22
 */
public class MiniApp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 小程序名称 */
    @Excel(name = "小程序名称")
    private String name;

    /** 小程序appid */
    @Excel(name = "小程序appid")
    private String appid;

    /** 小程序密钥 */
    @Excel(name = "小程序密钥")
    private String secret;

    /** 令牌 */
    @Excel(name = "令牌")
    private String token;

    /** 消息加密密钥 */
    @Excel(name = "消息加密密钥")
    private String aesKey;

    /** 数据格式 JSON/XML */
    @Excel(name = "数据格式 JSON/XML")
    private String msgDataFormat;

    /**  主题色 */
    private String themeColor;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAppid()
    {
        return appid;
    }

    public void setAppid(String appid)
    {
        this.appid = appid;
    }

    public String getSecret()
    {
        return secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getAesKey()
    {
        return aesKey;
    }

    public void setAesKey(String aesKey)
    {
        this.aesKey = aesKey;
    }

    public String getMsgDataFormat()
    {
        return msgDataFormat;
    }

    public void setMsgDataFormat(String msgDataFormat)
    {
        this.msgDataFormat = msgDataFormat;
    }

    public String getThemeColor()
    {
        return themeColor;
    }

    public void setThemeColor(String themeColor)
    {
        this.themeColor = themeColor;
    }
}
