package com.north.netty.kafka.bean.produce;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

public class PartitionData implements Serializable {
    private Record recordSset;
    public void serializable(ByteBuf out){
        recordSset.serializable(out);
    }

    public Record getRecordSset() {
        return recordSset;
    }

    public void setRecordSset(Record recordSset) {
        this.recordSset = recordSset;
    }
}
