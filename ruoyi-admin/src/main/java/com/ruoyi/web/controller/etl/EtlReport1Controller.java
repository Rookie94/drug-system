package com.ruoyi.web.controller.etl;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.cms.etl.domain.EtlReport1;
import com.ruoyi.cms.etl.service.IEtlReport1Service;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 数据总览Controller
 * 
 * @author admin
 * @date 2025-10-22
 */
@RestController
@RequestMapping("/etl/report1")
public class EtlReport1Controller extends BaseController
{
    @Autowired
    private IEtlReport1Service etlReport1Service;

    /**
     * 查询数据总览列表
     */
    @GetMapping("/list")
    public TableDataInfo list(EtlReport1 etlReport1)
    {
        startPage();
        List<EtlReport1> list = etlReport1Service.selectEtlReport1List(etlReport1);
        return getDataTable(list);
    }


    /**
     * 获取数据总览详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(etlReport1Service.selectEtlReport1ById(id));
    }

    /**
     * 近30天每日登录用户数（成功）
     */
    @GetMapping("/loginTrend")
    public AjaxResult loginTrend() {
        return AjaxResult.success(etlReport1Service.selectLoginTrend());
    }


}
