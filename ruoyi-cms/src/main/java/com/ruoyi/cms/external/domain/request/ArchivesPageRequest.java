package com.ruoyi.cms.external.domain.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * 照管人员分页请求参数
 */
public class ArchivesPageRequest {
    @NotNull(message = "页码不能为空")
    private Integer pageIndex;

    @NotNull(message = "页大小不能为空")
    @Max(value = 1000, message = "页大小不能超过1000")
    private Integer pageSize;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}