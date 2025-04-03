package com.ruoyi.cms.res.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 资讯发布对象 res_articles
 * 
 * @author admin
 * @date 2025-04-02
 */
public class ResArticles extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文章ID */
    private Long articleId;

    /** 文章标题 */
    @Excel(name = "文章标题")
    private String title;

    /** 栏目 */
    @Excel(name = "栏目")
    private Integer categoryId;

    /** 分类 */
    @Excel(name = "分类")
    private Long type;

    /** 简介 */
    @Excel(name = "简介")
    private String desc;

    /** 文本编辑器类型 */
    @Excel(name = "文本编辑器类型")
    private String contentType;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

    /** Markdown格式内容 */
    @Excel(name = "Markdown格式内容")
    private String contentMarkdown;

    /** 置顶（0否 1是） */
    @Excel(name = "置顶", readConverterExp = "0=否,1=是")
    private String top;

    /** 阅读 */
    @Excel(name = "阅读")
    private Long views;

    /** 状态（0编辑中 1已发布） */
    @Excel(name = "状态", readConverterExp = "0=编辑中,1=已发布")
    private String status;

    /** 首页图片类型（0地址 1上传） */
    @Excel(name = "首页图片类型", readConverterExp = "0=地址,1=上传")
    private String picType;

    /** 首页图片（ 1上传） */
    @Excel(name = "首页图片", readConverterExp = "1=上传")
    private String pic;

    /** 首页图片（ 0地址） */
    @Excel(name = "首页图片", readConverterExp = "0=地址")
    private String picLink;

    /** 附件列表 */
    @Excel(name = "附件列表")
    private String files;

    public void setArticleId(Long articleId) 
    {
        this.articleId = articleId;
    }

    public Long getArticleId() 
    {
        return articleId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setCategoryId(Integer categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() 
    {
        return categoryId;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }
    public void setDesc(String desc) 
    {
        this.desc = desc;
    }

    public String getDesc() 
    {
        return desc;
    }
    public void setContentType(String contentType) 
    {
        this.contentType = contentType;
    }

    public String getContentType() 
    {
        return contentType;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setContentMarkdown(String contentMarkdown) 
    {
        this.contentMarkdown = contentMarkdown;
    }

    public String getContentMarkdown() 
    {
        return contentMarkdown;
    }
    public void setTop(String top) 
    {
        this.top = top;
    }

    public String getTop() 
    {
        return top;
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
    public void setPicType(String picType) 
    {
        this.picType = picType;
    }

    public String getPicType() 
    {
        return picType;
    }
    public void setPic(String pic) 
    {
        this.pic = pic;
    }

    public String getPic() 
    {
        return pic;
    }
    public void setPicLink(String picLink) 
    {
        this.picLink = picLink;
    }

    public String getPicLink() 
    {
        return picLink;
    }
    public void setFiles(String files) 
    {
        this.files = files;
    }

    public String getFiles() 
    {
        return files;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("articleId", getArticleId())
            .append("title", getTitle())
            .append("categoryId", getCategoryId())
            .append("type", getType())
            .append("desc", getDesc())
            .append("contentType", getContentType())
            .append("content", getContent())
            .append("contentMarkdown", getContentMarkdown())
            .append("top", getTop())
            .append("views", getViews())
            .append("status", getStatus())
            .append("picType", getPicType())
            .append("pic", getPic())
            .append("picLink", getPicLink())
            .append("files", getFiles())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
