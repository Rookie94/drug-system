package com.ruoyi.cms.res.service;

import java.util.List;

import com.ruoyi.cms.res.domain.ResCase;
import com.ruoyi.cms.res.domain.ResNews;

/**
 * 戒毒资讯Service接口
 * 
 * @author admin
 * @date 2025-04-10
 */
public interface IResNewsService 
{
    /**
     * 查询戒毒资讯
     * 
     * @param newsid 戒毒资讯主键
     * @return 戒毒资讯
     */
    public ResNews selectResNewsByNewsid(Long newsid);

    /**
     * 查询戒毒资讯列表
     * 
     * @param resNews 戒毒资讯
     * @return 戒毒资讯集合
     */
    public List<ResNews> selectResNewsList(ResNews resNews);

    /**
     * 新增戒毒资讯
     * 
     * @param resNews 戒毒资讯
     * @return 结果
     */
    public int insertResNews(ResNews resNews);

    /**
     * 修改戒毒资讯
     * 
     * @param resNews 戒毒资讯
     * @return 结果
     */
    public int updateResNews(ResNews resNews);

    /**
     * 批量删除戒毒资讯
     * 
     * @param newsids 需要删除的戒毒资讯主键集合
     * @return 结果
     */
    public int deleteResNewsByNewsids(Long[] newsids);

    /**
     * 删除戒毒资讯信息
     * 
     * @param newsid 戒毒资讯主键
     * @return 结果
     */
    public int deleteResNewsByNewsid(Long newsid);

    /**
     * 修改戒毒资讯状态
     *
     * @param resNews 戒治机构
     * @return 结果
     */
    public int updateStatus(ResNews resNews);

    /**
     * 批量审批戒毒资讯
     *
     * @param ids 需要删除的戒治案例主键集合
     * @return 结果
     */
    public int apporByIds(Long[] ids);

    /**
     * 审批戒毒资讯
     *
     * @param id 戒治案例主键
     * @return 结果
     */
    public int apporById(Long id);

    /**
     * 批量反审批戒毒资讯
     *
     * @param ids 需要删除的戒治案例主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] ids);

    /**
     * 反审批戒毒资讯信息
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public int unApporById(Long ids);

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids);

}
