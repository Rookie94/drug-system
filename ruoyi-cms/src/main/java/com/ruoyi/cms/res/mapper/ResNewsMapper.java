package com.ruoyi.cms.res.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.cms.res.domain.ResCase;
import com.ruoyi.cms.res.domain.ResNews;
import org.apache.ibatis.annotations.Param;

/**
 * 戒毒资讯Mapper接口
 * 
 * @author admin
 * @date 2025-04-10
 */
public interface ResNewsMapper 
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
     * 删除戒毒资讯
     * 
     * @param newsid 戒毒资讯主键
     * @return 结果
     */
    public int deleteResNewsByNewsid(Long newsid);

    /**
     * 批量删除戒毒资讯
     * 
     * @param newsids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResNewsByNewsids(Long[] newsids);

    /**
     * 修改戒治案例状态
     *
     * @param resNews 戒治机构
     * @return 结果
     */
    public int updateStatus(ResNews resNews);

    /**
     * 审批戒治案例
     *
     * @param Id 戒治案例主键
     * @return 结果
     */
    public int apporById(@Param("Id") Long Id, @Param("apporBy") String apporBy, @Param("apporTime") Date apporTime);

    /**
     * 批量审批戒治案例
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int apporByIds(@Param("Ids") Long[] Ids,@Param("apporBy") String apporBy,@Param("apporTime") Date apporTime);

    /**
     * 反审批戒治案例
     *
     * @param Id 戒治案例主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批戒治案例
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 戒治案例主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);

}
