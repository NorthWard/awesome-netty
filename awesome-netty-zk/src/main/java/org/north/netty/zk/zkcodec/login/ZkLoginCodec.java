package org.north.netty.zk.zkcodec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.common.utils.SerializeUtils;
import org.north.netty.zk.bean.login.ZkLoginRequest;
import org.north.netty.zk.bean.login.ZkLoginResp;

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
        String passWord = msg.getPassword();
        SerializeUtils.writeStringToBuffer(passWord, outByteBuf);
        outByteBuf.writeBoolean(msg.isReadOnly());
    }

    /**
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ZkLoginResp zkLoginResp = new ZkLoginResp();
        zkLoginResp.setProtocolVersion(in.readInt());
        zkLoginResp.setTimeout(in.readInt());
        zkLoginResp.setSessionId(in.readLong());
        String password = SerializeUtils.readStringToBuffer(in);
        zkLoginResp.setPassword(password);
        zkLoginResp.setReadOnly(in.readBoolean());
        out.add(zkLoginResp);
    }
}
