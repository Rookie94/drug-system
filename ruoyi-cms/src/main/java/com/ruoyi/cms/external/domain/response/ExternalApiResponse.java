package com.ruoyi.cms.external.domain.response;

/**
 * API响应封装
 */
public class ExternalApiResponse<T> {
    private String code;
    private String msg;
    private String data;
    private T decodedData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public T getDecodedData() {
        return decodedData;
    }

    public void setDecodedData(T decodedData) {
        this.decodedData = decodedData;
    }

    public boolean isSuccess() {
        return "0".equals(code);
    }
}
