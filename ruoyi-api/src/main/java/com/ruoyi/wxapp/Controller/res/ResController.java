package com.ruoyi.wxapp.Controller.res;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.common.core.controller.BaseController;

import com.ruoyi.cms.res.domain.ResExpert;
import com.ruoyi.cms.job.domain.ResJobinfo;
import com.ruoyi.cms.job.domain.ResSkill;
import com.ruoyi.cms.job.service.IResJobinfoService;
import com.ruoyi.cms.job.service.IResSkillService;
import com.ruoyi.cms.res.domain.*;
import com.ruoyi.cms.res.service.*;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;


import java.util.List;

@RestController
@RequestMapping("/api/res")
public class ResController extends BaseController
{

    @Autowired
    private IResOrginfoService resOrginfoService;

    @Autowired
    private IResExpertService resExpertService;

    @Autowired
    private IResCaseService resCaseService;

    @Autowired
    private IResJobinfoService resJobinfoService;

    @Autowired
    private IResNewsService resNewsService;

    @Autowired
    private IResSkillService resSkillService;

    @Autowired
    private ISysNoticeService sysNoticeService;


    @Autowired
    private IResArticlesService resArticlesService;

    @Autowired
    private IResRxdataService resRxdataService;

    /**
     * 查询戒治机构列表
     */
    @GetMapping("/getorginfo")
    public TableDataInfo listOrginfo(ResOrginfo resOrginfo)
    {
        startPage();
        resOrginfo.setUseDataScope(false);
        resOrginfo.setAppored("2");
        resOrginfo.setStatus("0");
        List<ResOrginfo> list = resOrginfoService.selectResOrginfoList(resOrginfo);
        return getDataTable(list);
    }

    /**
     * 查询专家列表
     */
    @GetMapping("/getexpert")
    public TableDataInfo listExpert(ResExpert resExpert)
    {
        startPage();
        resExpert.setUseDataScope(false);
        resExpert.setAppored("2");
        resExpert.setStatus("0");
        List<ResExpert> list = resExpertService.selectResExpertList(resExpert);
        return getDataTable(list);
    }

    /**
     * 查询案例列表
     */
    @GetMapping("/getcase")
    public TableDataInfo listCase(ResCase resCase)
    {
        startPage();
        resCase.setUseDataScope(false);
        resCase.setAppored("2");
        resCase.setStatus("0");
        List<ResCase> list = resCaseService.selectResCaseList(resCase);
        return getDataTable(list);
    }

    /**
     * 查询工作列表
     */
    @GetMapping("/getjobinfo")
    public TableDataInfo listJobinfo(ResJobinfo resJobinfo)
    {
        startPage();
        resJobinfo.setUseDataScope(false);
        resJobinfo.setAppored("2");
        resJobinfo.setStatus("0");
        List<ResJobinfo> list = resJobinfoService.selectResJobinfoList(resJobinfo);
        return getDataTable(list);
    }

    /**
     * 查询戒毒资讯列表
     */
    @GetMapping("/getnews")
    public TableDataInfo listNews(ResNews resNews)
    {
        startPage();
        resNews.setUseDataScope(false);
        resNews.setAppored("2");
        resNews.setStatus("0");
        List<ResNews> list = resNewsService.selectResNewsList(resNews);
        return getDataTable(list);
    }

    /**
     * 获取技能培训
     */
    @GetMapping("/getskill")
    public TableDataInfo listSkill(ResSkill resSkill)
    {
        startPage();
        resSkill.setUseDataScope(false);
        resSkill.setAppored("2");
        resSkill.setStatus("0");
        List<ResSkill> list = resSkillService.selectResSkillList(resSkill);
        return getDataTable(list);
    }


    /**
     * 获取公告通知
     */
    @GetMapping("/getnotice")
    public TableDataInfo listNotice(SysNotice sysNotice)
    {
        startPage();
        sysNotice.setUseDataScope(false);
        sysNotice.setAppored("2");
        sysNotice.setStatus("0");
        List<SysNotice> list = sysNoticeService.selectSysNoticeList(sysNotice);
        return getDataTable(list);
    }

    /**
     * 获取戒治资源一级分类
     */
    @GetMapping("/getcategory")
    public TableDataInfo listCategory()
    {
        startPage();
        List<ResCategoryInfo> list = resArticlesService.selectCategoryList();
        return getDataTable(list);
    }

    /**
     * 获取戒治资源二级分类
     */
    @GetMapping("/getsubcategory")
    public TableDataInfo listSubCategory(@RequestParam(name="categoryId") Long categoryId)
    {
        startPage();
        List<ResCategoryInfo> list = resArticlesService.selectSubCategoryList(categoryId);
        return getDataTable(list);
    }

    /**
     * 获取戒治资源
     */
    @GetMapping("/getarticles")
    public TableDataInfo listArticles(ResArticles resArticles)
    {
        startPage();
        resArticles.setUseDataScope(false);
        resArticles.setAppored("2");
        resArticles.setStatus("0");
        List<ResArticles> list = resArticlesService.selectResArticlesList(resArticles);
        return getDataTable(list);
    }


    /**
     * 获取戒治处方
     */
    @GetMapping("/getrxdata")
    public TableDataInfo listRxData(ResRxdata resRxdata)
    {
        startPage();
        resRxdata.setUseDataScope(false);
        resRxdata.setAppored("2");
        resRxdata.setStatus("0");
        List<ResRxdata> list = resRxdataService.selectResRxdataList(resRxdata);
        return getDataTable(list);
    }

}