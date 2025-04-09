package com.ruoyi.cms.res.service;

import java.util.List;
import com.ruoyi.cms.res.domain.ResExpert;

/**
 * 戒治专家Service接口
 * 
 * @author admin
 * @date 2025-04-09
 */
public interface IResExpertService 
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
     * 批量删除戒治专家
     * 
     * @param expertids 需要删除的戒治专家主键集合
     * @return 结果
     */
    public int deleteResExpertByExpertids(Long[] expertids);

    /**
     * 删除戒治专家信息
     * 
     * @param expertid 戒治专家主键
     * @return 结果
     */
    public int deleteResExpertByExpertid(Long expertid);
}
