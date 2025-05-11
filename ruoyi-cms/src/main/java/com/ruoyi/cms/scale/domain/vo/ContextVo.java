package com.ruoyi.cms.scale.domain.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder(alphabetic = false)
public class ContextVo {

    private String contextId;
    private String contextName;
    private String pic;
    private String content;
    private List<TopicVo> topics;

    // Getters and Setters

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TopicVo> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicVo> topics) {
        this.topics = topics;
    }
}
