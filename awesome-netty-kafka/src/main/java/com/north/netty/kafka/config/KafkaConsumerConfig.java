package com.north.netty.kafka.config;

import java.io.Serializable;

public class KafkaConsumerConfig implements Serializable {
    /**
     * 等待响应返回的最大ms
     */
    private Integer maxWaitTime;
    /**
     * 响应的最小字节数
     */
    private Integer minBytes;
    /**
     * 响应的最大字节数
     */
    private Integer maxBytes;

    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public Integer getMinBytes() {
        return minBytes;
    }

    public void setMinBytes(Integer minBytes) {
        this.minBytes = minBytes;
    }

    public Integer getMaxBytes() {
        return maxBytes;
    }

    public void setMaxBytes(Integer maxBytes) {
        this.maxBytes = maxBytes;
    }
}
