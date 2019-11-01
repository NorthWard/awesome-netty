package org.north.netty.zk.zkcodec.getchildren;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.List;

/**
 * 利用LengthFieldPrepender 将ZkGetChildrenRequest的ByteBuffer封装成 head-content形式的报文
 * @author laihaohua
 */
public class ZkGetChildrenLengthFieldPrepender extends LengthFieldPrepender {
    public ZkGetChildrenLengthFieldPrepender(int lengthFieldLength) {
        super(lengthFieldLength);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        super.encode(ctx, msg, out);
        int len = msg.writerIndex();
        byte [] tmp = new byte[len];
        msg.getBytes(0, tmp);
    }
}
