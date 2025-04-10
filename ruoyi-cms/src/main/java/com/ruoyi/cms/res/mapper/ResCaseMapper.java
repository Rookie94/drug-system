package com.ruoyi.cms.res.mapper;

import java.util.Date;
import java.util.List;
import com.ruoyi.cms.res.domain.ResCase;
import org.apache.ibatis.annotations.Param;

/**
 * 戒治案例Mapper接口
 * 
 * @author admin
 * @date 2025-04-10
 */
public interface ResCaseMapper 
{
    /**
     * 查询戒治案例
     * 
     * @param caseid 戒治案例主键
     * @return 戒治案例
     */
    public ResCase selectResCaseByCaseid(Long caseid);

    /**
     * 查询戒治案例列表
     * 
     * @param resCase 戒治案例
     * @return 戒治案例集合
     */
    public List<ResCase> selectResCaseList(ResCase resCase);

    /**
     * 新增戒治案例
     * 
     * @param resCase 戒治案例
     * @return 结果
     */
    public int insertResCase(ResCase resCase);

    /**
     * 修改戒治案例
     * 
     * @param resCase 戒治案例
     * @return 结果
     */
    public int updateResCase(ResCase resCase);

    /**
     * 删除戒治案例
     * 
     * @param caseid 戒治案例主键
     * @return 结果
     */
    public int deleteResCaseByCaseid(Long caseid);

    /**
     * 批量删除戒治案例
     * 
     * @param caseids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResCaseByCaseids(Long[] caseids);

    /**
     * 修改戒治案例状态
     *
     * @param resCase 戒治机构
     * @return 结果
     */
    public int updateStatus(ResCase resCase);

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
