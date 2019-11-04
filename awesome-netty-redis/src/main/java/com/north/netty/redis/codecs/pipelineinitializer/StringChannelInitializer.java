package com.north.netty.redis.codecs.pipelineinitializer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class StringChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline()
                .addLast("stringEncoder", new StringEncoder())
                .addLast("stringDecoder", new StringDecoder())
        .addLast(new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                super.channelRead(ctx, msg);
                System.out.println("ctx = [" + ctx + "], msg = [" + msg + "]");
            }
        });

    }
}
