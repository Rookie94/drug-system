package com.ruoyi.cms.external.domain.response;

import java.util.List;

/**
 * 分页响应
 */
public class ExternalPageResponse<T> {
    private Integer total;
    private Integer pageIndex;
    private Integer pageSize;
    private String list;
    private List<T> decodedList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

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

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public List<T> getDecodedList() {
        return decodedList;
    }

    public void setDecodedList(List<T> decodedList) {
        this.decodedList = decodedList;
    }
}