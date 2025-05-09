package com.ruoyi.cms.offline.service.impl;

import java.util.List;

import com.ruoyi.cms.offline.domain.vo.ActivitiesReviewVo;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.cms.offline.mapper.ActivitiesReviewMapper;
import com.ruoyi.cms.offline.domain.ActivitiesReview;
import com.ruoyi.cms.offline.service.IActivitiesReviewService;

import static com.ruoyi.common.utils.SecurityUtils.getUsername;

/**
 * 活动评价Service业务层处理
 * 
 * @author admin
 * @date 2025-05-09
 */
@Service
public class ActivitiesReviewServiceImpl implements IActivitiesReviewService 
{
    @Autowired
    private ActivitiesReviewMapper activitiesReviewMapper;

    /**
     * 查询活动评价
     * 
     * @param reviewId 活动评价主键
     * @return 活动评价
     */
    @Override
    public ActivitiesReviewVo selectActivitiesReviewByReviewId(Long reviewId)
    {
        return activitiesReviewMapper.selectActivitiesReviewByReviewId(reviewId);
    }

    /**
     * 查询活动评价列表
     * 
     * @param activitiesReview 活动评价
     * @return 活动评价
     */
    @Override
    @DataScope(deptAlias = "t", userAlias = "t")
    public List<ActivitiesReviewVo> selectActivitiesReviewList(ActivitiesReviewVo activitiesReview)
    {
        return activitiesReviewMapper.selectActivitiesReviewList(activitiesReview);
    }

    /**
     * 新增活动评价
     * 
     * @param activitiesReview 活动评价
     * @return 结果
     */
    @Override
    public int insertActivitiesReview(ActivitiesReview activitiesReview)
    {
        activitiesReview.setCreateTime(DateUtils.getNowDate());
        return activitiesReviewMapper.insertActivitiesReview(activitiesReview);
    }

    /**
     * 修改活动评价
     * 
     * @param activitiesReview 活动评价
     * @return 结果
     */
    @Override
    public int updateActivitiesReview(ActivitiesReview activitiesReview)
    {
        activitiesReview.setUpdateBy(getUsername());
        activitiesReview.setUpdateTime(DateUtils.getNowDate());
        return activitiesReviewMapper.updateActivitiesReview(activitiesReview);
    }

    /**
     * 批量删除活动评价
     * 
     * @param reviewIds 需要删除的活动评价主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesReviewByReviewIds(Long[] reviewIds)
    {
        return activitiesReviewMapper.deleteActivitiesReviewByReviewIds(reviewIds);
    }

    /**
     * 删除活动评价信息
     * 
     * @param reviewId 活动评价主键
     * @return 结果
     */
    @Override
    public int deleteActivitiesReviewByReviewId(Long reviewId)
    {
        return activitiesReviewMapper.deleteActivitiesReviewByReviewId(reviewId);
    }
}
