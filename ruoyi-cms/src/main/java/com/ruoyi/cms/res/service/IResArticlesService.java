package com.ruoyi.cms.res.service;

import java.util.List;

import com.ruoyi.cms.res.domain.*;
import com.ruoyi.cms.res.domain.ResArticlesVo;
import com.ruoyi.system.domain.ResApporParam;

/**
 * 资讯发布Service接口
 *
 * @author admin
 * @date 2025-04-11
 */
public interface IResArticlesService
{
    /**
     * 查询资讯发布
     *
     * @param articleId 资讯发布主键
     * @return 资讯发布
     */
    public ResArticlesVo selectResArticlesByArticleId(Long articleId);

    /**
     * 查询资讯栏目
     *
     * @return 栏目集合
     */
    public List<ResCategoryInfo> selectCategoryList();

    /**
     * 查询资讯分类
     *
     * @return 分类集合
     */
    public List<ResCategoryInfo> selectSubCategoryList(Long categoryId);

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

    /**
     * 修改状态
     *
     * @param resArticles 戒治机构
     * @return 结果
     */
    public int updateStatus(ResArticles resArticles);

    /**
     * 批量审批
     *
     * @param apporParams 主键集合
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);


    /**
     * 批量反审批
     *
     * @param ids 需要删除的戒治案例主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] ids);

    /**
     * 反审批
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public int unApporById(Long ids);

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids);

}
