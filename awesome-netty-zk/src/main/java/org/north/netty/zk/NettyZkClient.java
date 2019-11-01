package org.north.netty.zk;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.north.netty.zk.bean.RequestHeader;
import org.north.netty.zk.bean.ZkGetChildrenRequest;
import org.north.netty.zk.bean.ZkLoginRequest;
import org.north.netty.zk.utils.OpCode;
import org.north.netty.zk.zkcodec.getchildren.ZkGetChildrenCodec;
import org.north.netty.zk.zkcodec.getchildren.ZkGetChildrenLengthFieldPrepender;
import org.north.netty.zk.zkcodec.getchildren.ZkGetChildrenRespDecoder;
import org.north.netty.zk.zkcodec.login.*;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laihaohua
 */
public class NettyZkClient {
    private int protocolVersion = 0;
    private Long lastZxidSeen = 0L;
    private int timeout = 3000;
    private Long sessionId = 0L;
    private String zkServer;
    private int zkPort;
    private String passWord;
    private Channel channel;
    private  AtomicBoolean isDoingLogin = new AtomicBoolean(false);
    private  AtomicBoolean isLogon = new AtomicBoolean(false);
    private  AtomicInteger xid = new AtomicInteger(1);

    public NettyZkClient(int timeout) throws Exception {
        this("localhost", 2181, "", timeout);
    }

    private boolean isSocketConnected(){
        if(channel != null && channel.isActive()){
            return true;
        }
        return connectToServer();
    }
    public NettyZkClient(String zkServer, int zkPort, String passWord, int timeout) throws Exception {
          this.zkServer = zkServer;
          this.zkPort = zkPort;
          this.passWord = passWord;
          this.timeout = timeout;
          boolean isConnectToServer = connectToServer();
          if(!isConnectToServer){
              throw new Exception("can not connect to server: " + zkServer + ":" + zkPort);
          }
    }
    private boolean connectToServer(){
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline()
                        .addLast("respDecoder",new ZkLoginRespDecoder(2048, 0, 4, 0, 4))
                        .addLast("lengthFieldPrepender", new ZkLoginLengthFieldPrepender(4))
                        .addLast("zkLoginCodec", new ZkLoginCodec())
                        .addLast("printRespHandler",new ZkLoginHandler())
                        ;
            }
        });
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        try {
            SocketAddress socketAddress = new InetSocketAddress(zkServer, zkPort);
            ChannelFuture channelFuture =  bootstrap.connect(socketAddress).sync();
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
     *     @return
     */
    public String login() throws Exception {
          if(isDoingLogin.compareAndSet(false, true)){
              if(!isSocketConnected()){
                  throw new Exception("can not connect to server: " + zkServer + ":" + zkPort);
              }
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
    public String getChildren(String path){
        ZkGetChildrenRequest zkGetChildrenRequest = new ZkGetChildrenRequest();
        zkGetChildrenRequest.setPath(path);
        zkGetChildrenRequest.setWatch(false);
        RequestHeader h = new RequestHeader();
        h.setType(OpCode.getChildren);
        h.setXid(xid.getAndIncrement());
        zkGetChildrenRequest.setRequestHeader(h);
        Iterator<Map.Entry<String, ChannelHandler>> iterator = channel.pipeline().iterator();
        while(iterator.hasNext()){
            ChannelHandler handler = iterator.next().getValue();
            Class clazz = handler.getClass();
            if(ZkLoginChannelHandler.class.isAssignableFrom(clazz)){
                channel.pipeline().remove(handler);
            }
        }
        channel.pipeline()
                .addLast(new ZkGetChildrenRespDecoder(2048,0,4,0,4))
                .addLast(new ZkGetChildrenLengthFieldPrepender(4))
                .addLast(new ZkGetChildrenCodec());
        ChannelFuture channelFuture = this.channel.writeAndFlush(zkGetChildrenRequest);
        return "success";
    }

    public static void main(String[] args) {
        System.out.println("args = [" + ZkLoginChannelHandler.class.isAssignableFrom(ZkLoginCodec.class) + "]");
    }
}
