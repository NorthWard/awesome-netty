package com.north.netty.kafka.bean.fetch;

import io.netty.buffer.ByteBuf;

public class AbortedTransaction {
    private Long producerId;
    private Long firstOffset;
    public void deserialize(ByteBuf in){
        this.producerId = in.readLong();
        this.firstOffset = in.readLong();
    }
}
