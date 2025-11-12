package com.ruoyi.cms.external.mapper;

import com.ruoyi.cms.external.domain.CarePerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 照管人员Mapper接口
 */
@Mapper
public interface CarePersonMapper {
    /**
     * 查询照管人员
     */
    CarePerson selectCarePersonById(String id);

    /**
     * 查询照管人员列表
     */
    List<CarePerson> selectCarePersonList(CarePerson carePerson);

    /**
     * 新增照管人员
     */
    int insertCarePerson(CarePerson carePerson);

    /**
     * 修改照管人员
     */
    int updateCarePerson(CarePerson carePerson);

    /**
     * 删除照管人员
     */
    int deleteCarePersonById(String id);

    /**
     * 批量删除照管人员
     */
    int deleteCarePersonByIds(String[] ids);

    /**
     * 根据身份证号查询照管人员
     */
    CarePerson selectCarePersonByIdNumber(String idNumber);

    /**
     * 查询照管人员数量
     */
    int selectCarePersonCount();

    /**
     * 批量插入照管人员
     */
    int batchInsertCarePerson(List<CarePerson> carePersonList);

    /**
     * 批量更新照管人员
     */
    int batchUpdateCarePerson(List<CarePerson> carePersonList);
}