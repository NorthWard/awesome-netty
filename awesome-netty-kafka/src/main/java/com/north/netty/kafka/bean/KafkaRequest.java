package com.north.netty.kafka.bean;


import io.netty.buffer.ByteBuf;

public interface KafkaRequest {
    public void serializable(ByteBuf out);
}
