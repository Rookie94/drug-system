package com.ruoyi.cms.scale.service;

import java.util.List;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.ContextsTreeSelect;

/**
 * 量表目录Service接口
 * 
 * @author admin
 * @date 2025-05-05
 */
public interface ILbsContextsService 
{
    /**
     * 查询量表目录
     * 
     * @param contextId 量表目录主键
     * @return 量表目录
     */
    public LbsContexts selectLbsContextsByContextId(Integer contextId);

    /**
     * 查询量表目录列表
     * 
     * @param lbsContexts 量表目录
     * @return 量表目录集合
     */
    public List<LbsContexts> selectLbsContextsList(LbsContexts lbsContexts);

    /**
     * 新增量表目录
     * 
     * @param lbsContexts 量表目录
     * @return 结果
     */
    public int insertLbsContexts(LbsContexts lbsContexts);

    /**
     * 修改量表目录
     * 
     * @param lbsContexts 量表目录
     * @return 结果
     */
    public int updateLbsContexts(LbsContexts lbsContexts);

    /**
     * 批量删除量表目录
     * 
     * @param contextIds 需要删除的量表目录主键集合
     * @return 结果
     */
    public int deleteLbsContextsByContextIds(Integer[] contextIds);

    /**
     * 删除量表目录信息
     * 
     * @param contextId 量表目录主键
     * @return 结果
     */
    public int deleteLbsContextsByContextId(Integer contextId);

    /**
     * 查询量表树结构信息
     *
     * @param lbsContexts 量表信息
     * @return 量表树信息集合
     */
    public List<ContextsTreeSelect> selectLbsContextsTreeList(LbsContexts lbsContexts);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param contextsList 量表列表
     * @return 下拉树结构列表
     */
    public List<ContextsTreeSelect> buildContextTreeSelect(List<LbsContexts> contextsList);

    /**
     * 构建前端所需要树结构
     *
     * @param contextsList 量表列表
     * @return 树结构列表
     */
    public List<LbsContexts> buildContextTree(List<LbsContexts> contextsList);

}
