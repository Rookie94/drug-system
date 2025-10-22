package com.ruoyi.cms.res.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.cms.res.domain.*;
import com.ruoyi.cms.res.domain.ResArticlesVo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResArticlesMapper;
import com.ruoyi.cms.res.service.IResArticlesService;

import static com.ruoyi.common.utils.SecurityUtils.*;

import com.ruoyi.system.domain.ResApporParam;

/**
 * 资讯发布Service业务层处理
 *
 * @author admin
 * @date 2025-04-11
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
    public ResArticlesVo selectResArticlesByArticleId(Long articleId)
    {
        return resArticlesMapper.selectResArticlesByArticleId(articleId);
    }

    /**
     * 查询资讯栏目
     *
     * @return 栏目集合
     */
    @Override
    public List<ResCategoryInfo> selectCategoryList(){
        return resArticlesMapper.selectCategoryList();
    }

    /**
     * 查询资讯分类
     *
     * @param categoryId 栏目ID
     * @return 分类集合
     */
    @Override
    public List<ResCategoryInfo> selectSubCategoryList(Long categoryId){
        return resArticlesMapper.selectSubCategoryList(categoryId);
    }

    /**
     * 查询资讯发布列表
     *
     * @param resArticles 资讯发布
     * @return 资讯发布
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ResArticlesVo> selectResArticlesList(ResArticles resArticles)
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
        resArticles.setUserId(getUserId());
        resArticles.setDeptId(getDeptId());
        resArticles.setCreateBy(getUsername());
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
        resArticles.setUpdateBy(getUsername());
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

    /**
     * 修改戒治案例状态
     *
     * @param resArticles 戒治机构
     * @return 结果
     */
    public int updateStatus(ResArticles resArticles)
    {
        resArticles.setUpdateBy(getUsername());
        resArticles.setUpdateTime(DateUtils.getNowDate());
        return resArticlesMapper.updateStatus(resArticles);
    }

    /**
     * 批量审批
     *
     * @param apporParams 审批参数
     * @return 结果
     */
    @Override
    public int apporByIds(ResApporParam apporParams)
    {
        apporParams.setApporBy(getUsername());
        apporParams.setApporTime(DateUtils.getNowDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(apporParams.flag==2){
            apporParams.setPublishTime(dateFormat.format(DateUtils.getNowDate()));
        }
        return resArticlesMapper.apporByIds(apporParams);
    }

    /**
     * 反审批戒治案例
     *
     * @param id 戒治案例主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return resArticlesMapper.unApporById(id);
    }

    /**
     * 批量反审批戒治案例
     *
     * @param ids 需要删除的戒治案例主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return resArticlesMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resArticlesMapper.selectApporedByIds(ids);
    }

}
