package com.ruoyi.cms.scale.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.cms.scale.domain.ContextsTreeSelect;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.scale.mapper.LbsContextsMapper;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.service.ILbsContextsService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 量表目录Service业务层处理
 * 
 * @author admin
 * @date 2025-05-05
 */
@Service
public class LbsContextsServiceImpl implements ILbsContextsService 
{
    @Autowired
    private LbsContextsMapper lbsContextsMapper;

    /**
     * 查询量表目录
     * 
     * @param contextId 量表目录主键
     * @return 量表目录
     */
    @Override
    public LbsContexts selectLbsContextsByContextId(Integer contextId)
    {
        return lbsContextsMapper.selectLbsContextsByContextId(contextId);
    }

    /**
     * 查询量表目录列表
     * 
     * @param lbsContexts 量表目录
     * @return 量表目录
     */
    @Override
    public List<LbsContexts> selectLbsContextsList(LbsContexts lbsContexts)
    {
        return lbsContextsMapper.selectLbsContextsList(lbsContexts);
    }

    /**
     * 新增量表目录
     * 
     * @param lbsContexts 量表目录
     * @return 结果
     */
    @Override
    public int insertLbsContexts(LbsContexts lbsContexts)
    {
        lbsContexts.setCreateBy(getUsername());
        lbsContexts.setCreateTime(DateUtils.getNowDate());
        return lbsContextsMapper.insertLbsContexts(lbsContexts);
    }

    /**
     * 修改量表目录
     * 
     * @param lbsContexts 量表目录
     * @return 结果
     */
    @Override
    public int updateLbsContexts(LbsContexts lbsContexts)
    {
        lbsContexts.setUpdateBy(getUsername());
        lbsContexts.setUpdateTime(DateUtils.getNowDate());
        return lbsContextsMapper.updateLbsContexts(lbsContexts);
    }

    /**
     * 批量删除量表目录
     * 
     * @param contextIds 需要删除的量表目录主键
     * @return 结果
     */
    @Override
    public int deleteLbsContextsByContextIds(Integer[] contextIds)
    {
        return lbsContextsMapper.deleteLbsContextsByContextIds(contextIds);
    }

    /**
     * 删除量表目录信息
     * 
     * @param contextId 量表目录主键
     * @return 结果
     */
    @Override
    public int deleteLbsContextsByContextId(Integer contextId)
    {
        return lbsContextsMapper.deleteLbsContextsByContextId(contextId);
    }

    /**
     * 查询量表树结构信息
     *
     * @param lbsContexts 量表信息
     * @return 量表树信息集合
     */
    @Override
    public List<ContextsTreeSelect> selectLbsContextsTreeList(LbsContexts lbsContexts)
    {
        List<LbsContexts> contextsList = SpringUtils.getAopProxy(this).selectLbsContextsList(lbsContexts);
        return buildContextTreeSelect(contextsList);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param contextsList 量表列表
     * @return 树结构列表
     */
    @Override
    public List<LbsContexts> buildContextTree(List<LbsContexts> contextsList)
    {
        List<LbsContexts> returnList = new ArrayList<LbsContexts>();
        List<Long> tempList = contextsList.stream().map(LbsContexts::getContextId).collect(Collectors.toList());
        for (LbsContexts lbsContexts : contextsList)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(lbsContexts.getParentContextId()))
            {
                recursionFn(contextsList, lbsContexts);
                returnList.add(lbsContexts);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = contextsList;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param contextsList 量表列表
     * @return 下拉树结构列表
     */
    @Override
    public List<ContextsTreeSelect> buildContextTreeSelect(List<LbsContexts> contextsList)
    {
        List<LbsContexts> contextTrees = buildContextTree(contextsList);
        return contextTrees.stream().map(ContextsTreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<LbsContexts> list, LbsContexts t)
    {
        // 得到子节点列表
        List<LbsContexts> childList = getChildList(list, t);
        t.setChildren(childList);
        for (LbsContexts tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<LbsContexts> getChildList(List<LbsContexts> list, LbsContexts t)
    {
        List<LbsContexts> tlist = new ArrayList<LbsContexts>();
        Iterator<LbsContexts> it = list.iterator();
        while (it.hasNext())
        {
            LbsContexts n = (LbsContexts) it.next();
            if (StringUtils.isNotNull(n.getParentContextId()) && n.getParentContextId().longValue() == t.getContextId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<LbsContexts> list, LbsContexts t)
    {
        return getChildList(list, t).size() > 0;
    }

}
