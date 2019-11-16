package com.north.netty.kafka.codec;

import com.north.netty.kafka.bean.AbstractKafkaResponse;
import com.north.netty.kafka.bean.KafkaResponseHeader;
import com.north.netty.kafka.caches.RequestCacheCenter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class KafkaResponseDecoder extends ByteToMessageDecoder {
    private RequestCacheCenter requestCacheCenter;
    public KafkaResponseDecoder(RequestCacheCenter requestCacheCenter){
           this.requestCacheCenter = requestCacheCenter;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        KafkaResponseHeader kafkaResponseHeader = new KafkaResponseHeader();
        kafkaResponseHeader.deserialize(in);
        if(kafkaResponseHeader.getCorrelationId() == null){
            throw new IllegalStateException("服务端返回的correlationId 为null");
        }
        AbstractKafkaResponse abstractKafkaResponse = requestCacheCenter.getKafkaResponse(kafkaResponseHeader.getCorrelationId());
        if(abstractKafkaResponse == null){
            throw new IllegalStateException("服务端返回的correlationId不是本客户端发送的");
        }
        abstractKafkaResponse.deserialize(in);
        abstractKafkaResponse.setKafkaResponseHeader(kafkaResponseHeader);
        abstractKafkaResponse.setCorrelationId(kafkaResponseHeader.getCorrelationId());
        requestCacheCenter.putKafkaResponse(kafkaResponseHeader.getCorrelationId(), abstractKafkaResponse);
    }
}
