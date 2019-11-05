package com.north.netty.redis.connections;

import com.north.netty.redis.codecs.ByteBufToByteDecoder;
import com.north.netty.redis.codecs.ByteToByteBufEncoder;
import com.north.netty.redis.codecs.RedisRespHandler;
import com.north.netty.redis.config.RedisConfig;
import com.north.netty.redis.enums.ClientType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @author laihaohua
 */
public class ConnectionPool<T> {
    private BlockingQueue<RedisConnection<T>> connections;

    public ConnectionPool(ClientType clientType){
       this.connections = new LinkedBlockingQueue<>(RedisConfig.connectionCount);
       init(clientType);
    }
    private void init(final ClientType clientType){
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        try {
            int count = RedisConfig.connectionCount;
            int tryCount = RedisConfig.connectionCount * 2;
            while(connections.size() < count && tryCount-- > 0){
                // 公平的同步队列   传到RedisRespHandler中用于异步获取返回的数据
                final SynchronousQueue<T> synchronousQueue = new SynchronousQueue<>(true);
                bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        if(clientType.equals(ClientType.STRING)){
                            // string 类型的的kv
                            ch.pipeline()
                                    .addLast("stringEncoder", new StringEncoder())
                                    .addLast("stringDecoder", new StringDecoder())
                                    .addLast("redisRespHandler", new RedisRespHandler<>(synchronousQueue));
                        }else if(clientType.equals(ClientType.BINARY)){
                            // byte[] 类型的kv,  发送时先用byteToByteBufEncoder 转成ByteBuf, 发送到redis,
                            // 返回数据的处理链 byteBufToByteDecoder -> redisRespHandler
                            ch.pipeline()
                                    .addLast("byteBufToByteDecoder", new ByteBufToByteDecoder())
                                    .addLast("redisRespHandler", new RedisRespHandler<>(synchronousQueue));
                            ch.pipeline().addLast("byteToByteBufEncoder", new ByteToByteBufEncoder());
                        }else{
                            throw  new IllegalArgumentException();
                        }
                    }
                });
                ChannelFuture channelFuture = bootstrap.connect(RedisConfig.host, RedisConfig.port).sync();
                Channel channel = channelFuture.channel();
                if(channel.isActive()){
                    String name = "connect-" + connections.size();
                    this.connections.add(new RedisConnection<>(name, (NioSocketChannel)channel, synchronousQueue));
                }
            }
            if(connections.size() != count){
                   throw new IllegalStateException("");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RedisConnection borrowConnection(){
        try {
            RedisConnection connection = connections.take();
            System.out.println("borrowConnection :" + connection.getName());
            return connection;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void returnConnection(RedisConnection channel){
        // 清除
        channel.cleanUp();
        boolean flag = connections.offer(channel);
        if(!flag){
            // try again
            flag = connections.offer(channel);
        }
        if(!flag){
            channel.disconnect();
            channel.close();
        }
    }

    public boolean checkChannel(RedisConnection channel){
        return channel != null && channel.isActive();
    }

}
