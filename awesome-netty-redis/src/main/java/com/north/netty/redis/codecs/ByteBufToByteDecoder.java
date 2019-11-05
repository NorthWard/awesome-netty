package com.north.netty.redis.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteBufToByteDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte [] bytes = new byte[in.writerIndex()];
        in.readBytes(bytes);
        out.add(bytes);
    }
}
