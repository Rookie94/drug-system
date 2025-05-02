package com.ruoyi.cms.online.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 聊天群组对象 chat_group
 * 
 * @author admin
 * @date 2025-04-17
 */
public class ChatGroupVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 群组ID */
    private Long groupId;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 群组名称 */
    @Excel(name = "群组名称")
    private String groupName;

    /** 封面图片 */
    @Excel(name = "封面图片")
    private String pic;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 审批状态（0待审批 1已发布） */
    @Excel(name = "审批状态", readConverterExp = "0=待审批,1=已发布")
    private String appored;

    /** 是否已入群 */
    private String joinGroupState;

    public String getjoinGroupState() {
        return joinGroupState;
    }

    public void setJoinedGroup(String joinedGroup) {
        this.joinGroupState = joinedGroup;
    }

    /** 审核者 */
    @Excel(name = "审核者")
    private String apporBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
    }
    public void setPic(String pic) 
    {
        this.pic = pic;
    }

    public String getPic() 
    {
        return pic;
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

    public void setAppored(String appored)
    {
        this.appored = appored;
    }

    public String getAppored()
    {
        return appored;
    }

    public void setApporBy(String apporBy)
    {
        this.apporBy = apporBy;
    }

    public String getApporBy()
    {
        return apporBy;
    }
    public void setApporTime(Date apporTime)
    {
        this.apporTime = apporTime;
    }

    public Date getApporTime()
    {
        return apporTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("groupId", getGroupId())
            .append("orderNum", getOrderNum())
            .append("groupName", getGroupName())
            .append("pic", getPic())
            .append("status", getStatus())
            .append("appored", getAppored())
            .append("delFlag", getDelFlag())
            .append("joinedGroup",getjoinGroupState())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("apporBy", getApporBy())
            .append("apporTime", getApporTime())
            .append("remark", getRemark())
            .toString();
    }
}
