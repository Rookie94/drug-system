package com.ruoyi.cms.scale.service;

import java.util.List;

import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.LbsTopics;
import com.ruoyi.cms.scale.domain.vo.LbsTopicsVo;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;

/**
 * 量表题目Service接口
 * 
 * @author admin
 * @date 2025-05-05
 */
public interface ILbsTopicsService 
{
    /**
     * 查询量表题目
     * 
     * @param topicId 量表题目主键
     * @return 量表题目
     */
    public LbsTopics selectLbsTopicsByTopicId(Long topicId);

    /**
     * 查询量表题目列表
     * 
     * @param lbsTopics 量表题目
     * @return 量表题目集合
     */
    public List<LbsTopics> selectLbsTopicsList(LbsTopicsVo lbsTopics);

    /**
     * 新增量表题目
     * 
     * @param lbsTopics 量表题目
     * @return 结果
     */
    public int insertLbsTopics(LbsTopics lbsTopics);

    /**
     * 修改量表题目
     * 
     * @param lbsTopics 量表题目
     * @return 结果
     */
    public int updateLbsTopics(LbsTopics lbsTopics);

    /**
     * 批量删除量表题目
     * 
     * @param topicIds 需要删除的量表题目主键集合
     * @return 结果
     */
    public int deleteLbsTopicsByTopicIds(Long[] topicIds);

    /**
     * 删除量表题目信息
     * 
     * @param topicId 量表题目主键
     * @return 结果
     */
    public int deleteLbsTopicsByTopicId(Long topicId);



}
