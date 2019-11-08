package com.north.netty.kafka.bean;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public class KafkaResponseHeader implements Serializable {
    private Integer correlationId;
    public void deserialize(ByteBuf byteBuf){
         this.correlationId = byteBuf.readInt();
    }
}
