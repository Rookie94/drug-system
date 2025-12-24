package com.ruoyi.cms.scale.calcdata.strategy.standard;

import com.alibaba.fastjson.JSON;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.calcdata.params.standard.HAMACalcParams;
import com.ruoyi.cms.scale.calcdata.service.impl.HAMAScaleCalculator;
import com.ruoyi.cms.scale.domain.dto.HAMAResultDTO;
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
public class HAMACalcStrategy implements ICalcStrategy {

    private static final Logger logger = LoggerFactory.getLogger(HAMACalcStrategy.class);

    @Autowired
    private ILbsContextsService lbsContextsService;

    @Autowired
    private HAMAScaleCalculator hamaScaleCalculator;

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

            // 4. 解析HAMA计算参数
            String jsonStr = lbsContexts.getJsonParams();
            if (jsonStr == null || jsonStr.trim().isEmpty()) {
                procResult.setMsg("量表配置参数为空, contextId: " + contextId);
                logger.error("量表配置参数为空, contextId: {}", contextId);
                return procResult;
            }

            HAMACalcParams hamaCalcParams;
            try {
                hamaCalcParams = JSON.parseObject(jsonStr, HAMACalcParams.class);
            } catch (Exception e) {
                procResult.setMsg("HAMA计算参数解析失败: " + e.getMessage());
                logger.error("HAMA计算参数解析失败, json: {}, error: {}", jsonStr, e.getMessage());
                return procResult;
            }

            if (hamaCalcParams == null) {
                procResult.setMsg("HAMA计算参数解析后为空");
                logger.error("HAMA计算参数解析后为空");
                return procResult;
            }

            // 5. 检查计算器是否注入
            if (hamaScaleCalculator == null) {
                procResult.setMsg("HAMAScaleCalculator未正确注入");
                logger.error("HAMAScaleCalculator未正确注入");
                return procResult;
            }

            // 6. 检查答题数据
            if (contextAnswerVo.getAnswers() == null || contextAnswerVo.getAnswers().isEmpty()) {
                procResult.setMsg("答题数据为空");
                logger.error("答题数据为空");
                return procResult;
            }

            // 7. 调用计算器进行计算
            HAMAResultDTO hamaResult;
            try {
                hamaResult = hamaScaleCalculator.calculateResult(
                        contextAnswerVo,
                        hamaCalcParams,
                        contextVo,
                        null
                );
            } catch (Exception e) {
                procResult.setMsg("HAMA计算过程异常: " + e.getMessage());
                logger.error("HAMA计算过程异常, error: ", e);
                return procResult;
            }

            if (hamaResult == null) {
                procResult.setMsg("HAMA计算结果为空");
                logger.error("HAMA计算结果为空");
                return procResult;
            }

            // 8. 将结果转换为JSON字符串
            String resultJson;
            try {
                resultJson = hamaScaleCalculator.convertToJson(hamaResult);
            } catch (Exception e) {
                procResult.setMsg("HAMA结果JSON序列化失败: " + e.getMessage());
                logger.error("HAMA结果JSON序列化失败, error: ", e);
                return procResult;
            }

            if (resultJson == null || resultJson.trim().isEmpty()) {
                procResult.setMsg("HAMA结果JSON为空");
                logger.error("HAMA结果JSON为空");
                return procResult;
            }

            // 9. 设置返回结果
            procResult.setResult(true);
            procResult.setMsg("HAMA计算成功");
            procResult.setData(resultJson);

            logger.info("HAMA计算成功, contextId: {}, 总分: {}, 焦虑程度: {}",
                    contextId, hamaResult.getRawTotalScore(), hamaResult.getAnxietyLevel());

        } catch (Exception ex) {
            logger.error("HAMA计算发生未预期异常: ", ex);
            procResult.setMsg("HAMA计算发生未预期异常: " + ex.getMessage());
        }

        return procResult;
    }
}