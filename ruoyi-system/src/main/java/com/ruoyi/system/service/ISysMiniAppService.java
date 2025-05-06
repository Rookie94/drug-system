package com.ruoyi.system.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.ruoyi.common.core.domain.entity.MiniApp;

/**
 * 小程序信息Service接口
 * 
 * @author 盖子
 * @date 2024-11-22
 */
public interface ISysMiniAppService
{
    /**
     * 查询小程序信息
     * 
     * @param id 小程序信息主键
     * @return 小程序信息
     */
    public MiniApp selectMiniAppById(Long id);

    /**
     * 查询小程序信息列表
     * 
     * @param miniApp 小程序信息
     * @return 小程序信息集合
     */
    public List<MiniApp> selectMiniAppList(MiniApp miniApp);

    /**
     * 新增小程序信息
     * 
     * @param miniApp 小程序信息
     * @return 结果
     */
    public int insertMiniApp(MiniApp miniApp);

    /**
     * 修改小程序信息
     * 
     * @param miniApp 小程序信息
     * @return 结果
     */
    public int updateMiniApp(MiniApp miniApp);

    /**
     * 批量删除小程序信息
     * 
     * @param ids 需要删除的小程序信息主键集合
     * @return 结果
     */
    public int deleteMiniAppByIds(Long[] ids);

    /**
     * 删除小程序信息信息
     * 
     * @param id 小程序信息主键
     * @return 结果
     */
    public int deleteMiniAppById(Long id);

    MiniApp selectMiniAppByAppid(String appid);

    JSONArray listSelect();
}
