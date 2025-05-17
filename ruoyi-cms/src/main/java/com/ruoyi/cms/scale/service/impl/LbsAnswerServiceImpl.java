package com.ruoyi.cms.scale.service.impl;

import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import com.ruoyi.cms.scale.mapper.LbsAnswerMapper;
import com.ruoyi.cms.scale.service.ILbsAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class LbsAnswerServiceImpl implements ILbsAnswerService {

    @Autowired
    private LbsAnswerMapper answerMapper;

    @Override
    public AnswerVo getAnswerById(Long answerId) {
        return answerMapper.selectAnswerById(answerId);
    }

    @Override
    public List<AnswerVo> getAnswerList(AnswerVo query) {
        return answerMapper.selectAnswerList(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAnswer(AnswerVo answerVo) {
        if (answerVo.getResultId() == null || answerVo.getTopicId() == null) {
            throw new IllegalArgumentException("resultId 和 topicId 不能为空");
        }
        return answerMapper.insertAnswer(answerVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertAnswer(List<AnswerVo> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return answerMapper.batchInsertAnswer(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAnswer(AnswerVo answerVo) {
        return answerMapper.updateAnswer(answerVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAnswerById(Long answerId) {
        return answerMapper.deleteAnswerById(answerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAnswerByResultId(Long resultId) {
        return answerMapper.deleteAnswerByResultId(resultId);
    }
}
