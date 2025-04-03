package com.ruoyi.cms.res.mapper;

import java.util.List;
import com.ruoyi.cms.res.domain.BaseOrginfo;

/**
 * 戒治机构Mapper接口
 * 
 * @author admin
 * @date 2025-03-28
 */
public interface BaseOrginfoMapper 
{
    /**
     * 查询戒治机构
     * 
     * @param orgid 戒治机构主键
     * @return 戒治机构
     */
    public BaseOrginfo selectBaseOrginfoByOrgid(Long orgid);

    /**
     * 查询戒治机构列表
     * 
     * @param baseOrginfo 戒治机构
     * @return 戒治机构集合
     */
    public List<BaseOrginfo> selectBaseOrginfoList(BaseOrginfo baseOrginfo);

    /**
     * 新增戒治机构
     * 
     * @param baseOrginfo 戒治机构
     * @return 结果
     */
    public int insertBaseOrginfo(BaseOrginfo baseOrginfo);

    /**
     * 修改戒治机构
     * 
     * @param baseOrginfo 戒治机构
     * @return 结果
     */
    public int updateBaseOrginfo(BaseOrginfo baseOrginfo);

    /**
     * 删除戒治机构
     * 
     * @param orgid 戒治机构主键
     * @return 结果
     */
    public int deleteBaseOrginfoByOrgid(Long orgid);

    /**
     * 批量删除戒治机构
     * 
     * @param orgids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseOrginfoByOrgids(Long[] orgids);
}
