package com.ruoyi.wxsys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.constant.MiniAppConstants;
import com.ruoyi.common.exception.MiniAppException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.file.FileUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.wxsys.mapper.MiniAppUserMapper;
import com.ruoyi.common.core.domain.entity.MiniAppUser;
import com.ruoyi.wxsys.service.IMiniAppUserService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 小程序用户Service业务层处理
 *
 * @author 盖子
 * @date 2024-11-22
 */
@Slf4j
@Service
public class MiniAppUserServiceImpl implements IMiniAppUserService
{
    @Autowired
    private MiniAppUserMapper miniAppUserMapper;

    //@Autowired
    //private OSSUtils ossUtils;

    /**
     * 查询小程序用户
     *
     * @param id 小程序用户主键
     * @return 小程序用户
     */
    @Override
    public MiniAppUser selectMiniAppUserById(Long id)
    {
        return miniAppUserMapper.selectMiniAppUserById(id);
    }

    /**
     * 查询小程序用户列表
     *
     * @param miniAppUser 小程序用户
     * @return 小程序用户
     */
    @Override
    public List<MiniAppUser> selectMiniAppUserList(MiniAppUser miniAppUser)
    {
        return miniAppUserMapper.selectMiniAppUserList(miniAppUser);
    }

    /**
     * 新增小程序用户
     *
     * @param miniAppUser 小程序用户
     * @return 结果
     */
    @Override
    public int insertMiniAppUser(MiniAppUser miniAppUser)
    {
        miniAppUser.setCreateTime(DateUtils.getNowDate());
        return miniAppUserMapper.insertMiniAppUser(miniAppUser);
    }

    /**
     * 修改小程序用户
     *
     * @param miniAppUser 小程序用户
     * @return 结果
     */
    @Override
    public int updateMiniAppUser(MiniAppUser miniAppUser)
    {
        miniAppUser.setUpdateTime(DateUtils.getNowDate());
        return miniAppUserMapper.updateMiniAppUser(miniAppUser);
    }

    /**
     * 批量删除小程序用户
     *
     * @param ids 需要删除的小程序用户主键
     * @return 结果
     */
    @Override
    public int deleteMiniAppUserByIds(Long[] ids)
    {
        return miniAppUserMapper.deleteMiniAppUserByIds(ids);
    }

    /**
     * 删除小程序用户信息
     *
     * @param id 小程序用户主键
     * @return 结果
     */
    @Override
    public int deleteMiniAppUserById(Long id)
    {
        return miniAppUserMapper.deleteMiniAppUserById(id);
    }

    @Override
    public MiniAppUser selectMiniAppUserByOpenIdAndMiniAppId(String openId, Long miniAppId)
    {
        return miniAppUserMapper.selectMiniAppUserByOpenIdAndMiniAppId(openId, miniAppId);
    }

    @Override
    public JSONObject uploadAvatar(MiniAppUser miniAppUser, MultipartFile file) throws MiniAppException
    {
        JSONObject result = new JSONObject();
        try
        {
            String format = DateUtil.format(new Date(), "/yyyyMMddHHmmssSSS.");
            String fileExtendName = FileUtils.getFileExtendName(file.getBytes());
            String filePath = MiniAppConstants.FilePrefix.USER_AVATAR + miniAppUser.getOpenId() + format + fileExtendName;
            //ossUtils.upload(filePath, file.getBytes());
            //String imgUrl = ossUtils.getUrl(filePath, false);
            String imgUrl="";
            result.put("imgUrl", imgUrl);
            result.put("filePath", filePath);
            miniAppUser.setAvatar(filePath);
            updateMiniAppUser(miniAppUser);
            return result;
        }
        catch (Exception e)
        {
            log.error("用户上传头像失败,openId:{},miniAppId:{}", miniAppUser.getOpenId(), miniAppUser.getMiniAppId(), e);
            throw new MiniAppException("头像上传失败,请重试！");
        }
    }

    @Override
    public List<MiniAppUser> selectMiniAppUserInnerJoinMiniAppList(MiniAppUser miniAppUser)
    {
        List<MiniAppUser> miniAppUserList = miniAppUserMapper.selectMiniAppUserInnerJoinMiniAppList(miniAppUser);
        List<MiniAppUser> result = miniAppUserList.stream().peek(user ->
        {
            if(!ObjectUtils.isEmpty(user.getAvatar()))
            {
                //user.setAvatar(ossUtils.getUrl(user.getAvatar(), false));
            }
        }).collect(Collectors.toList());
        return result;
    }

}
