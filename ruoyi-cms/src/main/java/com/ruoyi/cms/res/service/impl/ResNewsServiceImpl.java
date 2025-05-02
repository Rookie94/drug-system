package com.ruoyi.cms.res.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ruoyi.system.domain.ResApporParam;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.res.mapper.ResNewsMapper;
import com.ruoyi.cms.res.domain.ResNews;
import com.ruoyi.cms.res.service.IResNewsService;

import static com.ruoyi.common.utils.SecurityUtils.*;

/**
 * 戒毒资讯Service业务层处理
 * 
 * @author admin
 * @date 2025-04-10
 */
@Service
public class ResNewsServiceImpl implements IResNewsService 
{
    @Autowired
    private ResNewsMapper resNewsMapper;

    /**
     * 查询戒毒资讯
     * 
     * @param newsid 戒毒资讯主键
     * @return 戒毒资讯
     */
    @Override
    public ResNews selectResNewsByNewsid(Long newsid)
    {
        return resNewsMapper.selectResNewsByNewsid(newsid);
    }

    /**
     * 查询戒毒资讯列表
     * 
     * @param resNews 戒毒资讯
     * @return 戒毒资讯
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ResNews> selectResNewsList(ResNews resNews)
    {
        return resNewsMapper.selectResNewsList(resNews);
    }

    /**
     * 新增戒毒资讯
     * 
     * @param resNews 戒毒资讯
     * @return 结果
     */
    @Override
    public int insertResNews(ResNews resNews)
    {
        resNews.setUserId(getUserId());
        resNews.setDeptId(getDeptId());
        resNews.setCreateBy(getUsername());
        resNews.setCreateTime(DateUtils.getNowDate());
        return resNewsMapper.insertResNews(resNews);
    }

    /**
     * 修改戒毒资讯
     * 
     * @param resNews 戒毒资讯
     * @return 结果
     */
    @Override
    public int updateResNews(ResNews resNews)
    {
        resNews.setUpdateBy(getUsername());
        resNews.setUpdateTime(DateUtils.getNowDate());
        return resNewsMapper.updateResNews(resNews);
    }

    /**
     * 批量删除戒毒资讯
     * 
     * @param newsids 需要删除的戒毒资讯主键
     * @return 结果
     */
    @Override
    public int deleteResNewsByNewsids(Long[] newsids)
    {
        return resNewsMapper.deleteResNewsByNewsids(newsids);
    }

    /**
     * 删除戒毒资讯信息
     * 
     * @param newsid 戒毒资讯主键
     * @return 结果
     */
    @Override
    public int deleteResNewsByNewsid(Long newsid)
    {
        return resNewsMapper.deleteResNewsByNewsid(newsid);
    }

    /**
     * 修改戒毒资讯状态
     *
     * @param resNews 戒治机构
     * @return 结果
     */
    public int updateStatus(ResNews resNews)
    {
        resNews.setUpdateBy(getUsername());
        resNews.setUpdateTime(DateUtils.getNowDate());
        return resNewsMapper.updateStatus(resNews);
    }

    /**
     * 批量审批
     *
     * @param apporParams 审批参数
     * @return 结果
     */
    @Override
    public int apporByIds(ResApporParam apporParams)
    {
        apporParams.setApporBy(getUsername());
        apporParams.setApporTime(DateUtils.getNowDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(apporParams.flag==2){
            apporParams.setPublishTime(dateFormat.format(DateUtils.getNowDate()));
        }
        return resNewsMapper.apporByIds(apporParams);
    }

    /**
     * 反审批戒毒资讯
     *
     * @param id 戒毒资讯主键
     * @return 结果
     */
    @Override
    public int unApporById(Long id)
    {
        return resNewsMapper.unApporById(id);
    }

    /**
     * 批量反审批戒毒资讯
     *
     * @param ids 需要删除的戒毒资讯主键
     * @return 结果
     */
    @Override
    public int unApporByIds(Long[] ids)
    {
        return resNewsMapper.unApporByIds(ids);
    }

    /**
     * 查询已审核的单据清单
     *
     * @param ids 戒毒资讯主键
     * @return 结果
     */
    public List<Integer> selectApporedByIds(Long[] ids){
        return resNewsMapper.selectApporedByIds(ids);
    }


}
