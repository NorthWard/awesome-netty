package org.north.netty.zk.zkcodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.ZkLoginRequest;

import java.util.List;

public class ZkLoginCodec extends ByteToMessageCodec<ZkLoginRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ZkLoginRequest msg, ByteBuf outByteBuf) throws Exception {
        outByteBuf.writeInt(msg.getProtocolVersion());
        outByteBuf.writeLong(msg.getLastZxidSeen());
        outByteBuf.writeInt(msg.getTimeout());
        outByteBuf.writeLong(msg.getTimeout());
        byte [] bytes = new byte[16];
        String passWord = msg.getPassword();
        if(passWord == null){
            // 密码的字节长度
            outByteBuf.writeInt(16);
            outByteBuf.writeBytes(bytes);
        }else{
            bytes = passWord.getBytes();
            // 密码的字节长度
            outByteBuf.writeInt(bytes.length);
            outByteBuf.writeBytes(bytes);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
