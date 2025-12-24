package com.ruoyi.common.core.domain;

import com.ruoyi.common.utils.StringUtils;

public class ProcResult {

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private Boolean result;
    private String msg;
    private Object data;

    public ProcResult(Boolean fResult, String fMsg, Object fData)
    {
        result=fResult;
        msg=fMsg;
        data=fData;
    }

}
