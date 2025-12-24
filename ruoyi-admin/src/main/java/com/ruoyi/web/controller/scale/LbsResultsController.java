package com.ruoyi.web.controller.scale;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.cms.scale.calcdata.strategy.CalcStrategyFactory;
import com.ruoyi.cms.scale.calcdata.strategy.ICalcStrategy;
import com.ruoyi.cms.scale.domain.LbsContexts;
import com.ruoyi.cms.scale.domain.LbsResults;
import com.ruoyi.cms.scale.domain.vo.AnswerVo;
import com.ruoyi.cms.scale.domain.vo.ContextAnswerVo;
import com.ruoyi.cms.scale.report.ITemplateStrategy;
import com.ruoyi.cms.scale.report.TemplateStrategyFactory;
import com.ruoyi.cms.scale.service.ILbsAnswerService;
import com.ruoyi.cms.scale.service.ILbsContextsService;
import com.ruoyi.cms.scale.service.ILbsResultsService;
import com.ruoyi.cms.scale.service.impl.LbsResultsServiceImpl;
import com.ruoyi.common.core.domain.ProcResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.cms.scale.domain.vo.LbsResultsVo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 测评报告Controller
 * 
 * @author admin
 * @date 2025-05-16
 */
@RestController
@RequestMapping("/scale/report")
public class LbsResultsController extends BaseController
{

    @Autowired
    private ILbsContextsService lbsContextsService;

    @Autowired
    private ILbsResultsService lbsResultsService;

    @Autowired
    private ILbsAnswerService lbsAnswerService;

    @Autowired
    private CalcStrategyFactory calcFactory;

    @Autowired
    private TemplateStrategyFactory reportFactory;

    /**
     * 查询测评报告列表
     */
    @PreAuthorize("@ss.hasPermi('scale:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(LbsResultsVo lbsResults)
    {
        startPage();
        if(lbsResults.getContextId()!=null){
            Long contextId=lbsResults.getContextId();
            LbsContexts contexts=lbsContextsService.selectLbsContextsByContextId(contextId);
            if(contexts.getParentContextId()==0)
            {
                lbsResults.setContextId(null);
            }
        }
        List<LbsResultsVo> list = lbsResultsService.selectLbsResultsList(lbsResults);
        return getDataTable(list);
    }

    /**
     * 导出测评报告列表
     */
    @PreAuthorize("@ss.hasPermi('scale:report:export')")
    @Log(title = "测评报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LbsResultsVo lbsResults)
    {
        List<LbsResultsVo> list = lbsResultsService.selectLbsResultsList(lbsResults);
        ExcelUtil<LbsResultsVo> util = new ExcelUtil<LbsResultsVo>(LbsResultsVo.class);
        util.exportExcel(response, list, "测评报告数据");
    }

    /**
     * 获取测评报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('scale:report:query')")
    @GetMapping(value = "/{resultId}")
    public AjaxResult getInfo(@PathVariable("resultId") Long resultId)
    {
        return success(lbsResultsService.selectLbsResultsByResultId(resultId));
    }

    /**
     * 新增测评报告
     */
    @PreAuthorize("@ss.hasPermi('scale:report:add')")
    @Log(title = "测评报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LbsResults lbsResults)
    {
        return toAjax(lbsResultsService.insertLbsResults(lbsResults));
    }

    /**
     * 修改测评报告
     */
    @PreAuthorize("@ss.hasPermi('scale:report:edit')")
    @Log(title = "测评报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LbsResults lbsResults)
    {
        return toAjax(lbsResultsService.updateLbsResults(lbsResults));
    }

    /**
     * 删除测评报告
     */
    @PreAuthorize("@ss.hasPermi('scale:report:remove')")
    @Log(title = "测评报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{resultIds}")
    public AjaxResult remove(@PathVariable Long[] resultIds)
    {
        return toAjax(lbsResultsService.deleteLbsResultsByResultIds(resultIds));
    }

    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('scale:report:list')")
    @GetMapping("/contextTree")
    public AjaxResult contextTree(LbsContexts lbsContexts)
    {
        return success(lbsContextsService.selectLbsContextsTreeList(lbsContexts));
    }

    @PreAuthorize("@ss.hasPermi('scale:report:edit')")
    @Log(title = "测评报告", businessType = BusinessType.UPDATE)
    @PutMapping("/refreshResult/{resultIds}")
    public AjaxResult refreshResult(@PathVariable Long[] resultIds)
    {
        try{
            for (Long resultId : resultIds) {
                try {
                    LbsResultsVo lbsResults= lbsResultsService.selectLbsResultsByResultId(resultId);
                    if(lbsResults!=null) {
                        ContextAnswerVo contextAnswerVo = new ContextAnswerVo();
                        contextAnswerVo.setContextId(lbsResults.getContextId());
                        // 解析JSON
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(lbsResults.getJsonResult());
                        JsonNode answersArray = rootNode.get("answers");
                        List<AnswerVo> list = new ArrayList<>();
                        if (answersArray != null && answersArray.isArray()) {
                            for (JsonNode answerNode : answersArray) {
                                AnswerVo answerVo = new AnswerVo();
                                // 设置resultId
                                answerVo.setResultId(resultId);
                                // 转换topicId为Long
                                if (answerNode.has("topicId")) {
                                    String topicIdStr = answerNode.get("topicId").asText();
                                    answerVo.setTopicId(Long.parseLong(topicIdStr));
                                }
                                // 转换optionId为字符串，直接存储
                                if (answerNode.has("optionId")) {
                                    String optionIdStr = answerNode.get("optionId").asText();
                                    answerVo.setOptionIds(optionIdStr);
                                }
                                // answer字段可能需要从其他地方获取或为空
                                // answerVo.setAnswer("");
                                list.add(answerVo);
                            }
                        }
                        contextAnswerVo.setAnswers(list);
                        lbsAnswerService.deleteAnswerByResultId(resultId);
                        lbsAnswerService.batchInsertAnswer(list);

                        ICalcStrategy strategy = calcFactory.getStrategy(contextAnswerVo.getContextId());
                        ProcResult procResult = strategy.calculate(contextAnswerVo);
                        if (procResult.getResult()) {
                            LbsResults lb = new LbsResults();
                            lb.setResultId(resultId);
                            lb.setJsonReport(procResult.getData().toString());
                            lbsResultsService.updateLbsResults(lb);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
            return success("刷新成功");
        }catch (Exception ex){
            return error("刷新失败");
        }
    }

    @GetMapping("/getReport")
    public String renderTemplate(@RequestParam Long resultId, @RequestParam(defaultValue = "pc") String deviceType)
    {
        LbsResultsVo lbsResults = lbsResultsService.selectLbsResultsByResultId(resultId);
        Long contextId = lbsResults.getContextId();
        ITemplateStrategy strategy = reportFactory.getStrategy(contextId, deviceType);
        if (strategy == null) {
            return "templates/mobile/default/" + contextId + "-" + deviceType + ".html";
        }
        return strategy.getTemplate(contextId,deviceType,lbsResults);
    }

}
