package com.bupt.wangfu.info.entry;

import com.bupt.wangfu.info.entry.Flow;

/**
 * Created by HanB on 2016/7/13 0013.
 */
public class Queue {
    private String queueName; // 队列名
    private String portId; // 端口号
    private String max_rate;
    private String min_rate;
    private String enqueue; // 入队数据
    private String dequeue; // 出队数据
    private String brandWidth; // 带宽
    private String queueLength; // 队列长度
    private Flow flow;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getMax_rate() {
        return max_rate;
    }

    public void setMax_rate(String max_rate) {
        this.max_rate = max_rate;
    }

    public String getMin_rate() {
        return min_rate;
    }

    public void setMin_rate(String min_rate) {
        this.min_rate = min_rate;
    }

    public String getEnqueue() {
        return enqueue;
    }

    public void setEnqueue(String enqueue) {
        this.enqueue = enqueue;
    }

    public String getDequeue() {
        return dequeue;
    }

    public void setDequeue(String dequeue) {
        this.dequeue = dequeue;
    }

    public String getBrandWidth() {
        return brandWidth;
    }

    public void setBrandWidth(String brandWidth) {
        this.brandWidth = brandWidth;
    }

    public String getQueueLength() {
        return queueLength;
    }

    public void setQueueLength(String queueLength) {
        this.queueLength = queueLength;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }
}
