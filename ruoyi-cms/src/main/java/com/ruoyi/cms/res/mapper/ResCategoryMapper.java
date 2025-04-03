package com.ruoyi.cms.res.mapper;

import java.util.List;
import com.ruoyi.cms.res.domain.ResCategory;

/**
 * 资源分类Mapper接口
 *
 * @author admin
 * @date 2025-03-28
 */
public interface ResCategoryMapper
{
    /**
     * 查询资源分类
     *
     * @param id 资源分类主键
     * @return 资源分类
     */
    public ResCategory selectResCategoryById(Long id);

    /**
     * 查询资源分类列表
     *
     * @param resCategory 资源分类
     * @return 资源分类集合
     */
    public List<ResCategory> selectResCategoryList(ResCategory resCategory);

    /**
     * 新增资源分类
     *
     * @param resCategory 资源分类
     * @return 结果
     */
    public int insertResCategory(ResCategory resCategory);

    /**
     * 修改资源分类
     *
     * @param resCategory 资源分类
     * @return 结果
     */
    public int updateResCategory(ResCategory resCategory);

    /**
     * 删除资源分类
     *
     * @param id 资源分类主键
     * @return 结果
     */
    public int deleteResCategoryById(Long id);

    /**
     * 批量删除资源分类
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResCategoryByIds(Long[] ids);
}
