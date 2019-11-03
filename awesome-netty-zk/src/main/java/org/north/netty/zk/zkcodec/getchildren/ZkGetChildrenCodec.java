package org.north.netty.zk.zkcodec.getchildren;

import com.google.gson.Gson;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenRequest;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenResponse;
import org.north.netty.zk.registrys.ZkRegistry;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laihaohua
 */
public class ZkGetChildrenCodec extends ByteToMessageCodec<ZkGetChildrenRequest> {
    private ZkRegistry codecRegistry;
    public ZkGetChildrenCodec(ZkRegistry codecRegistry){
        this.codecRegistry = codecRegistry;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, ZkGetChildrenRequest msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getXid());
        out.writeInt(msg.getType());
        String path = msg.getPath();
        if(path == null){
            // 字符串的长度
            out.writeInt(-1);
        }else{
           byte [] bytes = path.getBytes(StandardCharsets.UTF_8);
            // 字符串的长度
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        }
        out.writeBoolean(msg.isWatch());
        int len = out.writerIndex();
        byte [] tmp = new byte[len];
        out.getBytes(0, tmp);
    }

    /**
     * 内容:
     *  4字节的xid
     *  8字节的zxid
     *  4字节的err
     *  如果 err==0 后面紧接着是resp
     *    4个字节的len, 表示children的长度
     *    再读len个字符串(每个字符串是4个字节的长度 + 字符串内容字节数组)
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ZkGetChildrenResponse zkGetChildrenResponse = new ZkGetChildrenResponse();
        int xid = in.readInt();
        long zxid = in.readLong();
        int err = in.readInt();
        zkGetChildrenResponse.setXid(xid);
        zkGetChildrenResponse.setZxid(zxid);
        zkGetChildrenResponse.setErr(err);
        if(err == 0){
            int listLen = in.readInt();
            if(listLen < 0){
                return;
            }
            List<String> list = new ArrayList<>(listLen);
            while(listLen-- > 0){
                    int strLen = in.readInt();
                    if(strLen < 0){
                        continue;
                    }
                    byte [] bytes = new byte[strLen];
                    in.readBytes(bytes);
                    String s = new String(bytes, StandardCharsets.UTF_8);
                    list.add(s);

            }
            zkGetChildrenResponse.setChildren(list);
        }
        codecRegistry.putResp(xid, zkGetChildrenResponse);
    }
}
