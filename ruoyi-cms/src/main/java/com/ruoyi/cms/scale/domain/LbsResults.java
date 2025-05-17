package com.ruoyi.cms.scale.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 测评报告对象 lbs_results
 * 
 * @author admin
 * @date 2025-05-16
 */
public class LbsResults extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 结果id */
    private Long resultId;

    /** 结果编号 */
    private String resultNo;

    /** 量表id */
    private Long contextId;


    /** 部门ID */
    private Long deptId;


    /** 用户ID */
    private Long userId;


    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date commitTime;


    private String status;

    /** 量表答案 */
    private String jsonResult;

    /** 简化答案 */
    private String thinJsonResult;

    /** 测评报告 */
    private String jsonReport;

    public void setResultId(Long resultId) 
    {
        this.resultId = resultId;
    }

    public Long getResultId() 
    {
        return resultId;
    }

    public String getResultNo() {
        return resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo;
    }

    public void setContextId(Long contextId) 
    {
        this.contextId = contextId;
    }

    public Long getContextId() 
    {
        return contextId;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }


    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }


    public void setCommitTime(Date commitTime) 
    {
        this.commitTime = commitTime;
    }

    public Date getCommitTime() 
    {
        return commitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setJsonResult(String jsonResult) 
    {
        this.jsonResult = jsonResult;
    }

    public String getJsonResult() 
    {
        return jsonResult;
    }

    public void setThinJsonResult(String thinJsonResult) 
    {
        this.thinJsonResult = thinJsonResult;
    }

    public String getThinJsonResult() 
    {
        return thinJsonResult;
    }

    public void setJsonReport(String jsonReport) 
    {
        this.jsonReport = jsonReport;
    }

    public String getJsonReport() 
    {
        return jsonReport;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("resultId", getResultId())
            .append("resultNo", getResultNo())
            .append("contextId", getContextId())
            .append("deptId", getDeptId())
            .append("userId", getUserId())
            .append("commitTime", getCommitTime())
            .append("status", getStatus())
            .append("jsonResult", getJsonResult())
            .append("thinJsonResult", getThinJsonResult())
            .append("jsonReport", getJsonReport())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
