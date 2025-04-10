package com.ruoyi.cms.job.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.cms.job.domain.ResJobinfo;
import org.apache.ibatis.annotations.Param;

/**
 * 招聘信息Mapper接口
 *
 * @author admin
 * @date 2025-04-10
 */
public interface ResJobinfoMapper
{
    /**
     * 查询招聘信息
     *
     * @param jobid 招聘信息主键
     * @return 招聘信息
     */
    public ResJobinfo selectResJobinfoByJobid(Long jobid);

    /**
     * 查询招聘信息列表
     *
     * @param resJobinfo 招聘信息
     * @return 招聘信息集合
     */
    public List<ResJobinfo> selectResJobinfoList(ResJobinfo resJobinfo);

    /**
     * 新增招聘信息
     *
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    public int insertResJobinfo(ResJobinfo resJobinfo);

    /**
     * 修改招聘信息
     *
     * @param resJobinfo 招聘信息
     * @return 结果
     */
    public int updateResJobinfo(ResJobinfo resJobinfo);

    /**
     * 删除招聘信息
     *
     * @param jobid 招聘信息主键
     * @return 结果
     */
    public int deleteResJobinfoByJobid(Long jobid);

    /**
     * 批量删除招聘信息
     *
     * @param jobids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResJobinfoByJobids(Long[] jobids);

    /**
     * 修改工作状态
     *
     * @param resJobInfo 工作
     * @return 结果
     */
    public int updateStatus(ResJobinfo resJobInfo);

    /**
     * 审批工作
     *
     * @param Id 工作主键
     * @return 结果
     */
    public int apporById(@Param("Id") Long Id, @Param("apporBy") String apporBy, @Param("apporTime") Date apporTime);

    /**
     * 批量审批工作
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int apporByIds(@Param("Ids") Long[] Ids,@Param("apporBy") String apporBy,@Param("apporTime") Date apporTime);

    /**
     * 反审批工作
     *
     * @param Id 工作主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批工作
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 工作主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);
}
