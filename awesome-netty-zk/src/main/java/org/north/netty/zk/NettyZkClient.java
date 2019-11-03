package org.north.netty.zk;


import com.google.gson.Gson;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.north.netty.common.utils.SerializeUtils;
import org.north.netty.zk.bean.create.ZkCreateResponse;
import org.north.netty.zk.bean.create.ZkAcl;
import org.north.netty.zk.bean.create.ZkAclId;
import org.north.netty.zk.bean.create.ZkCreateRequest;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenRequest;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenResponse;
import org.north.netty.zk.bean.login.ZkLoginRequest;
import org.north.netty.zk.bean.login.ZkLoginResp;
import org.north.netty.zk.registrys.ZkRegistry;
import org.north.netty.zk.utils.CreateMode;
import org.north.netty.zk.utils.OpCode;
import org.north.netty.zk.zkcodec.login.ZkLoginCodec;
import org.north.netty.zk.zkcodec.login.ZkLoginHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laihaohua
 */
public class NettyZkClient {
    private int timeout;
    private String zkServer;
    private int zkPort;
    private String passWord;
    private Channel channel;
    private final ZkRegistry zkRegistry = new ZkRegistry();
    private final ZkLoginHandler zkLoginHandler = new ZkLoginHandler(zkRegistry);
    private final AtomicInteger atomicIntegerXid = new AtomicInteger(1);


    public NettyZkClient(int timeout) throws Exception {
        this("localhost", 2181, "", timeout);
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
          ZkLoginResp zkLoginResp = login();
          // login响应中的sessionId大于0才算成功建立连接
          if(zkLoginResp == null || zkLoginResp.getSessionId() == 0){
              throw new Exception("login to server: " + zkServer + ":" + zkPort + " failed , resp : " + new Gson().toJson(zkLoginResp));
          }
    }

    private boolean connectToServer(){
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline()
                        // 解码器, 将HEADER-CONTENT格式的报文解析成只包含CONTENT
                        .addLast(ZkLoginHandler.LOGIN_LENGTH_FIELD_BASED_FRAME_DECODER,new LengthFieldBasedFrameDecoder(2048, 0, 4, 0, 4))
                        // 编码器, 给报文加上一个4个字节大小的HEADER
                        .addLast(ZkLoginHandler.LOGIN_LENGTH_FIELD_PREPENDER, new LengthFieldPrepender(4))
                        // 编码解码器  编码ZkLoginRequest , 解码ZkLoginResp
                        .addLast(ZkLoginHandler.ZK_LOGIN_CODEC, new ZkLoginCodec())
                        // login的handler, remove该channel所有的handler, 然后返回resp
                        .addLast(ZkLoginHandler.ZK_LOGIN_HANDLER,zkLoginHandler)
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
    private boolean isSocketConnected(){
        if(channel != null && channel.isActive()){
            return true;
        }
        return connectToServer();
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
    private ZkLoginResp login() throws Exception {
        if(!isSocketConnected()){
            throw new Exception("can not connect to server: " + zkServer + ":" + zkPort);
        }
        ZkLoginRequest zkLoginRequest = new ZkLoginRequest();
        int protocolVersion = 0;
        zkLoginRequest.setProtocolVersion(protocolVersion);
        Long lastZxidSeen = 0L;
        zkLoginRequest.setLastZxidSeen(lastZxidSeen);
        zkLoginRequest.setTimeout(timeout);
        Long sessionId = 0L;
        zkLoginRequest.setSessionId(sessionId);
        zkLoginRequest.setPassword(passWord);
        zkLoginRequest.setReadOnly(true);
        this.channel.writeAndFlush(zkLoginRequest);
        long t = System.currentTimeMillis();
        while(!zkLoginHandler.getIsLogon()){
            long t2 = System.currentTimeMillis();
            if(t2 > t + timeout){
                throw new Exception("login to server: " + zkServer + ":" + zkPort + " timeout after " +  timeout + "ms");
            }
        }
        return zkLoginHandler.getZkLoginResp();
    }
    public List<String> getChildren(String path){
        ZkGetChildrenRequest zkGetChildrenRequest = new ZkGetChildrenRequest();
        zkGetChildrenRequest.setPath(path);
        zkGetChildrenRequest.setWatch(false);
        zkGetChildrenRequest.setType(OpCode.GET_CHILDREN);
        int xid = atomicIntegerXid.getAndIncrement();
        zkGetChildrenRequest.setXid(xid);
        ChannelFuture channelFuture = this.channel.writeAndFlush(zkGetChildrenRequest);
        try {
            ZkGetChildrenResponse response = (ZkGetChildrenResponse)zkRegistry.getResp(xid, this.timeout, TimeUnit.MILLISECONDS);
            return response == null ? null : response.getChildren();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ZkCreateResponse create(String path, Object data, CreateMode mode){
        ZkCreateRequest createRequest = new ZkCreateRequest();
        createRequest.setPath(path);
        createRequest.setData(SerializeUtils.toByteArray(data));
        int xid = atomicIntegerXid.getAndIncrement();
        createRequest.setXid(xid);
        createRequest.setType(OpCode.CREATE);
        /*******************/
        ZkAclId zkAclId = new ZkAclId();
        // 所有人都有权限
        zkAclId.setId("anyone");
        zkAclId.setScheme("world");
        ZkAcl zkAcl = new ZkAcl();
        // 拥有read write update delete admin权限
        zkAcl.setPerms(31);
        zkAcl.setId(zkAclId);
        /*******************/
        createRequest.setAcl(new ArrayList<>());
        createRequest.getAcl().add(zkAcl);
        createRequest.setFlags(mode.getFlag());
        ChannelFuture channelFuture = this.channel.writeAndFlush(createRequest);
        try {
            ZkCreateResponse response = (ZkCreateResponse)zkRegistry.getResp(xid, this.timeout, TimeUnit.MILLISECONDS);
            return response;
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
}
