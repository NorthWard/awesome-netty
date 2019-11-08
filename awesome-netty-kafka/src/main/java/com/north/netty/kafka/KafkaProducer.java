package com.north.netty.kafka;

import com.google.common.collect.Lists;
import com.north.netty.kafka.bean.*;
import com.north.netty.kafka.caches.RequestCacheCenter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaProducer {
    private Channel channel;
    private String clientId;
    private AtomicInteger requestId = new AtomicInteger(1);
    public KafkaProducer(){
        this.clientId = "produce";
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(eventLoopGroup);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new LengthFieldPrepender(4))
                        .addLast(new LengthFieldBasedFrameDecoder(2048,0,4,0,4))
                        .addLast(new ByteToMessageDecoder() {
                            @Override
                            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                                KafkaResponseHeader kafkaResponseHeader = new KafkaResponseHeader();
                                kafkaResponseHeader.deserialize(in);
                                if(kafkaResponseHeader.getCorrelationId() == null){
                                      throw new IllegalStateException("服务端返回的correlationId 为null");
                                }
                                AbstractKafkaResponse kafkaMetaResponse = RequestCacheCenter.getKafkaResponse(kafkaResponseHeader.getCorrelationId());
                                kafkaMetaResponse.deserialize(in);
                                kafkaMetaResponse.setKafkaResponseHeader(kafkaResponseHeader);
                                kafkaMetaResponse.setCorrelationId(kafkaResponseHeader.getCorrelationId());
                                RequestCacheCenter.putKafkaResponse(kafkaResponseHeader.getCorrelationId(), kafkaMetaResponse);
                            }
                        });
            }
        });
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect("localhost", 9092).sync();
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void fetchMataData(){
        Integer xId = requestId.getAndIncrement();
        KafkaMetaRequest kafkaMetaRequest = new KafkaMetaRequest(clientId, xId);
        kafkaMetaRequest.setTopics(Lists.newArrayList("test"));
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        kafkaMetaRequest.serializable(byteBuf);
        try {
            RequestCacheCenter.putKafkaResponse(xId,  new KafkaMetaResponse());
            this.channel.writeAndFlush(byteBuf).sync();
            AbstractKafkaResponse response =  RequestCacheCenter.waitForResp(xId, 400000);
            System.out.println(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
