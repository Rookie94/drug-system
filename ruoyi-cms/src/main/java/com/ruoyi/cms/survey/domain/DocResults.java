package com.ruoyi.cms.survey.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 问卷答案结果json对象 tb_answer_json
 *
 * @author Shure
 * @date 2021-10-18
 */
public class DocResults extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 结果主键 */
    private Long resultId;

    /** 问卷ID */
    private Long surveyId;

    /** 结果编号 */
    private String resultNo;

    /** 部门ID */
    private Long deptId;

    /** 用户ID */
    private Long userId;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitTime;

    /** 状态（0=正常 1=作废） */
    private String status;

    /** 完整JSON结果 */
    private String jsonResult;

    /** 精简JSON结果 */
    private String thinJsonResult;

    /** JSON报告 */
    private String jsonReport;

    /** 备注 */
    private String remark;

    /* ==================== getter / setter ==================== */
    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getResultNo() {
        return resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    public String getThinJsonResult() {
        return thinJsonResult;
    }

    public void setThinJsonResult(String thinJsonResult) {
        this.thinJsonResult = thinJsonResult;
    }

    public String getJsonReport() {
        return jsonReport;
    }

    public void setJsonReport(String jsonReport) {
        this.jsonReport = jsonReport;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("resultId", getResultId())
                .append("surveyId", getSurveyId())
                .append("resultNo", getResultNo())
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
