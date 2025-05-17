package com.ruoyi.cms.scale.mapper;

import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LbsAnswerMapper {

    // 查询单个
    public AnswerVo selectAnswerById(Long answerId);

    // 查询列表（支持条件过滤）
    public List<AnswerVo> selectAnswerList(AnswerVo answerVo);

    // 插入单条数据
    public int insertAnswer(AnswerVo answerVo);

    // 批量插入数据（注意参数注解 @Param）
    public int batchInsertAnswer(@Param("list") List<AnswerVo> list);

    // 更新数据
    public  int updateAnswer(AnswerVo answerVo);

    // 根据 answerId 删除
    public int deleteAnswerById(Long answerId);

    // 根据 resultId 删除
    public int deleteAnswerByResultId(Long resultId);
}