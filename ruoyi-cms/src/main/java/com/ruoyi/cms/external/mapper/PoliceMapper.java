package com.ruoyi.cms.external.mapper;

import com.ruoyi.cms.external.domain.Police;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 警员Mapper接口
 */
@Mapper
public interface PoliceMapper {
    /**
     * 查询警员
     */
    Police selectPoliceById(String id);

    /**
     * 查询警员列表
     */
    List<Police> selectPoliceList(Police police);

    /**
     * 新增警员
     */
    int insertPolice(Police police);

    /**
     * 修改警员
     */
    int updatePolice(Police police);

    /**
     * 删除警员
     */
    int deletePoliceById(String id);

    /**
     * 批量删除警员
     */
    int deletePoliceByIds(String[] ids);

    /**
     * 根据手机号查询警员
     */
    Police selectPoliceByMobile(String mobileNumber);

    /**
     * 批量插入警员
     */
    int batchInsertPolice(List<Police> policeList);

    /**
     * 批量更新警员
     */
    int batchUpdatePolice(List<Police> policeList);
}