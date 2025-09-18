package com.ruoyi.system.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import org.springframework.web.multipart.MultipartFile;

/**
 * 小程序用户Service接口
 * 
 * @author 盖子
 * @date 2024-11-22
 */
public interface ISysMiniAppUserService
{
    /**
     * 查询小程序用户
     * 
     * @param id 小程序用户主键
     * @return 小程序用户
     */
    public MiniAppUser selectMiniAppUserById(Long id);

    /**
     * 查询小程序用户列表
     * 
     * @param miniAppUser 小程序用户
     * @return 小程序用户集合
     */
    public List<MiniAppUser> selectMiniAppUserList(MiniAppUser miniAppUser);

    /**
     * 新增小程序用户
     * 
     * @param miniAppUser 小程序用户
     * @return 结果
     */
    public int insertMiniAppUser(MiniAppUser miniAppUser);


    /**
     * 新增小程序游客用户
     *
     * @param miniAppUser 小程序用户
     * @return 结果
     */
    public int insertMiniAppGuestUser(MiniAppUser miniAppUser);

    /**
     * 修改小程序用户
     * 
     * @param miniAppUser 小程序用户
     * @return 结果
     */
    public int updateMiniAppUser(MiniAppUser miniAppUser);

    /**
     * 批量删除小程序用户
     * 
     * @param ids 需要删除的小程序用户主键集合
     * @return 结果
     */
    public int deleteMiniAppUserByIds(Long[] ids);

    /**
     * 删除小程序用户信息
     * 
     * @param id 小程序用户主键
     * @return 结果
     */
    public int deleteMiniAppUserById(Long id);

    MiniAppUser selectMiniAppUserByOpenIdAndMiniAppId(String openId,Long miniAppId);

    JSONObject uploadAvatar(MiniAppUser miniAppUser, MultipartFile file) throws Exception;

    List<MiniAppUser> selectMiniAppUserInnerJoinMiniAppList(MiniAppUser miniAppUser);
}
