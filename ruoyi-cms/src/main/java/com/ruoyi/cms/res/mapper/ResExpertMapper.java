package com.ruoyi.cms.res.mapper;

import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.cms.res.domain.ResExpert;
import org.apache.ibatis.annotations.Param;

/**
 * 戒治专家Mapper接口
 * 
 * @author admin
 * @date 2025-04-09
 */
public interface ResExpertMapper 
{
    /**
     * 查询戒治专家
     * 
     * @param expertid 戒治专家主键
     * @return 戒治专家
     */
    public ResExpert selectResExpertByExpertid(Long expertid);

    /**
     * 查询戒治专家列表
     * 
     * @param resExpert 戒治专家
     * @return 戒治专家集合
     */
    public List<ResExpert> selectResExpertList(ResExpert resExpert);

    /**
     * 新增戒治专家
     * 
     * @param resExpert 戒治专家
     * @return 结果
     */
    public int insertResExpert(ResExpert resExpert);

    /**
     * 修改戒治专家
     * 
     * @param resExpert 戒治专家
     * @return 结果
     */
    public int updateResExpert(ResExpert resExpert);

    /**
     * 删除戒治专家
     * 
     * @param expertid 戒治专家主键
     * @return 结果
     */
    public int deleteResExpertByExpertid(Long expertid);

    /**
     * 批量删除戒治专家
     * 
     * @param expertids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResExpertByExpertids(Long[] expertids);

    /**
     * 修改专家状态
     *
     * @param resExpert 戒治机构
     * @return 结果
     */
    public int updateStatus(ResExpert resExpert);

    /**
     * 批量审批
     *
     * @param apporParams 批量审批参数
     * @return 结果
     */
    public int apporByIds(ResApporParam apporParams);
    /**
     * 反审批专家
     *
     * @param Id 专家主键
     * @return 结果
     */
    public int unApporById(Long Id);

    /**
     * 批量反审批专家
     *
     * @param Ids 需要删除的数据主键集合
     * @return 结果
     */
    public int unApporByIds(Long[] Ids);

    /**
     * 查询已审核的单据清单
     *
     * @param Ids 专家主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] Ids);

}
