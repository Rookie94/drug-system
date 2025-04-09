package com.ruoyi.cms.res.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResExpertMapper;
import com.ruoyi.cms.res.domain.ResExpert;
import com.ruoyi.cms.res.service.IResExpertService;

/**
 * 戒治专家Service业务层处理
 * 
 * @author admin
 * @date 2025-04-09
 */
@Service
public class ResExpertServiceImpl implements IResExpertService 
{
    @Autowired
    private ResExpertMapper resExpertMapper;

    /**
     * 查询戒治专家
     * 
     * @param expertid 戒治专家主键
     * @return 戒治专家
     */
    @Override
    public ResExpert selectResExpertByExpertid(Long expertid)
    {
        return resExpertMapper.selectResExpertByExpertid(expertid);
    }

    /**
     * 查询戒治专家列表
     * 
     * @param resExpert 戒治专家
     * @return 戒治专家
     */
    @Override
    public List<ResExpert> selectResExpertList(ResExpert resExpert)
    {
        return resExpertMapper.selectResExpertList(resExpert);
    }

    /**
     * 新增戒治专家
     * 
     * @param resExpert 戒治专家
     * @return 结果
     */
    @Override
    public int insertResExpert(ResExpert resExpert)
    {
        resExpert.setCreateTime(DateUtils.getNowDate());
        return resExpertMapper.insertResExpert(resExpert);
    }

    /**
     * 修改戒治专家
     * 
     * @param resExpert 戒治专家
     * @return 结果
     */
    @Override
    public int updateResExpert(ResExpert resExpert)
    {
        resExpert.setUpdateTime(DateUtils.getNowDate());
        return resExpertMapper.updateResExpert(resExpert);
    }

    /**
     * 批量删除戒治专家
     * 
     * @param expertids 需要删除的戒治专家主键
     * @return 结果
     */
    @Override
    public int deleteResExpertByExpertids(Long[] expertids)
    {
        return resExpertMapper.deleteResExpertByExpertids(expertids);
    }

    /**
     * 删除戒治专家信息
     * 
     * @param expertid 戒治专家主键
     * @return 结果
     */
    @Override
    public int deleteResExpertByExpertid(Long expertid)
    {
        return resExpertMapper.deleteResExpertByExpertid(expertid);
    }
}
