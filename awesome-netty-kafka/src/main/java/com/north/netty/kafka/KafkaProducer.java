package com.north.netty.kafka;

import com.google.common.collect.Lists;
import com.north.netty.kafka.bean.KafkaMetaRequest;
import com.north.netty.kafka.bean.KafkaMetaResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.north.netty.common.utils.SerializeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaProducer {
    private Channel channel;
    private String clientId;
    private AtomicInteger requestId = new AtomicInteger(1);
    public KafkaProducer(){
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
                                KafkaMetaResponse kafkaMetaResponse = new KafkaMetaResponse();
                                kafkaMetaResponse.deserialize(in);
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
        KafkaMetaRequest kafkaMetaRequest = new KafkaMetaRequest("producer-1", 2);
        kafkaMetaRequest.setTopics(Lists.newArrayList("test"));
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
        kafkaMetaRequest.serializable(byteBuf);
        byte [] bytes = new byte[byteBuf.writerIndex()];
        byteBuf.getBytes(0, bytes);
        System.out.println(Arrays.toString(bytes));
        this.channel.writeAndFlush(byteBuf);
       // SerializeUtils.
    }
}
