package com.ruoyi.cms.res.service.impl;

import java.util.List;

import com.ruoyi.cms.res.domain.ResArticles;
import com.ruoyi.cms.res.domain.ResArticlesVo;
import com.ruoyi.cms.res.domain.ResCategoryInfo;
import com.ruoyi.cms.res.mapper.ResArticlesMapper;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResCategoryMapper;
import com.ruoyi.cms.res.domain.ResCategory;
import com.ruoyi.cms.res.service.IResCategoryService;

/**
 * 资源分类Service业务层处理
 *
 * @author admin
 * @date 2025-03-28
 */
@Service
public class ResCategoryServiceImpl implements IResCategoryService
{
    @Autowired
    private ResCategoryMapper resCategoryMapper;

    @Autowired
    private ResArticlesMapper resArticlesMapper;

    /**
     * 查询资源分类
     *
     * @param id 资源分类主键
     * @return 资源分类
     */
    @Override
    public ResCategory selectResCategoryById(Long id)
    {
        return resCategoryMapper.selectResCategoryById(id);
    }

    /**
     * 查询资源分类列表
     *
     * @param resCategory 资源分类
     * @return 资源分类
     */
    @Override
    public List<ResCategory> selectResCategoryList(ResCategory resCategory)
    {
        return resCategoryMapper.selectResCategoryList(resCategory);
    }


    /**
     * 查询资源分类列表
     *
     * @param ids 资源分类
     * @return 资源分类
     */
    @Override
    public List<ResCategory> selectResCategoryListByIds(Long[] ids)
    {
        return resCategoryMapper.selectResCategoryListByIds(ids);
    }

    /**
     * 新增资源分类
     *
     * @param resCategory 资源分类
     * @return 结果
     */
    @Override
    public int insertResCategory(ResCategory resCategory)
    {
        resCategory.setCreateTime(DateUtils.getNowDate());
        return resCategoryMapper.insertResCategory(resCategory);
    }

    /**
     * 修改资源分类
     *
     * @param resCategory 资源分类
     * @return 结果
     */
    @Override
    public int updateResCategory(ResCategory resCategory)
    {
        resCategory.setUpdateTime(DateUtils.getNowDate());
        return resCategoryMapper.updateResCategory(resCategory);
    }

    /**
     * 批量删除资源分类
     *
     * @param ids 需要删除的资源分类主键
     * @return 结果
     */
    @Override
    public int deleteResCategoryByIds(Long[] ids)
    {
        List<ResCategory> categories = resCategoryMapper.selectResCategoryListByIds(ids);
        for (ResCategory category : categories) {
            if (category.getParentId() == 0) {
                throw new RuntimeException("不能删除顶级分类");
            }
            else if (category.getParentId() == 1) {
                //检查栏目是否包含子分类
                List<ResCategoryInfo> list = resArticlesMapper.selectSubCategoryList(category.getId());
                if(list!=null && list.size()>0){
                    throw new RuntimeException("该栏目已存在文章分类不能删除,请删除栏目中的文章分类以后再试!");
                }

                //检查栏目是否包含文章
                ResArticles resArticles=new ResArticles();
                resArticles.setCategoryId(category.getId());
                List<ResArticlesVo> list1= resArticlesMapper.selectResArticlesList(resArticles);
                if(list1!=null && list1.size()>0){
                    throw new RuntimeException("该栏目已经发布了文章不能删除,请删除栏目中的文章以后再试!");
                }
            }
            else
            {
                //检查子分类是否包含文章
                ResArticles resArticles=new ResArticles();
                resArticles.setTypeId(category.getId());
                List<ResArticlesVo> list1= resArticlesMapper.selectResArticlesList(resArticles);
                if(list1!=null && list1.size()>0){
                    throw new RuntimeException("该分类已经发布了文章不能删除,请删除分类中的文章以后再试!");
                }
            }
        }
        return resCategoryMapper.deleteResCategoryByIds(ids);
    }

    /**
     * 删除资源分类信息
     *
     * @param id 资源分类主键
     * @return 结果
     */
    @Override
    public int deleteResCategoryById(Long id)
    {
        return resCategoryMapper.deleteResCategoryById(id);
    }
}
