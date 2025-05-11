package com.ruoyi.cms.scale.domain.calc;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * SDS量表计算参数
 * ascTopic: 升序分题目
 * descTopic: 降序分题目
 * score: 分数
 * rate: 比率因子
 */
public class SDSParams {
    @JsonProperty("ascTopic")
    private List<Integer> ascTopic;

    @JsonProperty("descTopic")
    private List<Integer> descTopic;

    @JsonProperty("score")
    private List<Integer> score;

    @JsonProperty("rate")
    private double rate;

    // 无参构造器
    public SDSParams() {}

    // 完整构造器
    public SDSParams(List<Integer> ascTopic,
                     List<Integer> descTopic,
                     List<Integer> score,
                     double rate) {
        this.ascTopic = ascTopic;
        this.descTopic = descTopic;
        this.score = score;
        this.rate = rate;
    }

    // Getter & Setter
    public List<Integer> getAscTopic() { return ascTopic; }
    public void setAscTopic(List<Integer> ascTopic) { this.ascTopic = ascTopic; }

    public List<Integer> getDescTopic() { return descTopic; }
    public void setDescTopic(List<Integer> descTopic) { this.descTopic = descTopic; }

    public List<Integer> getScore() { return score; }
    public void setScore(List<Integer> score) { this.score = score; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }
}

