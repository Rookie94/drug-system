package com.ruoyi.cms.offline.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 现场经颅磁对象 v_ofa_activities_tmsdata
 * 
 * @author admin
 * @date 2025-05-09
 */
public class ActivitiesTmsdata extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分析id */
    @Excel(name = "分析id")
    private Long analyzeId;

    /** 活动ID */
    @Excel(name = "活动ID")
    private Long activityId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /** 图片 */
    @Excel(name = "记录单照片")
    private String pic;

    /** 内容 */
    @Excel(name = "康复建议")
    private String content;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;

    /** 年龄 */
    @Excel(name = "年龄")
    private String age;

    /** 病例号 */
    @Excel(name = "病例号")
    private String recordNo;

    /** 诊断 */
    @Excel(name = "诊断")
    private String diagnosis;

    /** 靶向症 */
    @Excel(name = "靶向症")
    private String targetedTherapy;

    /** 头颅CT/MRI */
    @Excel(name = "头颅CT/MRI")
    private String ctMri;

    /** 治疗次数 */
    @Excel(name = "治疗次数")
    private Long numberOfTimes;

    /** 刺激频率 */
    @Excel(name = "刺激频率")
    private String rate;

    /** 刺激部位 */
    @Excel(name = "刺激部位")
    private String area;

    /** 运动阈值 */
    @Excel(name = "运动阈值")
    private String mt;

    /** 刺激强度 */
    @Excel(name = "刺激强度")
    private String intensity;

    /** 脉冲总数 */
    @Excel(name = "脉冲总数")
    private String numOfPulses;

    /** 不良反应 */
    @Excel(name = "不良反应")
    private String aes;

    /** 治疗医师 */
    @Excel(name = "治疗医师")
    private String doctor;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 部门名称 */
    @Excel(name = "部门ID")
    private Long deptId;

    public void setAnalyzeId(Long analyzeId) 
    {
        this.analyzeId = analyzeId;
    }

    public Long getAnalyzeId() 
    {
        return analyzeId;
    }

    public void setActivityId(Long activityId) 
    {
        this.activityId = activityId;
    }

    public Long getActivityId() 
    {
        return activityId;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }

    public void setAge(String age) 
    {
        this.age = age;
    }

    public String getAge() 
    {
        return age;
    }

    public void setRecordNo(String recordNo) 
    {
        this.recordNo = recordNo;
    }

    public String getRecordNo() 
    {
        return recordNo;
    }

    public void setDiagnosis(String diagnosis) 
    {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() 
    {
        return diagnosis;
    }

    public void setTargetedTherapy(String targetedTherapy) 
    {
        this.targetedTherapy = targetedTherapy;
    }

    public String getTargetedTherapy() 
    {
        return targetedTherapy;
    }

    public void setCtMri(String ctMri) 
    {
        this.ctMri = ctMri;
    }

    public String getCtMri() 
    {
        return ctMri;
    }

    public void setNumberOfTimes(Long numberOfTimes) 
    {
        this.numberOfTimes = numberOfTimes;
    }

    public Long getNumberOfTimes() 
    {
        return numberOfTimes;
    }

    public void setRate(String rate) 
    {
        this.rate = rate;
    }

    public String getRate() 
    {
        return rate;
    }

    public void setArea(String area) 
    {
        this.area = area;
    }

    public String getArea() 
    {
        return area;
    }

    public void setMt(String mt) 
    {
        this.mt = mt;
    }

    public String getMt() 
    {
        return mt;
    }

    public void setIntensity(String intensity)
    {
        this.intensity = intensity;
    }

    public String getIntensity()
    {
        return intensity;
    }

    public void setNumOfPulses(String numOfPulses) 
    {
        this.numOfPulses = numOfPulses;
    }

    public String getNumOfPulses() 
    {
        return numOfPulses;
    }

    public void setAes(String aes) 
    {
        this.aes = aes;
    }

    public String getAes() 
    {
        return aes;
    }

    public void setDoctor(String doctor) 
    {
        this.doctor = doctor;
    }

    public String getDoctor() 
    {
        return doctor;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("analyzeId", getAnalyzeId())
            .append("activityId", getActivityId())
            .append("pic", getPic())
            .append("content", getContent())
            .append("name", getName())
            .append("sex", getSex())
            .append("age", getAge())
            .append("recordNo", getRecordNo())
            .append("diagnosis", getDiagnosis())
            .append("targetedTherapy", getTargetedTherapy())
            .append("ctMri", getCtMri())
            .append("numberOfTimes", getNumberOfTimes())
            .append("rate", getRate())
            .append("area", getArea())
            .append("mt", getMt())
            .append("intensity", getIntensity())
            .append("numOfPulses", getNumOfPulses())
            .append("aes", getAes())
            .append("doctor", getDoctor())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("userId", getUserId())
            .append("deptId", getDeptId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
