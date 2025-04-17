package com.ruoyi.cms.res.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资讯发布对象 res_articles
 *
 * @author admin
 * @date 2025-04-11
 */
public class ResArticles extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private Long articleId;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 文章标题 */
    @Excel(name = "文章标题")
    private String title;

    /** 栏目 */
    private Long categoryId;

    /** 分类 */
    private Long typeId;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** 阅读量 */
    @Excel(name = "阅读量")
    private Long views;

    /** 状态（0编辑中 1已发布） */
    @Excel(name = "状态", readConverterExp = "0=编辑中,1=已发布")
    private String status;

    /** 审批状态（0待审批 1已发布） */
    @Excel(name = "审批状态", readConverterExp = "0=待审批,1=已发布")
    private String appored;

    /** 首页图片（ 1上传） */
    @Excel(name = "首页图片", readConverterExp = "1=上传")
    private String pic;

    /** 用户ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 更新者 */
    @Excel(name = "更新者")
    private String apporBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date apporTime;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public void setArticleId(Long articleId)
    {
        this.articleId = articleId;
    }

    public Long getArticleId()
    {
        return articleId;
    }
    public void setOrderNum(Long orderNum)
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum()
    {
        return orderNum;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }
    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public Long getTypeId()
    {
        return typeId;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setViews(Long views)
    {
        this.views = views;
    }

    public Long getViews()
    {
        return views;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setAppored(String appored)
    {
        this.appored = appored;
    }

    public String getAppored()
    {
        return appored;
    }
    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getPic()
    {
        return pic;
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
                .append("articleId", getArticleId())
                .append("orderNum", getOrderNum())
                .append("title", getTitle())
                .append("categoryId", getCategoryId())
                .append("typeId", getTypeId())
                .append("content", getContent())
                .append("views", getViews())
                .append("status", getStatus())
                .append("appored", getAppored())
                .append("publishTime", getPublishTime())
                .append("pic", getPic())
                .append("userId", getUserId())
                .append("deptId", getDeptId())
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
