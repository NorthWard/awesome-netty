package org.north.netty.zk.zkcodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.ZkRequest;
import org.north.netty.zk.factories.ZkCodecFactories;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class ZkCodec extends ByteToMessageCodec<ZkRequest> {
    private Map<Integer, ByteToMessageCodec> codecMap = new ConcurrentHashMap<>();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ZkRequest zkRequest, ByteBuf byteBuf) throws Exception {
        ByteToMessageCodec codec =  ZkCodecFactories.getCodec(zkRequest.getClass());
        Method method = codec.getClass().getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
        method.setAccessible(true);
        codecMap.putIfAbsent(zkRequest.getRequestId(), codec);
        try {
            method.invoke(codec, channelHandlerContext, zkRequest, byteBuf);
        }catch (Exception e){
            e.printStackTrace();
            codecMap.remove(zkRequest.getRequestId());
        }

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int xid = byteBuf.readInt();
        ByteToMessageCodec codec = codecMap.get(xid);
        if(codec == null){
            throw new IllegalAccessException("cannot find codec for xid = " + xid);
        }
        Method method = codec.getClass().getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
        method.setAccessible(true);
        method.invoke(codec, channelHandlerContext, byteBuf, list);
    }
}
