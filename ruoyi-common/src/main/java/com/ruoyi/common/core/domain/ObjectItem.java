package com.ruoyi.common.core.domain;

/**
 * @author ze.chen
 * @description minio存储对象
 * @date 2022/8/30
 **/
public class ObjectItem {
    /** 对象名 */
    private String objectName;

    /** 桶存储大小 */
    private Long size;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
