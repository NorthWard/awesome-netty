package com.north.netty.kafka.bean;

import io.netty.buffer.ByteBuf;

/**
 * @author laihaohua
 */
public interface KafkaResponse {

    /**
     *  反序列化这个响应体
     * @param byteBuf
     */
     void deserialize(ByteBuf byteBuf);

}
