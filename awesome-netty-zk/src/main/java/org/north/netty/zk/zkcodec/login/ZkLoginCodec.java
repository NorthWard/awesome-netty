package org.north.netty.zk.zkcodec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.login.ZkLoginRequest;
import org.north.netty.zk.bean.login.ZkLoginResp;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author laihaohua
 */
public class ZkLoginCodec extends ByteToMessageCodec<ZkLoginRequest>{

    public ZkLoginCodec(){

    }
    @Override
    protected void encode(ChannelHandlerContext ctx, ZkLoginRequest msg, ByteBuf outByteBuf) throws Exception {
        outByteBuf.writeInt(msg.getProtocolVersion());
        outByteBuf.writeLong(msg.getLastZxidSeen());
        outByteBuf.writeInt(msg.getTimeout());
        outByteBuf.writeLong(msg.getSessionId());
        byte [] bytes = new byte[0];
        String passWord = msg.getPassword();
        if(passWord == null){
            // 密码的字节长度
            outByteBuf.writeInt(0);
            outByteBuf.writeBytes(bytes);
        }else{
            bytes = passWord.getBytes();
            // 密码的字节长度
            outByteBuf.writeInt(bytes.length);
            outByteBuf.writeBytes(bytes);
        }
        outByteBuf.writeBoolean(msg.isReadOnly());
    }

    /**
     *     protocolVersion=a_.readInt("protocolVersion");
     *     timeOut=a_.readInt("timeOut");
     *     sessionId=a_.readLong("sessionId");
     *     passwd=a_.readBuffer("passwd");
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int l = in.writerIndex();
        byte [] bytes1 = new byte[l];
        in.getBytes(0,bytes1);
        ZkLoginResp zkLoginResp = new ZkLoginResp();
        zkLoginResp.setProtocolVersion(in.readInt());
        zkLoginResp.setTimeout(in.readInt());
        zkLoginResp.setSessionId(in.readLong());
        int passwordLen = in.readInt();
        if(passwordLen < 0 ){
            throw new Exception();
        }
        byte [] bytes = new byte[passwordLen];
        in.readBytes(bytes);
        zkLoginResp.setPassword(new String(bytes, StandardCharsets.UTF_8));
        zkLoginResp.setReadOnly(in.readBoolean());
        out.add(zkLoginResp);
    }
}
