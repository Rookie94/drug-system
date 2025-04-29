package com.ruoyi.ruoyiapi.Controller.wxapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.MiniApp;
import com.ruoyi.common.exception.MiniAppException;
import com.ruoyi.wxsys.entity.R;
import com.ruoyi.wxsys.service.IMiniAppService;

@RestController
@RequestMapping("/miniapp")
public class WxMiniAppController extends BaseController
{
    @Autowired
    private IMiniAppService miniAppService;

    /**
     * 获取主题色
     * @return
     */
    @GetMapping("/getThemeColor/{appid}")
    public R getThemeColor(@PathVariable("appid") String appid) throws MiniAppException
    {
        MiniApp miniApp = miniAppService.selectMiniAppByAppid(appid);
        if(ObjectUtils.isEmpty(miniApp))
        {
            throw new MiniAppException("appid is error");
        }
        return R.success("操作成功",miniApp.getThemeColor());
    }
}
