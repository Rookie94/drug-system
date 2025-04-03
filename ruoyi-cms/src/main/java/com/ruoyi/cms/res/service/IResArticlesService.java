package com.ruoyi.cms.res.service;

import java.util.List;
import com.ruoyi.cms.res.domain.ResArticles;

/**
 * 资讯发布Service接口
 * 
 * @author admin
 * @date 2025-04-02
 */
public interface IResArticlesService 
{
    /**
     * 查询资讯发布
     * 
     * @param articleId 资讯发布主键
     * @return 资讯发布
     */
    public ResArticles selectResArticlesByArticleId(Long articleId);

    /**
     * 查询资讯发布列表
     * 
     * @param resArticles 资讯发布
     * @return 资讯发布集合
     */
    public List<ResArticles> selectResArticlesList(ResArticles resArticles);

    /**
     * 新增资讯发布
     * 
     * @param resArticles 资讯发布
     * @return 结果
     */
    public int insertResArticles(ResArticles resArticles);

    /**
     * 修改资讯发布
     * 
     * @param resArticles 资讯发布
     * @return 结果
     */
    public int updateResArticles(ResArticles resArticles);

    /**
     * 批量删除资讯发布
     * 
     * @param articleIds 需要删除的资讯发布主键集合
     * @return 结果
     */
    public int deleteResArticlesByArticleIds(Long[] articleIds);

    /**
     * 删除资讯发布信息
     * 
     * @param articleId 资讯发布主键
     * @return 结果
     */
    public int deleteResArticlesByArticleId(Long articleId);
}
