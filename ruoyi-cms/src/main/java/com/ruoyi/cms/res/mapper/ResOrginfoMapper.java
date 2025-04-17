package com.ruoyi.cms.res.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.cms.res.domain.ResOrginfo;
import org.apache.ibatis.annotations.Param;

/**
 * 戒治机构Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-08
 */
public interface ResOrginfoMapper 
{
    /**
     * 查询戒治机构
     * 
     * @param orgid 戒治机构主键
     * @return 戒治机构
     */
    public ResOrginfo selectResOrginfoByOrgid(Long orgid);

    /**
     * 查询戒治机构列表
     * 
     * @param resOrginfo 戒治机构
     * @return 戒治机构集合
     */
    public List<ResOrginfo> selectResOrginfoList(ResOrginfo resOrginfo);

    /**
     * 新增戒治机构
     * 
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    public int insertResOrginfo(ResOrginfo resOrginfo);

    /**
     * 修改戒治机构
     * 
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    public int updateResOrginfo(ResOrginfo resOrginfo);

    /**
     * 修改戒治机构状态
     *
     * @param resOrginfo 戒治机构
     * @return 结果
     */
    public int updateStatus(ResOrginfo resOrginfo);

    /**
     * 删除戒治机构
     * 
     * @param orgid 戒治机构主键
     * @return 结果
     */
    public int deleteResOrginfoByOrgid(Long orgid);

    /**
     * 批量删除戒治机构
     * 
     * @param orgids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResOrginfoByOrgids(Long[] orgids);

    /**
     * 批量审批
     *
     * @param apporParams 批量审批参数
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);

    /**
     * 反审批戒治机构
     *
     * @param orgid 戒治机构主键
     * @return 结果
     */
    public int unApporResOrginfoByOrgid(Long orgid);

    /**
     * 批量反审批戒治机构
     *
     * @param orgids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporResOrginfoByOrgids(Long[] orgids);

    /**
     * 查询已审核的单据清单
     *
     * @param orgids 戒治机构主键
     * @return 结果
     */
    public List<Integer> selectApporedOrgByOrgids(Long[] orgids);

}
