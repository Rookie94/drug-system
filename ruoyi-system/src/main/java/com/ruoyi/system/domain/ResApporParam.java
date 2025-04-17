package com.ruoyi.system.domain;

import java.util.Date;

public class ResApporParam {
    public int flag;
    public Long[] ids;
    public String publishTime;
    public String apporBy;
    public Date apporTime;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Long[]  getIds() {
        return ids;
    }

    public void setIds(Long[] ids) {
        this.ids = ids;
    }
    public String getApporBy() {
        return apporBy;
    }

    public void setApporBy(String apporBy) {
        this.apporBy = apporBy;
    }

    public Date getApporTime() {
        return apporTime;
    }

    public void setApporTime(Date apporTime) {
        this.apporTime = apporTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }




}
