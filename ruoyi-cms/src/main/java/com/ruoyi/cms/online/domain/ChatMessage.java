package com.ruoyi.cms.online.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 留言板对象 v_chat_message
 * 
 * @author admin
 * @date 2025-05-07
 */
public class ChatMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 留言id */
    private Long messageId;

    /** 父留言ID */
    private Long parentMessageId;

    /** 学员id */
    private Long userId;

    /** 群组id */
    @Excel(name = "群组id")
    private Long groupId;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 聊天消息 */
    @Excel(name = "留言主题")
    private String title;

    /** 聊天消息 */
    @Excel(name = "留言消息")
    private String message;

    /** 状态（0未回复 1已回复） */
    @Excel(name = "状态", readConverterExp = "0=未回复,1=已回复")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setMessageId(Long messageId) 
    {
        this.messageId = messageId;
    }

    public Long getMessageId() 
    {
        return messageId;
    }

    public void setParentMessageId(Long parentMessageId) 
    {
        this.parentMessageId = parentMessageId;
    }

    public Long getParentMessageId() 
    {
        return parentMessageId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) 
    {
        this.message = message;
    }

    public String getMessage() 
    {
        return message;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("messageId", getMessageId())
            .append("parentMessageId", getParentMessageId())
            .append("userId", getUserId())
            .append("groupId", getGroupId())
            .append("deptId", getDeptId())
            .append("title", getTitle())
            .append("message", getMessage())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
