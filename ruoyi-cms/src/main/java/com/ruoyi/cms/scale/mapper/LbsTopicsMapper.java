package com.ruoyi.cms.scale.mapper;

import java.util.List;

import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.LbsTopics;
import com.ruoyi.cms.scale.domain.LbsOptions;
import com.ruoyi.cms.scale.domain.vo.LbsTopicsVo;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;

/**
 * 量表题目Mapper接口
 *
 * @author admin
 * @date 2025-05-05
 */
public interface LbsTopicsMapper
{

    /**
     * 查询部门管理数据
     *
     * @param lbsContexts 部门信息
     * @return 部门信息集合
     */
    public List<LbsContexts> selectContextList(LbsContexts lbsContexts);

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
     * 删除量表题目
     *
     * @param topicId 量表题目主键
     * @return 结果
     */
    public int deleteLbsTopicsByTopicId(Long topicId);

    /**
     * 批量删除量表题目
     *
     * @param topicIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLbsTopicsByTopicIds(Long[] topicIds);

    /**
     * 批量删除量选项
     *
     * @param topicIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLbsOptionsByTopicIds(Long[] topicIds);

    /**
     * 批量新增量选项
     *
     * @param lbsOptionsList 量选项列表
     * @return 结果
     */
    public int batchLbsOptions(List<LbsOptions> lbsOptionsList);


    /**
     * 通过量表题目主键删除量选项信息
     *
     * @param topicId 量表题目ID
     * @return 结果
     */
    public int deleteLbsOptionsByTopicId(Long topicId);

}
