package com.ruoyi.cms.etl.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据源管理对象 etl_datasource
 * 
 * @author admin
 * @date 2025-09-21
 */
public class EtlDatasource extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 数据源ID */
    private Long configId;

    /** 序号 */
    @Excel(name = "序号")
    private Long orderNum;

    /** 数据源名称 */
    @Excel(name = "数据源名称")
    private String configName;

    /** 数据库类型 */
    @Excel(name = "数据库类型")
    private String dbType;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String host;

    /** 端口 */
    @Excel(name = "端口")
    private String port;

    /** 数据库名 */
    @Excel(name = "数据库名")
    private String dbName;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 连接参数 */
    @Excel(name = "连接参数")
    private String dbParams;

    /** 状态（0禁用 1启用） */
    @Excel(name = "状态", readConverterExp = "0=禁用,1=启用")
    private String status;

    public void setConfigId(Long configId) 
    {
        this.configId = configId;
    }

    public Long getConfigId() 
    {
        return configId;
    }

    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }

    public void setConfigName(String configName) 
    {
        this.configName = configName;
    }

    public String getConfigName() 
    {
        return configName;
    }

    public void setDbType(String dbType) 
    {
        this.dbType = dbType;
    }

    public String getDbType() 
    {
        return dbType;
    }

    public void setHost(String host) 
    {
        this.host = host;
    }

    public String getHost() 
    {
        return host;
    }

    public void setPort(String port) 
    {
        this.port = port;
    }

    public String getPort() 
    {
        return port;
    }

    public void setDbName(String dbName) 
    {
        this.dbName = dbName;
    }

    public String getDbName() 
    {
        return dbName;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public String getPassword() 
    {
        return password;
    }

    public void setDbParams(String dbParams) 
    {
        this.dbParams = dbParams;
    }

    public String getDbParams() 
    {
        return dbParams;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("orderNum", getOrderNum())
            .append("configName", getConfigName())
            .append("dbType", getDbType())
            .append("host", getHost())
            .append("port", getPort())
            .append("dbName", getDbName())
            .append("userName", getUserName())
            .append("password", getPassword())
            .append("dbParams", getDbParams())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
