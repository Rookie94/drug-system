package com.ruoyi.cms.scale.service.impl;

import java.util.List;

import com.ruoyi.cms.scale.domain.vo.LbsTopicsVo;
import com.ruoyi.cms.scale.service.ILbsTopicsService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.cms.scale.domain.LbsOptions;
import com.ruoyi.cms.scale.mapper.LbsTopicsMapper;
import com.ruoyi.cms.scale.domain.LbsTopics;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 量表题目Service业务层处理
 * 
 * @author admin
 * @date 2025-05-05
 */
@Service
public class LbsTopicsServiceImpl implements ILbsTopicsService
{
    @Autowired
    private LbsTopicsMapper lbsTopicsMapper;

    /**
     * 查询量表题目
     * 
     * @param topicId 量表题目主键
     * @return 量表题目
     */
    @Override
    public LbsTopics selectLbsTopicsByTopicId(Long topicId)
    {
        return lbsTopicsMapper.selectLbsTopicsByTopicId(topicId);
    }

    /**
     * 查询量表题目列表
     * 
     * @param lbsTopics 量表题目
     * @return 量表题目
     */
    @Override
    public List<LbsTopics> selectLbsTopicsList(LbsTopicsVo lbsTopics)
    {
        return lbsTopicsMapper.selectLbsTopicsList(lbsTopics);
    }

    /**
     * 新增量表题目
     * 
     * @param lbsTopics 量表题目
     * @return 结果
     */
    @Transactional
    @Override
    public int insertLbsTopics(LbsTopics lbsTopics)
    {
        lbsTopics.setCreateBy(getUsername());
        lbsTopics.setCreateTime(DateUtils.getNowDate());
        int rows = lbsTopicsMapper.insertLbsTopics(lbsTopics);
        insertLbsOptions(lbsTopics);
        return rows;
    }

    /**
     * 修改量表题目
     * 
     * @param lbsTopics 量表题目
     * @return 结果
     */
    @Transactional
    @Override
    public int updateLbsTopics(LbsTopics lbsTopics)
    {
        lbsTopics.setUpdateBy(getUsername());
        lbsTopics.setUpdateTime(DateUtils.getNowDate());
        lbsTopicsMapper.deleteLbsOptionsByTopicId(lbsTopics.getTopicId());
        insertLbsOptions(lbsTopics);
        return lbsTopicsMapper.updateLbsTopics(lbsTopics);
    }

    /**
     * 批量删除量表题目
     * 
     * @param topicIds 需要删除的量表题目主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteLbsTopicsByTopicIds(Long[] topicIds)
    {
        lbsTopicsMapper.deleteLbsOptionsByTopicIds(topicIds);
        return lbsTopicsMapper.deleteLbsTopicsByTopicIds(topicIds);
    }

    /**
     * 删除量表题目信息
     * 
     * @param topicId 量表题目主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteLbsTopicsByTopicId(Long topicId)
    {
        lbsTopicsMapper.deleteLbsOptionsByTopicId(topicId);
        return lbsTopicsMapper.deleteLbsTopicsByTopicId(topicId);
    }

    /**
     * 新增量选项信息
     * 
     * @param lbsTopics 量表题目对象
     */
    public void insertLbsOptions(LbsTopics lbsTopics)
    {
        List<LbsOptions> lbsOptionsList = lbsTopics.getLbsOptionsList();
        Long topicId = lbsTopics.getTopicId();
        if (StringUtils.isNotNull(lbsOptionsList))
        {
            List<LbsOptions> list = new ArrayList<LbsOptions>();
            for (LbsOptions lbsOptions : lbsOptionsList)
            {
                lbsOptions.setTopicId(topicId);
                list.add(lbsOptions);
            }
            if (list.size() > 0)
            {
                lbsTopicsMapper.batchLbsOptions(list);
            }
        }
    }
}
