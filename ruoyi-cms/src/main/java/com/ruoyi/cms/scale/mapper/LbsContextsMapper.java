package com.ruoyi.cms.scale.mapper;

import java.util.List;
import com.ruoyi.cms.scale.domain.LbsContexts;

/**
 * 量表目录Mapper接口
 * 
 * @author admin
 * @date 2025-05-05
 */
public interface LbsContextsMapper 
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
     * 删除量表目录
     * 
     * @param contextId 量表目录主键
     * @return 结果
     */
    public int deleteLbsContextsByContextId(Integer contextId);

    /**
     * 批量删除量表目录
     * 
     * @param contextIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLbsContextsByContextIds(Integer[] contextIds);
}
