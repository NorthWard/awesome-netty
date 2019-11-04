package com.north.netty.redis;

import com.north.netty.redis.config.RedisConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.channels.Channel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class ConnectionPool {
    private Queue<Channel> connections;
    public ConnectionPool(){
        connections = new LinkedBlockingDeque<>(RedisConfig.connectionCount);
    }
    private void init(){
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline()
                        .addLast(new StringEncoder())
                        .addLast(new StringDecoder())
                        .addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                super.channelRead(ctx, msg);
                                System.out.println("ctx = [" + ctx + "], msg = [" + msg + "]");
                            }
                        });
            }
        });
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
