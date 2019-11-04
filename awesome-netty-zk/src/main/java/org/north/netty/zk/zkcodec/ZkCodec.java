package org.north.netty.zk.zkcodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.ZkRequest;
import org.north.netty.zk.factories.ZkCodecFactories;
import org.north.netty.zk.registrys.ZkRegistry;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author laihaohua
 */
public class ZkCodec extends ZkAbstractCodec<ZkRequest> {

    public ZkCodec(ZkRegistry codecRegistry) {
        super(codecRegistry);
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ZkRequest zkRequest, ByteBuf byteBuf) throws Exception {
        ByteToMessageCodec codec =  ZkCodecFactories.getCodec(zkRequest, codecRegistry);
        Method method = codec.getClass().getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class, ByteBuf.class);
        method.setAccessible(true);
        int requestId = zkRequest.getRequestId();
        if(requestId == 0){
            throw  new Exception(" requestId can not be zero");
        }
        codecRegistry.putCodec(requestId, codec);
        try {
            method.invoke(codec, channelHandlerContext, zkRequest, byteBuf);
        }catch (Exception e){
            e.printStackTrace();
            codecRegistry.removeCodec(requestId);
        }

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int xid = byteBuf.readInt();
        ByteToMessageCodec codec = codecRegistry.getCodec(xid);
        if(codec == null){
            throw new IllegalAccessException("cannot find codecs for xid = " + xid);
        }
        // 恢复readIndex, 让后面的handler也能读到xid
        byteBuf.readerIndex(byteBuf.readerIndex() - 4);
        Method method = codec.getClass().getDeclaredMethod("decode", ChannelHandlerContext.class, ByteBuf.class, List.class);
        method.setAccessible(true);
        method.invoke(codec, channelHandlerContext, byteBuf, list);
        codecRegistry.removeCodec(xid);
    }
}
