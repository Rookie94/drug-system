package com.ruoyi.cms.scale.domain.vo;

import java.util.List;

public class TopicVo {

    private String topicId;
    private int topicType; // Assuming topicType is an integer based on the example
    private String orderNum;
    private String title;
    private boolean required;
    private List<OptionVo> options;

    // Getters and Setters

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        this.topicType = topicType;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<OptionVo> getOptions() {
        return options;
    }

    public void setOptions(List<OptionVo> options) {
        this.options = options;
    }
}