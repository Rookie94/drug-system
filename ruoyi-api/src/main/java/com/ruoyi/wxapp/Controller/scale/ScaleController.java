package com.ruoyi.wxapp.Controller.scale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.cms.scale.service.ILbsCalcService;

@RestController
@RequestMapping("/api/scale")
public class ScaleController {

    @Autowired
    private ILbsCalcService lbsCalcService;

}
