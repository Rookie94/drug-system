package com.ruoyi.cms.survey.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class DocResultsVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 结果主键 */
    @Excel(name = "结果主键")
    private Long resultId;

    /** 问卷ID */
    @Excel(name = "问卷ID")
    private Long surveyId;

    /** 问卷名 */
    @Excel(name = "问卷名")
    private String surveyName;

    /** 结果编号 */
    @Excel(name = "结果编号")
    private String resultNo;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 归属部门 */
    @Excel(name = "归属部门")
    private String deptName;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 学员账号 */
    @Excel(name = "学员账号")
    private String userName;

    /** 学员名称 */
    @Excel(name = "学员名称")
    private String nickName;

    /** 用户类型（00系统用户） */
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户")
    private String userType;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 生日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生日", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phoneNumber;

    /** 提交时间 */
    @Excel(name = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitTime;

    /** 状态（0=正常 1=作废） */
    @Excel(name = "状态")
    private String status;

    /** 完整JSON结果 */
    private String jsonResult;

    /** 精简JSON结果 */
    private String thinJsonResult;

    /** JSON报告 */
    private String jsonReport;

    /** 备注 */
    @Excel(name = "备注")
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

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
                .append("surveyName", getSurveyName())
                .append("resultNo", getResultNo())
                .append("deptId", getDeptId())
                .append("deptName", getDeptName())
                .append("userId", getUserId())
                .append("userName", getUserName())
                .append("nickName", getNickName())
                .append("userType", getUserType())
                .append("sex", getSex())
                .append("birthday", getBirthday())
                .append("phoneNumber", getPhoneNumber())
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
