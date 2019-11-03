package com.north.netty.redis;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RedisClient {
    private String host;
    private int port;
    private Channel channel;

    public RedisClient(String host, int port) {
        this.host = host;
        this.port = port;
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

    public boolean set(String key, String v){
        int len = key.length();
        int lv = v.length();
        StringBuilder sb = new StringBuilder("*3\r\n")
                .append("$3\r\nset\r\n")
                .append("$").append(len).append("\r\n").append(key).append("\r\n")
                .append("$").append(lv).append("\r\n").append(v).append("\r\n");
        String cmd = sb.toString();
        channel.writeAndFlush(cmd);
        return true;

    }
}
