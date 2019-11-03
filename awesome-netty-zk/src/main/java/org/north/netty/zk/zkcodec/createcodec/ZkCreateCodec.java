package org.north.netty.zk.zkcodec.createcodec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.north.netty.zk.bean.create.ZkAcl;
import org.north.netty.zk.bean.create.ZkAclId;
import org.north.netty.zk.bean.create.ZkCreateRequest;
import org.north.netty.zk.bean.create.ZkCreateResponse;
import org.north.netty.zk.registrys.ZkRegistry;
import org.north.netty.zk.utils.SerializeUtils;
import org.north.netty.zk.zkcodec.ZkAbstractCodec;

import java.util.List;

/**
 * @author laihaohua
 */
public class ZkCreateCodec extends ZkAbstractCodec<ZkCreateRequest> {
    public ZkCreateCodec(ZkRegistry codecRegistry) {
        super(codecRegistry);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ZkCreateRequest msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getXid());
        out.writeInt(msg.getType());
        String path = msg.getPath();
        SerializeUtils.writeStringToBuffer(path, out);
        byte [] bytes = msg.getData();
        SerializeUtils.writeByteArrToBuffer(bytes, out);
        List<ZkAcl> list = msg.getAcl();
        if (list == null) {
            out.writeInt(-1);
        }else {
            out.writeInt(list.size());
            for(ZkAcl acl : list){
                out.writeInt(acl.getPerms());
                ZkAclId zkAclId = acl.getId();
                SerializeUtils.writeStringToBuffer(zkAclId.getScheme(), out);
                SerializeUtils.writeStringToBuffer(zkAclId.getId(), out);
            }
        }
        out.writeInt(msg.getFlags());
    }

    /**
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ZkCreateResponse zkCreateResponse = new ZkCreateResponse();
        int xid = in.readInt();
        long zxid = in.readLong();
        int err = in.readInt();
        zkCreateResponse.setXid(xid);
        zkCreateResponse.setZxid(zxid);
        zkCreateResponse.setErr(err);
        if(err == 0){
           String path = SerializeUtils.readStringToBuffer(in);
           out.add(path);
           zkCreateResponse.setPath(path);
        }
        codecRegistry.putResp(xid, zkCreateResponse);
    }
}
