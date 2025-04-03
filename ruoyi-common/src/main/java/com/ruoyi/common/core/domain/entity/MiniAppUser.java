package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.MiniAppUserDTO;

/**
 * 小程序用户对象 t_mini_app_user
 * 
 * @author 盖子
 * @date 2024-11-22
 */
public class MiniAppUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 头像 */
    @Excel(name = "头像")
    private String avatar;

    /** 用户唯一标识 */
    @Excel(name = "用户唯一标识")
    private String openId;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 归属小程序id */
    @Excel(name = "归属小程序id")
    private Long miniAppId;

    private String miniAppName;

    public MiniAppUser()
    {
    }

    public MiniAppUser(Long id, String nickName, String avatar, String openId, String sex, Long miniAppId)
    {
        this.id = id;
        this.nickName = nickName;
        this.avatar = avatar;
        this.openId = openId;
        this.sex = sex;
        this.miniAppId = miniAppId;
    }

    public MiniAppUser(MiniAppUserDTO miniAppUserDTO,Long id)
    {
        this.id = id;
        this.nickName = miniAppUserDTO.getNickName();
        this.sex = miniAppUserDTO.getSex();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getNickName()
    {
        return nickName;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getOpenId()
    {
        return openId;
    }

    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public Long getMiniAppId()
    {
        return miniAppId;
    }

    public void setMiniAppId(Long miniAppId)
    {
        this.miniAppId = miniAppId;
    }

    public String getMiniAppName()
    {
        return miniAppName;
    }

    public void setMiniAppName(String miniAppName)
    {
        this.miniAppName = miniAppName;
    }

    @Override
    public String toString()
    {
        return "MiniAppUser{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", openId='" + openId + '\'' +
                ", sex='" + sex + '\'' +
                ", miniAppId=" + miniAppId +
                '}';
    }
}
