package com.ruoyi.cms.online.domain.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 留言板对象 v_chat_message
 * 
 * @author admin
 * @date 2025-05-07
 */
public class ChatMessageVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 留言id */
    private Long messageId;

    /** 父留言ID */
    private Long parentMessageId;

    /** 学员id */
    private Long userId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userName;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 用户类型（00系统用户） */
    @Excel(name = "用户类型", readConverterExp = "0=0系统用户")
    private String userType;

    /** 群组id */
    @Excel(name = "群组id")
    private Long groupId;

    /** 群组名称 */
    @Excel(name = "群组名称")
    private String groupName;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 聊天消息 */
    @Excel(name = "留言主题")
    private String title;

    /** 聊天消息 */
    @Excel(name = "留言内容")
    private String message;

    /** 状态（0未回复 1已回复） */
    @Excel(name = "状态", readConverterExp = "0=未回复,1=已回复")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    // 新增的查询条件属性
    private Boolean queryRootOnly;

    // getter方法（布尔类型通常用isXXX命名，更符合规范）
    public Boolean isQueryRootOnly() {
        return queryRootOnly;
    }

    // setter方法
    public void setQueryRootOnly(Boolean queryRootOnly) {
        this.queryRootOnly = queryRootOnly;
    }


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

    public void setGroupId(Long groupId) 
    {
        this.groupId = groupId;
    }

    public Long getGroupId() 
    {
        return groupId;
    }

    public void setGroupName(String groupName) 
    {
        this.groupName = groupName;
    }

    public String getGroupName() 
    {
        return groupName;
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
            .append("userName", getUserName())
            .append("nickName", getNickName())
            .append("userType", getUserType())
            .append("groupId", getGroupId())
            .append("groupName", getGroupName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
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
