package com.ruoyi.common.exception.user;

/**
 * 用户电话号码不存在异常类
 *
 * @author ruoyi
 */
public class UserPhoneNotExistsException extends UserException
{
    private static final long serialVersionUID = 1L;

    public UserPhoneNotExistsException()
    {
        super("user.phone.not.exists", null);
    }
}