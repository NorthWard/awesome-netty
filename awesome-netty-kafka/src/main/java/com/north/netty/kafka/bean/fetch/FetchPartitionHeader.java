package com.north.netty.kafka.bean.fetch;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FetchPartitionHeader implements Serializable {
    private Integer partition;
    private Short errorCode;
    private Long highWaterMark;
    private Long lastStableOffset;
    private Long logStartOffset;
    private List<AbortedTransaction> abortedTransactions;
    public void deserialize(ByteBuf in){
        this.partition = in.readInt();
        this.errorCode = in.readShort();
        this.highWaterMark = in.readLong();
        this.lastStableOffset = in.readLong();
        this.logStartOffset = in.readLong();
        int count = in.readInt();
        if(count >= 0){
            abortedTransactions = new ArrayList<>(count);
            for(int i =0 ; i < count; i++){
                AbortedTransaction abortedTransaction = new AbortedTransaction();
                abortedTransaction.deserialize(in);
                abortedTransactions.add(abortedTransaction);
            }
        }

    }
}
