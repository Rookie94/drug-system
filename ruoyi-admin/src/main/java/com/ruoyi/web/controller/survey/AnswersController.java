package com.ruoyi.web.controller.survey;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.cms.survey.domain.Answers;
import com.ruoyi.cms.survey.service.IAnswersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 问卷答案结果Controller
 *
 * @author Shure
 * @date 2021-10-18
 */
@RestController
@RequestMapping("/survey/answers")
public class AnswersController extends BaseController {
    @Autowired
    private IAnswersService answersService;

    /**
     * 查询问卷答案结果列表
     */
    @PreAuthorize("@ss.hasPermi('survey:answer:list')")
    @GetMapping("/list")
    public TableDataInfo list(Answers answers) {
        startPage();
        List<Answers> list = answersService.selectAnswersList(answers);
        return getDataTable(list);
    }

    /**
     * 导出问卷答案结果列表
     */
    @PreAuthorize("@ss.hasPermi('survey:answer:export')")
    @Log(title = "问卷答案结果", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(Answers answers) {
        List<Answers> list = answersService.selectAnswersList(answers);
        ExcelUtil<Answers> util = new ExcelUtil<Answers>(Answers.class);
        return util.exportExcel(list, "问卷答案结果数据");
    }

    /**
     * 获取问卷答案结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('survey:answer:query')")
    @GetMapping(value = "/{answerId}")
    public AjaxResult getInfo(@PathVariable("answerId") Long answerId) {
        return AjaxResult.success(answersService.selectAnswersById(answerId));
    }

    /**
     * 新增问卷答案结果
     */
    @PreAuthorize("@ss.hasPermi('survey:answer:add')")
    @Log(title = "问卷答案结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Answers answers) {
        return toAjax(answersService.insertAnswers(answers));
    }

    /**
     * 修改问卷答案结果
     */
    @PreAuthorize("@ss.hasPermi('survey:answer:edit')")
    @Log(title = "问卷答案结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Answers answers) {
        return toAjax(answersService.updateAnswers(answers));
    }

    /**
     * 删除问卷答案结果
     */
    @PreAuthorize("@ss.hasPermi('survey:answer:remove')")
    @Log(title = "问卷答案结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{answerIds}")
    public AjaxResult remove(@PathVariable Long[] answerIds) {
        return toAjax(answersService.deleteAnswersByIds(answerIds));
    }
}
