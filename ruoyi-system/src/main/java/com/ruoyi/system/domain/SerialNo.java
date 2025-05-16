package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流水号管理对象 sys_serialno
 * 
 * @author admin
 * @date 2025-05-16
 */
public class SerialNo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 规则ID */
    @Excel(name = "规则ID")
    private String id;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String name;

    /** 固定前缀 */
    @Excel(name = "固定前缀")
    private String prefix;

    /** 固定后缀 */
    @Excel(name = "固定后缀")
    private String suffix;

    /** 日期格式 */
    @Excel(name = "日期格式")
    private String dateFormat;

    /** 序列号长度 */
    @Excel(name = "序列号长度")
    private Long seqLength;

    /** 当前序列号 */
    @Excel(name = "当前序列号")
    private Long currentSeq;

    /** 重置策略()DAILY/MONTHLY/YEARLY/NEVER) */
    @Excel(name = "重置策略()DAILY/MONTHLY/YEARLY/NEVER)")
    private String resetStrategy;

    /** 最后重置日期（YYYYMMDD） */
    @Excel(name = "最后重置日期", readConverterExp = "Y=YYYMMDD")
    private String lastResetDate;

    /** 乐观锁版本 */
    @Excel(name = "乐观锁版本")
    private Long version;

    /** 工作状态 */
    @Excel(name = "工作状态")
    private String status;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setPrefix(String prefix) 
    {
        this.prefix = prefix;
    }

    public String getPrefix() 
    {
        return prefix;
    }

    public void setSuffix(String suffix) 
    {
        this.suffix = suffix;
    }

    public String getSuffix() 
    {
        return suffix;
    }

    public void setDateFormat(String dateFormat) 
    {
        this.dateFormat = dateFormat;
    }

    public String getDateFormat() 
    {
        return dateFormat;
    }

    public void setSeqLength(Long seqLength) 
    {
        this.seqLength = seqLength;
    }

    public Long getSeqLength() 
    {
        return seqLength;
    }

    public void setCurrentSeq(Long currentSeq) 
    {
        this.currentSeq = currentSeq;
    }

    public Long getCurrentSeq() 
    {
        return currentSeq;
    }

    public void setResetStrategy(String resetStrategy) 
    {
        this.resetStrategy = resetStrategy;
    }

    public String getResetStrategy() 
    {
        return resetStrategy;
    }

    public void setLastResetDate(String lastResetDate) 
    {
        this.lastResetDate = lastResetDate;
    }

    public String getLastResetDate() 
    {
        return lastResetDate;
    }

    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
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
            .append("id", getId())
            .append("name", getName())
            .append("prefix", getPrefix())
            .append("suffix", getSuffix())
            .append("dateFormat", getDateFormat())
            .append("seqLength", getSeqLength())
            .append("currentSeq", getCurrentSeq())
            .append("resetStrategy", getResetStrategy())
            .append("lastResetDate", getLastResetDate())
            .append("version", getVersion())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
