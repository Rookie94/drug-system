package com.ruoyi.cms.res.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResArticlesMapper;
import com.ruoyi.cms.res.domain.ResArticles;
import com.ruoyi.cms.res.service.IResArticlesService;

/**
 * 资讯发布Service业务层处理
 * 
 * @author admin
 * @date 2025-04-02
 */
@Service
public class ResArticlesServiceImpl implements IResArticlesService 
{
    @Autowired
    private ResArticlesMapper resArticlesMapper;

    /**
     * 查询资讯发布
     * 
     * @param articleId 资讯发布主键
     * @return 资讯发布
     */
    @Override
    public ResArticles selectResArticlesByArticleId(Long articleId)
    {
        return resArticlesMapper.selectResArticlesByArticleId(articleId);
    }

    /**
     * 查询资讯发布列表
     * 
     * @param resArticles 资讯发布
     * @return 资讯发布
     */
    @Override
    public List<ResArticles> selectResArticlesList(ResArticles resArticles)
    {
        return resArticlesMapper.selectResArticlesList(resArticles);
    }

    /**
     * 新增资讯发布
     * 
     * @param resArticles 资讯发布
     * @return 结果
     */
    @Override
    public int insertResArticles(ResArticles resArticles)
    {
        resArticles.setCreateTime(DateUtils.getNowDate());
        return resArticlesMapper.insertResArticles(resArticles);
    }

    /**
     * 修改资讯发布
     * 
     * @param resArticles 资讯发布
     * @return 结果
     */
    @Override
    public int updateResArticles(ResArticles resArticles)
    {
        resArticles.setUpdateTime(DateUtils.getNowDate());
        return resArticlesMapper.updateResArticles(resArticles);
    }

    /**
     * 批量删除资讯发布
     * 
     * @param articleIds 需要删除的资讯发布主键
     * @return 结果
     */
    @Override
    public int deleteResArticlesByArticleIds(Long[] articleIds)
    {
        return resArticlesMapper.deleteResArticlesByArticleIds(articleIds);
    }

    /**
     * 删除资讯发布信息
     * 
     * @param articleId 资讯发布主键
     * @return 结果
     */
    @Override
    public int deleteResArticlesByArticleId(Long articleId)
    {
        return resArticlesMapper.deleteResArticlesByArticleId(articleId);
    }
}
