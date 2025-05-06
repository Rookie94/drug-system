package com.ruoyi.wxapp.Controller;

import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ruoyi.cms.res.domain.ResOrginfo;
import com.ruoyi.cms.res.service.IResOrginfoService;
import com.ruoyi.common.core.controller.BaseController;

import java.util.List;

@RestController
@RequestMapping("/miniapp/res")
public class ResController extends BaseController
{

    @Autowired
    private IResOrginfoService resOrginfoService;

    /**
     * 查询戒治机构列表
     */
    @GetMapping("/getorginfo")
    public TableDataInfo list(ResOrginfo resOrginfo)
    {
        startPage();
        List<ResOrginfo> list = resOrginfoService.selectResOrginfoList(resOrginfo);
        return getDataTable(list);
    }

}