package com.north.netty.redis;

import com.north.netty.redis.cmd.impl.getcmd.binary.GetBinaryCmd;
import com.north.netty.redis.cmd.impl.setcmd.binary.SetBinaryCmd;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author laihaohua
 */
public class RedisBinaryClient {
    private String host;
    private int port;
    private Channel channel;

    public RedisBinaryClient(String host, int port) {
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
                        //.addLast(new StringEncoder())
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

    public boolean set(byte [] key, byte [] v){
        SetBinaryCmd setCmd = new SetBinaryCmd(key, v);
        byte [] bytes = setCmd.build();
        ByteBuf b = PooledByteBufAllocator.DEFAULT.buffer(bytes.length);
        b.writeBytes(bytes);
        channel.writeAndFlush(b);
        return true;

    }

    public byte[] get(byte [] key){
        GetBinaryCmd getBinaryCmd = new GetBinaryCmd(key);
        byte [] bytes = getBinaryCmd.build();
        ByteBuf b = PooledByteBufAllocator.DEFAULT.buffer(bytes.length);
        b.writeBytes(bytes);
        channel.writeAndFlush(b);
        return null;
    }
}
