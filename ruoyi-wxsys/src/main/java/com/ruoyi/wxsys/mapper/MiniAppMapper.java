package com.ruoyi.wxsys.mapper;

import java.util.List;
import com.ruoyi.common.core.domain.entity.MiniApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 小程序信息Mapper接口
 * 
 * @author 盖子
 * @date 2024-11-22
 */
@Mapper
public interface MiniAppMapper 
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
     * 删除小程序信息
     * 
     * @param id 小程序信息主键
     * @return 结果
     */
    public int deleteMiniAppById(Long id);

    /**
     * 批量删除小程序信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMiniAppByIds(Long[] ids);

    MiniApp selectMiniAppByAppid(@Param("appid") String appid);
}
