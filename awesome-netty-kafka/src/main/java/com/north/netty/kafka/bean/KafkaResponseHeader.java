package com.north.netty.kafka.bean;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

/**
 * @author laihaohua
 */
public class KafkaResponseHeader implements Serializable {
    private Integer correlationId;
    public void deserialize(ByteBuf byteBuf){
         this.correlationId = byteBuf.readInt();
    }

    public Integer getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(Integer correlationId) {
        this.correlationId = correlationId;
    }
}
