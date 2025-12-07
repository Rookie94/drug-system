package com.ruoyi.cms.offline.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.cms.offline.domain.ActivitiesReview;
import com.ruoyi.cms.offline.domain.vo.ActivitiesReviewVo;

/**
 * 活动评价Service接口
 * 
 * @author admin
 * @date 2025-05-09
 */
public interface IActivitiesReviewService 
{
    /**
     * 查询活动评价
     * 
     * @param reviewId 活动评价主键
     * @return 活动评价
     */
    public ActivitiesReviewVo selectActivitiesReviewByReviewId(Long reviewId);

    /**
     * 查询活动评价列表
     * 
     * @param activitiesReview 活动评价
     * @return 活动评价集合
     */
    public List<ActivitiesReviewVo> selectActivitiesReviewList(ActivitiesReviewVo activitiesReview);

    /**
     * 新增活动评价
     * 
     * @param activitiesReview 活动评价
     * @return 结果
     */
    public int insertActivitiesReview(ActivitiesReview activitiesReview);

    /**
     * 修改活动评价
     * 
     * @param activitiesReview 活动评价
     * @return 结果
     */
    public int updateActivitiesReview(ActivitiesReview activitiesReview);

    /**
     * 批量删除活动评价
     * 
     * @param reviewIds 需要删除的活动评价主键集合
     * @return 结果
     */
    public int deleteActivitiesReviewByReviewIds(Long[] reviewIds);

    /**
     * 删除活动评价信息
     * 
     * @param reviewId 活动评价主键
     * @return 结果
     */
    public int deleteActivitiesReviewByReviewId(Long reviewId);


    /**
     * 插入默认好评记录
     * @return 插入的记录数
     */
    int insertDefaultReviews();

    /**
     * 查询需要插入默认好评的记录
     * @return 需要插入的记录列表
     */
    List<Map<String, Object>> selectNeedDefaultReviews();

}
