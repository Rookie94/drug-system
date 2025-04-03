package com.ruoyi.wxsys.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class R
{
    private Object data;

    private Integer code;

    private String msg;

    private static final Integer SUCCESS = 200;

    private static final Integer FAIL = 500;

    public static R success(String msg)
    {
        R r = new R();
        r.setCode(SUCCESS)
                .setMsg(msg)
                .setData(null);
        return r;
    }

    public static R success()
    {
        R r = new R();
        r.setCode(SUCCESS)
                .setMsg("操作成功")
                .setData(null);
        return r;
    }

    public static R success(Object data)
    {
        R r = new R();
        r.setCode(SUCCESS)
                .setMsg(null)
                .setData(data);
        return r;
    }

    public static R success(String msg,Object data)
    {
        R r = new R();
        r.setCode(SUCCESS)
                .setMsg(msg)
                .setData(data);
        return r;
    }

    public static R fail(String msg)
    {
        R r = new R();
        r.setCode(FAIL)
                .setMsg(msg)
                .setData(null);
        return r;
    }

    public static R fail()
    {
        R r = new R();
        r.setCode(FAIL)
                .setMsg(null)
                .setData(null);
        return r;
    }

}
