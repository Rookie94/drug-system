package com.ruoyi.cms.res.service.impl;

import java.util.List;
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
