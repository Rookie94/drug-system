package com.ruoyi.ruoyiapi.wxapi;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.framework.web.service.MiniAppLoginService;
import com.ruoyi.common.core.domain.model.WxLoginBody;
import com.ruoyi.common.exception.MiniAppException;
import com.ruoyi.wxsys.entity.R;

@RestController
@RequestMapping("/miniapp")
public class WxLoginController
{

    @Autowired
    private MiniAppLoginService miniAppLoginService;

    @PostMapping("/wxMiniLogin")
    public R wxMiniLogin(@RequestBody WxLoginBody wxLoginBody) throws MiniAppException
    {
        JSONObject data = miniAppLoginService.wxMiniLogin(wxLoginBody);
        return R.success(data);
    }

}
