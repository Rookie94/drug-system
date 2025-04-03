package com.ruoyi.wxsys.service.impl;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.wxsys.mapper.MiniAppMapper;
import com.ruoyi.common.core.domain.entity.MiniApp;
import com.ruoyi.wxsys.service.IMiniAppService;


/**
 * 小程序信息Service业务层处理
 *
 * @author 盖子
 * @date 2024-11-22
 */
@Service
public class MiniAppServiceImpl implements IMiniAppService
{
    @Autowired
    private MiniAppMapper miniAppMapper;

    /**
     * 查询小程序信息
     *
     * @param id 小程序信息主键
     * @return 小程序信息
     */
    @Override
    public MiniApp selectMiniAppById(Long id)
    {
        return miniAppMapper.selectMiniAppById(id);
    }

    /**
     * 查询小程序信息列表
     *
     * @param miniApp 小程序信息
     * @return 小程序信息
     */
    @Override
    public List<MiniApp> selectMiniAppList(MiniApp miniApp)
    {
        return miniAppMapper.selectMiniAppList(miniApp);
    }

    /**
     * 新增小程序信息
     *
     * @param miniApp 小程序信息
     * @return 结果
     */
    @Override
    public int insertMiniApp(MiniApp miniApp)
    {
        miniApp.setCreateTime(DateUtils.getNowDate());
        return miniAppMapper.insertMiniApp(miniApp);
    }

    /**
     * 修改小程序信息
     *
     * @param miniApp 小程序信息
     * @return 结果
     */
    @Override
    public int updateMiniApp(MiniApp miniApp)
    {
        miniApp.setUpdateTime(DateUtils.getNowDate());
        return miniAppMapper.updateMiniApp(miniApp);
    }

    /**
     * 批量删除小程序信息
     *
     * @param ids 需要删除的小程序信息主键
     * @return 结果
     */
    @Override
    public int deleteMiniAppByIds(Long[] ids)
    {
        return miniAppMapper.deleteMiniAppByIds(ids);
    }

    /**
     * 删除小程序信息信息
     *
     * @param id 小程序信息主键
     * @return 结果
     */
    @Override
    public int deleteMiniAppById(Long id)
    {
        return miniAppMapper.deleteMiniAppById(id);
    }

    @Override
    public MiniApp selectMiniAppByAppid(String appid)
    {
        return miniAppMapper.selectMiniAppByAppid(appid);
    }

    @Override
    public JSONArray listSelect()
    {
        List<MiniApp> miniAppList = miniAppMapper.selectMiniAppList(new MiniApp());
        JSONArray array = new JSONArray();
        for (MiniApp miniApp : miniAppList)
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", miniApp.getName());
            jsonObject.put("value", miniApp.getId());
            array.add(jsonObject);
        }
        return array;
    }
}
