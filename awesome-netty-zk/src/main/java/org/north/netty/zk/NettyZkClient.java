package org.north.netty.zk;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import org.north.netty.zk.bean.ZkLoginRequest;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author laihaohua
 */
public class NettyZkClient {
    private Integer protocolVersion = 0;
    private Long lastZxidSeen = 0L;
    private int timeout = 3000;
    private Long sessionId = 0L;
    private String zkServer;
    private int zkPort;
    private String passWord;
    private Channel channel;
    private volatile AtomicBoolean isLogining;

    public NettyZkClient(){
        this("localhost", 2181, null);
    }

    private boolean isSocketConnected(){
        if(channel != null && channel.isActive()){
            return true;
        }
        return connectToServer();
    }
    public NettyZkClient(String zkServer, int zkPort, String passWord){
          this.zkServer = zkServer;
          this.zkPort = zkPort;
          this.passWord = passWord;
          connectToServer();
    }
    private boolean connectToServer(){
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline().addLast(new LengthFieldPrepender(4));
            }
        });
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture channelFuture =  bootstrap.connect(zkServer, zkPort).sync();
            this.channel = channelFuture.channel();
            return channel != null && channel.isActive();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *     0. header, 4个字节, 表示报文的大小
     *
     *     1. 4个字节的protocolVersion(Integer, 默认为0即可)
     *     2. 8个字节的lastZxidSeen(Long , 默认为0即可)
     *     3. 4个字节的超时时间(Integer)
     *     4. 8个字节的sessionId(Long, 默认为0)
     *     5. 4个字节, 表示passwd的长度n
     *     6. n个字节
     *     a_.writeInt(protocolVersion,"protocolVersion");
     *     a_.writeLong(lastZxidSeen,"lastZxidSeen");
     *     a_.writeInt(timeOut,"timeOut");
     *     a_.writeLong(sessionId,"sessionId");
     *     a_.writeBuffer(passwd,"passwd");
     * @return
     */
    public String login() throws Exception {
          if(!isLogining.compareAndSet(false, true)){
              if(!isSocketConnected()){
                  throw new Exception("can not connect to server: " + zkServer + ":" + zkPort);
              }
              ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.buffer();
              ZkLoginRequest zkLoginRequest = new ZkLoginRequest();
              zkLoginRequest.setProtocolVersion(protocolVersion);
              zkLoginRequest.setLastZxidSeen(lastZxidSeen);
              zkLoginRequest.setLastZxidSeen(lastZxidSeen);
              zkLoginRequest.setTimeout(timeout);
              zkLoginRequest.setSessionId(sessionId);
              zkLoginRequest.setPassword(passWord);
              ChannelFuture channelFuture = this.channel.writeAndFlush(zkLoginRequest);
              return "success";
          }else{
              return "thread " + Thread.currentThread().getName() + " is connecting";
          }

    }
}
