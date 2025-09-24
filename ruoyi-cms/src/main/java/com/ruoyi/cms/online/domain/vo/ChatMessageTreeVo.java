package com.ruoyi.cms.online.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 聊天消息树形结构VO（包含子回复层级）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY) // 忽略空集合，减少无用数据
public class ChatMessageTreeVo  extends ChatMessageVo {

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 父消息ID（根消息为0）
     */
    private Long parentMessageId;

    /**
     * 发送用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 所属群组ID
     */
    private Long groupId;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息状态
     */
    private String status;

    /**
     * 删除标志（0正常 1删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 子回复集合（用于存储当前消息的所有子回复）
     * 对应方法：setReplies() / getReplies()
     */
    private List<ChatMessageTreeVo> replies;

    // getter/setter
    public List<ChatMessageTreeVo> getReplies() {
        return replies;
    }
    public void setReplies(List<ChatMessageTreeVo> replies) {
        this.replies = replies;
    }

}