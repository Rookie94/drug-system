package com.ruoyi.cms.scale.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 测评报告对象 lbs_results
 * 
 * @author admin
 * @date 2025-05-16
 */
public class LbsResultsVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 结果id */
    private Long resultId;

    /** 结果编号 */
    private String resultNo;

    /** 量表id */
    private Long contextId;

    /** 量表编号 */
    @Excel(name = "量表编号")
    private String contextNo;

    /** 量表名称 */
    @Excel(name = "量表名称")
    private String contextName;

    /** 部门ID */
    private Long deptId;

    /** 归属部门 */
    @Excel(name = "归属部门")
    private String deptName;

    /** 用户ID */
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
    private String phonenumber;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date commitTime;

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

    public String getResultNo() {
        return resultNo;
    }

    public void setResultNo(String resultNo) {
        this.resultNo = resultNo;
    }

    public Long getResultId() 
    {
        return resultId;
    }

    public void setContextId(Long contextId) 
    {
        this.contextId = contextId;
    }

    public Long getContextId() 
    {
        return contextId;
    }

    public void setContextNo(String contextNo) 
    {
        this.contextNo = contextNo;
    }

    public String getContextNo() 
    {
        return contextNo;
    }

    public void setContextName(String contextName) 
    {
        this.contextName = contextName;
    }

    public String getContextName() 
    {
        return contextName;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }

    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }

    public void setPhonenumber(String phonenumber) 
    {
        this.phonenumber = phonenumber;
    }

    public String getPhonenumber() 
    {
        return phonenumber;
    }

    public void setCommitTime(Date commitTime) 
    {
        this.commitTime = commitTime;
    }

    public Date getCommitTime() 
    {
        return commitTime;
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
            .append("contextNo", getContextNo())
            .append("contextName", getContextName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("userType", getUserType())
            .append("sex", getSex())
            .append("birthday", getBirthday())
            .append("phonenumber", getPhonenumber())
            .append("commitTime", getCommitTime())
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
