package com.ruoyi.cms.scale.service;

import java.util.List;
import com.ruoyi.cms.scale.domain.vo.AnswerVo;

public interface ILbsAnswerService {

    // 查询单个答案
    public AnswerVo getAnswerById(Long answerId);

    // 条件查询答案列表
    public List<AnswerVo> getAnswerList(AnswerVo query);

    // 新增答案
    public int insertAnswer(AnswerVo answerVo);

    // 批量新增答案
    public int batchInsertAnswer(List<AnswerVo> list);

    // 更新答案
    public int updateAnswer(AnswerVo answerVo);

    // 删除答案
    public int deleteAnswerById(Long answerId);

    // 根据 resultId 删除答案
    public int deleteAnswerByResultId(Long resultId);
}
