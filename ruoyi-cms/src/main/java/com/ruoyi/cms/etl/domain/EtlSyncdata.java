package com.ruoyi.cms.etl.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据清洗对象 etl_syncdata
 * 
 * @author admin
 * @date 2025-09-21
 */
public class EtlSyncdata extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据源ID */
    private Long syncId;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 源数据源ID */
    private Long sourceConfigId;

    /** 目标数据源ID */
    private Long targetConfigId;

    /** 脚本 */
    @Excel(name = "脚本")
    private String sqlScript;

    /** 状态（0禁用 1启用） */
    @Excel(name = "状态", readConverterExp = "0=禁用,1=启用")
    private String status;

    /** 数据源名称 */
    @Excel(name = "数据源名称")
    private String sourceConfigName;

    /** 数据源名称 */
    @Excel(name = "数据源名称")
    private String targetConfigName;

    public void setSyncId(Long syncId) 
    {
        this.syncId = syncId;
    }

    public Long getSyncId() 
    {
        return syncId;
    }

    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }

    public void setSourceConfigId(Long sourceConfigId) 
    {
        this.sourceConfigId = sourceConfigId;
    }

    public Long getSourceConfigId() 
    {
        return sourceConfigId;
    }

    public void setTargetConfigId(Long targetConfigId) 
    {
        this.targetConfigId = targetConfigId;
    }

    public Long getTargetConfigId() 
    {
        return targetConfigId;
    }

    public void setSqlScript(String sqlScript) 
    {
        this.sqlScript = sqlScript;
    }

    public String getSqlScript() 
    {
        return sqlScript;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setSourceConfigName(String sourceConfigName) 
    {
        this.sourceConfigName = sourceConfigName;
    }

    public String getSourceConfigName() 
    {
        return sourceConfigName;
    }

    public void setTargetConfigName(String targetConfigName) 
    {
        this.targetConfigName = targetConfigName;
    }

    public String getTargetConfigName() 
    {
        return targetConfigName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("syncId", getSyncId())
            .append("orderNum", getOrderNum())
            .append("sourceConfigId", getSourceConfigId())
            .append("targetConfigId", getTargetConfigId())
            .append("sqlScript", getSqlScript())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("sourceConfigName", getSourceConfigName())
            .append("targetConfigName", getTargetConfigName())
            .toString();
    }
}
