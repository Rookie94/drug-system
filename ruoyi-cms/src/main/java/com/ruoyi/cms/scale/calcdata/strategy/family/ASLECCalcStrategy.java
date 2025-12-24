package com.ruoyi.cms.scale.calcdata.strategy.family;

import com.alibaba.fastjson.JSON;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.calcdata.params.family.ASLECCalcParams;
import com.ruoyi.cms.scale.calcdata.service.impl.ASLECScaleCalculator;
import com.ruoyi.cms.scale.domain.dto.ASLECResultDTO;
import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.cms.scale.domain.vo.ContextVo;
import com.ruoyi.cms.scale.calcdata.strategy.ICalcStrategy;
import com.ruoyi.cms.scale.service.ILbsContextsService;
import com.ruoyi.common.core.domain.ProcResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ASLECCalcStrategy implements ICalcStrategy {

    private static final Logger logger = LoggerFactory.getLogger(ASLECCalcStrategy.class);

    @Autowired
    private ILbsContextsService lbsContextsService;

    @Autowired
    private ASLECScaleCalculator aslecScaleCalculator;

    @Override
    public ProcResult calculate(ContextAnswerVo contextAnswerVo) {
        ProcResult procResult = new ProcResult(false, "", null);

        try {
            // 1. 检查输入参数
            if (contextAnswerVo == null) {
                procResult.setMsg("测评数据为空");
                logger.error("测评数据为空");
                return procResult;
            }

            Long contextId = contextAnswerVo.getContextId();
            if (contextId == null) {
                procResult.setMsg("量表ID为空");
                logger.error("量表ID为空");
                return procResult;
            }

            // 2. 获取量表配置
            LbsContexts lbsContexts = lbsContextsService.selectLbsContextsByContextId(contextId);
            if (lbsContexts == null) {
                procResult.setMsg("未找到量表配置, contextId: " + contextId);
                logger.error("未找到量表配置, contextId: {}", contextId);
                return procResult;
            }

            // 3. 获取题目详情
            ContextVo contextVo = lbsContextsService.selectContextWithRelations(contextId);
            if (contextVo == null) {
                procResult.setMsg("未找到量表题目详情, contextId: " + contextId);
                logger.error("未找到量表题目详情, contextId: {}", contextId);
                return procResult;
            }

            // 4. 解析ASLEC计算参数
            String jsonStr = lbsContexts.getJsonParams();
            ASLECCalcParams aslecCalcParams = new ASLECCalcParams();

            if (jsonStr != null && !jsonStr.trim().isEmpty()) {
                try {
                    aslecCalcParams = JSON.parseObject(jsonStr, ASLECCalcParams.class);
                } catch (Exception e) {
                    logger.warn("ASLEC计算参数解析失败，使用默认参数: {}", e.getMessage());
                    // 使用默认参数
                }
            }

            if (aslecCalcParams == null) {
                aslecCalcParams = new ASLECCalcParams();
            }

            // 5. 检查计算器是否注入
            if (aslecScaleCalculator == null) {
                procResult.setMsg("ASLECScaleCalculator未正确注入");
                logger.error("ASLECScaleCalculator未正确注入");
                return procResult;
            }

            // 6. 检查答题数据
            if (contextAnswerVo.getAnswers() == null || contextAnswerVo.getAnswers().isEmpty()) {
                procResult.setMsg("答题数据为空");
                logger.error("答题数据为空");
                return procResult;
            }

            // 7. 调用计算器进行计算
            ASLECResultDTO aslecResult;
            try {
                aslecResult = aslecScaleCalculator.calculateResult(
                        contextAnswerVo,
                        aslecCalcParams,
                        contextVo,
                        null
                );
            } catch (Exception e) {
                procResult.setMsg("ASLEC计算过程异常: " + e.getMessage());
                logger.error("ASLEC计算过程异常, error: ", e);
                return procResult;
            }

            if (aslecResult == null) {
                procResult.setMsg("ASLEC计算结果为空");
                logger.error("ASLEC计算结果为空");
                return procResult;
            }

            // 8. 将结果转换为JSON字符串
            String resultJson;
            try {
                resultJson = aslecScaleCalculator.convertToJson(aslecResult);
            } catch (Exception e) {
                procResult.setMsg("ASLEC结果JSON序列化失败: " + e.getMessage());
                logger.error("ASLEC结果JSON序列化失败, error: ", e);
                return procResult;
            }

            if (resultJson == null || resultJson.trim().isEmpty()) {
                procResult.setMsg("ASLEC结果JSON为空");
                logger.error("ASLEC结果JSON为空");
                return procResult;
            }

            // 9. 设置返回结果
            procResult.setResult(true);
            procResult.setMsg("ASLEC计算成功");
            procResult.setData(resultJson);

            logger.info("ASLEC计算成功, contextId: {}, 总分: {}, 压力水平: {}",
                    contextId, aslecResult.getRawTotalScore(), aslecResult.getStressLevel());

        } catch (Exception ex) {
            logger.error("ASLEC计算发生未预期异常: ", ex);
            procResult.setMsg("ASLEC计算发生未预期异常: " + ex.getMessage());
        }

        return procResult;
    }
}