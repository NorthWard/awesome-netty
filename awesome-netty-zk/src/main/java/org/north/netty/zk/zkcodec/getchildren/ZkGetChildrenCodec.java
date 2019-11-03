package org.north.netty.zk.zkcodec.getchildren;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.north.netty.common.utils.SerializeUtils;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenRequest;
import org.north.netty.zk.bean.getchildren.ZkGetChildrenResponse;
import org.north.netty.zk.registrys.ZkRegistry;
import org.north.netty.zk.zkcodec.ZkAbstractCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laihaohua
 */
public class ZkGetChildrenCodec extends ZkAbstractCodec<ZkGetChildrenRequest> {
    public ZkGetChildrenCodec(ZkRegistry codecRegistry) {
        super(codecRegistry);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ZkGetChildrenRequest msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getXid());
        out.writeInt(msg.getType());
        String path = msg.getPath();
        SerializeUtils.writeStringToBuffer(path, out);
        out.writeBoolean(msg.isWatch());
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
        long zXid = in.readLong();
        int err = in.readInt();
        zkGetChildrenResponse.setXid(xid);
        zkGetChildrenResponse.setZxid(zXid);
        zkGetChildrenResponse.setErr(err);
        if(err == 0){
            int listLen = in.readInt();
            if(listLen < 0){
                codecRegistry.putResp(xid, zkGetChildrenResponse);
                return;
            }
            List<String> list = new ArrayList<>(listLen);
            while(listLen-- > 0){
                    String s = SerializeUtils.readStringToBuffer(in);
                    list.add(s);
            }
            zkGetChildrenResponse.setChildren(list);
        }
        codecRegistry.putResp(xid, zkGetChildrenResponse);
    }
}
