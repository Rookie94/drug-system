package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 自定义日志对象 sys_custom_log
 */
public class SysCustomLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private Long logId;

    /** 日志标题 */
    @Excel(name = "日志标题")
    private String logTitle;

    /** 日志类型 */
    private String logType;

    /** 操作人员 */
    @Excel(name = "操作人员")
    private String operName;

    /** 请求URI */
    private String requestUri;

    /** 请求方法 */
    private String requestMethod;

    /** 请求参数 */
    private String requestParams;

    /** 响应结果 */
    private String responseResult;

    /** 错误信息 */
    private String errorMsg;

    /** 执行时间 */
    private Long executeTime;

    /** 操作IP */
    @Excel(name = "操作IP")
    private String operIp;

    /** 操作地点 */
    @Excel(name = "操作地点")
    private String operLocation;

    /** 操作状态 */
    @Excel(name = "操作状态", readConverterExp = "0=成功,1=失败")
    private String status;

    /** 业务模块 */
    private String businessModule;

    /** 业务ID */
    private String businessId;

    /** 日志详情 */
    private String logDetail;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** 开始操作时间（用于查询） */
    private String beginOperTime;

    /** 结束操作时间（用于查询） */
    private String endOperTime;

    // getter和setter方法
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getLogTitle() {
        return logTitle;
    }

    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public String getOperIp() {
        return operIp;
    }

    public void setOperIp(String operIp) {
        this.operIp = operIp;
    }

    public String getOperLocation() {
        return operLocation;
    }

    public void setOperLocation(String operLocation) {
        this.operLocation = operLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessModule() {
        return businessModule;
    }

    public void setBusinessModule(String businessModule) {
        this.businessModule = businessModule;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getBeginOperTime() {
        return beginOperTime;
    }

    public void setBeginOperTime(String beginOperTime) {
        this.beginOperTime = beginOperTime;
    }

    public String getEndOperTime() {
        return endOperTime;
    }

    public void setEndOperTime(String endOperTime) {
        this.endOperTime = endOperTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("logId", getLogId())
                .append("logTitle", getLogTitle())
                .append("logType", getLogType())
                .append("operName", getOperName())
                .append("requestUri", getRequestUri())
                .append("requestMethod", getRequestMethod())
                .append("requestParams", getRequestParams())
                .append("responseResult", getResponseResult())
                .append("errorMsg", getErrorMsg())
                .append("executeTime", getExecuteTime())
                .append("operIp", getOperIp())
                .append("operLocation", getOperLocation())
                .append("status", getStatus())
                .append("businessModule", getBusinessModule())
                .append("businessId", getBusinessId())
                .append("logDetail", getLogDetail())
                .append("operTime", getOperTime())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}